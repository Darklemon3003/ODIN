package com.odin.core.data.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SecureLocalStorage centraliza la configuración de la persistencia local.
 * Actualmente usa SharedPreferences con MODE_PRIVATE, lo que es apropiado
 * para la fase inicial de SentryCore como almacén protegido en el dispositivo.
 */
public final class SecureLocalStorage {

    private static final String PREFERENCES_NAME = "odin_sentry_prefs";
    private final SharedPreferences preferences;

    public SecureLocalStorage(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void saveString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String loadString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public void saveBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean loadBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }
}
