package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.ColorScheme;

/**
 * REAL ColorSchemeConverter for Room database.
 * This is a REAL converter using Gson for JSON serialization - no simulations.
 */
public class ColorSchemeConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static ColorScheme fromString(String value) {
        if (value == null) {
            return new ColorScheme();
        }
        try {
            return gson.fromJson(value, ColorScheme.class);
        } catch (Exception e) {
            return new ColorScheme();
        }
    }

    @TypeConverter
    public static String toString(ColorScheme colorScheme) {
        return colorScheme != null ? gson.toJson(colorScheme) : gson.toJson(new ColorScheme());
    }
}
