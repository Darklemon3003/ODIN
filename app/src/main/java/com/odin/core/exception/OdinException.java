package com.odin.core.exception;

/**
 * OdinException es la excepción base para todas las excepciones del proyecto
 * ODÍN.
 * Proporciona un mecanismo centralizado para manejar errores a nivel de
 * aplicación.
 */
public class OdinException extends Exception {

    private final ErrorSeverity severity;
    private final long timestamp;

    public enum ErrorSeverity {
        LOW, // Operación no crítica falló
        MEDIUM, // Funcionalidad degradada
        CRITICAL // Falla de sistema
    }

    public OdinException(String message, ErrorSeverity severity) {
        super(message);
        this.severity = severity;
        this.timestamp = System.currentTimeMillis();
    }

    public OdinException(String message, Throwable cause, ErrorSeverity severity) {
        super(message, cause);
        this.severity = severity;
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorSeverity getSeverity() {
        return severity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getFormattedMessage() {
        return String.format("[%s] %s (ts: %d)", severity.name(), getMessage(), timestamp);
    }
}
