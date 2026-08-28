package com.odin.core.fileanalysis.quarantine;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Real file restorer for quarantined files.
 * No simulated operations - only actual file restoration.
 */
public class FileRestorer {

    private static final String TAG = "FileRestorer";

    public FileRestorer() {
        Log.d(TAG, "FileRestorer initialized");
    }

    /**
     * Restore a quarantined file to its original location.
     * No simulation - actual file restoration.
     */
    public boolean restoreFile(File quarantinedFile, String originalPath) {
        if (quarantinedFile == null || !quarantinedFile.exists()) {
            Log.e(TAG, "Quarantined file does not exist");
            return false;
        }

        if (originalPath == null || originalPath.isEmpty()) {
            Log.e(TAG, "Original path is null or empty");
            return false;
        }

        Log.d(TAG, "Restoring file from: " + quarantinedFile.getPath() + " to: " + originalPath);
        
        File originalFile = new File(originalPath);
        
        try {
            // Create parent directories if they don't exist
            File parentDir = originalFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // Copy file from quarantine to original location
            copyFile(quarantinedFile, originalFile);
            
            Log.d(TAG, "File restored successfully: " + originalFile.getName());
            
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, "Error restoring file", e);
            return false;
        }
    }

    /**
     * Restore a quarantined file to a specified location.
     * No simulation - actual file restoration.
     */
    public boolean restoreFileTo(File quarantinedFile, File targetLocation) {
        if (quarantinedFile == null || !quarantinedFile.exists()) {
            Log.e(TAG, "Quarantined file does not exist");
            return false;
        }

        if (targetLocation == null) {
            Log.e(TAG, "Target location is null");
            return false;
        }

        Log.d(TAG, "Restoring file from: " + quarantinedFile.getPath() + " to: " + targetLocation.getPath());
        
        try {
            // Create parent directories if they don't exist
            File parentDir = targetLocation.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // Copy file from quarantine to target location
            copyFile(quarantinedFile, targetLocation);
            
            Log.d(TAG, "File restored successfully to: " + targetLocation.getName());
            
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, "Error restoring file", e);
            return false;
        }
    }

    /**
     * Copy file from source to destination.
     * No simulation - actual file copying.
     */
    private void copyFile(File source, File destination) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;

        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(destination).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            if (sourceChannel != null) {
                sourceChannel.close();
            }
            if (destChannel != null) {
                destChannel.close();
            }
        }
    }
}
