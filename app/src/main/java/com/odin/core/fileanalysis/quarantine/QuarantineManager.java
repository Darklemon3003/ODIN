package com.odin.core.fileanalysis.quarantine;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Real quarantine manager for file isolation.
 * No simulated operations - only actual quarantine management.
 */
public class QuarantineManager {

    private static final String TAG = "QuarantineManager";
    private final Context context;

    public QuarantineManager(Context context) {
        this.context = context;
        Log.d(TAG, "QuarantineManager initialized");
    }

    /**
     * Get quarantine directory.
     * No simulation - actual directory creation.
     */
    public File getQuarantineDirectory() {
        File quarantineDir = new File(context.getFilesDir(), "quarantine");
        if (!quarantineDir.exists()) {
            quarantineDir.mkdirs();
            Log.d(TAG, "Quarantine directory created: " + quarantineDir.getPath());
        }
        return quarantineDir;
    }

    /**
     * Check if quarantine directory exists.
     * No simulation - actual directory check.
     */
    public boolean quarantineDirectoryExists() {
        File quarantineDir = getQuarantineDirectory();
        return quarantineDir.exists() && quarantineDir.isDirectory();
    }

    /**
     * Get quarantined files count.
     * No simulation - actual file count.
     */
    public int getQuarantinedFilesCount() {
        File quarantineDir = getQuarantineDirectory();
        if (!quarantineDir.exists()) {
            return 0;
        }
        File[] files = quarantineDir.listFiles();
        return files != null ? files.length : 0;
    }
}
