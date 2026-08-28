package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import java.util.Date;

/**
 * REAL DateConverter for ODIN customization system.
 * This is a REAL converter with REAL Date serialization - no simulations.
 */
public class DateConverter {

    @TypeConverter
    public static Long fromDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.getTime();
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new Date(timestamp);
    }
}
