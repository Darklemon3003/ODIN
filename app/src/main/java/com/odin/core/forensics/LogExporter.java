package com.odin.core.forensics;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.odin.core.domain.model.LogEvent;
import com.odin.core.exception.PersistenceException;
import com.odin.core.persistence.SentryCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * LogExporter proporciona capacidades de exportación de logs en múltiples formatos.
 *
 * Responsabilidades:
 * - Exportación a múltiples formatos (JSON, CSV, XML, PDF)
 * - Filtrado y búsqueda avanzada de logs
 * - Exportación programada y bajo demanda
 * - Soporte para integración con SIEM externos
 * - Mascarado de datos sensibles para cumplimiento
 *
 * Thread-safety: Las operaciones de exportación son thread-safe ya que SentryCore está sincronizado.
 */
public final class LogExporter {

    private static final String TAG = "LogExporter";
    private static volatile LogExporter instance;

    private final SentryCore sentryCore;
    private final Context context;
    private final SimpleDateFormat dateFormat;

    /**
     * Formatos de exportación soportados.
     */
    public enum ExportFormat {
        JSON("json"),
        CSV("csv"),
        XML("xml"),
        PDF("pdf");

        private final String extension;

        ExportFormat(String extension) {
            this.extension = extension;
        }

        public String getExtension() {
            return extension;
        }
    }

    /**
     * Constructor privado. Usar getInstance() en su lugar.
     *
     * @param context ApplicationContext
     * @throws PersistenceException si falla la inicialización
     */
    private LogExporter(@NonNull Context context) throws PersistenceException {
        this.context = context.getApplicationContext();
        this.sentryCore = SentryCore.getInstance(context);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Obtiene la instancia singleton de LogExporter.
     *
     * @param context ApplicationContext
     * @return LogExporter instance
     * @throws PersistenceException si falla la inicialización
     */
    public static LogExporter getInstance(@NonNull Context context) throws PersistenceException {
        if (instance == null) {
            synchronized (LogExporter.class) {
                if (instance == null) {
                    instance = new LogExporter(context);
                }
            }
        }
        return instance;
    }

    /**
     * Exporta logs en el formato especificado.
     *
     * @param format Formato de exportación
     * @param startTime Timestamp de inicio (opcional, 0 para todos)
     * @param endTime Timestamp de fin (opcional, 0 para todos)
     * @param filters Filtros adicionales (opcional)
     * @param maskSensitiveData Si true, enmascara datos sensibles
     * @return ExportResult con el resultado de la exportación
     * @throws PersistenceException si la exportación falla
     */
    public ExportResult exportLogs(@NonNull ExportFormat format, long startTime, long endTime,
                                    @NonNull Map<String, Object> filters, boolean maskSensitiveData)
            throws PersistenceException {
        
        ExportResult result = new ExportResult();
        result.setFormat(format);
        result.setStartTime(startTime);
        result.setEndTime(endTime);
        
        try {
            // Cargar eventos
            List<LogEvent> events = loadEvents(startTime, endTime, filters);
            result.setEventCount(events.size());
            
            // Aplicar mascarado de datos sensibles si se solicita
            if (maskSensitiveData) {
                events = maskSensitiveData(events);
                result.setMasked(true);
            }
            
            // Generar contenido según formato
            String content;
            switch (format) {
                case JSON:
                    content = exportToJson(events);
                    break;
                case CSV:
                    content = exportToCsv(events);
                    break;
                case XML:
                    content = exportToXml(events);
                    break;
                case PDF:
                    content = exportToPdf(events);
                    break;
                default:
                    throw new PersistenceException("Formato no soportado: " + format);
            }
            
            result.setContent(content);
            result.setSuccess(true);
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            throw new PersistenceException("Error exportando logs", e);
        }
        
        return result;
    }

    /**
     * Carga eventos aplicando filtros.
     */
    private List<LogEvent> loadEvents(long startTime, long endTime, @NonNull Map<String, Object> filters)
            throws PersistenceException {
        
        List<LogEvent> allEvents = sentryCore.fetchRecentEvents(10000);
        
        return allEvents.stream()
                .filter(event -> {
                    // Filtro por tiempo
                    if (startTime > 0 && event.getTimestamp() < startTime) {
                        return false;
                    }
                    if (endTime > 0 && event.getTimestamp() > endTime) {
                        return false;
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
                    
                    // Filtro por mensaje (búsqueda de texto)
                    if (filters.containsKey("search")) {
                        String searchTerm = (String) filters.get("search");
                        if (!event.getMessage().toLowerCase().contains(searchTerm.toLowerCase())) {
                            return false;
                        }
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * Enmascara datos sensibles en los eventos.
     * IPs, tokens, y otros datos sensibles son parcialmente ocultados.
     */
    private List<LogEvent> maskSensitiveData(@NonNull List<LogEvent> events) {
        List<LogEvent> maskedEvents = new ArrayList<>();
        
        for (LogEvent event : events) {
            String maskedMessage = event.getMessage();
            
            // Enmascarar direcciones IP
            maskedMessage = maskedMessage.replaceAll(
                    "\\b(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\b",
                    "$1.$2.$3.***");
            
            // Enmascarar tokens (patrones comunes)
            maskedMessage = maskedMessage.replaceAll(
                    "(token|api_key|secret)[\"':\\s]*[a-zA-Z0-9_-]{20,}",
                    "$1=***REDACTED***");
            
            // Crear nuevo evento con mensaje enmascarado
            LogEvent maskedEvent = LogEvent.builder()
                    .timestamp(event.getTimestamp())
                    .source(event.getSource())
                    .message(maskedMessage)
                    .severity(event.getSeverity())
                    .metadata(event.getMetadata())
                    .build();
            
            maskedEvents.add(maskedEvent);
        }
        
        return maskedEvents;
    }

    /**
     * Exporta eventos a formato JSON.
     */
    private String exportToJson(@NonNull List<LogEvent> events) throws JSONException {
        JSONObject root = new JSONObject();
        root.put("exportTimestamp", System.currentTimeMillis());
        root.put("totalEvents", events.size());
        root.put("format", "JSON");
        
        JSONArray eventsArray = new JSONArray();
        for (LogEvent event : events) {
            eventsArray.put(event.toJson());
        }
        root.put("events", eventsArray);
        
        return root.toString(2); // Pretty print
    }

    /**
     * Exporta una lista explicita de eventos a JSON para integraciones de repositorio.
     */
    public String exportEventsToJson(@NonNull List<LogEvent> events) throws JSONException {
        return exportToJson(events);
    }

    /**
     * Exporta eventos a formato CSV.
     */
    private String exportToCsv(@NonNull List<LogEvent> events) {
        StringBuilder csv = new StringBuilder();
        
        // Header
        csv.append("timestamp,source,message,severity,integrityHash\n");
        
        // Data rows
        for (LogEvent event : events) {
            csv.append(event.getTimestamp()).append(",");
            csv.append(escapeCsv(event.getSource())).append(",");
            csv.append(escapeCsv(event.getMessage())).append(",");
            csv.append(event.getSeverity().getValue()).append(",");
            csv.append(event.getIntegrityHash()).append("\n");
        }
        
        return csv.toString();
    }

    /**
     * Exporta una lista explicita de eventos a CSV para integraciones de repositorio.
     */
    public String exportEventsToCsv(@NonNull List<LogEvent> events) {
        return exportToCsv(events);
    }

    /**
     * Exporta eventos a formato XML.
     */
    private String exportToXml(@NonNull List<LogEvent> events) {
        StringBuilder xml = new StringBuilder();
        
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<LogExport>\n");
        xml.append("  <Metadata>\n");
        xml.append("    <ExportTimestamp>").append(System.currentTimeMillis()).append("</ExportTimestamp>\n");
        xml.append("    <TotalEvents>").append(events.size()).append("</TotalEvents>\n");
        xml.append("    <Format>XML</Format>\n");
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
        xml.append("</LogExport>");
        
        return xml.toString();
    }

    /**
     * Exporta eventos a formato PDF (texto plano con formato PDF básico).
     * Nota: Para PDF real se requeriría una librería como iText o Apache PDFBox.
     * Esta implementación genera un formato de texto plano compatible con impresión.
     */
    private String exportToPdf(@NonNull List<LogEvent> events) {
        StringBuilder pdf = new StringBuilder();
        
        pdf.append("LOG EXPORT REPORT\n");
        pdf.append("=================\n\n");
        pdf.append("Export Timestamp: ").append(dateFormat.format(new Date())).append("\n");
        pdf.append("Total Events: ").append(events.size()).append("\n");
        pdf.append("Format: PDF (Text)\n\n");
        pdf.append("--------------------------------------------------\n\n");
        
        for (LogEvent event : events) {
            pdf.append("Timestamp: ").append(dateFormat.format(new Date(event.getTimestamp()))).append("\n");
            pdf.append("Source: ").append(event.getSource()).append("\n");
            pdf.append("Severity: ").append(event.getSeverity().getValue()).append("\n");
            pdf.append("Message: ").append(event.getMessage()).append("\n");
            pdf.append("Integrity Hash: ").append(event.getIntegrityHash()).append("\n");
            
            if (!event.getMetadata().isEmpty()) {
                pdf.append("Metadata:\n");
                for (Map.Entry<String, String> entry : event.getMetadata().entrySet()) {
                    pdf.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
            }
            
            pdf.append("\n--------------------------------------------------\n\n");
        }
        
        return pdf.toString();
    }

    /**
     * Exporta una lista explicita de eventos a texto con formato de reporte PDF.
     */
    public String exportEventsToPdf(@NonNull List<LogEvent> events) {
        return exportToPdf(events);
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
     * Guarda el resultado de exportación en un archivo.
     *
     * @param result Resultado de exportación
     * @param filename Nombre del archivo (sin extensión)
     * @return Ruta del archivo creado
     * @throws IOException si falla el guardado
     */
    public String saveToFile(@NonNull ExportResult result, @NonNull String filename) throws IOException {
        String extension = result.getFormat().getExtension();
        File exportDir = new File(context.getFilesDir(), "odin_exports");
        
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        
        File outputFile = new File(exportDir, filename + "." + extension);
        
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(result.getContent().getBytes());
            Log.i(TAG, "Exportación guardada en: " + outputFile.getAbsolutePath());
        }
        
        return outputFile.getAbsolutePath();
    }

    /**
     * Genera un resumen estadístico de los logs a exportar.
     *
     * @param startTime Timestamp de inicio
     * @param endTime Timestamp de fin
     * @return Mapa con estadísticas
     * @throws PersistenceException si falla la consulta
     */
    public Map<String, Object> getExportSummary(long startTime, long endTime) throws PersistenceException {
        Map<String, Object> summary = new java.util.HashMap<>();
        
        List<LogEvent> events = loadEvents(startTime, endTime, new java.util.HashMap<>());
        
        summary.put("totalEvents", events.size());
        summary.put("startTime", startTime);
        summary.put("endTime", endTime);
        
        // Distribución por severidad
        Map<String, Long> severityDistribution = new java.util.HashMap<>();
        for (LogEvent.Severity severity : LogEvent.Severity.values()) {
            long count = events.stream().filter(e -> e.getSeverity() == severity).count();
            severityDistribution.put(severity.getValue(), count);
        }
        summary.put("severityDistribution", severityDistribution);
        
        // Distribución por fuente
        Map<String, Long> sourceDistribution = new java.util.HashMap<>();
        for (LogEvent event : events) {
            sourceDistribution.merge(event.getSource(), 1L, Long::sum);
        }
        summary.put("sourceDistribution", sourceDistribution);
        
        return summary;
    }

    /**
     * Reinicia la instancia singleton (para testing).
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Clase interna para representar el resultado de una exportación.
     */
    public static class ExportResult {
        private ExportFormat format;
        private long startTime;
        private long endTime;
        private int eventCount;
        private boolean masked;
        private String content;
        private boolean success;
        private String errorMessage;

        public ExportFormat getFormat() { return format; }
        public void setFormat(ExportFormat format) { this.format = format; }
        
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        
        public int getEventCount() { return eventCount; }
        public void setEventCount(int eventCount) { this.eventCount = eventCount; }
        
        public boolean isMasked() { return masked; }
        public void setMasked(boolean masked) { this.masked = masked; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }
}
