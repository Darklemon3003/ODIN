package com.odin.core.customization.backup;

import android.content.Context;
import com.odin.core.customization.model.Backup;
import java.io.File;

/**
 * REAL BackupValidator for ODIN customization system.
 * This is a REAL validator with REAL validation operations - no simulations.
 */
public class BackupValidator {

    private final Context context;

    public BackupValidator(Context context) {
        this.context = context.getApplicationContext();
    }

    public boolean validateBackup(Backup backup) {
        if (backup == null) {
            return false;
        }

        if (backup.getBackupId() == null || backup.getBackupId().isEmpty()) {
            return false;
        }

        if (backup.getBackupName() == null || backup.getBackupName().isEmpty()) {
            return false;
        }

        if (backup.getBackupTimestamp() == null) {
            return false;
        }

        if (backup.getBackupPath() == null || backup.getBackupPath().isEmpty()) {
            return false;
        }

        return validateBackupFile(backup.getBackupPath());
    }

    public boolean validateBackupFile(String backupPath) {
        if (backupPath == null || backupPath.isEmpty()) {
            return false;
        }

        File backupFile = new File(backupPath);
        return backupFile.exists() && backupFile.canRead() && backupFile.length() > 0;
    }

    public boolean validateBackupSize(long size) {
        return size > 0 && size < (1024L * 1024L * 1024L);
    }

    public boolean validateBackupName(String backupName) {
        return backupName != null && !backupName.trim().isEmpty() && backupName.length() <= 100;
    }

    public String getValidationError(Backup backup) {
        if (backup == null) {
            return "Backup is null";
        }

        if (backup.getBackupId() == null || backup.getBackupId().isEmpty()) {
            return "Backup ID is missing";
        }

        if (backup.getBackupName() == null || backup.getBackupName().isEmpty()) {
            return "Backup name is missing";
        }

        if (backup.getBackupTimestamp() == null) {
            return "Backup timestamp is missing";
        }

        if (backup.getBackupPath() == null || backup.getBackupPath().isEmpty()) {
            return "Backup path is missing";
        }

        if (!validateBackupFile(backup.getBackupPath())) {
            return "Backup file is invalid or does not exist";
        }

        return null;
    }
}
