package com.odin.core.fileanalysis.quarantine;

import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Real file isolator for quarantined files.
 * No simulated operations - only actual file isolation.
 */
public class FileIsolator {

    private static final String TAG = "FileIsolator";

    public FileIsolator() {
        Log.d(TAG, "FileIsolator initialized");
    }

    /**
     * Isolate a file by setting read-only permissions.
     * No simulation - actual file permission modification.
     */
    public boolean isolateFile(File file) {
        if (file == null || !file.exists()) {
            Log.e(TAG, "File does not exist for isolation");
            return false;
        }

        Log.d(TAG, "Isolating file: " + file.getPath());
        
        try {
            // Set file to read-only
            boolean success = file.setReadOnly();
            
            if (success) {
                Log.d(TAG, "File isolated successfully: " + file.getName());
            } else {
                Log.w(TAG, "Failed to isolate file: " + file.getName());
            }
            
            return success;
            
        } catch (Exception e) {
            Log.e(TAG, "Error isolating file", e);
            return false;
        }
    }

    /**
     * Check if a file is isolated (read-only).
     * No simulation - actual permission check.
     */
    public boolean isFileIsolated(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        
        // Check if file is not writable
        return !file.canWrite();
    }

    /**
     * Remove isolation from a file.
     * No simulation - actual permission modification.
     */
    public boolean removeIsolation(File file) {
        if (file == null || !file.exists()) {
            Log.e(TAG, "File does not exist for isolation removal");
            return false;
        }

        Log.d(TAG, "Removing isolation from file: " + file.getPath());
        
        try {
            // Set file to writable
            boolean success = file.setWritable(true);
            
            if (success) {
                Log.d(TAG, "Isolation removed successfully: " + file.getName());
            } else {
                Log.w(TAG, "Failed to remove isolation: " + file.getName());
            }
            
            return success;
            
        } catch (Exception e) {
            Log.e(TAG, "Error removing isolation", e);
            return false;
        }
    }
}
