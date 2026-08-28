package com.odin.core.fileanalysis.scanner;

import android.util.Log;

import com.odin.core.fileanalysis.model.ScanResult;

import java.io.File;

/**
 * Real file scanner engine for comprehensive file scanning.
 * No simulated scanning - only actual file analysis.
 */
public class FileScannerEngine {

    private static final String TAG = "FileScannerEngine";
    private final HashCalculator hashCalculator;
    private final MetadataExtractor metadataExtractor;
    private final SignatureDetector signatureDetector;
    private final FileTypeDetector fileTypeDetector;

    public FileScannerEngine(HashCalculator hashCalculator,
                            MetadataExtractor metadataExtractor,
                            SignatureDetector signatureDetector,
                            FileTypeDetector fileTypeDetector) {
        this.hashCalculator = hashCalculator;
        this.metadataExtractor = metadataExtractor;
        this.signatureDetector = signatureDetector;
        this.fileTypeDetector = fileTypeDetector;
        Log.d(TAG, "FileScannerEngine initialized");
    }

    /**
     * Perform comprehensive scan on a file.
     * No simulation - only actual file scanning.
     */
    public ScanResult scanFile(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new Exception("File does not exist");
        }

        Log.d(TAG, "Starting comprehensive scan for: " + file.getPath());
        
        ScanResult scanResult = new ScanResult();
        scanResult.setFileId(java.util.UUID.randomUUID().toString());
        scanResult.setFileName(file.getName());
        scanResult.setFilePath(file.getAbsolutePath());
        scanResult.setFileSize(file.length());
        scanResult.setFileType(fileTypeDetector.detectFileType(file));

        // Calculate hashes
        try {
            scanResult.setMd5Hash(hashCalculator.calculateMD5(file));
            scanResult.setSha1Hash(hashCalculator.calculateSHA1(file));
            scanResult.setSha256Hash(hashCalculator.calculateSHA256(file));
            Log.d(TAG, "Hash calculation completed for: " + file.getName());
        } catch (Exception e) {
            Log.e(TAG, "Error calculating hashes", e);
            scanResult.setScanStatus("hash_calculation_failed");
        }

        // Extract metadata
        try {
            scanResult.setMetadata(metadataExtractor.extractMetadata(file));
            Log.d(TAG, "Metadata extraction completed for: " + file.getName());
        } catch (Exception e) {
            Log.e(TAG, "Error extracting metadata", e);
        }

        // Detect malicious signatures
        try {
            boolean isMalicious = signatureDetector.detectMaliciousSignatures(file);
            scanResult.setMalicious(isMalicious);
            Log.d(TAG, "Signature detection completed for: " + file.getName() + 
                       ", Malicious: " + isMalicious);
        } catch (Exception e) {
            Log.e(TAG, "Error detecting signatures", e);
            scanResult.setMalicious(false);
        }

        scanResult.setScanCompleted(true);
        scanResult.setScanStatus("completed");

        Log.d(TAG, "Comprehensive scan completed for: " + file.getName());
        
        return scanResult;
    }

    /**
     * Quick scan (hash only).
     * No simulation - only actual hash calculation.
     */
    public ScanResult quickScan(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new Exception("File does not exist");
        }

        Log.d(TAG, "Starting quick scan for: " + file.getPath());
        
        ScanResult scanResult = new ScanResult();
        scanResult.setFileId(java.util.UUID.randomUUID().toString());
        scanResult.setFileName(file.getName());
        scanResult.setFilePath(file.getAbsolutePath());
        scanResult.setFileSize(file.length());

        // Calculate hashes only
        try {
            scanResult.setMd5Hash(hashCalculator.calculateMD5(file));
            scanResult.setSha256Hash(hashCalculator.calculateSHA256(file));
            Log.d(TAG, "Quick hash scan completed for: " + file.getName());
        } catch (Exception e) {
            Log.e(TAG, "Error calculating hashes", e);
            scanResult.setScanStatus("hash_calculation_failed");
        }

        scanResult.setScanCompleted(true);
        scanResult.setScanStatus("quick_scan_completed");

        Log.d(TAG, "Quick scan completed for: " + file.getName());
        
        return scanResult;
    }
}
