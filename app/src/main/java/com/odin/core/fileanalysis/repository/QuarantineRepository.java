package com.odin.core.fileanalysis.repository;

import com.odin.core.fileanalysis.model.QuarantinedFile;

import java.io.File;
import java.util.List;

/**
 * Real repository for quarantine operations.
 * No simulated data - only actual quarantine operations.
 */
public interface QuarantineRepository {
    
    /**
     * Quarantine a real file.
     * No simulated quarantine - only actual file quarantine.
     */
    QuarantinedFile quarantineFile(File file, String reason);
    
    /**
     * Restore a quarantined file.
     * No simulated restoration - only actual file restoration.
     */
    boolean restoreFile(String quarantinedFileId);
    
    /**
     * Securely delete a quarantined file.
     * No simulated deletion - only actual secure deletion.
     */
    boolean securelyDeleteFile(String quarantinedFileId);
    
    /**
     * Isolate a quarantined file.
     * No simulated isolation - only actual file isolation.
     */
    boolean isolateFile(String quarantinedFileId);
    
    /**
     * Get quarantined file by ID from actual database.
     */
    QuarantinedFile getQuarantinedFileById(String quarantinedFileId);
    
    /**
     * Get all quarantined files from actual database.
     */
    List<QuarantinedFile> getAllQuarantinedFiles();
    
    /**
     * Get quarantined files by threat level from actual database.
     */
    List<QuarantinedFile> getQuarantinedFilesByThreatLevel(String threatLevel);
    
    /**
     * Save quarantined file info to database.
     */
    void saveQuarantinedFile(QuarantinedFile quarantinedFile);
    
    /**
     * Delete quarantined file record from database.
     */
    void deleteQuarantinedFile(String quarantinedFileId);
    
    /**
     * Check if a file is quarantined.
     */
    boolean isFileQuarantined(String filePath);
}
