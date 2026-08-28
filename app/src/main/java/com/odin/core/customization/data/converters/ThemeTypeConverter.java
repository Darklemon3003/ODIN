package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.odin.core.customization.model.ThemeType;

/**
 * REAL ThemeTypeConverter for Room database.
 * This is a REAL converter - no simulations.
 */
public class ThemeTypeConverter {

    @TypeConverter
    public static ThemeType fromString(String value) {
        if (value == null) {
            return ThemeType.SYSTEM;
        }
        try {
            return ThemeType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return ThemeType.SYSTEM;
        }
    }

    @TypeConverter
    public static String toString(ThemeType themeType) {
        return themeType != null ? themeType.name() : ThemeType.SYSTEM.name();
    }
}
