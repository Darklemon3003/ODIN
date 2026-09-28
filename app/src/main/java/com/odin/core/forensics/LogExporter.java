package com.odin.core.forensics;

import android.content.Context;

import androidx.annotation.NonNull;

import com.odin.core.domain.model.LogEvent;
import com.odin.core.exception.PersistenceException;
import com.odin.core.persistence.SentryCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * LogExporter proporciona capacidades de exportación de logs en múltiples formatos.
 *
 * Responsabilidades:
 * - Exportación a JSON y CSV (formatos soportados para integración con repositorio)
 * - Mascarado de datos sensibles para cumplimiento
 *
 * Nota: PDF y XML han sido eliminados. La exportación canónica es Phase 9.2 SAF.
 *
 * Thread-safety: Las operaciones de exportación son thread-safe ya que SentryCore está sincronizado.
 */
public final class LogExporter {

    private static volatile LogExporter instance;

    private final SentryCore sentryCore;

    /**
     * Formatos de exportación soportados.
     */
    public enum ExportFormat {
        JSON("json"),
        CSV("csv");

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
        this.sentryCore = SentryCore.getInstance(context);
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
     * Reinicia la instancia singleton (para testing).
     */
    public static void resetInstance() {
        instance = null;
    }
}
