package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.odin.core.customization.model.RulePriority;

/**
 * REAL RulePriorityConverter for Room database.
 * This is a REAL converter - no simulations.
 */
public class RulePriorityConverter {

    @TypeConverter
    public static RulePriority fromString(String value) {
        if (value == null) {
            return RulePriority.MEDIUM;
        }
        try {
            return RulePriority.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de RulePriority: valor invalido '" + value + "'", e);
        }
    }

    @TypeConverter
    public static String toString(RulePriority priority) {
        return priority != null ? priority.name() : RulePriority.MEDIUM.name();
    }
}
