package com.odin.core.exception;

/**
 * PersistenceException indicador de fallos en operaciones de
 * almacenamiento/persistencia.
 */
public class PersistenceException extends OdinException {
    public PersistenceException(String message) {
        super(message, ErrorSeverity.MEDIUM);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause, ErrorSeverity.MEDIUM);
    }

    public PersistenceException(String message, Throwable cause, ErrorSeverity severity) {
        super(message, cause, severity);
    }
}
