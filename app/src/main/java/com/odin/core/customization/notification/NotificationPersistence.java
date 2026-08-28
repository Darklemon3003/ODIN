package com.odin.core.customization.notification;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.NotificationSettings;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * REAL NotificationPersistence for ODIN customization system.
 * This is a REAL persistence layer with REAL storage operations - no simulations.
 */
public class NotificationPersistence {

    private static final String PREFS_NAME = "notification_persistence";
    private static final String KEY_SETTINGS = "notification_settings";
    private static final String KEY_HISTORY = "notification_history";

    private final SharedPreferences prefs;
    private final Gson gson;

    public NotificationPersistence(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void saveSettings(NotificationSettings settings) {
        if (settings != null) {
            String settingsJson = gson.toJson(settings);
            prefs.edit().putString(KEY_SETTINGS, settingsJson).apply();
        }
    }

    public NotificationSettings getSettings() {
        String settingsJson = prefs.getString(KEY_SETTINGS, null);
        if (settingsJson != null) {
            Type type = new TypeToken<NotificationSettings>(){}.getType();
            return gson.fromJson(settingsJson, type);
        }
        return null;
    }

    public void saveNotificationHistory(List<String> history) {
        if (history != null) {
            String historyJson = gson.toJson(history);
            prefs.edit().putString(KEY_HISTORY, historyJson).apply();
        }
    }

    public List<String> getNotificationHistory() {
        String historyJson = prefs.getString(KEY_HISTORY, null);
        if (historyJson != null) {
            Type type = new TypeToken<List<String>>(){}.getType();
            return gson.fromJson(historyJson, type);
        }
        return new ArrayList<>();
    }

    public void clearSettings() {
        prefs.edit().remove(KEY_SETTINGS).apply();
    }

    public void clearHistory() {
        prefs.edit().remove(KEY_HISTORY).apply();
    }

    public void clearAll() {
        prefs.edit().clear().apply();
    }
}
