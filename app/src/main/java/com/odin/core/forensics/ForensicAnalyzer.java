package com.odin.core.forensics;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.odin.core.domain.model.LogEvent;
import com.odin.core.exception.PersistenceException;
import com.odin.core.persistence.SentryCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * ForensicAnalyzer proporciona capacidades de análisis forense digital para incidentes de seguridad.
 *
 * Responsabilidades:
 * - Extracción de evidencia de incidentes
 * - Reconstrucción de timeline de eventos
 * - Análisis de patrones de ataque
 * - Generación de reportes forenses
 * - Exportación de evidencia en formatos estándar (JSON, CSV, XML)
 *
 * Thread-safety: Las operaciones de lectura son thread-safe ya que SentryCore está sincronizado.
 */
public final class ForensicAnalyzer {

    private static final String TAG = "ForensicAnalyzer";
    private static volatile ForensicAnalyzer instance;

    private final SentryCore sentryCore;
    private final SimpleDateFormat dateFormat;

    /**
     * Constructor privado. Usar getInstance() en su lugar.
     *
     * @param context ApplicationContext
     * @throws PersistenceException si falla la inicialización
     */
    private ForensicAnalyzer(@NonNull Context context) throws PersistenceException {
        this.sentryCore = SentryCore.getInstance(context);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Obtiene la instancia singleton de ForensicAnalyzer.
     *
     * @param context ApplicationContext
     * @return ForensicAnalyzer instance
     * @throws PersistenceException si falla la inicialización
     */
    public static ForensicAnalyzer getInstance(@NonNull Context context) throws PersistenceException {
        if (instance == null) {
            synchronized (ForensicAnalyzer.class) {
                if (instance == null) {
                    instance = new ForensicAnalyzer(context);
                }
            }
        }
        return instance;
    }

    /**
     * Consulta logs por rango de tiempo.
     *
     * @param startTime Timestamp de inicio (milisegundos desde epoch)
     * @param endTime Timestamp de fin (milisegundos desde epoch)
     * @return Lista de LogEvent en el rango especificado
     * @throws PersistenceException si la consulta falla
     */
    public List<LogEvent> queryLogsByTimeRange(long startTime, long endTime) throws PersistenceException {
        try {
            List<LogEvent> allEvents = sentryCore.fetchRecentEvents(10000); // Máximo 10k eventos
            
            return allEvents.stream()
                    .filter(event -> event.getTimestamp() >= startTime && event.getTimestamp() <= endTime)
                    .sorted(Comparator.comparingLong(LogEvent::getTimestamp))
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            throw new PersistenceException("Error consultando logs por rango de tiempo", e);
        }
    }

    /**
     * Consulta logs por severidad.
     *
     * @param severity Nivel de severidad a filtrar
     * @return Lista de LogEvent con la severidad especificada
     * @throws PersistenceException si la consulta falla
     */
    public List<LogEvent> queryLogsBySeverity(@NonNull LogEvent.Severity severity) throws PersistenceException {
        try {
            List<LogEvent> allEvents = sentryCore.fetchRecentEvents(10000);
            
            return allEvents.stream()
                    .filter(event -> event.getSeverity() == severity)
                    .sorted(Comparator.comparingLong(LogEvent::getTimestamp))
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            throw new PersistenceException("Error consultando logs por severidad", e);
        }
    }

    /**
     * Consulta logs por fuente (source).
     *
     * @param source Fuente del evento (ej. "VPN", "THREAT_DETECTION")
     * @return Lista de LogEvent de la fuente especificada
     * @throws PersistenceException si la consulta falla
     */
    public List<LogEvent> queryLogsBySource(@NonNull String source) throws PersistenceException {
        try {
            List<LogEvent> allEvents = sentryCore.fetchRecentEvents(10000);
            
            return allEvents.stream()
                    .filter(event -> event.getSource().equals(source))
                    .sorted(Comparator.comparingLong(LogEvent::getTimestamp))
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            throw new PersistenceException("Error consultando logs por fuente", e);
        }
    }

    /**
     * Consulta logs con filtros múltiples.
     *
     * @param filters Mapa de filtros (startTime, endTime, severity, source)
     * @return Lista de LogEvent que cumplen todos los filtros
     * @throws PersistenceException si la consulta falla
     */
    public List<LogEvent> queryLogsWithFilters(@NonNull Map<String, Object> filters) throws PersistenceException {
        try {
            List<LogEvent> allEvents = sentryCore.fetchRecentEvents(10000);
            
            return allEvents.stream()
                    .filter(event -> {
                        // Filtro por rango de tiempo
                        if (filters.containsKey("startTime") && filters.containsKey("endTime")) {
                            long startTime = (long) filters.get("startTime");
                            long endTime = (long) filters.get("endTime");
                            if (event.getTimestamp() < startTime || event.getTimestamp() > endTime) {
                                return false;
                            }
                        }
                        
                        // Filtro por severidad
                        if (filters.containsKey("severity")) {
                            LogEvent.Severity severity = (LogEvent.Severity) filters.get("severity");
                            if (event.getSeverity() != severity) {
                                return false;
                            }
                        }
                        
                        // Filtro por fuente
                        if (filters.containsKey("source")) {
                            String source = (String) filters.get("source");
                            if (!event.getSource().equals(source)) {
                                return false;
                            }
                        }
                        
                        return true;
                    })
                    .sorted(Comparator.comparingLong(LogEvent::getTimestamp))
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            throw new PersistenceException("Error consultando logs con filtros", e);
        }
    }

    /**
     * Reconstruye un timeline de eventos para un incidente.
     *
     * @param startTime Timestamp de inicio del incidente
     * @param endTime Timestamp de fin del incidente
     * @return Timeline ordenado de eventos
     * @throws PersistenceException si la reconstrucción falla
     */
    public ForensicTimeline reconstructTimeline(long startTime, long endTime) throws PersistenceException {
        try {
            List<LogEvent> events = queryLogsByTimeRange(startTime, endTime);
            
            ForensicTimeline timeline = new ForensicTimeline();
            timeline.setStartTime(startTime);
            timeline.setEndTime(endTime);
            timeline.setTotalEvents(events.size());
            
            // Agrupar eventos por fuente
            Map<String, List<LogEvent>> eventsBySource = new HashMap<>();
            for (LogEvent event : events) {
                eventsBySource.computeIfAbsent(event.getSource(), k -> new ArrayList<>()).add(event);
            }
            timeline.setEventsBySource(eventsBySource);
            
            // Detectar patrones
            timeline.setAttackPatterns(detectAttackPatterns(events));
            
            // Calcular estadísticas
            timeline.setStatistics(calculateEventStatistics(events));
            
            return timeline;
            
        } catch (Exception e) {
            throw new PersistenceException("Error reconstruyendo timeline", e);
        }
    }

    /**
     * Detecta patrones de ataque en los eventos.
     *
     * @param events Lista de eventos a analizar
     * @return Lista de patrones detectados
     */
    private List<AttackPattern> detectAttackPatterns(@NonNull List<LogEvent> events) {
        List<AttackPattern> patterns = new ArrayList<>();
        
        // Patrón 1: Múltiples errores críticos en corto tiempo
        long criticalCount = events.stream()
                .filter(e -> e.getSeverity() == LogEvent.Severity.CRITICAL)
                .count();
        if (criticalCount > 5) {
            patterns.add(new AttackPattern(
                    "MULTIPLE_CRITICAL_ERRORS",
                    "Se detectaron múltiples errores críticos",
                    "HIGH",
                    criticalCount
            ));
        }
        
        // Patrón 2: Eventos de VPN seguidos de amenazas
        boolean vpnThreatSequence = false;
        for (int i = 0; i < events.size() - 1; i++) {
            if (events.get(i).getSource().equals("VPN") && 
                events.get(i + 1).getSource().equals("THREAT_DETECTION")) {
                vpnThreatSequence = true;
                break;
            }
        }
        if (vpnThreatSequence) {
            patterns.add(new AttackPattern(
                    "VPN_THREAT_SEQUENCE",
                    "Secuencia de VPN seguida de detección de amenaza",
                    "MEDIUM",
                    1
            ));
        }
        
        // Patrón 3: Alta frecuencia de eventos de firewall
        long firewallCount = events.stream()
                .filter(e -> e.getSource().equals("FIREWALL"))
                .count();
        if (firewallCount > 20) {
            patterns.add(new AttackPattern(
                    "HIGH_FIREWALL_ACTIVITY",
                    "Actividad inusualmente alta en firewall",
                    "MEDIUM",
                    firewallCount
            ));
        }
        
        return patterns;
    }

    /**
     * Calcula estadísticas de los eventos.
     *
     * @param events Lista de eventos
     * @return Mapa de estadísticas
     */
    private Map<String, Object> calculateEventStatistics(@NonNull List<LogEvent> events) {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalEvents", events.size());
        
        // Contar por severidad
        Map<String, Long> severityCount = new HashMap<>();
        for (LogEvent.Severity severity : LogEvent.Severity.values()) {
            long count = events.stream().filter(e -> e.getSeverity() == severity).count();
            severityCount.put(severity.getValue(), count);
        }
        stats.put("severityDistribution", severityCount);
        
        // Contar por fuente
        Map<String, Long> sourceCount = new HashMap<>();
        for (LogEvent event : events) {
            sourceCount.merge(event.getSource(), 1L, Long::sum);
        }
        stats.put("sourceDistribution", sourceCount);
        
        // Duración del incidente
        if (!events.isEmpty()) {
            long duration = events.get(events.size() - 1).getTimestamp() - events.get(0).getTimestamp();
            stats.put("durationMs", duration);
        }
        
        return stats;
    }

    /**
     * Genera un reporte forense completo.
     *
     * @param startTime Timestamp de inicio
     * @param endTime Timestamp de fin
     * @return JSONObject con el reporte forense
     * @throws PersistenceException si la generación falla
     */
    public JSONObject generateForensicReport(long startTime, long endTime) throws PersistenceException {
        try {
            ForensicTimeline timeline = reconstructTimeline(startTime, endTime);
            
            JSONObject report = new JSONObject();
            report.put("reportGeneratedAt", System.currentTimeMillis());
            report.put("incidentStartTime", startTime);
            report.put("incidentEndTime", endTime);
            report.put("totalEvents", timeline.getTotalEvents());
            
            // Timeline
            JSONArray timelineArray = new JSONArray();
            for (Map.Entry<String, List<LogEvent>> entry : timeline.getEventsBySource().entrySet()) {
                JSONObject sourceObj = new JSONObject();
                sourceObj.put("source", entry.getKey());
                sourceObj.put("eventCount", entry.getValue().size());
                
                JSONArray eventsArray = new JSONArray();
                for (LogEvent event : entry.getValue()) {
                    eventsArray.put(event.toJson());
                }
                sourceObj.put("events", eventsArray);
                timelineArray.put(sourceObj);
            }
            report.put("timeline", timelineArray);
            
            // Patrones de ataque
            JSONArray patternsArray = new JSONArray();
            for (AttackPattern pattern : timeline.getAttackPatterns()) {
                JSONObject patternObj = new JSONObject();
                patternObj.put("type", pattern.getType());
                patternObj.put("description", pattern.getDescription());
                patternObj.put("severity", pattern.getSeverity());
                patternObj.put("occurrenceCount", pattern.getOccurrenceCount());
                patternsArray.put(patternObj);
            }
            report.put("attackPatterns", patternsArray);
            
            // Estadísticas
            JSONObject statsObj = new JSONObject();
            for (Map.Entry<String, Object> entry : timeline.getStatistics().entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Map) {
                    JSONObject nestedObj = new JSONObject();
                    for (Map.Entry<?, ?> nestedEntry : ((Map<?, ?>) value).entrySet()) {
                        nestedObj.put(nestedEntry.getKey().toString(), nestedEntry.getValue());
                    }
                    statsObj.put(entry.getKey(), nestedObj);
                } else {
                    statsObj.put(entry.getKey(), value);
                }
            }
            report.put("statistics", statsObj);
            
            return report;
            
        } catch (Exception e) {
            throw new PersistenceException("Error generando reporte forense", e);
        }
    }

    /**
     * Exporta evidencia en formato JSON.
     *
     * @param startTime Timestamp de inicio
     * @param endTime Timestamp de fin
     * @return String JSON con la evidencia
     * @throws PersistenceException si la exportación falla
     */
    public String exportEvidenceAsJson(long startTime, long endTime) throws PersistenceException {
        try {
            JSONObject report = generateForensicReport(startTime, endTime);
            return report.toString(2); // Pretty print con indentación de 2 espacios
        } catch (Exception e) {
            throw new PersistenceException("Error exportando evidencia como JSON", e);
        }
    }

    /**
     * Exporta evidencia en formato CSV.
     *
     * @param startTime Timestamp de inicio
     * @param endTime Timestamp de fin
     * @return String CSV con la evidencia
     * @throws PersistenceException si la exportación falla
     */
    public String exportEvidenceAsCsv(long startTime, long endTime) throws PersistenceException {
        try {
            List<LogEvent> events = queryLogsByTimeRange(startTime, endTime);
            
            StringBuilder csv = new StringBuilder();
            csv.append("timestamp,source,message,severity,integrityHash\n");
            
            for (LogEvent event : events) {
                csv.append(event.getTimestamp()).append(",");
                csv.append(escapeCsv(event.getSource())).append(",");
                csv.append(escapeCsv(event.getMessage())).append(",");
                csv.append(event.getSeverity().getValue()).append(",");
                csv.append(event.getIntegrityHash()).append("\n");
            }
            
            return csv.toString();
            
        } catch (Exception e) {
            throw new PersistenceException("Error exportando evidencia como CSV", e);
        }
    }

    /**
     * Exporta evidencia en formato XML.
     *
     * @param startTime Timestamp de inicio
     * @param endTime Timestamp de fin
     * @return String XML con la evidencia
     * @throws PersistenceException si la exportación falla
     */
    public String exportEvidenceAsXml(long startTime, long endTime) throws PersistenceException {
        try {
            List<LogEvent> events = queryLogsByTimeRange(startTime, endTime);
            
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xml.append("<ForensicEvidence>\n");
            xml.append("  <Metadata>\n");
            xml.append("    <StartTime>").append(startTime).append("</StartTime>\n");
            xml.append("    <EndTime>").append(endTime).append("</EndTime>\n");
            xml.append("    <ExportTime>").append(System.currentTimeMillis()).append("</ExportTime>\n");
            xml.append("    <TotalEvents>").append(events.size()).append("</TotalEvents>\n");
            xml.append("  </Metadata>\n");
            xml.append("  <Events>\n");
            
            for (LogEvent event : events) {
                xml.append("    <Event>\n");
                xml.append("      <Timestamp>").append(event.getTimestamp()).append("</Timestamp>\n");
                xml.append("      <Source>").append(escapeXml(event.getSource())).append("</Source>\n");
                xml.append("      <Message>").append(escapeXml(event.getMessage())).append("</Message>\n");
                xml.append("      <Severity>").append(event.getSeverity().getValue()).append("</Severity>\n");
                xml.append("      <IntegrityHash>").append(event.getIntegrityHash()).append("</IntegrityHash>\n");
                
                if (!event.getMetadata().isEmpty()) {
                    xml.append("      <Metadata>\n");
                    for (Map.Entry<String, String> entry : event.getMetadata().entrySet()) {
                        xml.append("        <Entry key=\"").append(escapeXml(entry.getKey()))
                           .append("\">").append(escapeXml(entry.getValue())).append("</Entry>\n");
                    }
                    xml.append("      </Metadata>\n");
                }
                
                xml.append("    </Event>\n");
            }
            
            xml.append("  </Events>\n");
            xml.append("</ForensicEvidence>");
            
            return xml.toString();
            
        } catch (Exception e) {
            throw new PersistenceException("Error exportando evidencia como XML", e);
        }
    }

    /**
     * Escapa caracteres especiales para CSV.
     */
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Escapa caracteres especiales para XML.
     */
    private String escapeXml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }

    /**
     * Reinicia la instancia singleton (para testing).
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Clase interna para representar un timeline forense.
     */
    public static class ForensicTimeline {
        private long startTime;
        private long endTime;
        private int totalEvents;
        private Map<String, List<LogEvent>> eventsBySource;
        private List<AttackPattern> attackPatterns;
        private Map<String, Object> statistics;

        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        
        public int getTotalEvents() { return totalEvents; }
        public void setTotalEvents(int totalEvents) { this.totalEvents = totalEvents; }
        
        public Map<String, List<LogEvent>> getEventsBySource() { return eventsBySource; }
        public void setEventsBySource(Map<String, List<LogEvent>> eventsBySource) { this.eventsBySource = eventsBySource; }
        
        public List<AttackPattern> getAttackPatterns() { return attackPatterns; }
        public void setAttackPatterns(List<AttackPattern> attackPatterns) { this.attackPatterns = attackPatterns; }
        
        public Map<String, Object> getStatistics() { return statistics; }
        public void setStatistics(Map<String, Object> statistics) { this.statistics = statistics; }
    }

    /**
     * Clase interna para representar un patrón de ataque detectado.
     */
    public static class AttackPattern {
        private final String type;
        private final String description;
        private final String severity;
        private final long occurrenceCount;

        public AttackPattern(String type, String description, String severity, long occurrenceCount) {
            this.type = type;
            this.description = description;
            this.severity = severity;
            this.occurrenceCount = occurrenceCount;
        }

        public String getType() { return type; }
        public String getDescription() { return description; }
        public String getSeverity() { return severity; }
        public long getOccurrenceCount() { return occurrenceCount; }
    }
}
