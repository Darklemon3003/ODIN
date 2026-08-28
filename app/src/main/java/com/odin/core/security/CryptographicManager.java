package com.odin.core.security;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * CryptographicManager gestiona el cifrado de datos utilizando AndroidKeyStore
 * con especificación AES-256-GCM. Todas las operaciones de cifrado/descifrado
 * están encapsuladas y son thread-safe.
 *
 * Diseño defensivo: Si alguna operación falla, lanza excepciones específicas
 * para que SentryCore pueda tomar decisiones sobre auto-sanación.
 */
public final class CryptographicManager {

    private static final String TAG = "CryptographicManager";
    private static final String KEYSTORE_ALIAS = "odin_master_key";
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // 128 bits = 16 bytes
    private static final int GCM_IV_LENGTH = 12; // 96 bits = 12 bytes (recomendado para GCM)
    private static final int AES_KEY_SIZE = 256; // AES-256

    private final Context context;
    private SecretKey masterKey;

    /**
     * Constructor que inicializa el contexto y carga/genera la clave maestra.
     */
    public CryptographicManager(@NonNull Context context) {
        this.context = context.getApplicationContext();
        initializeMasterKey();
    }

    /**
     * Inicializa la clave maestra desde AndroidKeyStore o la genera si no existe.
     * Esta operación se ejecuta una sola vez durante la instanciación.
     *
     * @throws SecurityException si no se puede acceder o generar la clave
     */
    private synchronized void initializeMasterKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            keyStore.load(null);

            // Verificar si la clave ya existe
            if (keyStore.containsAlias(KEYSTORE_ALIAS)) {
                KeyStore.Entry entry = keyStore.getEntry(KEYSTORE_ALIAS, null);
                if (entry instanceof KeyStore.SecretKeyEntry) {
                    masterKey = ((KeyStore.SecretKeyEntry) entry).getSecretKey();
                    return;
                }
            }

            // Si no existe, generar una nueva clave
            generateMasterKey();

        } catch (Exception e) {
            throw new SecurityException("No se pudo inicializar la clave maestra del KeyStore", e);
        }
    }

    /**
     * Genera una nueva clave maestra AES-256-GCM en el AndroidKeyStore.
     * Esta clave está protegida por hardware en dispositivos compatibles.
     *
     * @throws SecurityException si la generación de clave falla
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateMasterKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    KEYSTORE_PROVIDER);

            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                    KEYSTORE_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setKeySize(AES_KEY_SIZE)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setRandomizedEncryptionRequired(true)
                    .build();

            keyGenerator.init(spec);
            masterKey = keyGenerator.generateKey();

        } catch (Exception e) {
            throw new SecurityException("No se pudo generar la clave maestra AES-256-GCM", e);
        }
    }

    /**
     * Cifra una cadena de texto usando AES-256-GCM.
     * El IV (Initialization Vector) es generado aleatoriamente y se incluye
     * en el resultado junto con el ciphertext para permitir descifrado posterior.
     *
     * @param plaintext Texto plano a cifrar
     * @return Base64-encoded string con formato: [IV (12 bytes)][CIPHERTEXT][TAG
     *         (16 bytes)]
     * @throws SecurityException si el cifrado falla
     */
    public synchronized String encrypt(@NonNull String plaintext) {
        if (plaintext.isEmpty()) {
            throw new IllegalArgumentException("El texto plano no puede estar vacío");
        }

        try {
            // Android Keystore con randomizedEncryptionRequired=true debe generar el IV.
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, masterKey);
            byte[] iv = cipher.getIV();
            if (iv == null || iv.length != GCM_IV_LENGTH) {
                throw new SecurityException("IV AES-GCM invalido generado por KeyStore");
            }

            // Cifrar datos
            byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertext = cipher.doFinal(plaintextBytes);

            // Combinar IV + ciphertext y codificar en Base64
            ByteBuffer buffer = ByteBuffer.allocate(GCM_IV_LENGTH + ciphertext.length);
            buffer.put(iv);
            buffer.put(ciphertext);
            byte[] encryptedData = buffer.array();

            return Base64.getEncoder().encodeToString(encryptedData);

        } catch (Exception e) {
            throw new SecurityException("Error durante cifrado AES-256-GCM", e);
        }
    }

    /**
     * Descifra una cadena cifrada con AES-256-GCM.
     * Extrae el IV del inicio de los datos y lo utiliza para inicializar el Cipher.
     *
     * @param ciphertext Base64-encoded string con formato: [IV][CIPHERTEXT][TAG]
     * @return Texto plano descifrado
     * @throws SecurityException si el descifrado falla (datos corruptos o
     *                           autenticación falla)
     */
    public synchronized String decrypt(@NonNull String ciphertext) {
        if (ciphertext.isEmpty()) {
            throw new IllegalArgumentException("El ciphertext no puede estar vacío");
        }

        try {
            // Decodificar del Base64
            byte[] encryptedData = Base64.getDecoder().decode(ciphertext);

            // Validar tamaño mínimo (IV + al menos algo de ciphertext + TAG)
            if (encryptedData.length < GCM_IV_LENGTH + GCM_TAG_LENGTH) {
                throw new SecurityException("Datos cifrados corrupta: tamaño insuficiente");
            }

            // Extraer IV
            ByteBuffer buffer = ByteBuffer.wrap(encryptedData);
            byte[] iv = new byte[GCM_IV_LENGTH];
            buffer.get(iv);

            // Extraer ciphertext
            byte[] encryptedText = new byte[buffer.remaining()];
            buffer.get(encryptedText);

            // Inicializar cipher en modo de descifrado
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, masterKey, spec);

            // Descifrar
            byte[] plaintextBytes = cipher.doFinal(encryptedText);
            return new String(plaintextBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new SecurityException("Error durante descifrado AES-256-GCM (datos pueden estar corruptos)", e);
        }
    }

    /**
     * Verifica si la clave maestra está disponible y operativa.
     *
     * @return true si la clave está lista para usarse
     */
    public boolean isKeyAvailable() {
        return masterKey != null;
    }

    /**
     * Limpia la clave de la memoria (aunque Java GC se encargará eventualmente).
     * Se recomienda llamar al destruir el manager.
     */
    public void clearKey() {
        masterKey = null;
    }
}
