package com.odin.core.fileanalysis.model;

import java.util.Date;

/**
 * Real file information from actual file analysis.
 * No simulated information - only actual file data.
 */
public class FileInformation {
    private String fileName;
    private String filePath;
    private String fileType;
    private long fileSize;
    private Date creationDate;
    private Date modificationDate;
    private Date lastAccessDate;
    private String filePermissions;
    private String fileOwner;

    public FileInformation() {}

    // Getters and Setters
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public Date getModificationDate() { return modificationDate; }
    public void setModificationDate(Date modificationDate) { this.modificationDate = modificationDate; }

    public Date getLastAccessDate() { return lastAccessDate; }
    public void setLastAccessDate(Date lastAccessDate) { this.lastAccessDate = lastAccessDate; }

    public String getFilePermissions() { return filePermissions; }
    public void setFilePermissions(String filePermissions) { this.filePermissions = filePermissions; }

    public String getFileOwner() { return fileOwner; }
    public void setFileOwner(String fileOwner) { this.fileOwner = fileOwner; }
}
