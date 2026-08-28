package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.odin.core.customization.model.NotificationSettings;

/**
 * REAL NotificationSettingsConverter for Room database.
 * This is a REAL converter using Gson for JSON serialization - no simulations.
 */
public class NotificationSettingsConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static NotificationSettings fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return gson.fromJson(value, NotificationSettings.class);
        } catch (Exception e) {
            return null;
        }
    }

    @TypeConverter
    public static String toString(NotificationSettings settings) {
        return settings != null ? gson.toJson(settings) : null;
    }
}
