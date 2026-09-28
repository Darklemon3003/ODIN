package com.odin.core.customization.backup;

import android.content.Context;
import android.util.Base64;
import androidx.annotation.NonNull;
import com.odin.core.security.CryptographicManager;

/**
 * BackupEncryptor para el sistema de personalizacion de ODIN.
 *
 * Usa el proveedor criptografico canonico {@link CryptographicManager} con
 * AES-256-GCM respaldado por AndroidKeyStore. No contiene claves hardcodeadas,
 * no usa AES/ECB, y nunca retorna plaintext ante fallos.
 *
 * Formato de envelope versionado:
 *   [MAGIC (7 bytes "ODINBAK")][VERSION (1 byte)][PAYLOAD]
 *
 * Version 0x02: payload = [IV (12 bytes)][CIPHERTEXT][TAG (16 bytes)]
 *               generado por CryptographicManager.encryptBytes().
 *
 * El formato legacy (AES/ECB con clave hardcodeada) fue eliminado porque:
 * - No existen backups legacy en el dispositivo ni en el proyecto.
 * - La app no ha tenido release production.
 * - Mantener la clave hardcodeada en el binario seria un riesgo critico.
 */
public class BackupEncryptor {

    /** Magic bytes para identificar el formato de backup de ODIN. */
    static final byte[] MAGIC = "ODINBAK".getBytes(java.nio.charset.StandardCharsets.US_ASCII);

    /** Version del formato de envelope. 0x02 = AES-256-GCM via CryptographicManager. */
    static final byte FORMAT_VERSION_2 = 0x02;

    /** Version legacy no soportada (AES/ECB con clave hardcodeada). */
    static final byte FORMAT_VERSION_LEGACY_1 = 0x01;

    private final CryptographicManager cryptographicManager;

    public BackupEncryptor(@NonNull Context context) {
        this.cryptographicManager = new CryptographicManager(context);
    }

    /**
     * Constructor para pruebas que permite inyectar un CryptographicManager.
     */
    BackupEncryptor(@NonNull CryptographicManager cryptographicManager) {
        this.cryptographicManager = cryptographicManager;
    }

    /**
     * Cifra datos usando AES-256-GCM via CryptographicManager.
     *
     * @param data Bytes a cifrar. No debe ser null ni vacio.
     * @return Bytes cifrados con envelope versionado [MAGIC][VERSION][PAYLOAD].
     * @throws BackupEncryptionException si el cifrado falla. Nunca retorna plaintext.
     */
    public byte[] encrypt(@NonNull byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Los datos a cifrar no pueden ser null o vacios");
        }

        byte[] encryptedPayload = cryptographicManager.encryptBytes(data);

        // Construir envelope: MAGIC + VERSION + PAYLOAD
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(
                MAGIC.length + 1 + encryptedPayload.length);
        buffer.put(MAGIC);
        buffer.put(FORMAT_VERSION_2);
        buffer.put(encryptedPayload);
        return buffer.array();
    }

    /**
     * Descifra datos cifrados con {@link #encrypt(byte[])}.
     *
     * @param encryptedData Bytes con envelope versionado.
     * @return Bytes descifrados.
     * @throws BackupEncryptionException si el formato es desconocido, legacy no
     *         soportado, corrupto, o la autenticacion GCM falla. Nunca retorna
     *         ciphertext como si fuera plaintext valido.
     */
    public byte[] decrypt(@NonNull byte[] encryptedData) {
        if (encryptedData == null || encryptedData.length == 0) {
            throw new IllegalArgumentException("Los datos cifrados no pueden ser null o vacios");
        }

        // Validar tamano minimo: MAGIC (7) + VERSION (1) + IV (12) + TAG (16) = 36
        if (encryptedData.length < MAGIC.length + 1 + 12 + 16) {
            throw new BackupEncryptionException("Datos de backup corruptos: tamano insuficiente");
        }

        // Verificar magic
        for (int i = 0; i < MAGIC.length; i++) {
            if (encryptedData[i] != MAGIC[i]) {
                throw new BackupEncryptionException(
                        "Formato de backup desconocido: magic invalido");
            }
        }

        // Leer version
        byte version = encryptedData[MAGIC.length];
        if (version == FORMAT_VERSION_LEGACY_1) {
            throw new BackupEncryptionException(
                    "Formato de backup legacy (AES/ECB) no soportado");
        }
        if (version != FORMAT_VERSION_2) {
            throw new BackupEncryptionException(
                    "Version de formato de backup no soportada: " + (version & 0xFF));
        }

        // Extraer payload y descifrar
        int payloadOffset = MAGIC.length + 1;
        byte[] payload = new byte[encryptedData.length - payloadOffset];
        System.arraycopy(encryptedData, payloadOffset, payload, 0, payload.length);

        return cryptographicManager.decryptBytes(payload);
    }

    /**
     * Cifra una cadena usando AES-256-GCM y retorna Base64.
     */
    public String encryptString(@NonNull String data) {
        if (data == null) {
            throw new IllegalArgumentException("Los datos a cifrar no pueden ser null");
        }
        byte[] encrypted = encrypt(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return Base64.encodeToString(encrypted, Base64.NO_WRAP);
    }

    /**
     * Descifra una cadena Base64 cifrada con {@link #encryptString(String)}.
     */
    public String decryptString(@NonNull String encryptedData) {
        if (encryptedData == null) {
            throw new IllegalArgumentException("Los datos cifrados no pueden ser null");
        }
        byte[] decoded = Base64.decode(encryptedData, Base64.NO_WRAP);
        byte[] decrypted = decrypt(decoded);
        return new String(decrypted, java.nio.charset.StandardCharsets.UTF_8);
    }
}
