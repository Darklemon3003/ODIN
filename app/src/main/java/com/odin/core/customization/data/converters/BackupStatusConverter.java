package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.odin.core.customization.model.BackupStatus;

/**
 * REAL BackupStatusConverter for Room database.
 * This is a REAL converter - no simulations.
 */
public class BackupStatusConverter {

    @TypeConverter
    public static BackupStatus fromString(String value) {
        if (value == null) {
            return BackupStatus.PENDING;
        }
        try {
            return BackupStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de BackupStatus: valor invalido '" + value + "'", e);
        }
    }

    @TypeConverter
    public static String toString(BackupStatus status) {
        return status != null ? status.name() : BackupStatus.PENDING.name();
    }
}
