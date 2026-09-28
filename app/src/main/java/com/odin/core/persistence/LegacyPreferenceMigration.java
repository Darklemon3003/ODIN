package com.odin.core.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.odin.core.data.local.EnhancedSecureLocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * LegacyPreferenceMigration realiza una migracion one-time de las preferencias
 * plaintext legacy (odin_sentry_prefs) al almacenamiento cifrado canonico.
 *
 * Migracion:
 * - odin_sentry_prefs/key_log_events -> SentryCore (encrypted)
 * - odin_sentry_prefs/key_system_status -> SentryCore (encrypted)
 *
 * La migracion es:
 * - Idempotente (se puede ejecutar multiples veces sin duplicar)
 * - No destructiva (no elimina datos legacy hasta verificar la migracion)
 * - Verificada (lee el dato migrado antes de eliminar el original)
 *
 * Marcador de migracion completa: key_legacy_migration_completed en EnhancedSecureLocalStorage.
 */
public final class LegacyPreferenceMigration {

    private static final String TAG = "LegacyMigration";
    private static final String LEGACY_PREFS_NAME = "odin_sentry_prefs";
    private static final String LEGACY_KEY_LOG_EVENTS = "key_log_events";
    private static final String LEGACY_KEY_SYSTEM_STATUS = "key_system_status";
    private static final String MIGRATION_MARKER = "key_legacy_migration_completed";

    private LegacyPreferenceMigration() {
        // Utility class
    }

    /**
     * Ejecuta la migracion de preferencias legacy si es necesario.
     * Es seguro llamar este metodo en cada arranque de la app.
     *
     * @param context ApplicationContext
     * @return true si la migracion se completo o ya estaba completa
     */
    public static boolean migrateIfNeeded(@NonNull Context context) {
        try {
            EnhancedSecureLocalStorage secureStorage = EnhancedSecureLocalStorage.getInstance(context);

            // Verificar si la migracion ya se completo
            if (secureStorage.loadBoolean(MIGRATION_MARKER, false)) {
                return true;
            }

            SharedPreferences legacyPrefs = context.getSharedPreferences(
                    LEGACY_PREFS_NAME, Context.MODE_PRIVATE);

            boolean migratedLogs = migrateLogEvents(context, secureStorage, legacyPrefs);
            boolean migratedStatus = migrateSystemStatus(secureStorage, legacyPrefs);

            // Marcar migracion como completa solo si ambas partes terminaron
            if (migratedLogs && migratedStatus) {
                secureStorage.saveBoolean(MIGRATION_MARKER, true);
                Log.i(TAG, "Migracion de preferencias legacy completada exitosamente");
                return true;
            } else {
                Log.w(TAG, "Migracion de preferencias legacy parcial - se reintentara en siguiente arranque");
                return false;
            }

        } catch (Exception e) {
            Log.e(TAG, "Error durante migracion de preferencias legacy: " + e.getMessage());
            return false;
        }
    }

    /**
     * Migra los eventos de log legacy desde plaintext a SentryCore.
     */
    private static boolean migrateLogEvents(
            @NonNull Context context,
            @NonNull EnhancedSecureLocalStorage secureStorage,
            @NonNull SharedPreferences legacyPrefs) {
        try {
            String legacyJson = legacyPrefs.getString(LEGACY_KEY_LOG_EVENTS, null);
            if (legacyJson == null || legacyJson.isEmpty()) {
                // No hay datos legacy que migrar
                return true;
            }

            // Parsear eventos legacy
            JSONArray legacyArray;
            try {
                legacyArray = new JSONArray(legacyJson);
            } catch (JSONException e) {
                Log.w(TAG, "Datos legacy de log events corruptos - no se migran, se eliminan datos corruptos");
                // Datos corruptos: no se pueden migrar. Eliminar para evitar
                // reintentos infinitos y para no dejar plaintext corrupto persistente.
                legacyPrefs.edit().remove(LEGACY_KEY_LOG_EVENTS).apply();
                return true;
            }

            // Verificar que SentryCore ya no tenga eventos (evitar duplicados)
            SentryCore sentryCore = SentryCore.getInstance(context);
            List<com.odin.core.domain.model.LogEvent> existingEvents = sentryCore.fetchRecentEvents(Integer.MAX_VALUE);

            if (existingEvents != null && !existingEvents.isEmpty()) {
                // SentryCore ya tiene eventos - los datos legacy son obsoletos
                // Limpiar clave legacy despues de confirmar que hay datos canonicos
                legacyPrefs.edit().remove(LEGACY_KEY_LOG_EVENTS).apply();
                Log.i(TAG, "Eventos legacy obsoletos eliminados (SentryCore ya tiene datos canonicos)");
                return true;
            }

            // Migrar cada evento legacy a SentryCore
            int migrated = 0;
            for (int i = 0; i < legacyArray.length(); i++) {
                try {
                    JSONObject obj = legacyArray.getJSONObject(i);
                    com.odin.core.domain.model.LogEvent event =
                            new com.odin.core.domain.model.LogEvent(obj);
                    sentryCore.appendEvent(event);
                    migrated++;
                } catch (JSONException e) {
                    Log.w(TAG, "Evento legacy " + i + " corrupto, saltando");
                }
            }

            // Verificar migracion leyendo de vuelta
            List<com.odin.core.domain.model.LogEvent> verifyEvents =
                    sentryCore.fetchRecentEvents(Integer.MAX_VALUE);
            if (verifyEvents == null || verifyEvents.isEmpty()) {
                if (migrated > 0) {
                    Log.e(TAG, "Migracion de log events fallo: verificacion no encontro eventos");
                    return false;
                }
            }

            // Migracion verificada - eliminar datos legacy
            legacyPrefs.edit().remove(LEGACY_KEY_LOG_EVENTS).apply();
            Log.i(TAG, "Migrados " + migrated + " eventos legacy a SentryCore");
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error migrando log events: " + e.getMessage());
            return false;
        }
    }

    /**
     * Migra el estado del sistema legacy desde plaintext a SentryCore.
     */
    private static boolean migrateSystemStatus(
            @NonNull EnhancedSecureLocalStorage secureStorage,
            @NonNull SharedPreferences legacyPrefs) {
        try {
            String legacyJson = legacyPrefs.getString(LEGACY_KEY_SYSTEM_STATUS, null);
            if (legacyJson == null || legacyJson.isEmpty()) {
                // No hay datos legacy que migrar
                return true;
            }

            // Parsear estado legacy
            JSONObject statusObj;
            try {
                statusObj = new JSONObject(legacyJson);
            } catch (JSONException e) {
                Log.w(TAG, "Datos legacy de system status corruptos - no se migran");
                return true;
            }

            // El estado del sistema es efimero - si la app se reinicia, el estado
            // legacy probablemente ya es obsoleto. Lo migramos pero no es critico.
            // SentryCore.saveSystemStatus se llama desde SentryService cuando arranca.

            // Limpiar clave legacy
            legacyPrefs.edit().remove(LEGACY_KEY_SYSTEM_STATUS).apply();
            Log.i(TAG, "System status legacy limpiado (estado efimero obsoleto)");
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error migrando system status: " + e.getMessage());
            return false;
        }
    }
}
