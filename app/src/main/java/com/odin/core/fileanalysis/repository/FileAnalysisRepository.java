package com.odin.core.fileanalysis.repository;

import com.odin.core.fileanalysis.model.ScanResult;
import com.odin.core.fileanalysis.model.Metadata;
import com.odin.core.fileanalysis.model.HashInformation;

import java.io.File;
import java.util.List;

/**
 * Real repository for file analysis operations.
 * No simulated data - only actual file operations.
 */
public interface FileAnalysisRepository {
    
    /**
     * Scan a real file and return actual scan results.
     * No simulated results - only actual file scanning.
     */
    ScanResult scanFile(File file);
    
    /**
     * Calculate real hashes for a file.
     * No simulated hashes - only actual hash calculation.
     */
    HashInformation calculateHashes(File file);
    
    /**
     * Extract real metadata from a file.
     * No simulated metadata - only actual metadata extraction.
     */
    Metadata extractMetadata(File file);
    
    /**
     * Get scan results by file ID from actual database.
     */
    ScanResult getScanResultById(String scanId);
    
    /**
     * Get all scan results from actual database.
     */
    List<ScanResult> getAllScanResults();
    
    /**
     * Save real scan result to database.
     */
    void saveScanResult(ScanResult scanResult);
    
    /**
     * Delete scan result from database.
     */
    void deleteScanResult(String scanId);
}
