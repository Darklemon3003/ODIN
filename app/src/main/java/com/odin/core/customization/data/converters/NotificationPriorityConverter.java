package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.odin.core.customization.model.NotificationPriority;

/**
 * REAL NotificationPriorityConverter for Room database.
 * This is a REAL converter - no simulations.
 */
public class NotificationPriorityConverter {

    @TypeConverter
    public static NotificationPriority fromString(String value) {
        if (value == null) {
            return NotificationPriority.HIGH;
        }
        try {
            return NotificationPriority.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de NotificationPriority: valor invalido '" + value + "'", e);
        }
    }

    @TypeConverter
    public static String toString(NotificationPriority priority) {
        return priority != null ? priority.name() : NotificationPriority.HIGH.name();
    }
}
