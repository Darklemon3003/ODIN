package com.odin.core.domain.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * LogEvent representa un evento de auditoría generado por el agente de seguridad.
 * Este modelo es inmutable para garantizar integridad en la fase de dominio.
 *
 * Características:
 * - Timestamp único para ordenamiento temporal
 * - Source identificador del componente generador
 * - Message descriptivo del evento
 * - Severity nivel de importancia (INFO, WARNING, ERROR, CRITICAL)
 * - Metadata opcional para información adicional
 * - Soporte para serialización JSON
 * - Validación de integridad de datos
 */
public final class LogEvent {

    /**
     * Niveles de severidad para eventos de log.
     */
    public enum Severity {
        INFO("INFO"),
        WARNING("WARNING"),
        ERROR("ERROR"),
        CRITICAL("CRITICAL");

        private final String value;

        Severity(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Severity fromString(String value) {
            for (Severity s : values()) {
                if (s.value.equals(value)) {
                    return s;
                }
            }
            return INFO;
        }
    }

    private final long timestamp;
    private final String source;
    private final String message;
    private final Severity severity;
    private final Map<String, String> metadata;
    private final String integrityHash;

    /**
     * Constructor completo con todos los campos.
     *
     * @param timestamp Timestamp del evento en milisegundos desde epoch
     * @param source Componente que generó el evento
     * @param message Mensaje descriptivo del evento
     * @param severity Nivel de severidad del evento
     * @param metadata Metadatos opcionales (puede ser null)
     */
    public LogEvent(long timestamp, String source, String message, Severity severity, 
                    @Nullable Map<String, String> metadata) {
        this.timestamp = timestamp;
        this.source = Objects.requireNonNull(source, "Source cannot be null");
        this.message = Objects.requireNonNull(message, "Message cannot be null");
        this.severity = severity != null ? severity : Severity.INFO;
        this.metadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
        this.integrityHash = calculateIntegrityHash();
    }

    /**
     * Constructor simplificado (compatibilidad con código existente).
     */
    public LogEvent(long timestamp, String source, String message) {
        this(timestamp, source, message, Severity.INFO, null);
    }

    /**
     * Constructor con severity pero sin metadata.
     */
    public LogEvent(long timestamp, String source, String message, Severity severity) {
        this(timestamp, source, message, severity, null);
    }

    /**
     * Constructor desde JSON (para deserialización).
     */
    public LogEvent(@NonNull JSONObject json) throws JSONException {
        this.timestamp = json.getLong("timestamp");
        this.source = json.getString("source");
        this.message = json.getString("message");
        this.severity = Severity.fromString(json.optString("severity", "INFO"));
        
        this.metadata = new HashMap<>();
        if (json.has("metadata")) {
            JSONObject metadataJson = json.getJSONObject("metadata");
            java.util.Iterator<String> keys = metadataJson.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                this.metadata.put(key, metadataJson.getString(key));
            }
        }
        
        this.integrityHash = json.optString("integrityHash", "");
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }

    public String getIntegrityHash() {
        return integrityHash;
    }

    /**
     * Calcula hash de integridad SHA-256 del evento.
     * Se basa en timestamp, source, message, severity y metadata.
     */
    private String calculateIntegrityHash() {
        try {
            String data = timestamp + "|" + source + "|" + message + "|" + 
                          severity.getValue() + "|" + metadata.toString();
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            // Nunca devolver una cadena vacía: eso haría que verifyIntegrity()
            // pase trivialmente cuando el hash almacenado también es vacío,
            // dando una falsa garantía de integridad. Lanzar para que el
            // llamador gestione el fallo explícitamente.
            throw new IllegalStateException("Failed to calculate integrity hash", e);
        }
    }

    /**
     * Verifica la integridad del evento comparando el hash calculado con el almacenado.
     *
     * @return true si la integridad es válida
     */
    public boolean verifyIntegrity() {
        // Un hash vacío/nulo significa que la integridad nunca se calculó
        // (p. ej. evento deserializado sin hash, o fallo previo). NO debe
        // considerarse válido: devolver false en lugar de permitir una
        // comparación trivial que pasaría si calculateIntegrityHash() también
        // devolviera vacío.
        if (integrityHash == null || integrityHash.isEmpty()) {
            return false;
        }
        return integrityHash.equals(calculateIntegrityHash());
    }

    /**
     * Serializa el evento a JSON.
     *
     * @return JSONObject representando el evento
     * @throws JSONException si la serialización falla
     */
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("timestamp", timestamp);
        json.put("source", source);
        json.put("message", message);
        json.put("severity", severity.getValue());
        json.put("integrityHash", integrityHash);
        
        if (!metadata.isEmpty()) {
            JSONObject metadataJson = new JSONObject();
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                metadataJson.put(entry.getKey(), entry.getValue());
            }
            json.put("metadata", metadataJson);
        }
        
        return json;
    }

    /**
     * Serializa el evento a string JSON.
     *
     * @return String JSON
     */
    public String toJsonString() {
        try {
            return toJson().toString();
        } catch (JSONException e) {
            return "{}";
        }
    }

    /**
     * Crea un builder para construir LogEvent de forma fluida.
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEvent logEvent = (LogEvent) o;
        return timestamp == logEvent.timestamp &&
               Objects.equals(source, logEvent.source) &&
               Objects.equals(message, logEvent.message) &&
               severity == logEvent.severity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, source, message, severity);
    }

    @Override
    public String toString() {
        return "LogEvent{" +
               "timestamp=" + timestamp +
               ", source='" + source + '\'' +
               ", message='" + message + '\'' +
               ", severity=" + severity +
               ", metadata=" + metadata +
               '}';
    }

    /**
     * Builder pattern para construcción fluida de LogEvent.
     */
    public static class Builder {
        private long timestamp;
        private String source;
        private String message;
        private Severity severity = Severity.INFO;
        private Map<String, String> metadata = new HashMap<>();

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder severity(Severity severity) {
            this.severity = severity;
            return this;
        }

        public Builder addMetadata(String key, String value) {
            this.metadata.put(key, value);
            return this;
        }

        public Builder metadata(Map<String, String> metadata) {
            this.metadata = new HashMap<>(metadata);
            return this;
        }

        public LogEvent build() {
            if (timestamp == 0) {
                timestamp = System.currentTimeMillis();
            }
            return new LogEvent(timestamp, source, message, severity, metadata);
        }
    }
}
