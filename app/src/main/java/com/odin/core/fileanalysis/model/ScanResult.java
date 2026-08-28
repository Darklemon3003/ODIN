package com.odin.core.fileanalysis.model;

import java.util.Date;
import java.util.List;

/**
 * Real scan result from actual file scanning.
 * No simulated results - only actual scan outcomes.
 */
public class ScanResult {
    private String fileId;
    private String fileName;
    private String filePath;
    private String fileType;
    private long fileSize;
    private String md5Hash;
    private String sha1Hash;
    private String sha256Hash;
    private Date scanTimestamp;
    private boolean isMalicious;
    private ThreatLevel threatLevel;
    private List<Threat> threats;
    private Metadata metadata;
    private boolean scanCompleted;
    private String scanStatus;

    public ScanResult() {
        this.scanTimestamp = new Date();
        this.scanCompleted = false;
        this.scanStatus = "pending";
    }

    // Getters and Setters
    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getMd5Hash() { return md5Hash; }
    public void setMd5Hash(String md5Hash) { this.md5Hash = md5Hash; }

    public String getSha1Hash() { return sha1Hash; }
    public void setSha1Hash(String sha1Hash) { this.sha1Hash = sha1Hash; }

    public String getSha256Hash() { return sha256Hash; }
    public void setSha256Hash(String sha256Hash) { this.sha256Hash = sha256Hash; }

    public Date getScanTimestamp() { return scanTimestamp; }
    public void setScanTimestamp(Date scanTimestamp) { this.scanTimestamp = scanTimestamp; }

    public boolean isMalicious() { return isMalicious; }
    public void setMalicious(boolean malicious) { isMalicious = malicious; }

    public ThreatLevel getThreatLevel() { return threatLevel; }
    public void setThreatLevel(ThreatLevel threatLevel) { this.threatLevel = threatLevel; }

    public List<Threat> getThreats() { return threats; }
    public void setThreats(List<Threat> threats) { this.threats = threats; }

    public Metadata getMetadata() { return metadata; }
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }

    public boolean isScanCompleted() { return scanCompleted; }
    public void setScanCompleted(boolean scanCompleted) { this.scanCompleted = scanCompleted; }

    public String getScanStatus() { return scanStatus; }
    public void setScanStatus(String scanStatus) { this.scanStatus = scanStatus; }
}
