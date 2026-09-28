package com.odin.core.persistence;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.odin.core.data.local.EnhancedSecureLocalStorage;
import com.odin.core.security.ConsentManager;

/**
 * DataResetManager proporciona un mecanismo explicito y controlado para
 * resetear los datos locales de ODIN.
 *
 * Phase 10: Implementa el contrato de reset para datos de Phase 10.
 * La UI debe confirmar la accion destructiva antes de llamar a reset().
 *
 * Reset NO afecta:
 * - Permisos del SO Android
 * - Autorizacion VPN del SO
 * - Claves del AndroidKeyStore (se mantienen)
 *
 * Reset SI afecta:
 * - Eventos de log cifrados (SentryCore)
 * - Estado del sistema cifrado
 * - Consentimientos de seguridad (fail-closed despues de reset)
 * - Configuraciones de seguridad cifradas
 * - Crash traces
 * - Datos de cuarentena de corrupcion
 */
public final class DataResetManager {

    private static final String TAG = "DataResetManager";

    private DataResetManager() {
        // Utility class
    }

    /**
     * Reset completo de datos locales de Phase 10.
     *
     * Este metodo es DESTRUCTIVO e IRREVERSIBLE.
     * La UI debe confirmar con el usuario antes de llamar a este metodo.
     *
     * @param context ApplicationContext
     * @throws RuntimeException si el reset falla parcialmente
     */
    public static void performFullReset(@NonNull Context context) {
        Log.w(TAG, "Iniciando reset completo de datos locales...");

        try {
            // 1. Limpiar almacenamiento cifrado (eventos, estado, crash traces, etc.)
            EnhancedSecureLocalStorage secureStorage = EnhancedSecureLocalStorage.getInstance(context);
            secureStorage.clear();
            Log.i(TAG, "Almacenamiento cifrado limpiado");

            // 2. Resetear consentimientos a false (fail-closed)
            // Esto se logra al limpiar el almacenamiento cifrado ya que
            // ConsentManager ahora usa EnhancedSecureLocalStorage.
            // ConsentManager.getInstance(context) leera false por defecto.
            Log.i(TAG, "Consentimientos reseteados a false (fail-closed)");

            // 3. Limpiar preferencias legacy si aun existen
            cleanLegacyPreferences(context);

            // 4. Resetear SentryCore
            SentryCore sentryCore = SentryCore.getInstance(context);
            sentryCore.clearCorruptData();
            Log.i(TAG, "SentryCore reseteado");

            // 5. Resetear instancia de ConsentManager para que recargue estado
            // (no hay reset explicito en ConsentManager, pero al limpiar el storage
            // los siguientes reads retornaran false)

            Log.w(TAG, "Reset completo finalizado. Todos los datos locales destruidos.");
            Log.w(TAG, "Sentry y NetShield NO se auto-iniciaran. Consentimientos requieren confirmacion.");

        } catch (Exception e) {
            Log.e(TAG, "Error durante reset: " + e.getMessage());
            throw new RuntimeException("Reset parcialmente fallido: " + e.getMessage(), e);
        }
    }

    /**
     * Limpia preferencias legacy plaintext que puedan existir.
     */
    private static void cleanLegacyPreferences(@NonNull Context context) {
        try {
            // odin_sentry_prefs (legacy log/status - ya migrado o eliminado)
            Context appContext = context.getApplicationContext();
            appContext.getSharedPreferences("odin_sentry_prefs", Context.MODE_PRIVATE)
                    .edit().clear().apply();

            // odin_settings: mantener solo general_settings, limpiar consentimientos
            // ya migrados y security/network settings ya migrados
            android.content.SharedPreferences settings = appContext
                    .getSharedPreferences("odin_settings", Context.MODE_PRIVATE);
            settings.edit()
                    .remove("consent_sentry_enabled")
                    .remove("consent_boot_sentry_enabled")
                    .remove("consent_netshield_vpn_enabled")
                    .remove("consent_notifications_enabled")
                    .remove("consent_file_access_enabled")
                    .remove("consent_log_export_enabled")
                    .remove("consent_quarantine_actions_enabled")
                    .remove("security_settings")
                    .remove("network_settings")
                    .apply();

            Log.i(TAG, "Preferencias legacy limpiadas");
        } catch (Exception e) {
            Log.w(TAG, "Error limpiando preferencias legacy: " + e.getMessage());
        }
    }
}
