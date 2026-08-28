package com.odin.core.network.response;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatDetectionEngine;
import com.odin.core.network.rules.DynamicRuleEngine;
import com.odin.core.persistence.SentryCore;
import com.odin.core.exception.PersistenceException;

/**
 * AutomatedResponseSystem: Sistema de respuesta automatizada.
 * 
 * Integra todos los componentes de respuesta automatizada con el sistema existente:
 * - ResponseOrchestrator
 * - QuarantineManager
 * - RateLimiter
 * - ResponsePolicyEngine
 * - IncidentResponder
 * - DynamicRuleEngine
 * - ThreatDetectionEngine
 * - SentryCore
 * 
 * Patrón: Singleton thread-safe
 */
public final class AutomatedResponseSystem {

    private static final String TAG = "AutomatedResponseSystem";
    private static volatile AutomatedResponseSystem instance;

    private final ResponseOrchestrator responseOrchestrator;
    private final QuarantineManager quarantineManager;
    private final RateLimiter rateLimiter;
    private final ResponsePolicyEngine responsePolicyEngine;
    private final IncidentResponder incidentResponder;
    private final DynamicRuleEngine dynamicRuleEngine;
    private SentryCore sentryCore;
    private ThreatDetectionEngine threatDetectionEngine;

    private volatile boolean isInitialized;
    private volatile boolean isRunning;

    /**
     * Constructor privado.
     */
    private AutomatedResponseSystem() {
        this.dynamicRuleEngine = new DynamicRuleEngine();
        this.responseOrchestrator = new ResponseOrchestrator(dynamicRuleEngine);
        this.quarantineManager = new QuarantineManager(null); // Context will be set during initialization
        this.rateLimiter = new RateLimiter();
        this.responsePolicyEngine = new ResponsePolicyEngine();
        this.incidentResponder = new IncidentResponder();
        this.isInitialized = false;
        this.isRunning = false;
    }

    /**
     * Obtiene la instancia singleton.
     */
    public static AutomatedResponseSystem getInstance() {
        if (instance == null) {
            synchronized (AutomatedResponseSystem.class) {
                if (instance == null) {
                    instance = new AutomatedResponseSystem();
                }
            }
        }
        return instance;
    }

    /**
     * Inicializa el sistema con contexto.
     */
    public void initialize(@NonNull Context context) throws PersistenceException {
        if (isInitialized) {
            Log.w(TAG, "Sistema ya inicializado");
            return;
        }

        try {
            // Inicializar SentryCore
            this.sentryCore = SentryCore.getInstance(context);

            // Inicializar componentes con SentryCore
            responseOrchestrator.initializeComponents(sentryCore, responsePolicyEngine, quarantineManager, rateLimiter);
            quarantineManager.initializeSentryCore(sentryCore);
            incidentResponder.initializeSentryCore(sentryCore);

            // Inicializar ThreatDetectionEngine
            this.threatDetectionEngine = new ThreatDetectionEngine(context);

            // Registrar listener en ThreatDetectionEngine
            threatDetectionEngine.addListener(new ThreatDetectionEngine.ThreatDetectionListener() {
                @Override
                public void onThreatDetected(DetectionResult detection) {
                    handleThreatDetection(detection);
                }

                @Override
                public void onAnomalyDetected(String anomalyType, int severity) {
                    handleAnomalyDetection(anomalyType, severity);
                }
            });

            // Iniciar componentes
            responseOrchestrator.start();
            quarantineManager.startMonitoring();
            rateLimiter.start();

            isInitialized = true;
            Log.i(TAG, "Sistema de respuesta automatizada inicializado exitosamente");

        } catch (Exception e) {
            Log.e(TAG, "Error inicializando sistema de respuesta automatizada", e);
            throw new PersistenceException(
                    "Error inicializando AutomatedResponseSystem",
                    e,
                    com.odin.core.exception.OdinException.ErrorSeverity.CRITICAL
            );
        }
    }

    /**
     * Inicia el sistema de respuesta.
     */
    public void start() {
        if (!isInitialized) {
            throw new IllegalStateException("Sistema no inicializado. Llamar initialize() primero.");
        }

        if (!isRunning) {
            try {
                threatDetectionEngine.start();
                isRunning = true;
                Log.i(TAG, "Sistema de respuesta automatizada iniciado");
            } catch (Exception e) {
                Log.e(TAG, "Error iniciando sistema", e);
            }
        }
    }

    /**
     * Detiene el sistema de respuesta.
     */
    public void stop() {
        if (isRunning) {
            try {
                threatDetectionEngine.stop();
                responseOrchestrator.stop();
                quarantineManager.stopMonitoring();
                rateLimiter.stop();
                dynamicRuleEngine.shutdown();
                isRunning = false;
                Log.i(TAG, "Sistema de respuesta automatizada detenido");
            } catch (Exception e) {
                Log.e(TAG, "Error deteniendo sistema", e);
            }
        }
    }

    /**
     * Maneja una detección de amenaza.
     */
    private void handleThreatDetection(@NonNull DetectionResult detection) {
        try {
            // 1. Evaluar con DynamicRuleEngine
            DynamicRuleEngine.RuleAction ruleAction = dynamicRuleEngine.evaluateDetection(detection);

            // 2. Clasificar como incidente si es significativo
            IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
            if (incident != null) {
                // Añadir detección relacionada al incidente
                incidentResponder.addRelatedDetection(incident.getIncidentId(), detection);
            }

            // 3. Enviar a ResponseOrchestrator
            responseOrchestrator.submitDetection(detection);

            // 4. Aplicar rate limiting si es necesario
            if (ruleAction == DynamicRuleEngine.RuleAction.RATE_LIMIT) {
                rateLimiter.limitIP(detection.getSourceIp(), 100, 60);
            }

            Log.d(TAG, "Threat detection handled: " + detection.getThreatType());

        } catch (Exception e) {
            Log.e(TAG, "Error manejando detección de amenaza", e);
        }
    }

    /**
     * Maneja una detección de anomalía.
     */
    private void handleAnomalyDetection(String anomalyType, int severity) {
        try {
            // Las anomalías de baja severidad solo se loguean
            if (severity < 3) {
                Log.d(TAG, "Anomaly detected (low severity): " + anomalyType);
                return;
            }

            // Anomalías de alta severidad pueden requerir acción
            Log.w(TAG, "Anomaly detected (high severity): " + anomalyType + ", severity: " + severity);

            // Aquí se podría tomar acción basada en el tipo de anomalía
            // Por ejemplo, si es demasiados paquetes por segundo, aplicar rate limiting

        } catch (Exception e) {
            Log.e(TAG, "Error manejando detección de anomalía", e);
        }
    }

    /**
     * Obtiene el ResponseOrchestrator.
     */
    @NonNull
    public ResponseOrchestrator getResponseOrchestrator() {
        return responseOrchestrator;
    }

    /**
     * Obtiene el QuarantineManager.
     */
    @NonNull
    public QuarantineManager getQuarantineManager() {
        return quarantineManager;
    }

    /**
     * Obtiene el RateLimiter.
     */
    @NonNull
    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    /**
     * Obtiene el ResponsePolicyEngine.
     */
    @NonNull
    public ResponsePolicyEngine getResponsePolicyEngine() {
        return responsePolicyEngine;
    }

    /**
     * Obtiene el IncidentResponder.
     */
    @NonNull
    public IncidentResponder getIncidentResponder() {
        return incidentResponder;
    }

    /**
     * Obtiene el DynamicRuleEngine.
     */
    @NonNull
    public DynamicRuleEngine getDynamicRuleEngine() {
        return dynamicRuleEngine;
    }

    /**
     * Obtiene el SentryCore.
     */
    @Nullable
    public SentryCore getSentryCore() {
        return sentryCore;
    }

    /**
     * Obtiene el ThreatDetectionEngine.
     */
    @Nullable
    public ThreatDetectionEngine getThreatDetectionEngine() {
        return threatDetectionEngine;
    }

    /**
     * Verifica si el sistema está inicializado.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Verifica si el sistema está corriendo.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Obtiene estadísticas consolidadas del sistema.
     */
    public SystemStatistics getSystemStatistics() {
        SystemStatistics stats = new SystemStatistics();
        stats.ruleEngineStats = dynamicRuleEngine.getStatistics();
        stats.orchestratorStats = responseOrchestrator.getStatistics();
        stats.quarantineStats = quarantineManager.getStatistics();
        stats.rateLimiterStats = rateLimiter.getStatistics();
        stats.policyEngineStats = responsePolicyEngine.getStatistics();
        stats.responderStats = incidentResponder.getStatistics();
        return stats;
    }

    /**
     * Estadísticas consolidadas del sistema.
     */
    public static final class SystemStatistics {
        public DynamicRuleEngine.RuleEngineStatistics ruleEngineStats;
        public ResponseOrchestrator.OrchestratorStatistics orchestratorStats;
        public QuarantineManager.QuarantineStatistics quarantineStats;
        public RateLimiter.RateLimiterStatistics rateLimiterStats;
        public ResponsePolicyEngine.PolicyEngineStatistics policyEngineStats;
        public IncidentResponder.ResponderStatistics responderStats;

        @Override
        public String toString() {
            return String.format(
                    "SystemStatistics{\n  Rules: %s\n  Orchestrator: %s\n  Quarantine: %s\n  RateLimiter: %s\n  Policy: %s\n  Responder: %s\n}",
                    ruleEngineStats, orchestratorStats, quarantineStats, rateLimiterStats, policyEngineStats, responderStats
            );
        }
    }

    /**
     * Reinicia la instancia singleton (para testing).
     */
    public static void resetInstance() {
        if (instance != null && instance.isRunning) {
            instance.stop();
        }
        instance = null;
    }
}
