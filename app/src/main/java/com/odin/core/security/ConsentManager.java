package com.odin.core.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.odin.core.data.local.EnhancedSecureLocalStorage;

/**
 * ConsentManager gestiona los consentimientos de seguridad del usuario.
 *
 * Phase 10: Los consentimientos se almacenan en EnhancedSecureLocalStorage
 * (cifrado AES-256-GCM via AndroidKeyStore).
 *
 * Semantica fail-closed: si el almacenamiento cifrado no puede leerse,
 * el consentimiento es false (requiere confirmacion del usuario).
 * No se usa plaintext como fallback de autorizacion.
 *
 * Migracion: al primer arranque, si existen valores legacy en plaintext
 * (odin_settings), se migran al almacenamiento cifrado y se eliminan.
 */
public final class ConsentManager {

    private static final String TAG = "ConsentManager";
    private static final String LEGACY_PREFS_NAME = "odin_settings";
    private static final String MIGRATION_MARKER = "key_consent_migration_completed";

    public static final String CONSENT_SENTRY_ENABLED = "consent_sentry_enabled";
    public static final String CONSENT_BOOT_SENTRY_ENABLED = "consent_boot_sentry_enabled";
    public static final String CONSENT_NETSHIELD_VPN_ENABLED = "consent_netshield_vpn_enabled";
    public static final String CONSENT_NOTIFICATIONS_ENABLED = "consent_notifications_enabled";
    public static final String CONSENT_FILE_ACCESS_ENABLED = "consent_file_access_enabled";
    public static final String CONSENT_LOG_EXPORT_ENABLED = "consent_log_export_enabled";
    public static final String CONSENT_QUARANTINE_ACTIONS_ENABLED = "consent_quarantine_actions_enabled";

    private static volatile ConsentManager instance;

    private final EnhancedSecureLocalStorage secureStorage;
    private final SharedPreferences legacyPrefs;

    private ConsentManager(@NonNull Context context) {
        EnhancedSecureLocalStorage storage;
        try {
            storage = EnhancedSecureLocalStorage.getInstance(context);
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando almacenamiento cifrado: " + e.getMessage());
            storage = null;
        }
        this.secureStorage = storage;
        this.legacyPrefs = context
                .getApplicationContext()
                .getSharedPreferences(LEGACY_PREFS_NAME, Context.MODE_PRIVATE);
        if (storage != null) {
            migrateLegacyConsentsIfNeeded();
        }
    }

    public static ConsentManager getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (ConsentManager.class) {
                if (instance == null) {
                    instance = new ConsentManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * Migra consentimientos legacy desde plaintext a almacenamiento cifrado.
     * Idempotente: seguro de ejecutar en cada arranque.
     */
    private void migrateLegacyConsentsIfNeeded() {
        if (secureStorage == null) {
            return;
        }
        try {
            if (secureStorage.loadBoolean(MIGRATION_MARKER, false)) {
                return;
            }

            String[] consentKeys = {
                    CONSENT_SENTRY_ENABLED,
                    CONSENT_BOOT_SENTRY_ENABLED,
                    CONSENT_NETSHIELD_VPN_ENABLED,
                    CONSENT_NOTIFICATIONS_ENABLED,
                    CONSENT_FILE_ACCESS_ENABLED,
                    CONSENT_LOG_EXPORT_ENABLED,
                    CONSENT_QUARANTINE_ACTIONS_ENABLED
            };

            SharedPreferences.Editor editor = legacyPrefs.edit();
            boolean anyMigrated = false;

            for (String key : consentKeys) {
                if (legacyPrefs.contains(key)) {
                    boolean value = legacyPrefs.getBoolean(key, false);
                    secureStorage.saveBoolean(key, value);
                    // Verificar que el consentimiento se guardo correctamente antes de eliminar plaintext
                    boolean verifyValue = secureStorage.loadBoolean(key, !value);
                    if (verifyValue == value) {
                        editor.remove(key);
                        anyMigrated = true;
                    } else {
                        Log.e(TAG, "Verificacion de migracion fallo para " + key + " - no se elimina plaintext");
                    }
                }
            }

            if (anyMigrated) {
                editor.apply();
                Log.i(TAG, "Consentimientos legacy migrados a almacenamiento cifrado");
            }

            secureStorage.saveBoolean(MIGRATION_MARKER, true);
        } catch (Exception e) {
            Log.w(TAG, "Migracion de consentimientos pendiente: " + e.getMessage());
            // No bloquear la app. Se reintentara en el siguiente arranque.
        }
    }

    public boolean isSentryEnabled() {
        return getBoolean(CONSENT_SENTRY_ENABLED);
    }

    public void setSentryEnabled(boolean enabled) {
        putBoolean(CONSENT_SENTRY_ENABLED, enabled);
    }

    public boolean isBootSentryEnabled() {
        return getBoolean(CONSENT_BOOT_SENTRY_ENABLED);
    }

    public void setBootSentryEnabled(boolean enabled) {
        putBoolean(CONSENT_BOOT_SENTRY_ENABLED, enabled);
    }

    public boolean canStartSentryOnBoot() {
        return isSentryEnabled() && isBootSentryEnabled();
    }

    public boolean isNetShieldVpnEnabled() {
        return getBoolean(CONSENT_NETSHIELD_VPN_ENABLED);
    }

    public void setNetShieldVpnEnabled(boolean enabled) {
        putBoolean(CONSENT_NETSHIELD_VPN_ENABLED, enabled);
    }

    public boolean isNotificationsEnabled() {
        return getBoolean(CONSENT_NOTIFICATIONS_ENABLED);
    }

    public void setNotificationsEnabled(boolean enabled) {
        putBoolean(CONSENT_NOTIFICATIONS_ENABLED, enabled);
    }

    public boolean isFileAccessEnabled() {
        return getBoolean(CONSENT_FILE_ACCESS_ENABLED);
    }

    public void setFileAccessEnabled(boolean enabled) {
        putBoolean(CONSENT_FILE_ACCESS_ENABLED, enabled);
    }

    public boolean isLogExportEnabled() {
        return getBoolean(CONSENT_LOG_EXPORT_ENABLED);
    }

    public void setLogExportEnabled(boolean enabled) {
        putBoolean(CONSENT_LOG_EXPORT_ENABLED, enabled);
    }

    public boolean isQuarantineActionsEnabled() {
        return getBoolean(CONSENT_QUARANTINE_ACTIONS_ENABLED);
    }

    public void setQuarantineActionsEnabled(boolean enabled) {
        putBoolean(CONSENT_QUARANTINE_ACTIONS_ENABLED, enabled);
    }

    /**
     * Lee un consentimiento desde almacenamiento cifrado.
     * Fail-closed: si el almacenamiento falla o no esta disponible, retorna false.
     */
    private boolean getBoolean(String key) {
        if (secureStorage == null) {
            // Fail-closed: sin almacenamiento cifrado, no autorizar.
            return false;
        }
        try {
            return secureStorage.loadBoolean(key, false);
        } catch (Exception e) {
            // Fail-closed: sin consentimiento explicito, no autorizar.
            return false;
        }
    }

    /**
     * Guarda un consentimiento en almacenamiento cifrado.
     */
    private void putBoolean(String key, boolean value) {
        if (secureStorage == null) {
            Log.e(TAG, "No se puede guardar consentimiento " + key + ": almacenamiento no disponible");
            return;
        }
        try {
            secureStorage.saveBoolean(key, value);
        } catch (Exception e) {
            Log.e(TAG, "Error guardando consentimiento " + key + ": " + e.getMessage());
        }
    }
}
