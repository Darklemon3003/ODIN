package com.odin.core.exception;

/**
 * SecurityException indicador de fallos en operaciones de
 * seguridad/criptografía.
 */
public class SecurityException extends OdinException {
    public SecurityException(String message) {
        super(message, ErrorSeverity.CRITICAL);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause, ErrorSeverity.CRITICAL);
    }
}
