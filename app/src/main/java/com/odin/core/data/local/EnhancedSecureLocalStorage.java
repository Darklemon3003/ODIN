package com.odin.core.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.odin.core.exception.OdinException;
import com.odin.core.exception.PersistenceException;
import com.odin.core.security.CryptographicManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * EnhancedSecureLocalStorage proporciona almacenamiento encriptado para datos
 * sensibles.
 *
 * Características:
 * - Usa EncryptedSharedPreferences (AES-256-GCM nativo de Android)
 * - Thread-safe: sincroniza acceso a SharedPreferences
 * - Manejo defensivo: try-catch granulares en cada operación
 * - Auto-sanación: limpia caché corrupta si falla el acceso
 *
 * Patrón de diseño: Singleton lazy-initialized.
 */
public final class EnhancedSecureLocalStorage {

    private static final String TAG = "EnhancedSecureLocalStorage";
    private static final String PREFERENCES_NAME = "odin_encrypted_prefs";
    private static volatile EnhancedSecureLocalStorage instance;

    private final SharedPreferences encryptedPrefs;
    private final Context context;

    /**
     * Constructor privado. Usar getInstance() en su lugar.
     *
     * @param context ApplicationContext
     * @throws PersistenceException si no se puede inicializar
     *                              EncryptedSharedPreferences
     */
    private EnhancedSecureLocalStorage(@NonNull Context context) throws PersistenceException {
        this.context = context.getApplicationContext();
        try {
            this.encryptedPrefs = initializeEncryptedPreferences(context);
        } catch (Exception e) {
            throw new PersistenceException(
                    "No se pudo inicializar EncryptedSharedPreferences",
                    e,
                    OdinException.ErrorSeverity.CRITICAL);
        }
    }

    /**
     * Inicializa EncryptedSharedPreferences con MasterKey generada automáticamente.
     * MasterKey está protegida por hardware en dispositivos compatibles (API 30+).
     *
     * @param context ApplicationContext
     * @return EncryptedSharedPreferences instance
     * @throws GeneralSecurityException si falla generación de MasterKey
     * @throws IOException              si falla acceso a SharedPreferences
     */
    private SharedPreferences initializeEncryptedPreferences(@NonNull Context context)
            throws GeneralSecurityException, IOException {

        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                context,
                PREFERENCES_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
    }

    /**
     * Obtiene la instancia singleton del storage encriptado.
     * Inicialización lazy con double-check locking.
     *
     * @param context ApplicationContext
     * @return EnhancedSecureLocalStorage instance
     * @throws PersistenceException si falla la inicialización
     */
    public static EnhancedSecureLocalStorage getInstance(@NonNull Context context)
            throws PersistenceException {

        if (instance == null) {
            synchronized (EnhancedSecureLocalStorage.class) {
                if (instance == null) {
                    instance = new EnhancedSecureLocalStorage(context);
                }
            }
        }
        return instance;
    }

    /**
     * Guarda un String encriptado.
     *
     * @param key   Clave de almacenamiento
     * @param value Valor a guardar (será encriptado por EncryptedSharedPreferences)
     * @throws PersistenceException si la operación falla
     */
    public synchronized void saveString(@NonNull String key, @NonNull String value)
            throws PersistenceException {

        if (key.isEmpty() || value.isEmpty()) {
            throw new IllegalArgumentException("Key y value no pueden estar vacíos");
        }

        try {
            encryptedPrefs.edit()
                    .putString(key, value)
                    .apply();

        } catch (Exception e) {
            handlePersistenceError("saveString", key, e);
        }
    }

    /**
     * Carga un String encriptado.
     *
     * @param key          Clave de almacenamiento
     * @param defaultValue Valor por defecto si no existe
     * @return Valor descifrado
     * @throws PersistenceException si la operación falla
     */
    public synchronized String loadString(@NonNull String key, @NonNull String defaultValue)
            throws PersistenceException {

        try {
            return encryptedPrefs.getString(key, defaultValue);

        } catch (SecurityException e) {
            // EncryptedSharedPreferences lanza SecurityException si la clave está corrupta
            handlePersistenceError("loadString", key, e);
            return defaultValue;

        } catch (Exception e) {
            handlePersistenceError("loadString", key, e);
            return defaultValue;
        }
    }

    /**
     * Guarda un Boolean encriptado.
     *
     * @param key   Clave de almacenamiento
     * @param value Valor a guardar
     * @throws PersistenceException si la operación falla
     */
    public synchronized void saveBoolean(@NonNull String key, boolean value)
            throws PersistenceException {

        try {
            encryptedPrefs.edit()
                    .putBoolean(key, value)
                    .apply();

        } catch (Exception e) {
            handlePersistenceError("saveBoolean", key, e);
        }
    }

    /**
     * Carga un Boolean encriptado.
     *
     * @param key          Clave de almacenamiento
     * @param defaultValue Valor por defecto si no existe
     * @return Valor booleano
     * @throws PersistenceException si la operación falla
     */
    public synchronized boolean loadBoolean(@NonNull String key, boolean defaultValue)
            throws PersistenceException {

        try {
            return encryptedPrefs.getBoolean(key, defaultValue);

        } catch (SecurityException e) {
            handlePersistenceError("loadBoolean", key, e);
            return defaultValue;

        } catch (Exception e) {
            handlePersistenceError("loadBoolean", key, e);
            return defaultValue;
        }
    }

    /**
     * Guarda un Long encriptado.
     *
     * @param key   Clave de almacenamiento
     * @param value Valor a guardar
     * @throws PersistenceException si la operación falla
     */
    public synchronized void saveLong(@NonNull String key, long value)
            throws PersistenceException {

        try {
            encryptedPrefs.edit()
                    .putLong(key, value)
                    .apply();

        } catch (Exception e) {
            handlePersistenceError("saveLong", key, e);
        }
    }

    /**
     * Carga un Long encriptado.
     *
     * @param key          Clave de almacenamiento
     * @param defaultValue Valor por defecto si no existe
     * @return Valor long
     * @throws PersistenceException si la operación falla
     */
    public synchronized long loadLong(@NonNull String key, long defaultValue)
            throws PersistenceException {

        try {
            return encryptedPrefs.getLong(key, defaultValue);

        } catch (SecurityException e) {
            handlePersistenceError("loadLong", key, e);
            return defaultValue;

        } catch (Exception e) {
            handlePersistenceError("loadLong", key, e);
            return defaultValue;
        }
    }

    /**
     * Elimina una clave del almacenamiento encriptado.
     *
     * @param key Clave a eliminar
     * @throws PersistenceException si la operación falla
     */
    public synchronized void remove(@NonNull String key) throws PersistenceException {
        try {
            encryptedPrefs.edit()
                    .remove(key)
                    .apply();

        } catch (Exception e) {
            handlePersistenceError("remove", key, e);
        }
    }

    /**
     * Limpia todo el almacenamiento encriptado.
     *
     * @throws PersistenceException si la operación falla
     */
    public synchronized void clear() throws PersistenceException {
        try {
            encryptedPrefs.edit().clear().apply();

        } catch (Exception e) {
            handlePersistenceError("clear", "ALL", e);
        }
    }

    /**
     * Verifica si existe una clave en el almacenamiento.
     *
     * @param key Clave a verificar
     * @return true si existe
     * @throws PersistenceException si la operación falla
     */
    public synchronized boolean contains(@NonNull String key) throws PersistenceException {
        try {
            return encryptedPrefs.contains(key);

        } catch (Exception e) {
            handlePersistenceError("contains", key, e);
            return false;
        }
    }

    /**
     * Maneja errores de persistencia con estrategia de auto-sanación.
     * Si los datos están corruptos, intenta limpiarlos e reinicializar.
     *
     * @param operation Nombre de la operación que falló
     * @param key       Clave afectada
     * @param cause     Excepción original
     * @throws PersistenceException siempre, con información del error
     */
    private void handlePersistenceError(String operation, String key, Exception cause)
            throws PersistenceException {

        String message = String.format(
                "Error en operación '%s' con clave '%s': %s",
                operation, key, cause.getMessage());

        // Intentar auto-sanación: limpiar la clave corrupta
        if (cause instanceof SecurityException) {
            try {
                encryptedPrefs.edit()
                        .remove(key)
                        .apply();
                message += " [RECUPERADO: clave corrupta removida]";
            } catch (Exception cleanupError) {
                message += " [SANACIÓN FALLÓ]";
            }
        }

        throw new PersistenceException(message, cause, OdinException.ErrorSeverity.MEDIUM);
    }

    /**
     * Reinicia la instancia singleton (útil para testing o recuperación de errores
     * críticos).
     */
    public static void resetInstance() {
        instance = null;
    }
}
