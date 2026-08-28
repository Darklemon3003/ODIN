package com.odin.core.fileanalysis.quarantine;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * Real secure deleter for quarantined files.
 * No simulated operations - only actual secure deletion.
 */
public class SecureDeleter {

    private static final String TAG = "SecureDeleter";
    private final SecureRandom secureRandom;

    public SecureDeleter() {
        this.secureRandom = new SecureRandom();
        Log.d(TAG, "SecureDeleter initialized");
    }

    /**
     * Securely delete a file by overwriting with random data.
     * No simulation - actual secure deletion.
     */
    public boolean securelyDelete(File file) {
        if (file == null || !file.exists()) {
            Log.e(TAG, "File does not exist for secure deletion");
            return false;
        }

        Log.d(TAG, "Securely deleting file: " + file.getPath());
        
        try {
            // Overwrite file with random data (3 passes)
            for (int pass = 0; pass < 3; pass++) {
                overwriteFile(file);
            }
            
            // Delete the file
            boolean deleted = file.delete();
            
            if (deleted) {
                Log.d(TAG, "File securely deleted: " + file.getName());
            } else {
                Log.w(TAG, "Failed to delete file after overwriting: " + file.getName());
            }
            
            return deleted;
            
        } catch (Exception e) {
            Log.e(TAG, "Error securely deleting file", e);
            return false;
        }
    }

    /**
     * Overwrite file with random data.
     * No simulation - actual file overwriting.
     */
    private void overwriteFile(File file) throws IOException {
        long fileSize = file.length();
        byte[] randomData = new byte[8192];
        
        FileOutputStream fos = null;
        
        try {
            fos = new FileOutputStream(file);
            long bytesWritten = 0;
            
            while (bytesWritten < fileSize) {
                secureRandom.nextBytes(randomData);
                int bytesToWrite = (int) Math.min(randomData.length, fileSize - bytesWritten);
                fos.write(randomData, 0, bytesToWrite);
                bytesWritten += bytesToWrite;
            }
            
            fos.flush();
            
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error closing file output stream", e);
                }
            }
        }
    }

    /**
     * Quick delete (not secure) for testing purposes.
     * No simulation - actual file deletion.
     */
    public boolean quickDelete(File file) {
        if (file == null || !file.exists()) {
            Log.e(TAG, "File does not exist for quick deletion");
            return false;
        }

        Log.d(TAG, "Quick deleting file: " + file.getPath());
        
        boolean deleted = file.delete();
        
        if (deleted) {
            Log.d(TAG, "File quick deleted: " + file.getName());
        } else {
            Log.w(TAG, "Failed to quick delete file: " + file.getName());
        }
        
        return deleted;
    }
}
