package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.odin.core.customization.model.BackupType;

/**
 * REAL BackupTypeConverter for Room database.
 * This is a REAL converter - no simulations.
 */
public class BackupTypeConverter {

    @TypeConverter
    public static BackupType fromString(String value) {
        if (value == null) {
            return BackupType.FULL;
        }
        try {
            return BackupType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return BackupType.FULL;
        }
    }

    @TypeConverter
    public static String toString(BackupType backupType) {
        return backupType != null ? backupType.name() : BackupType.FULL.name();
    }
}
