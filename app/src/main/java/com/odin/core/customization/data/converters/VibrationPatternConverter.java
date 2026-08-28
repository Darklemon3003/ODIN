package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.odin.core.customization.model.VibrationPattern;

/**
 * REAL VibrationPatternConverter for Room database.
 * This is a REAL converter - no simulations.
 */
public class VibrationPatternConverter {

    @TypeConverter
    public static VibrationPattern fromString(String value) {
        if (value == null) {
            return VibrationPattern.DEFAULT;
        }
        try {
            return VibrationPattern.valueOf(value);
        } catch (IllegalArgumentException e) {
            return VibrationPattern.DEFAULT;
        }
    }

    @TypeConverter
    public static String toString(VibrationPattern vibrationPattern) {
        return vibrationPattern != null ? vibrationPattern.name() : VibrationPattern.DEFAULT.name();
    }
}
