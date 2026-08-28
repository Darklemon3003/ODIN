package com.odin.core.fileanalysis.scanner;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Real hash calculator using Java Security API.
 * No simulated hashes - only actual cryptographic hash calculations.
 */
public class HashCalculator {

    private static final String TAG = "HashCalculator";

    public HashCalculator() {
        Log.d(TAG, "HashCalculator initialized");
    }

    /**
     * Calculate real MD5 hash of a file.
     * No simulation - actual MD5 calculation.
     */
    public String calculateMD5(File file) throws NoSuchAlgorithmException, IOException {
        if (file == null || !file.exists()) {
            throw new IOException("File does not exist");
        }

        Log.d(TAG, "Calculating real MD5 hash for: " + file.getPath());
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        return calculateHash(file, md);
    }

    /**
     * Calculate real SHA-1 hash of a file.
     * No simulation - actual SHA-1 calculation.
     */
    public String calculateSHA1(File file) throws NoSuchAlgorithmException, IOException {
        if (file == null || !file.exists()) {
            throw new IOException("File does not exist");
        }

        Log.d(TAG, "Calculating real SHA-1 hash for: " + file.getPath());
        
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return calculateHash(file, md);
    }

    /**
     * Calculate real SHA-256 hash of a file.
     * No simulation - actual SHA-256 calculation.
     */
    public String calculateSHA256(File file) throws NoSuchAlgorithmException, IOException {
        if (file == null || !file.exists()) {
            throw new IOException("File does not exist");
        }

        Log.d(TAG, "Calculating real SHA-256 hash for: " + file.getPath());
        
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return calculateHash(file, md);
    }

    private String calculateHash(File file, MessageDigest md) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[8192];
        int bytesRead;
        
        try {
            while ((bytesRead = fis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
        } finally {
            fis.close();
        }

        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }

        Log.d(TAG, "Hash calculation completed: " + sb.substring(0, Math.min(16, sb.length())) + "...");
        
        return sb.toString();
    }
}
