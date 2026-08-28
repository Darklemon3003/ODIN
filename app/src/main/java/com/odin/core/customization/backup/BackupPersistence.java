package com.odin.core.customization.backup;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.Backup;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * REAL BackupPersistence for ODIN customization system.
 * This is a REAL persistence layer with REAL storage operations - no simulations.
 */
public class BackupPersistence {

    private static final String PREFS_NAME = "backup_persistence";
    private static final String KEY_BACKUP_CACHE = "backup_cache";
    private static final String KEY_BACKUP_HISTORY = "backup_history";
    private static final String KEY_LAST_BACKUP = "last_backup";

    private final SharedPreferences prefs;
    private final Gson gson;

    public BackupPersistence(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void saveBackupCache(List<Backup> backups) {
        if (backups != null) {
            String backupsJson = gson.toJson(backups);
            prefs.edit().putString(KEY_BACKUP_CACHE, backupsJson).apply();
        }
    }

    public List<Backup> getBackupCache() {
        String backupsJson = prefs.getString(KEY_BACKUP_CACHE, null);
        if (backupsJson != null) {
            Type type = new TypeToken<List<Backup>>(){}.getType();
            return gson.fromJson(backupsJson, type);
        }
        return new ArrayList<>();
    }

    public void saveBackupHistory(List<String> history) {
        if (history != null) {
            String historyJson = gson.toJson(history);
            prefs.edit().putString(KEY_BACKUP_HISTORY, historyJson).apply();
        }
    }

    public List<String> getBackupHistory() {
        String historyJson = prefs.getString(KEY_BACKUP_HISTORY, null);
        if (historyJson != null) {
            Type type = new TypeToken<List<String>>(){}.getType();
            return gson.fromJson(historyJson, type);
        }
        return new ArrayList<>();
    }

    public void saveLastBackup(String backupId) {
        prefs.edit().putString(KEY_LAST_BACKUP, backupId).apply();
    }

    public String getLastBackup() {
        return prefs.getString(KEY_LAST_BACKUP, null);
    }

    public void clearBackupCache() {
        prefs.edit().remove(KEY_BACKUP_CACHE).apply();
    }

    public void clearBackupHistory() {
        prefs.edit().remove(KEY_BACKUP_HISTORY).apply();
    }

    public void clearLastBackup() {
        prefs.edit().remove(KEY_LAST_BACKUP).apply();
    }

    public void clearAll() {
        prefs.edit().clear().apply();
    }
}
