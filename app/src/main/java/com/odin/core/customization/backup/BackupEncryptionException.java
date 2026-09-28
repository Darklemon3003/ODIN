package com.odin.core.customization.backup;

/**
 * Excepcion controlada para errores de cifrado/descifrado de backups.
 *
 * Se lanza cuando:
 * - El cifrado AES-256-GCM falla.
 * - El descifrado falla por datos corruptos o tag de autenticacion invalido.
 * - El formato de backup es desconocido o no soportado.
 *
 * Nunca se retorna plaintext ni ciphertext como si fuera valido.
 */
public class BackupEncryptionException extends RuntimeException {

    public BackupEncryptionException(String message) {
        super(message);
    }

    public BackupEncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
