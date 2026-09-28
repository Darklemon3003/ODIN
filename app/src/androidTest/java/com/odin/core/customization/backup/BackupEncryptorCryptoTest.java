package com.odin.core.customization.backup;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Pruebas de roundtrip criptografico real de BackupEncryptor en dispositivo.
 * Requiere AndroidKeyStore del dispositivo/emulador.
 *
 * Valida:
 * - encrypt -> decrypt roundtrip
 * - mismo plaintext produce ciphertext diferente (IV aleatorio)
 * - ciphertext corrupto falla
 * - tag de autenticacion corrupto falla
 * - formato desconocido falla
 * - formato legacy no soportado falla
 * - fallo de cifrado nunca retorna plaintext
 * - fallo de descifrado nunca retorna ciphertext como valido
 */
@RunWith(AndroidJUnit4.class)
public class BackupEncryptorCryptoTest {

    private BackupEncryptor createEncryptor() {
        Context context = ApplicationProvider.getApplicationContext();
        return new BackupEncryptor(context);
    }

    @Test
    public void encryptDecryptRoundtrip() {
        BackupEncryptor encryptor = createEncryptor();
        byte[] plaintext = "sensitive backup data 12345".getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = encryptor.encrypt(plaintext);
        assertNotNull(encrypted);
        assertTrue(encrypted.length > plaintext.length);

        byte[] decrypted = encryptor.decrypt(encrypted);
        assertArrayEquals(plaintext, decrypted);
    }

    @Test
    public void samePlaintextProducesDifferentCiphertext() {
        BackupEncryptor encryptor = createEncryptor();
        byte[] plaintext = "same data".getBytes(StandardCharsets.UTF_8);

        byte[] encrypted1 = encryptor.encrypt(plaintext);
        byte[] encrypted2 = encryptor.encrypt(plaintext);

        assertFalse(Arrays.equals(encrypted1, encrypted2),
                "El mismo plaintext debe producir ciphertext diferente por IV aleatorio");

        // Ambos deben descifrar al mismo plaintext
        assertArrayEquals(plaintext, encryptor.decrypt(encrypted1));
        assertArrayEquals(plaintext, encryptor.decrypt(encrypted2));
    }

    @Test
    public void corruptedCiphertextFails() {
        BackupEncryptor encryptor = createEncryptor();
        byte[] plaintext = "data to corrupt".getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = encryptor.encrypt(plaintext);

        // Corromper un byte del ciphertext (despues de magic + version + IV)
        int corruptIndex = BackupEncryptor.MAGIC.length + 1 + 12 + 4;
        encrypted[corruptIndex] ^= 0x01;

        try {
            encryptor.decrypt(encrypted);
            fail("Deberia lanzar excepcion por ciphertext corrupto");
        } catch (Exception e) {
            // Esperado: SecurityException o BackupEncryptionException
        }
    }

    @Test
    public void corruptedAuthenticationTagFails() {
        BackupEncryptor encryptor = createEncryptor();
        byte[] plaintext = "tag test data".getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = encryptor.encrypt(plaintext);

        // Corromper el ultimo byte (parte del tag GCM)
        encrypted[encrypted.length - 1] ^= 0x01;

        try {
            encryptor.decrypt(encrypted);
            fail("Deberia lanzar excepcion por tag corrupto");
        } catch (Exception e) {
            // Esperado: AEADBadTagException envuelto en SecurityException
        }
    }

    @Test
    public void unknownFormatFails() {
        BackupEncryptor encryptor = createEncryptor();
        byte[] unknownFormat = new byte[40];
        // Magic incorrecto
        System.arraycopy("FAKEBAK".getBytes(StandardCharsets.US_ASCII), 0,
                unknownFormat, 0, 7);
        unknownFormat[7] = BackupEncryptor.FORMAT_VERSION_2;

        try {
            encryptor.decrypt(unknownFormat);
            fail("Deberia lanzar excepcion por formato desconocido");
        } catch (BackupEncryptionException e) {
            assertTrue(e.getMessage().contains("magic") || e.getMessage().contains("desconocido"));
        }
    }

    @Test
    public void legacyFormatFails() {
        BackupEncryptor encryptor = createEncryptor();
        byte[] legacyData = new byte[40];
        System.arraycopy(BackupEncryptor.MAGIC, 0, legacyData, 0, 7);
        legacyData[7] = BackupEncryptor.FORMAT_VERSION_LEGACY_1;

        try {
            encryptor.decrypt(legacyData);
            fail("Deberia lanzar excepcion por formato legacy no soportado");
        } catch (BackupEncryptionException e) {
            assertTrue(e.getMessage().contains("legacy") || e.getMessage().contains("no soportado"));
        }
    }

    @Test
    public void unknownVersionFails() {
        BackupEncryptor encryptor = createEncryptor();
        byte[] unknownVersion = new byte[40];
        System.arraycopy(BackupEncryptor.MAGIC, 0, unknownVersion, 0, 7);
        unknownVersion[7] = 0x09; // Version desconocida

        try {
            encryptor.decrypt(unknownVersion);
            fail("Deberia lanzar excepcion por version no soportada");
        } catch (BackupEncryptionException e) {
            assertTrue(e.getMessage().contains("no soportada") || e.getMessage().contains("version"));
        }
    }

    @Test
    public void encryptNullThrowsException() {
        BackupEncryptor encryptor = createEncryptor();
        try {
            encryptor.encrypt(null);
            fail("Deberia lanzar IllegalArgumentException por null");
        } catch (IllegalArgumentException e) {
            // Esperado
        }
    }

    @Test
    public void encryptEmptyThrowsException() {
        BackupEncryptor encryptor = createEncryptor();
        try {
            encryptor.encrypt(new byte[0]);
            fail("Deberia lanzar IllegalArgumentException por vacio");
        } catch (IllegalArgumentException e) {
            // Esperado
        }
    }

    @Test
    public void decryptNullThrowsException() {
        BackupEncryptor encryptor = createEncryptor();
        try {
            encryptor.decrypt(null);
            fail("Deberia lanzar IllegalArgumentException por null");
        } catch (IllegalArgumentException e) {
            // Esperado
        }
    }

    @Test
    public void encryptDecryptStringRoundtrip() {
        BackupEncryptor encryptor = createEncryptor();
        String plaintext = "string backup data with special chars: !@#$%^&*()";

        String encrypted = encryptor.encryptString(plaintext);
        assertNotNull(encrypted);
        assertNotEquals(plaintext, encrypted);

        String decrypted = encryptor.decryptString(encrypted);
        assertEquals(plaintext, decrypted);
    }

    @Test
    public void encryptedDataHasMagicHeader() {
        BackupEncryptor encryptor = createEncryptor();
        byte[] plaintext = "magic header test".getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = encryptor.encrypt(plaintext);

        // Verificar que empieza con MAGIC
        assertEquals(7, BackupEncryptor.MAGIC.length);
        for (int i = 0; i < BackupEncryptor.MAGIC.length; i++) {
            assertEquals(BackupEncryptor.MAGIC[i], encrypted[i],
                    "Byte " + i + " debe coincidir con magic");
        }
        assertEquals(BackupEncryptor.FORMAT_VERSION_2, encrypted[7],
                "Byte 7 debe ser version 0x02");
    }
}
