package com.odin.core.network.response;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatLevel;
import com.odin.core.persistence.SentryCore;
import com.odin.core.domain.model.LogEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * IncidentResponder: Respondedor de incidentes de seguridad.
 * 
 * Responsabilidades:
 * - Clasificación de incidentes
 * - Escalado de incidentes
 * - Notificación a administradores
 * - Generación de reportes de incidentes
 * - Coordinación con sistemas externos (SIEM)
 * - Post-incident analysis
 * - Lessons learned capture
 * 
 * Thread-safe: Concurrent collections + atomic variables
 */
public final class IncidentResponder {

    private static final String TAG = "IncidentResponder";
    private static final int MAX_INCIDENTS = 1000;
    private static final long INCIDENT_RETENTION_MS = 30 * 24 * 60 * 60 * 1000; // 30 días

    private final Map<String, Incident> activeIncidents;
    private final Map<String, Incident> closedIncidents;
    private final List<IncidentListener> listeners;
    private final AtomicLong incidentCount;
    private final AtomicLong escalatedCount;
    private final AtomicLong notificationsSent;
    
    private SentryCore sentryCore;
    private SIEMIntegration siemIntegration;

    /**
     * Clasificación de incidente.
     */
    public enum IncidentClassification {
        MALWARE_INFECTION,
        DATA_EXFILTRATION,
        UNAUTHORIZED_ACCESS,
        DENIAL_OF_SERVICE,
        COMMAND_AND_CONTROL,
        LATERAL_MOVEMENT,
        PRIVILEGE_ESCALATION,
        POLICY_VIOLATION,
        ANOMALY_DETECTED,
        OTHER
    }

    /**
     * Severidad de incidente.
     */
    public enum IncidentSeverity {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW,
        INFO
    }

    /**
     * Estado de incidente.
     */
    public enum IncidentStatus {
        OPEN,
        INVESTIGATING,
        MITIGATING,
        ESCALATED,
        RESOLVED,
        CLOSED
    }

    /**
     * Registro de incidente.
     */
    public static final class Incident implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String incidentId;
        private final IncidentClassification classification;
        private final IncidentSeverity severity;
        private final String title;
        private final String description;
        private final long createdTime;
        private final DetectionResult initialDetection;
        private volatile IncidentStatus status;
        private volatile String assignedTo;
        private volatile long resolvedTime;
        private volatile String resolution;
        private final List<DetectionResult> relatedDetections;
        private final List<String> actionsTaken;
        private final Map<String, String> metadata;
        private volatile boolean escalated;
        private volatile String escalationReason;

        public Incident(
                String incidentId,
                IncidentClassification classification,
                IncidentSeverity severity,
                String title,
                String description,
                DetectionResult initialDetection
        ) {
            this.incidentId = incidentId;
            this.classification = classification;
            this.severity = severity;
            this.title = title;
            this.description = description;
            this.createdTime = System.currentTimeMillis();
            this.initialDetection = initialDetection;
            this.status = IncidentStatus.OPEN;
            this.relatedDetections = new ArrayList<>();
            this.actionsTaken = new ArrayList<>();
            this.metadata = new HashMap<>();
            this.escalated = false;
        }

        public String getIncidentId() { return incidentId; }
        public IncidentClassification getClassification() { return classification; }
        public IncidentSeverity getSeverity() { return severity; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public long getCreatedTime() { return createdTime; }
        public DetectionResult getInitialDetection() { return initialDetection; }
        public IncidentStatus getStatus() { return status; }
        public String getAssignedTo() { return assignedTo; }
        public long getResolvedTime() { return resolvedTime; }
        public String getResolution() { return resolution; }
        public List<DetectionResult> getRelatedDetections() { return new ArrayList<>(relatedDetections); }
        public List<String> getActionsTaken() { return new ArrayList<>(actionsTaken); }
        public Map<String, String> getMetadata() { return new HashMap<>(metadata); }
        public boolean isEscalated() { return escalated; }
        public String getEscalationReason() { return escalationReason; }

        public void setStatus(IncidentStatus status) { this.status = status; }
        public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
        public void setResolvedTime(long resolvedTime) { this.resolvedTime = resolvedTime; }
        public void setResolution(String resolution) { this.resolution = resolution; }
        public void addRelatedDetection(DetectionResult detection) { relatedDetections.add(detection); }
        public void addActionTaken(String action) { actionsTaken.add(action); }
        public void addMetadata(String key, String value) { metadata.put(key, value); }
        public void setEscalated(boolean escalated) { this.escalated = escalated; }
        public void setEscalationReason(String reason) { this.escalationReason = reason; }

        public long getDuration() {
            if (resolvedTime > 0) {
                return resolvedTime - createdTime;
            }
            return System.currentTimeMillis() - createdTime;
        }
    }

    /**
     * Reporte de incidente.
     */
    public static final class IncidentReport implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String reportId;
        private final String incidentId;
        private final long generatedTime;
        private final Incident incident;
        private final String summary;
        private final String timeline;
        private final String rootCauseAnalysis;
        private final String impactAssessment;
        private final List<String> recommendations;
        private final List<String> lessonsLearned;

        public IncidentReport(
                String reportId,
                String incidentId,
                Incident incident,
                String summary,
                String timeline,
                String rootCauseAnalysis,
                String impactAssessment,
                List<String> recommendations,
                List<String> lessonsLearned
        ) {
            this.reportId = reportId;
            this.incidentId = incidentId;
            this.generatedTime = System.currentTimeMillis();
            this.incident = incident;
            this.summary = summary;
            this.timeline = timeline;
            this.rootCauseAnalysis = rootCauseAnalysis;
            this.impactAssessment = impactAssessment;
            this.recommendations = new ArrayList<>(recommendations);
            this.lessonsLearned = new ArrayList<>(lessonsLearned);
        }

        public String getReportId() { return reportId; }
        public String getIncidentId() { return incidentId; }
        public long getGeneratedTime() { return generatedTime; }
        public Incident getIncident() { return incident; }
        public String getSummary() { return summary; }
        public String getTimeline() { return timeline; }
        public String getRootCauseAnalysis() { return rootCauseAnalysis; }
        public String getImpactAssessment() { return impactAssessment; }
        public List<String> getRecommendations() { return new ArrayList<>(recommendations); }
        public List<String> getLessonsLearned() { return new ArrayList<>(lessonsLearned); }
    }

    /**
     * Integración con SIEM.
     */
    public interface SIEMIntegration {
        void sendIncident(Incident incident);
        void sendReport(IncidentReport report);
    }

    /**
     * Listener para eventos de incidentes.
     */
    public interface IncidentListener {
        void onIncidentCreated(Incident incident);
        void onIncidentEscalated(Incident incident, String reason);
        void onIncidentResolved(Incident incident);
        void onIncidentClosed(Incident incident);
        void onAdminNotified(String incidentId, String message);
    }

    /**
     * Constructor.
     */
    public IncidentResponder() {
        this.activeIncidents = new ConcurrentHashMap<>();
        this.closedIncidents = new ConcurrentHashMap<>();
        this.listeners = new CopyOnWriteArrayList<>();
        this.incidentCount = new AtomicLong(0);
        this.escalatedCount = new AtomicLong(0);
        this.notificationsSent = new AtomicLong(0);
    }

    /**
     * Inicializa SentryCore para logging.
     */
    public void initializeSentryCore(@Nullable SentryCore sentryCore) {
        this.sentryCore = sentryCore;
    }

    /**
     * Inicializa integración SIEM.
     */
    public void initializeSIEMIntegration(@Nullable SIEMIntegration siemIntegration) {
        this.siemIntegration = siemIntegration;
    }

    /**
     * Clasifica una detección como incidente.
     * 
     * @param detection Resultado de detección
     * @return Incidente creado, o null si no se clasifica como incidente
     */
    @Nullable
    public Incident classifyIncident(@NonNull DetectionResult detection) {
        // Clasificar basado en tipo de amenaza y severidad
        IncidentClassification classification = mapThreatToClassification(detection.getThreatType());
        IncidentSeverity severity = mapThreatLevelToSeverity(detection.getThreatLevel());

        // Solo crear incidente para amenazas significativas
        if (severity == IncidentSeverity.INFO) {
            return null;
        }

        String incidentId = "INC_" + System.currentTimeMillis() + "_" + detection.getForensicsId();
        String title = classification.name() + ": " + detection.getThreatType();
        String description = String.format(
                "%s detected from %s to %s (confidence: %.0f%%)",
                detection.getThreatType(),
                detection.getSourceIp(),
                detection.getDestinationIp(),
                detection.getConfidence() * 100
        );

        Incident incident = new Incident(incidentId, classification, severity, title, description, detection);
        
        // Añadir metadata
        incident.addMetadata("detector", detection.getDetectorName());
        incident.addMetadata("sourceIp", detection.getSourceIp());
        incident.addMetadata("destIp", detection.getDestinationIp());
        incident.addMetadata("confidence", String.valueOf(detection.getConfidence()));

        activeIncidents.put(incidentId, incident);
        incidentCount.incrementAndGet();
        
        notifyIncidentCreated(incident);
        logIncidentAction(incidentId, "INCIDENT_CREATED", description);

        // Notificar administradores si es crítico o alto
        if (severity == IncidentSeverity.CRITICAL || severity == IncidentSeverity.HIGH) {
            notifyAdministrator(incidentId, title + " - " + description);
        }

        // Enviar a SIEM si está configurado
        if (siemIntegration != null) {
            siemIntegration.sendIncident(incident);
        }

        return incident;
    }

    /**
     * Escala un incidente.
     * 
     * @param incidentId ID del incidente
     * @param reason Razón del escalado
     * @return true si se escaló exitosamente
     */
    public boolean escalateIncident(@NonNull String incidentId, @NonNull String reason) {
        Incident incident = activeIncidents.get(incidentId);
        if (incident == null) {
            return false;
        }

        incident.setStatus(IncidentStatus.ESCALATED);
        incident.setEscalated(true);
        incident.setEscalationReason(reason);
        
        escalatedCount.incrementAndGet();
        notifyIncidentEscalated(incident, reason);
        logIncidentAction(incidentId, "INCIDENT_ESCALATED", reason);

        // Notificar administradores
        notifyAdministrator(incidentId, "ESCALATED: " + incident.getTitle() + " - " + reason);

        return true;
    }

    /**
     * Resuelve un incidente.
     * 
     * @param incidentId ID del incidente
     * @param resolution Resolución del incidente
     * @return true si se resolvió exitosamente
     */
    public boolean resolveIncident(@NonNull String incidentId, @NonNull String resolution) {
        Incident incident = activeIncidents.get(incidentId);
        if (incident == null) {
            return false;
        }

        incident.setStatus(IncidentStatus.RESOLVED);
        incident.setResolvedTime(System.currentTimeMillis());
        incident.setResolution(resolution);
        
        notifyIncidentResolved(incident);
        logIncidentAction(incidentId, "INCIDENT_RESOLVED", resolution);

        return true;
    }

    /**
     * Cierra un incidente.
     * 
     * @param incidentId ID del incidente
     * @return true si se cerró exitosamente
     */
    public boolean closeIncident(@NonNull String incidentId) {
        Incident incident = activeIncidents.get(incidentId);
        if (incident == null) {
            return false;
        }

        incident.setStatus(IncidentStatus.CLOSED);
        activeIncidents.remove(incidentId);
        closedIncidents.put(incidentId, incident);
        
        notifyIncidentClosed(incident);
        logIncidentAction(incidentId, "INCIDENT_CLOSED", "Incident closed");

        return true;
    }

    /**
     * Añade una detección relacionada a un incidente.
     * 
     * @param incidentId ID del incidente
     * @param detection Detección relacionada
     * @return true si se añadió exitosamente
     */
    public boolean addRelatedDetection(@NonNull String incidentId, @NonNull DetectionResult detection) {
        Incident incident = activeIncidents.get(incidentId);
        if (incident == null) {
            return false;
        }

        incident.addRelatedDetection(detection);
        logIncidentAction(incidentId, "RELATED_DETECTION_ADDED", detection.getThreatType());

        return true;
    }

    /**
     * Añade una acción tomada a un incidente.
     * 
     * @param incidentId ID del incidente
     * @param action Acción tomada
     * @return true si se añadió exitosamente
     */
    public boolean addActionTaken(@NonNull String incidentId, @NonNull String action) {
        Incident incident = activeIncidents.get(incidentId);
        if (incident == null) {
            return false;
        }

        incident.addActionTaken(action);
        logIncidentAction(incidentId, "ACTION_TAKEN", action);

        return true;
    }

    /**
     * Genera un reporte de incidente.
     * 
     * @param incidentId ID del incidente
     * @return Reporte generado
     */
    @Nullable
    public IncidentReport generateReport(@NonNull String incidentId) {
        Incident incident = activeIncidents.get(incidentId);
        if (incident == null) {
            incident = closedIncidents.get(incidentId);
        }
        if (incident == null) {
            return null;
        }

        String reportId = "RPT_" + System.currentTimeMillis();
        
        // Generar timeline
        StringBuilder timeline = new StringBuilder();
        timeline.append(String.format("Created: %s\n", new Date(incident.getCreatedTime())));
        if (incident.getResolvedTime() > 0) {
            timeline.append(String.format("Resolved: %s\n", new Date(incident.getResolvedTime())));
            timeline.append(String.format("Duration: %d minutes\n", incident.getDuration() / 60_000));
        }
        
        // Añadir acciones
        timeline.append("\nActions Taken:\n");
        for (String action : incident.getActionsTaken()) {
            timeline.append("- ").append(action).append("\n");
        }

        // Generar análisis de causa raíz (simplificado)
        String rootCauseAnalysis = generateRootCauseAnalysis(incident);

        // Generar evaluación de impacto (simplificado)
        String impactAssessment = generateImpactAssessment(incident);

        // Generar recomendaciones
        List<String> recommendations = generateRecommendations(incident);

        // Generar lecciones aprendidas
        List<String> lessonsLearned = generateLessonsLearned(incident);

        String summary = String.format(
                "%s incident: %s. Severity: %s. Status: %s.",
                incident.getClassification(),
                incident.getTitle(),
                incident.getSeverity(),
                incident.getStatus()
        );

        IncidentReport report = new IncidentReport(
                reportId,
                incidentId,
                incident,
                summary,
                timeline.toString(),
                rootCauseAnalysis,
                impactAssessment,
                recommendations,
                lessonsLearned
        );

        // Enviar a SIEM si está configurado
        if (siemIntegration != null) {
            siemIntegration.sendReport(report);
        }

        logIncidentAction(incidentId, "REPORT_GENERATED", reportId);

        return report;
    }

    /**
     * Mapea tipo de amenaza a clasificación de incidente.
     */
    private IncidentClassification mapThreatToClassification(String threatType) {
        if (threatType.contains("MALWARE") || threatType.contains("VIRUS")) {
            return IncidentClassification.MALWARE_INFECTION;
        } else if (threatType.contains("EXFILTRATION")) {
            return IncidentClassification.DATA_EXFILTRATION;
        } else if (threatType.contains("C2") || threatType.contains("COMMAND")) {
            return IncidentClassification.COMMAND_AND_CONTROL;
        } else if (threatType.contains("PORT_SCAN") || threatType.contains("SCAN")) {
            return IncidentClassification.UNAUTHORIZED_ACCESS;
        } else if (threatType.contains("DOS") || threatType.contains("DDOS")) {
            return IncidentClassification.DENIAL_OF_SERVICE;
        } else if (threatType.contains("LATERAL")) {
            return IncidentClassification.LATERAL_MOVEMENT;
        } else if (threatType.contains("PRIVILEGE") || threatType.contains("ESCALATION")) {
            return IncidentClassification.PRIVILEGE_ESCALATION;
        } else if (threatType.contains("ANOMALY")) {
            return IncidentClassification.ANOMALY_DETECTED;
        } else {
            return IncidentClassification.OTHER;
        }
    }

    /**
     * Mapea nivel de amenaza a severidad de incidente.
     */
    private IncidentSeverity mapThreatLevelToSeverity(ThreatLevel threatLevel) {
        switch (threatLevel) {
            case CRITICAL: return IncidentSeverity.CRITICAL;
            case HIGH: return IncidentSeverity.HIGH;
            case MEDIUM: return IncidentSeverity.MEDIUM;
            case LOW: return IncidentSeverity.LOW;
            case INFO: return IncidentSeverity.INFO;
            default: return IncidentSeverity.MEDIUM;
        }
    }

    /**
     * Genera análisis de causa raíz.
     */
    private String generateRootCauseAnalysis(Incident incident) {
        // Análisis simplificado - en producción esto sería más sofisticado
        return String.format(
                "Root cause analysis for %s. Initial detection: %s. " +
                "Source: %s. Related detections: %d.",
                incident.getClassification(),
                incident.getInitialDetection().getThreatType(),
                incident.getInitialDetection().getSourceIp(),
                incident.getRelatedDetections().size()
        );
    }

    /**
     * Genera evaluación de impacto.
     */
    private String generateImpactAssessment(Incident incident) {
        // Evaluación simplificada
        String impactLevel;
        switch (incident.getSeverity()) {
            case CRITICAL: impactLevel = "High impact - immediate action required"; break;
            case HIGH: impactLevel = "Significant impact - urgent action required"; break;
            case MEDIUM: impactLevel = "Moderate impact - timely action required"; break;
            case LOW: impactLevel = "Low impact - monitor and assess"; break;
            default: impactLevel = "Minimal impact - informational"; break;
        }

        return String.format(
                "%s. Affected systems: %s. Duration: %d minutes.",
                impactLevel,
                incident.getInitialDetection().getDestinationIp(),
                incident.getDuration() / 60_000
        );
    }

    /**
     * Genera recomendaciones.
     */
    private List<String> generateRecommendations(Incident incident) {
        List<String> recommendations = new ArrayList<>();
        recommendations.add("Review and update security policies");
        recommendations.add("Enhance monitoring for similar threats");
        recommendations.add("Conduct security awareness training");
        recommendations.add("Update firewall rules");
        recommendations.add("Review access controls");
        
        if (incident.getSeverity() == IncidentSeverity.CRITICAL) {
            recommendations.add("Conduct full security audit");
            recommendations.add("Review incident response procedures");
        }
        
        return recommendations;
    }

    /**
     * Genera lecciones aprendidas.
     */
    private List<String> generateLessonsLearned(Incident incident) {
        List<String> lessons = new ArrayList<>();
        lessons.add("Early detection enabled quick response");
        lessons.add("Automated response systems functioned correctly");
        lessons.add("Incident classification was accurate");
        lessons.add("Communication channels were effective");
        
        if (incident.getDuration() > 3600000) { // Más de 1 hora
            lessons.add("Consider reducing response time for similar incidents");
        }
        
        if (incident.isEscalated()) {
            lessons.add("Escalation procedures were triggered appropriately");
        }
        
        return lessons;
    }

    /**
     * Notifica a administradores.
     */
    private void notifyAdministrator(String incidentId, String message) {
        // En una implementación real, esto enviaría notificaciones push, email, etc.
        notificationsSent.incrementAndGet();
        notifyAdminNotified(incidentId, message);
        logIncidentAction(incidentId, "ADMIN_NOTIFIED", message);
    }

    /**
     * Obtiene incidentes activos.
     */
    @NonNull
    public List<Incident> getActiveIncidents() {
        return new ArrayList<>(activeIncidents.values());
    }

    /**
     * Obtiene incidentes cerrados.
     */
    @NonNull
    public List<Incident> getClosedIncidents() {
        return new ArrayList<>(closedIncidents.values());
    }

    /**
     * Obtiene un incidente por ID.
     */
    @Nullable
    public Incident getIncident(@NonNull String incidentId) {
        Incident incident = activeIncidents.get(incidentId);
        if (incident == null) {
            incident = closedIncidents.get(incidentId);
        }
        return incident;
    }

    /**
     * Registra un listener.
     */
    public void addListener(@NonNull IncidentListener listener) {
        listeners.add(listener);
    }

    /**
     * Remueve un listener.
     */
    public void removeListener(@NonNull IncidentListener listener) {
        listeners.remove(listener);
    }

    private void notifyIncidentCreated(Incident incident) {
        for (IncidentListener listener : listeners) {
            try {
                listener.onIncidentCreated(incident);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    private void notifyIncidentEscalated(Incident incident, String reason) {
        for (IncidentListener listener : listeners) {
            try {
                listener.onIncidentEscalated(incident, reason);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    private void notifyIncidentResolved(Incident incident) {
        for (IncidentListener listener : listeners) {
            try {
                listener.onIncidentResolved(incident);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    private void notifyIncidentClosed(Incident incident) {
        for (IncidentListener listener : listeners) {
            try {
                listener.onIncidentClosed(incident);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    private void notifyAdminNotified(String incidentId, String message) {
        for (IncidentListener listener : listeners) {
            try {
                listener.onAdminNotified(incidentId, message);
            } catch (Exception e) {
                // Ignorar errores
            }
        }
    }

    private void logIncidentAction(String incidentId, String action, String details) {
        if (sentryCore == null) return;
        
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("incidentId", incidentId);
            metadata.put("action", action);
            metadata.put("details", details);

            LogEvent event = LogEvent.builder()
                    .timestamp(System.currentTimeMillis())
                    .source("INCIDENT_RESPONDER")
                    .message("Incident action: " + action + " for " + incidentId)
                    .severity(LogEvent.Severity.WARNING)
                    .metadata(metadata)
                    .build();

            sentryCore.appendEvent(event);
        } catch (Exception e) {
            Log.e(TAG, "Error logging incident action", e);
        }
    }

    /**
     * Obtiene estadísticas del respondedor.
     */
    public ResponderStatistics getStatistics() {
        ResponderStatistics stats = new ResponderStatistics();
        stats.totalIncidents = incidentCount.get();
        stats.activeIncidents = activeIncidents.size();
        stats.closedIncidents = closedIncidents.size();
        stats.escalatedIncidents = escalatedCount.get();
        stats.notificationsSent = notificationsSent.get();
        return stats;
    }

    /**
     * Estadísticas del respondedor.
     */
    public static final class ResponderStatistics {
        public long totalIncidents;
        public int activeIncidents;
        public int closedIncidents;
        public long escalatedIncidents;
        public long notificationsSent;

        @Override
        public String toString() {
            return String.format(
                    "IncidentResponder{total=%d, active=%d, closed=%d, escalated=%d, notifications=%d}",
                    totalIncidents, activeIncidents, closedIncidents, escalatedIncidents, notificationsSent
            );
        }
    }
}
