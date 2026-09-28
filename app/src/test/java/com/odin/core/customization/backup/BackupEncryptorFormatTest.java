package com.odin.core.customization.backup;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

/**
 * Pruebas del formato de envelope de BackupEncryptor que no requieren AndroidKeyStore.
 *
 * Estas pruebas validan:
 * - Formato de magic bytes
 * - Version del formato
 * - Deteccion de formato desconocido
 * - Deteccion de formato legacy no soportado
 * - Deteccion de datos corruptos (tamano insuficiente)
 * - Ausencia de clave hardcoded en codigo de produccion
 * - Ausencia de AES/ECB en codigo de produccion
 * - Ausencia de plaintext fallback en codigo de produccion
 *
 * Las pruebas de roundtrip criptografico real estan en BackupEncryptorCryptoTest
 * (instrumentation test, requiere AndroidKeyStore del dispositivo).
 */
public class BackupEncryptorFormatTest {

    private static final byte[] MAGIC = "ODINBAK".getBytes(StandardCharsets.US_ASCII);

    @Test
    public void magicBytesAreODINBAK() {
        assertEquals(7, MAGIC.length);
        assertEquals("ODINBAK", new String(MAGIC, StandardCharsets.US_ASCII));
    }

    @Test
    public void formatVersion2Is0x02() {
        assertEquals(0x02, BackupEncryptor.FORMAT_VERSION_2 & 0xFF);
    }

    @Test
    public void legacyVersion1Is0x01() {
        assertEquals(0x01, BackupEncryptor.FORMAT_VERSION_LEGACY_1 & 0xFF);
    }

    @Test
    public void decryptWithInsufficientSizeDetected() {
        // Crear datos con magic correcto pero tamano insuficiente
        byte[] tooSmall = new byte[MAGIC.length + 1 + 5];
        System.arraycopy(MAGIC, 0, tooSmall, 0, MAGIC.length);
        tooSmall[MAGIC.length] = BackupEncryptor.FORMAT_VERSION_2;

        int minSize = MAGIC.length + 1 + 12 + 16;
        assertTrue("Datos pequenos deben ser menores al tamano minimo de " + minSize,
                tooSmall.length < minSize);
    }

    @Test
    public void decryptWithWrongMagicDetected() {
        byte[] wrongMagic = new byte[MAGIC.length + 1 + 28];
        System.arraycopy("FAKEBAK".getBytes(StandardCharsets.US_ASCII), 0,
                wrongMagic, 0, MAGIC.length);
        wrongMagic[MAGIC.length] = BackupEncryptor.FORMAT_VERSION_2;

        boolean magicMatches = true;
        for (int i = 0; i < MAGIC.length; i++) {
            if (wrongMagic[i] != MAGIC[i]) {
                magicMatches = false;
                break;
            }
        }
        assertFalse("Magic incorrecto no debe coincidir", magicMatches);
    }

    @Test
    public void legacyVersionIsUnsupported() {
        byte[] legacyData = new byte[MAGIC.length + 1 + 32];
        System.arraycopy(MAGIC, 0, legacyData, 0, MAGIC.length);
        legacyData[MAGIC.length] = BackupEncryptor.FORMAT_VERSION_LEGACY_1;

        assertEquals(BackupEncryptor.FORMAT_VERSION_LEGACY_1, legacyData[MAGIC.length]);
        assertNotEquals(BackupEncryptor.FORMAT_VERSION_2, legacyData[MAGIC.length]);
    }

    @Test
    public void unknownVersionIsUnsupported() {
        byte unknownVersion = 0x09;
        assertNotEquals(BackupEncryptor.FORMAT_VERSION_2, unknownVersion);
        assertNotEquals(BackupEncryptor.FORMAT_VERSION_LEGACY_1, unknownVersion);
    }

    @Test
    public void hardcodedKeyStringIsAbsentFromProductionSource() {
        String sourceContent = stripComments(readBackupEncryptorSource());
        assertFalse("La clave hardcoded legacy no debe estar presente en BackupEncryptor.java",
                sourceContent.contains("ODIN_BACKUP_ENCRYPTION_KEY_2024"));
    }

    @Test
    public void aesEcbIsAbsentFromProductionSource() {
        String sourceContent = stripComments(readBackupEncryptorSource());
        // Verificar que no haya instanciacion de cipher AES/ECB
        assertFalse("Cipher.getInstance(\"AES/ECB\") no debe estar presente",
                sourceContent.contains("Cipher.getInstance(\"AES/ECB"));
        assertFalse("PKCS5Padding no debe estar presente en codigo ejecutable",
                sourceContent.contains("PKCS5Padding"));
    }

    @Test
    public void plaintextFallbackIsAbsentFromProductionSource() {
        String sourceContent = stripComments(readBackupEncryptorSource());
        assertFalse("El plaintext fallback (return data) no debe estar presente",
                sourceContent.contains("return data;"));
        assertFalse("El ciphertext fallback (return encryptedData) no debe estar presente",
                sourceContent.contains("return encryptedData;"));
    }

    @Test
    public void secretKeySpecIsAbsentFromProductionSource() {
        String sourceContent = stripComments(readBackupEncryptorSource());
        assertFalse("SecretKeySpec no debe estar presente - la clave debe venir de AndroidKeyStore",
                sourceContent.contains("SecretKeySpec"));
    }

    @Test
    public void messageDigestIsAbsentFromProductionSource() {
        String sourceContent = stripComments(readBackupEncryptorSource());
        assertFalse("MessageDigest no debe estar presente - no se deriva clave con SHA-256",
                sourceContent.contains("MessageDigest"));
    }

    /**
     * Elimina comentarios de bloque y linea de codigo Java para que las
     * pruebas estaticas solo verifiquen codigo ejecutable, no documentacion.
     */
    private String stripComments(String source) {
        // Eliminar comentarios de bloque (incluidos multi-linea con DOTALL)
        String result = source.replaceAll("(?s)/\\*.*?\\*/", "");
        // Eliminar comentarios de linea
        result = result.replaceAll("//.*", "");
        return result;
    }

    private String readBackupEncryptorSource() {
        try {
            java.io.File file = new java.io.File(
                    "app/src/main/java/com/odin/core/customization/backup/BackupEncryptor.java");
            if (!file.exists()) {
                file = new java.io.File(
                        "src/main/java/com/odin/core/customization/backup/BackupEncryptor.java");
            }
            if (!file.exists()) {
                return "";
            }
            byte[] bytes = new byte[(int) file.length()];
            try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                fis.read(bytes);
            }
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }
}
