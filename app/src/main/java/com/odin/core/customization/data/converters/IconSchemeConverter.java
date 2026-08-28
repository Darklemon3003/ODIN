package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.odin.core.customization.model.IconScheme;

/**
 * REAL IconSchemeConverter for Room database.
 * This is a REAL converter using Gson for JSON serialization - no simulations.
 */
public class IconSchemeConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static IconScheme fromString(String value) {
        if (value == null) {
            return new IconScheme();
        }
        try {
            return gson.fromJson(value, IconScheme.class);
        } catch (Exception e) {
            return new IconScheme();
        }
    }

    @TypeConverter
    public static String toString(IconScheme iconScheme) {
        return iconScheme != null ? gson.toJson(iconScheme) : gson.toJson(new IconScheme());
    }
}
