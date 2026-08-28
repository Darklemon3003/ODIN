package com.odin.core.network.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatLevel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ResponsePolicyEngine: Motor de políticas de respuesta.
 * 
 * Responsabilidades:
 * - Definición de políticas de respuesta
 * - Mapeo de amenazas a acciones
 * - Context-aware response policies
 * - Políticas basadas en severidad
 * - Políticas basadas en confianza
 * - Validación de políticas
 * - Versionado de políticas
 * 
 * Thread-safe: Concurrent collections
 */
public final class ResponsePolicyEngine {

    /**
     * Acciones de respuesta disponibles.
     */
    public enum ResponseAction {
        BLOCK("Block traffic"),
        BLOCK_IP("Block IP address"),
        BLOCK_PORT("Block port"),
        QUARANTINE("Quarantine application"),
        QUARANTINE_APP("Quarantine application"),
        ALERT("Send alert"),
        ALERT_ADMIN("Alert administrator"),
        RATE_LIMIT("Apply rate limiting"),
        LOG("Log event"),
        LOG_EVENT("Log event"),
        NOTIFY_ADMIN("Notify administrator"),
        ISOLATE("Isolate from network"),
        ISOLATE_PROCESS("Isolate process"),
        TERMINATE("Terminate connection"),
        MONITOR("Increase monitoring"),
        GENERATE_RULE("Generate firewall rule"),
        REVOKE_PERMISSIONS("Revoke permissions");

        private final String description;

        ResponseAction(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private final Map<String, ResponsePolicy> policies;
    private final Map<ThreatLevel, List<ResponseAction>> severityBasedActions;
    private final Map<String, List<ResponseAction>> threatTypeActions;
    private final Map<String, Double> trustScores;
    private final List<PolicyChangeListener> listeners;
    private final AtomicLong policyVersion;
    private final AtomicLong policyEvaluationCount;

    /**
     * Política de respuesta.
     */
    public static final class ResponsePolicy implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String policyId;
        private final String name;
        private final String description;
        private final long version;
        private final long createdTime;
        private final List<ResponseAction> actions;
        private final PolicyCondition condition;
        private volatile boolean enabled;
        private volatile int priority;

        public ResponsePolicy(
                String policyId,
                String name,
                String description,
                long version,
                List<ResponseAction> actions,
                PolicyCondition condition
        ) {
            this.policyId = policyId;
            this.name = name;
            this.description = description;
            this.version = version;
            this.createdTime = System.currentTimeMillis();
            this.actions = new ArrayList<>(actions);
            this.condition = condition;
            this.enabled = true;
            this.priority = 0;
        }

        public String getPolicyId() { return policyId; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public long getVersion() { return version; }
        public long getCreatedTime() { return createdTime; }
        public List<ResponseAction> getActions() { return new ArrayList<>(actions); }
        public PolicyCondition getCondition() { return condition; }
        public boolean isEnabled() { return enabled; }
        public int getPriority() { return priority; }

        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public void setPriority(int priority) { this.priority = priority; }
    }

    /**
     * Condición de política.
     */
    public static final class PolicyCondition implements Serializable {
        private static final long serialVersionUID = 1L;

        private final ThreatLevel minThreatLevel;
        private final String threatTypePattern;
        private final double minConfidence;
        private final String sourceIPPattern;
        private final String destIPPattern;
        private final Double minTrustScore;

        public PolicyCondition(
                ThreatLevel minThreatLevel,
                String threatTypePattern,
                double minConfidence,
                String sourceIPPattern,
                String destIPPattern,
                Double minTrustScore
        ) {
            this.minThreatLevel = minThreatLevel;
            this.threatTypePattern = threatTypePattern;
            this.minConfidence = minConfidence;
            this.sourceIPPattern = sourceIPPattern;
            this.destIPPattern = destIPPattern;
            this.minTrustScore = minTrustScore;
        }

        public boolean matches(DetectionResult detection, Double trustScore) {
            // Verificar nivel de amenaza
            if (detection.getThreatLevel().getSeverity() < minThreatLevel.getSeverity()) {
                return false;
            }

            // Verificar tipo de amenaza
            if (threatTypePattern != null && !detection.getThreatType().matches(threatTypePattern)) {
                return false;
            }

            // Verificar confianza
            if (detection.getConfidence() < minConfidence) {
                return false;
            }

            // Verificar IP origen
            if (sourceIPPattern != null && !detection.getSourceIp().matches(sourceIPPattern.replace("*", ""))) {
                return false;
            }

            // Verificar IP destino
            if (destIPPattern != null && !detection.getDestinationIp().matches(destIPPattern.replace("*", ""))) {
                return false;
            }

            // Verificar score de confianza
            if (minTrustScore != null && trustScore != null && trustScore < minTrustScore) {
                return false;
            }

            return true;
        }
    }

    /**
     * Listener para cambios en políticas.
     */
    public interface PolicyChangeListener {
        void onPolicyAdded(ResponsePolicy policy);
        void onPolicyRemoved(String policyId);
        void onPolicyUpdated(ResponsePolicy policy);
        void onPolicyEnabled(String policyId, boolean enabled);
    }

    /**
     * Constructor.
     */
    public ResponsePolicyEngine() {
        this.policies = new ConcurrentHashMap<>();
        this.severityBasedActions = new ConcurrentHashMap<>();
        this.threatTypeActions = new ConcurrentHashMap<>();
        this.trustScores = new ConcurrentHashMap<>();
        this.listeners = new CopyOnWriteArrayList<>();
        this.policyVersion = new AtomicLong(1);
        this.policyEvaluationCount = new AtomicLong(0);

        // Inicializar políticas por defecto
        initializeDefaultPolicies();
    }

    /**
     * Inicializa políticas por defecto.
     */
    private void initializeDefaultPolicies() {
        // Políticas basadas en severidad
        List<ResponseAction> criticalActions = List.of(
                ResponseAction.BLOCK_IP,
                ResponseAction.QUARANTINE_APP,
                ResponseAction.GENERATE_RULE,
                ResponseAction.ALERT_ADMIN
        );
        severityBasedActions.put(ThreatLevel.CRITICAL, new ArrayList<>(criticalActions));

        List<ResponseAction> highActions = List.of(
                ResponseAction.BLOCK_IP,
                ResponseAction.GENERATE_RULE,
                ResponseAction.ALERT_ADMIN
        );
        severityBasedActions.put(ThreatLevel.HIGH, new ArrayList<>(highActions));

        List<ResponseAction> mediumActions = List.of(
                ResponseAction.RATE_LIMIT,
                ResponseAction.GENERATE_RULE,
                ResponseAction.LOG_EVENT
        );
        severityBasedActions.put(ThreatLevel.MEDIUM, new ArrayList<>(mediumActions));

        List<ResponseAction> lowActions = List.of(
                ResponseAction.LOG_EVENT,
                ResponseAction.ALERT_ADMIN
        );
        severityBasedActions.put(ThreatLevel.LOW, new ArrayList<>(lowActions));

        List<ResponseAction> infoActions = List.of(
                ResponseAction.LOG_EVENT
        );
        severityBasedActions.put(ThreatLevel.INFO, new ArrayList<>(infoActions));

        // Políticas específicas por tipo de amenaza
        threatTypeActions.put("C2_COMMUNICATION", List.of(
                ResponseAction.BLOCK_IP,
                ResponseAction.QUARANTINE_APP,
                ResponseAction.ALERT_ADMIN
        ));

        threatTypeActions.put("PORT_SCAN", List.of(
                ResponseAction.RATE_LIMIT,
                ResponseAction.GENERATE_RULE,
                ResponseAction.ALERT_ADMIN
        ));

        threatTypeActions.put("EXFILTRATION", List.of(
                ResponseAction.BLOCK_IP,
                ResponseAction.QUARANTINE_APP,
                ResponseAction.ALERT_ADMIN
        ));

        threatTypeActions.put("MALWARE", List.of(
                ResponseAction.BLOCK_IP,
                ResponseAction.QUARANTINE_APP,
                ResponseAction.GENERATE_RULE,
                ResponseAction.ALERT_ADMIN
        ));
    }

    /**
     * Añade una política personalizada.
     * 
     * @param policy Política a añadir
     * @return true si se añadió exitosamente
     */
    public boolean addPolicy(@NonNull ResponsePolicy policy) {
        if (!validatePolicy(policy)) {
            return false;
        }

        policies.put(policy.getPolicyId(), policy);
        policyVersion.incrementAndGet();
        notifyPolicyAdded(policy);

        return true;
    }

    /**
     * Remueve una política.
     * 
     * @param policyId ID de la política
     * @return true si se removió exitosamente
     */
    public boolean removePolicy(@NonNull String policyId) {
        ResponsePolicy removed = policies.remove(policyId);
        if (removed != null) {
            policyVersion.incrementAndGet();
            notifyPolicyRemoved(policyId);
            return true;
        }
        return false;
    }

    /**
     * Actualiza una política existente.
     * 
     * @param policy Política actualizada
     * @return true si se actualizó exitosamente
     */
    public boolean updatePolicy(@NonNull ResponsePolicy policy) {
        if (!policies.containsKey(policy.getPolicyId())) {
            return false;
        }

        if (!validatePolicy(policy)) {
            return false;
        }

        policies.put(policy.getPolicyId(), policy);
        policyVersion.incrementAndGet();
        notifyPolicyUpdated(policy);

        return true;
    }

    /**
     * Habilita/deshabilita una política.
     * 
     * @param policyId ID de la política
     * @param enabled Estado
     * @return true si se actualizó exitosamente
     */
    public boolean setPolicyEnabled(@NonNull String policyId, boolean enabled) {
        ResponsePolicy policy = policies.get(policyId);
        if (policy == null) {
            return false;
        }

        policy.setEnabled(enabled);
        policyVersion.incrementAndGet();
        notifyPolicyEnabled(policyId, enabled);

        return true;
    }

    /**
     * Obtiene las acciones de respuesta para una detección.
     * 
     * @param detection Resultado de detección
     * @return Lista de acciones de respuesta
     */
    @NonNull
    public List<ResponseAction> getActionsForThreat(@NonNull DetectionResult detection) {
        policyEvaluationCount.incrementAndGet();

        // Primero verificar políticas personalizadas
        List<ResponseAction> customActions = evaluateCustomPolicies(detection);
        if (!customActions.isEmpty()) {
            return customActions;
        }

        // Segundo verificar políticas por tipo de amenaza
        List<ResponseAction> typeActions = threatTypeActions.get(detection.getThreatType());
        if (typeActions != null && !typeActions.isEmpty()) {
            return new ArrayList<>(typeActions);
        }

        // Tercero usar políticas por severidad (default)
        List<ResponseAction> severityActions = severityBasedActions.get(detection.getThreatLevel());
        if (severityActions != null) {
            return new ArrayList<>(severityActions);
        }

        // Por defecto, solo log
        return List.of(ResponseAction.LOG_EVENT);
    }

    /**
     * Evalúa políticas personalizadas.
     */
    @NonNull
    private List<ResponseAction> evaluateCustomPolicies(@NonNull DetectionResult detection) {
        List<ResponseAction> matchedActions = new ArrayList<>();
        Double trustScore = trustScores.get(detection.getSourceIp());

        // Evaluar políticas en orden de prioridad
        List<ResponsePolicy> sortedPolicies = new ArrayList<>(policies.values());
        sortedPolicies.sort((p1, p2) -> Integer.compare(p2.getPriority(), p1.getPriority()));

        for (ResponsePolicy policy : sortedPolicies) {
            if (!policy.isEnabled()) {
                continue;
            }

            if (policy.getCondition().matches(detection, trustScore)) {
                matchedActions.addAll(policy.getActions());
                break; // Usar solo la primera política que coincida
            }
        }

        return matchedActions;
    }

    /**
     * Valida una política.
     * 
     * @param policy Política a validar
     * @return true si es válida
     */
    private boolean validatePolicy(@NonNull ResponsePolicy policy) {
        // Verificar que tenga ID
        if (policy.getPolicyId() == null || policy.getPolicyId().isEmpty()) {
            return false;
        }

        // Verificar que tenga nombre
        if (policy.getName() == null || policy.getName().isEmpty()) {
            return false;
        }

        // Verificar que tenga acciones
        if (policy.getActions() == null || policy.getActions().isEmpty()) {
            return false;
        }

        // Verificar que tenga condición
        if (policy.getCondition() == null) {
            return false;
        }

        return true;
    }

    /**
     * Establece el score de confianza para una IP.
     * 
     * @param ip Dirección IP
     * @param score Score de confianza (0.0 - 1.0)
     */
    public void setTrustScore(@NonNull String ip, double score) {
        trustScores.put(ip, Math.max(0.0, Math.min(1.0, score)));
    }

    /**
     * Obtiene el score de confianza de una IP.
     * 
     * @param ip Dirección IP
     * @return Score de confianza, o null si no existe
     */
    @Nullable
    public Double getTrustScore(@NonNull String ip) {
        return trustScores.get(ip);
    }

    /**
     * Crea una política basada en severidad.
     * 
     * @param name Nombre de la política
     * @param minThreatLevel Nivel mínimo de amenaza
     * @param actions Acciones de respuesta
     * @return Política creada
     */
    @NonNull
    public ResponsePolicy createSeverityBasedPolicy(
            @NonNull String name,
            @NonNull ThreatLevel minThreatLevel,
            @NonNull List<ResponseAction> actions
    ) {
        String policyId = "POLICY_" + System.currentTimeMillis();
        PolicyCondition condition = new PolicyCondition(
                minThreatLevel,
                null,
                0.0,
                null,
                null,
                null
        );

        return new ResponsePolicy(policyId, name, "Severity-based policy", policyVersion.get(), actions, condition);
    }

    /**
     * Crea una política basada en tipo de amenaza.
     * 
     * @param name Nombre de la política
     * @param threatTypePattern Patrón de tipo de amenaza
     * @param actions Acciones de respuesta
     * @return Política creada
     */
    @NonNull
    public ResponsePolicy createThreatTypePolicy(
            @NonNull String name,
            @NonNull String threatTypePattern,
            @NonNull List<ResponseAction> actions
    ) {
        String policyId = "POLICY_" + System.currentTimeMillis();
        PolicyCondition condition = new PolicyCondition(
                ThreatLevel.INFO,
                threatTypePattern,
                0.0,
                null,
                null,
                null
        );

        return new ResponsePolicy(policyId, name, "Threat-type-based policy", policyVersion.get(), actions, condition);
    }

    /**
     * Crea una política basada en confianza.
     * 
     * @param name Nombre de la política
     * @param minTrustScore Score mínimo de confianza
     * @param actions Acciones de respuesta
     * @return Política creada
     */
    @NonNull
    public ResponsePolicy createTrustBasedPolicy(
            @NonNull String name,
            double minTrustScore,
            @NonNull List<ResponseAction> actions
    ) {
        String policyId = "POLICY_" + System.currentTimeMillis();
        PolicyCondition condition = new PolicyCondition(
                ThreatLevel.INFO,
                null,
                0.0,
                null,
                null,
                minTrustScore
        );

        return new ResponsePolicy(policyId, name, "Trust-based policy", policyVersion.get(), actions, condition);
    }

    /**
     * Obtiene todas las políticas.
     * 
     * @return Lista de políticas
     */
    @NonNull
    public List<ResponsePolicy> getAllPolicies() {
        return new ArrayList<>(policies.values());
    }

    /**
     * Obtiene una política por ID.
     * 
     * @param policyId ID de la política
     * @return Política, o null si no existe
     */
    @Nullable
    public ResponsePolicy getPolicy(@NonNull String policyId) {
        return policies.get(policyId);
    }

    /**
     * Registra un listener.
     */
    public void addListener(@NonNull PolicyChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Remueve un listener.
     */
    public void removeListener(@NonNull PolicyChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyPolicyAdded(ResponsePolicy policy) {
        for (PolicyChangeListener listener : listeners) {
            try {
                listener.onPolicyAdded(policy);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    private void notifyPolicyRemoved(String policyId) {
        for (PolicyChangeListener listener : listeners) {
            try {
                listener.onPolicyRemoved(policyId);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    private void notifyPolicyUpdated(ResponsePolicy policy) {
        for (PolicyChangeListener listener : listeners) {
            try {
                listener.onPolicyUpdated(policy);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    private void notifyPolicyEnabled(String policyId, boolean enabled) {
        for (PolicyChangeListener listener : listeners) {
            try {
                listener.onPolicyEnabled(policyId, enabled);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    /**
     * Obtiene estadísticas del motor de políticas.
     */
    public PolicyEngineStatistics getStatistics() {
        PolicyEngineStatistics stats = new PolicyEngineStatistics();
        stats.totalPolicies = policies.size();
        stats.policyVersion = policyVersion.get();
        stats.evaluationCount = policyEvaluationCount.get();
        stats.enabledPolicies = (int) policies.values().stream().filter(ResponsePolicy::isEnabled).count();
        return stats;
    }

    /**
     * Estadísticas del motor de políticas.
     */
    public static final class PolicyEngineStatistics {
        public int totalPolicies;
        public long policyVersion;
        public long evaluationCount;
        public int enabledPolicies;

        @Override
        public String toString() {
            return String.format(
                    "PolicyEngine{total=%d, version=%d, evaluations=%d, enabled=%d}",
                    totalPolicies, policyVersion, evaluationCount, enabledPolicies
            );
        }
    }
}
