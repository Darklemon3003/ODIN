package com.odin.core.fileanalysis.model;

import java.util.Date;
import java.util.List;

/**
 * Real quarantined file information from actual quarantine operations.
 * No simulated quarantines - only actual quarantined files.
 */
public class QuarantinedFile {
    private String quarantinedFileId;
    private String originalFilePath;
    private String quarantinePath;
    private String fileName;
    private String fileType;
    private long fileSize;
    private String md5Hash;
    private String sha256Hash;
    private Date quarantineTimestamp;
    private ThreatLevel threatLevel;
    private List<Threat> threats;
    private boolean isIsolated;
    private boolean isSecurelyDeleted;
    private String quarantineReason;
    private Date lastAccessed;

    public QuarantinedFile() {
        this.quarantineTimestamp = new Date();
        this.lastAccessed = new Date();
        this.isIsolated = false;
        this.isSecurelyDeleted = false;
    }

    // Getters and Setters
    public String getQuarantinedFileId() { return quarantinedFileId; }
    public void setQuarantinedFileId(String quarantinedFileId) { this.quarantinedFileId = quarantinedFileId; }

    public String getOriginalFilePath() { return originalFilePath; }
    public void setOriginalFilePath(String originalFilePath) { this.originalFilePath = originalFilePath; }

    public String getQuarantinePath() { return quarantinePath; }
    public void setQuarantinePath(String quarantinePath) { this.quarantinePath = quarantinePath; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getMd5Hash() { return md5Hash; }
    public void setMd5Hash(String md5Hash) { this.md5Hash = md5Hash; }

    public String getSha256Hash() { return sha256Hash; }
    public void setSha256Hash(String sha256Hash) { this.sha256Hash = sha256Hash; }

    public Date getQuarantineTimestamp() { return quarantineTimestamp; }
    public void setQuarantineTimestamp(Date quarantineTimestamp) { this.quarantineTimestamp = quarantineTimestamp; }

    public ThreatLevel getThreatLevel() { return threatLevel; }
    public void setThreatLevel(ThreatLevel threatLevel) { this.threatLevel = threatLevel; }

    public List<Threat> getThreats() { return threats; }
    public void setThreats(List<Threat> threats) { this.threats = threats; }

    public boolean isIsolated() { return isIsolated; }
    public void setIsolated(boolean isolated) { isIsolated = isolated; }

    public boolean isSecurelyDeleted() { return isSecurelyDeleted; }
    public void setSecurelyDeleted(boolean securelyDeleted) { isSecurelyDeleted = securelyDeleted; }

    public String getQuarantineReason() { return quarantineReason; }
    public void setQuarantineReason(String quarantineReason) { this.quarantineReason = quarantineReason; }

    public Date getLastAccessed() { return lastAccessed; }
    public void setLastAccessed(Date lastAccessed) { this.lastAccessed = lastAccessed; }
}
