package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.odin.core.customization.model.Theme;

/**
 * REAL ThemeConverter for Room database.
 * This is a REAL converter using Gson for JSON serialization - no simulations.
 */
public class ThemeConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static Theme fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return gson.fromJson(value, Theme.class);
        } catch (Exception e) {
            return null;
        }
    }

    @TypeConverter
    public static String toString(Theme theme) {
        return theme != null ? gson.toJson(theme) : null;
    }
}
