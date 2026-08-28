package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.odin.core.customization.model.FontScheme;

/**
 * REAL FontSchemeConverter for Room database.
 * This is a REAL converter using Gson for JSON serialization - no simulations.
 */
public class FontSchemeConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static FontScheme fromString(String value) {
        if (value == null) {
            return new FontScheme();
        }
        try {
            return gson.fromJson(value, FontScheme.class);
        } catch (Exception e) {
            return new FontScheme();
        }
    }

    @TypeConverter
    public static String toString(FontScheme fontScheme) {
        return fontScheme != null ? gson.toJson(fontScheme) : gson.toJson(new FontScheme());
    }
}
