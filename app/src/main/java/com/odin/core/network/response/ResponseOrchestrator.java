package com.odin.core.network.response;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatLevel;
import com.odin.core.network.rules.DynamicRuleEngine;
import com.odin.core.persistence.SentryCore;
import com.odin.core.domain.model.LogEvent;
import com.odin.core.domain.model.FirewallRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ResponseOrchestrator: Orquestador central de respuestas automatizadas.
 * 
 * Responsabilidades:
 * - Coordinación de respuestas automatizadas
 * - Priorización de acciones de respuesta
 * - Ejecución secuencial o paralela de respuestas
 * - Manejo de dependencias entre respuestas
 * - Rollback de respuestas fallidas
 * - Logging de acciones de respuesta
 * - Integración con SentryCore para forensics
 * 
 * Performance: <200ms por incidente (orquestación)
 * Thread-safe: Concurrent collections + thread pools
 */
public final class ResponseOrchestrator {

    private static final String TAG = "ResponseOrchestrator";
    private static final int ORCHESTRATOR_THREADS = 4;
    private static final int QUEUE_CAPACITY = 1000;
    private static final int MAX_CONCURRENT_RESPONSES = 50;
    private static final long RESPONSE_TIMEOUT_MS = 30_000; // 30 segundos

    private final BlockingQueue<ResponseTask> responseQueue;
    private final ExecutorService executorService;
    private final List<ResponseListener> listeners;
    private final AtomicBoolean isRunning;
    private final AtomicLong responsesExecuted;
    private final AtomicLong responsesFailed;
    private final AtomicLong rollbacksExecuted;
    
    // Componentes integrados
    private final DynamicRuleEngine ruleEngine;
    private SentryCore sentryCore;
    private ResponsePolicyEngine policyEngine;
    private QuarantineManager quarantineManager;
    private RateLimiter rateLimiter;
    
    // Estado de respuestas activas
    private final Map<String, ResponseContext> activeResponses;
    private final PriorityQueue<ResponseTask> priorityQueue;

    /**
     * Prioridad de respuesta.
     */
    public enum ResponsePriority {
        CRITICAL(4),
        HIGH(3),
        MEDIUM(2),
        LOW(1),
        INFO(0);

        private final int value;
        ResponsePriority(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    /**
     * Contexto de una respuesta en ejecución.
     */
    public static final class ResponseContext {
        private final String responseId;
        private final DetectionResult detection;
        private final List<ResponsePolicyEngine.ResponseAction> actions;
        private final long startTime;
        private volatile boolean completed;
        private volatile boolean rolledBack;
        private volatile String failureReason;
        private final List<ResponsePolicyEngine.ResponseAction> completedActions;

        public ResponseContext(String responseId, DetectionResult detection, List<ResponsePolicyEngine.ResponseAction> actions) {
            this.responseId = responseId;
            this.detection = detection;
            this.actions = new ArrayList<>(actions);
            this.startTime = System.currentTimeMillis();
            this.completed = false;
            this.rolledBack = false;
            this.completedActions = new ArrayList<>();
        }

        public String getResponseId() { return responseId; }
        public DetectionResult getDetection() { return detection; }
        public List<ResponsePolicyEngine.ResponseAction> getActions() { return actions; }
        public long getStartTime() { return startTime; }
        public boolean isCompleted() { return completed; }
        public boolean isRolledBack() { return rolledBack; }
        public String getFailureReason() { return failureReason; }
        public List<ResponsePolicyEngine.ResponseAction> getCompletedActions() { return completedActions; }

        public void markCompleted() { this.completed = true; }
        public void markRolledBack() { this.rolledBack = true; }
        public void setFailureReason(String reason) { this.failureReason = reason; }
        public void addCompletedAction(ResponsePolicyEngine.ResponseAction action) { completedActions.add(action); }
    }

    /**
     * Tarea de respuesta para ejecución.
     */
    private static final class ResponseTask implements Comparable<ResponseTask> {
        private final String taskId;
        private final DetectionResult detection;
        private final ResponsePriority priority;
        private final List<ResponsePolicyEngine.ResponseAction> actions;
        private final long timestamp;

        public ResponseTask(String taskId, DetectionResult detection, ResponsePriority priority, List<ResponsePolicyEngine.ResponseAction> actions) {
            this.taskId = taskId;
            this.detection = detection;
            this.priority = priority;
            this.actions = actions;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public int compareTo(ResponseTask other) {
            // Primero por prioridad (descendente)
            int priorityComparison = Integer.compare(other.priority.getValue(), this.priority.getValue());
            if (priorityComparison != 0) return priorityComparison;
            
            // Luego por timestamp (ascendente - más antiguo primero)
            return Long.compare(this.timestamp, other.timestamp);
        }
    }

    /**
     * Listener para eventos de respuesta.
     */
    public interface ResponseListener {
        void onResponseStarted(String responseId, DetectionResult detection);
        void onResponseCompleted(String responseId, ResponseContext context);
        void onResponseFailed(String responseId, String reason);
        void onRollbackExecuted(String responseId);
    }

    /**
     * Constructor.
     */
    public ResponseOrchestrator(@NonNull DynamicRuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
        this.responseQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.listeners = new CopyOnWriteArrayList<>();
        this.isRunning = new AtomicBoolean(false);
        this.responsesExecuted = new AtomicLong(0);
        this.responsesFailed = new AtomicLong(0);
        this.rollbacksExecuted = new AtomicLong(0);
        this.activeResponses = new ConcurrentHashMap<>();
        this.priorityQueue = new PriorityQueue<>();

        this.executorService = Executors.newFixedThreadPool(
                ORCHESTRATOR_THREADS,
                r -> {
                    Thread t = new Thread(r, "ResponseOrchestrator-" + Thread.currentThread().getId());
                    t.setDaemon(true);
                    return t;
                }
        );
    }

    /**
     * Inicializa componentes integrados.
     */
    public void initializeComponents(
            @Nullable SentryCore sentryCore,
            @Nullable ResponsePolicyEngine policyEngine,
            @Nullable QuarantineManager quarantineManager,
            @Nullable RateLimiter rateLimiter
    ) {
        this.sentryCore = sentryCore;
        this.policyEngine = policyEngine;
        this.quarantineManager = quarantineManager;
        this.rateLimiter = rateLimiter;
    }

    /**
     * Inicia el orquestador.
     */
    public void start() {
        if (!isRunning.compareAndSet(false, true)) {
            return; // Ya está corriendo
        }

        // Iniciar workers
        for (int i = 0; i < ORCHESTRATOR_THREADS; i++) {
            executorService.submit(this::processingLoop);
        }

        Log.i(TAG, "ResponseOrchestrator iniciado");
    }

    /**
     * Detiene el orquestador.
     */
    public void stop() {
        if (!isRunning.compareAndSet(true, false)) {
            return; // Ya está detenido
        }

        try {
            // Drain remaining tasks
            List<ResponseTask> remaining = new ArrayList<>();
            responseQueue.drainTo(remaining);
            
            executorService.shutdown();
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }

            Log.i(TAG, "ResponseOrchestrator detenido. Tareas restantes: " + remaining.size());
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Envía una detección para respuesta automática.
     * 
     * @param detection Resultado de detección
     * @return true si fue encolado, false si queue está llena
     */
    public boolean submitDetection(@NonNull DetectionResult detection) {
        if (!isRunning.get()) {
            Log.w(TAG, "Orchestrator no está corriendo");
            return false;
        }

        // Determinar acciones basado en política
        List<ResponsePolicyEngine.ResponseAction> actions = determineActions(detection);
        if (actions.isEmpty()) {
            Log.d(TAG, "No hay acciones para detección: " + detection.getThreatType());
            return false;
        }

        // Determinar prioridad
        ResponsePriority priority = determinePriority(detection);

        // Crear tarea
        String taskId = "RESP_" + System.currentTimeMillis() + "_" + detection.getForensicsId();
        ResponseTask task = new ResponseTask(taskId, detection, priority, actions);

        // Validar rate limiting
        if (rateLimiter != null && !rateLimiter.allowResponse(detection.getSourceIp())) {
            Log.w(TAG, "Rate limit excedido para IP: " + detection.getSourceIp());
            return false;
        }

        // Encolar
        boolean enqueued = responseQueue.offer(task);
        if (enqueued) {
            priorityQueue.offer(task);
            logResponseQueued(taskId, detection, actions);
        }

        return enqueued;
    }

    /**
     * Determina las acciones de respuesta basado en la detección.
     */
    @NonNull
    private List<ResponsePolicyEngine.ResponseAction> determineActions(@NonNull DetectionResult detection) {
        List<ResponsePolicyEngine.ResponseAction> actions = new ArrayList<>();

        // Si hay policy engine, usarlo
        if (policyEngine != null) {
            actions = policyEngine.getActionsForThreat(detection);
            if (!actions.isEmpty()) {
                return actions;
            }
        }

        // Lógica por defecto basada en severidad
        switch (detection.getThreatLevel()) {
            case CRITICAL:
                actions.add(ResponsePolicyEngine.ResponseAction.BLOCK_IP);
                actions.add(ResponsePolicyEngine.ResponseAction.QUARANTINE_APP);
                actions.add(ResponsePolicyEngine.ResponseAction.GENERATE_RULE);
                actions.add(ResponsePolicyEngine.ResponseAction.ALERT_ADMIN);
                break;
            case HIGH:
                actions.add(ResponsePolicyEngine.ResponseAction.BLOCK_IP);
                actions.add(ResponsePolicyEngine.ResponseAction.GENERATE_RULE);
                actions.add(ResponsePolicyEngine.ResponseAction.ALERT_ADMIN);
                break;
            case MEDIUM:
                actions.add(ResponsePolicyEngine.ResponseAction.RATE_LIMIT);
                actions.add(ResponsePolicyEngine.ResponseAction.GENERATE_RULE);
                actions.add(ResponsePolicyEngine.ResponseAction.LOG_EVENT);
                break;
            case LOW:
                actions.add(ResponsePolicyEngine.ResponseAction.LOG_EVENT);
                actions.add(ResponsePolicyEngine.ResponseAction.ALERT_ADMIN);
                break;
            case INFO:
                actions.add(ResponsePolicyEngine.ResponseAction.LOG_EVENT);
                break;
        }

        return actions;
    }

    /**
     * Determina la prioridad de respuesta.
     */
    @NonNull
    private ResponsePriority determinePriority(@NonNull DetectionResult detection) {
        switch (detection.getThreatLevel()) {
            case CRITICAL: return ResponsePriority.CRITICAL;
            case HIGH: return ResponsePriority.HIGH;
            case MEDIUM: return ResponsePriority.MEDIUM;
            case LOW: return ResponsePriority.LOW;
            default: return ResponsePriority.INFO;
        }
    }

    /**
     * Loop de procesamiento de tareas.
     */
    private void processingLoop() {
        while (isRunning.get()) {
            try {
                ResponseTask task = responseQueue.poll(100, TimeUnit.MILLISECONDS);
                if (task != null) {
                    executeResponse(task);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Ejecuta una respuesta.
     */
    private void executeResponse(@NonNull ResponseTask task) {
        String responseId = task.taskId;
        ResponseContext context = new ResponseContext(responseId, task.detection, task.actions);
        
        activeResponses.put(responseId, context);
        notifyResponseStarted(responseId, task.detection);

        try {
            // Ejecutar acciones en orden
            for (ResponsePolicyEngine.ResponseAction action : task.actions) {
                if (!isRunning.get()) break;

                boolean success = executeAction(action, task.detection, context);
                
                if (success) {
                    context.addCompletedAction(action);
                } else {
                    // Acción falló, intentar rollback
                    Log.w(TAG, "Acción falló: " + action + ", iniciando rollback");
                    executeRollback(context);
                    responsesFailed.incrementAndGet();
                    notifyResponseFailed(responseId, "Acción falló: " + action);
                    return;
                }
            }

            // Todas las acciones completadas exitosamente
            context.markCompleted();
            responsesExecuted.incrementAndGet();
            notifyResponseCompleted(responseId, context);
            logResponseCompleted(responseId, context);

        } catch (Exception e) {
            Log.e(TAG, "Error ejecutando respuesta: " + responseId, e);
            executeRollback(context);
            responsesFailed.incrementAndGet();
            notifyResponseFailed(responseId, e.getMessage());
        } finally {
            activeResponses.remove(responseId);
        }
    }

    /**
     * Ejecuta una acción individual.
     */
    private boolean executeAction(
            @NonNull ResponsePolicyEngine.ResponseAction action,
            @NonNull DetectionResult detection,
            @NonNull ResponseContext context
    ) {
        try {
            switch (action) {
                case BLOCK_IP:
                    return executeBlockIP(detection);
                case BLOCK_PORT:
                    return executeBlockPort(detection);
                case QUARANTINE_APP:
                    return executeQuarantineApp(detection);
                case RATE_LIMIT:
                    return executeRateLimit(detection);
                case ALERT_ADMIN:
                    return executeAlertAdmin(detection);
                case GENERATE_RULE:
                    return executeGenerateRule(detection);
                case LOG_EVENT:
                    return executeLogEvent(detection);
                case ISOLATE_PROCESS:
                    return executeIsolateProcess(detection);
                case REVOKE_PERMISSIONS:
                    return executeRevokePermissions(detection);
                default:
                    Log.w(TAG, "Acción no implementada: " + action);
                    return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error ejecutando acción: " + action, e);
            return false;
        }
    }

    private boolean executeBlockIP(@NonNull DetectionResult detection) {
        // Generar regla de bloqueo
        DynamicRuleEngine.FirewallRule rule = ruleEngine.autoGenerateRule(detection);
        if (rule != null) {
            return true;
        }
        return false;
    }

    private boolean executeBlockPort(@NonNull DetectionResult detection) {
        DynamicRuleEngine.FirewallRule rule = new DynamicRuleEngine.FirewallRule(
                "Block-Port-" + detection.getDestinationPort(),
                DynamicRuleEngine.RulePriority.MEDIUM,
                DynamicRuleEngine.RuleAction.BLOCK,
                "*",
                "*",
                null,
                detection.getDestinationPort(),
                null,
                "*"
        );
        ruleEngine.addRule(rule);
        return true;
    }

    private boolean executeQuarantineApp(@NonNull DetectionResult detection) {
        if (quarantineManager != null) {
            // Usar packageName si está disponible en metadata
            String packageName = detection.getEvidence(); // Simplificado
            return quarantineManager.quarantineApplication(packageName, "Threat detected: " + detection.getThreatType());
        }
        Log.w(TAG, "QuarantineManager no disponible");
        return false;
    }

    private boolean executeRateLimit(@NonNull DetectionResult detection) {
        if (rateLimiter != null) {
            rateLimiter.limitIP(detection.getSourceIp(), 100, 60); // 100 req/min
            return true;
        }
        return false;
    }

    private boolean executeAlertAdmin(@NonNull DetectionResult detection) {
        Log.w(TAG, "ALERT ADMIN: " + detection.toString());
        // Aquí se integraría con sistema de notificaciones
        return true;
    }

    private boolean executeGenerateRule(@NonNull DetectionResult detection) {
        DynamicRuleEngine.FirewallRule rule = ruleEngine.autoGenerateRule(detection);
        return rule != null;
    }

    private boolean executeLogEvent(@NonNull DetectionResult detection) {
        if (sentryCore != null) {
            try {
                Map<String, String> metadata = new HashMap<>();
                metadata.put("threatType", detection.getThreatType());
                metadata.put("threatLevel", detection.getThreatLevel().name());
                metadata.put("sourceIp", detection.getSourceIp());
                metadata.put("confidence", String.valueOf(detection.getConfidence()));

                LogEvent event = LogEvent.builder()
                        .timestamp(System.currentTimeMillis())
                        .source("RESPONSE_ORCHESTRATOR")
                        .message("Response action executed for threat: " + detection.getThreatType())
                        .severity(LogEvent.Severity.WARNING)
                        .metadata(metadata)
                        .build();

                sentryCore.appendEvent(event);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Error logging event", e);
            }
        }
        return false;
    }

    private boolean executeIsolateProcess(@NonNull DetectionResult detection) {
        // Implementación simplificada - requeriría integración con Android
        Log.i(TAG, "Process isolation requested for: " + detection.getSourceIp());
        return true;
    }

    private boolean executeRevokePermissions(@NonNull DetectionResult detection) {
        if (quarantineManager != null) {
            String packageName = detection.getEvidence();
            return quarantineManager.revokeNetworkPermissions(packageName);
        }
        return false;
    }

    /**
     * Ejecuta rollback de acciones fallidas.
     */
    private void executeRollback(@NonNull ResponseContext context) {
        Log.w(TAG, "Iniciando rollback para respuesta: " + context.getResponseId());

        try {
            // Rollback en orden inverso
            List<ResponsePolicyEngine.ResponseAction> completedActions = new ArrayList<>(context.getCompletedActions());
            Collections.reverse(completedActions);

            for (ResponsePolicyEngine.ResponseAction action : completedActions) {
                rollbackAction(action, context.getDetection());
            }

            context.markRolledBack();
            rollbacksExecuted.incrementAndGet();
            notifyRollbackExecuted(context.getResponseId());
            logRollbackExecuted(context.getResponseId(), context);

        } catch (Exception e) {
            Log.e(TAG, "Error en rollback", e);
        }
    }

    /**
     * Rollback de una acción individual.
     */
    private void rollbackAction(@NonNull ResponsePolicyEngine.ResponseAction action, @NonNull DetectionResult detection) {
        try {
            switch (action) {
                case BLOCK_IP:
                    // Remover regla de bloqueo
                    ruleEngine.removeRule("Auto-" + detection.getThreatType());
                    break;
                case QUARANTINE_APP:
                    if (quarantineManager != null) {
                        String packageName = detection.getEvidence();
                        quarantineManager.releaseQuarantine(packageName);
                    }
                    break;
                case RATE_LIMIT:
                    if (rateLimiter != null) {
                        rateLimiter.unlimitIP(detection.getSourceIp());
                    }
                    break;
                // Otras acciones no tienen rollback directo
                default:
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en rollback de acción: " + action, e);
        }
    }

    /**
     * Registra un listener.
     */
    public void addListener(@NonNull ResponseListener listener) {
        listeners.add(listener);
    }

    /**
     * Remueve un listener.
     */
    public void removeListener(@NonNull ResponseListener listener) {
        listeners.remove(listener);
    }

    private void notifyResponseStarted(String responseId, DetectionResult detection) {
        for (ResponseListener listener : listeners) {
            try {
                listener.onResponseStarted(responseId, detection);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    private void notifyResponseCompleted(String responseId, ResponseContext context) {
        for (ResponseListener listener : listeners) {
            try {
                listener.onResponseCompleted(responseId, context);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    private void notifyResponseFailed(String responseId, String reason) {
        for (ResponseListener listener : listeners) {
            try {
                listener.onResponseFailed(responseId, reason);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    private void notifyRollbackExecuted(String responseId) {
        for (ResponseListener listener : listeners) {
            try {
                listener.onRollbackExecuted(responseId);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    private void logResponseQueued(String taskId, DetectionResult detection, List<ResponsePolicyEngine.ResponseAction> actions) {
        if (sentryCore == null) return;
        
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("taskId", taskId);
            metadata.put("threatType", detection.getThreatType());
            metadata.put("threatLevel", detection.getThreatLevel().name());
            metadata.put("actions", actions.toString());

            LogEvent event = LogEvent.builder()
                    .timestamp(System.currentTimeMillis())
                    .source("RESPONSE_ORCHESTRATOR")
                    .message("Response queued: " + taskId)
                    .severity(LogEvent.Severity.INFO)
                    .metadata(metadata)
                    .build();

            sentryCore.appendEvent(event);
        } catch (Exception e) {
            Log.e(TAG, "Error logging response queued", e);
        }
    }

    private void logResponseCompleted(String responseId, ResponseContext context) {
        if (sentryCore == null) return;
        
        try {
            long duration = System.currentTimeMillis() - context.getStartTime();
            
            Map<String, String> metadata = new HashMap<>();
            metadata.put("responseId", responseId);
            metadata.put("duration", String.valueOf(duration));
            metadata.put("actionsCompleted", String.valueOf(context.getCompletedActions().size()));
            metadata.put("totalActions", String.valueOf(context.getActions().size()));

            LogEvent event = LogEvent.builder()
                    .timestamp(System.currentTimeMillis())
                    .source("RESPONSE_ORCHESTRATOR")
                    .message("Response completed: " + responseId)
                    .severity(LogEvent.Severity.INFO)
                    .metadata(metadata)
                    .build();

            sentryCore.appendEvent(event);
        } catch (Exception e) {
            Log.e(TAG, "Error logging response completed", e);
        }
    }

    private void logRollbackExecuted(String responseId, ResponseContext context) {
        if (sentryCore == null) return;
        
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("responseId", responseId);
            metadata.put("failureReason", context.getFailureReason());
            metadata.put("actionsCompleted", String.valueOf(context.getCompletedActions().size()));

            LogEvent event = LogEvent.builder()
                    .timestamp(System.currentTimeMillis())
                    .source("RESPONSE_ORCHESTRATOR")
                    .message("Rollback executed: " + responseId)
                    .severity(LogEvent.Severity.ERROR)
                    .metadata(metadata)
                    .build();

            sentryCore.appendEvent(event);
        } catch (Exception e) {
            Log.e(TAG, "Error logging rollback", e);
        }
    }

    /**
     * Obtiene estadísticas del orquestador.
     */
    public OrchestratorStatistics getStatistics() {
        OrchestratorStatistics stats = new OrchestratorStatistics();
        stats.responsesExecuted = responsesExecuted.get();
        stats.responsesFailed = responsesFailed.get();
        stats.rollbacksExecuted = rollbacksExecuted.get();
        stats.activeResponses = activeResponses.size();
        stats.queueSize = responseQueue.size();
        return stats;
    }

    /**
     * Estadísticas del orquestador.
     */
    public static final class OrchestratorStatistics {
        public long responsesExecuted;
        public long responsesFailed;
        public long rollbacksExecuted;
        public int activeResponses;
        public int queueSize;

        @Override
        public String toString() {
            return String.format(
                    "Orchestrator{executed=%d, failed=%d, rollbacks=%d, active=%d, queue=%d}",
                    responsesExecuted, responsesFailed, rollbacksExecuted, activeResponses, queueSize
            );
        }
    }
}
