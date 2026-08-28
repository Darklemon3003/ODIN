package com.odin.core.fileanalysis.model;

import java.util.Date;
import java.util.Map;

/**
 * Real metadata extracted from actual files.
 * No simulated metadata - only actual extracted file metadata.
 */
public class Metadata {
    private String author;
    private String title;
    private String subject;
    private String keywords;
    private String creator;
    private String producer;
    private Date creationDate;
    private Date modificationDate;
    private String mimeType;
    private long fileSize;
    private String fileType;
    private Map<String, String> customProperties;

    public Metadata() {}

    // Getters and Setters
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public String getProducer() { return producer; }
    public void setProducer(String producer) { this.producer = producer; }

    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public Date getModificationDate() { return modificationDate; }
    public void setModificationDate(Date modificationDate) { this.modificationDate = modificationDate; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Map<String, String> getCustomProperties() { return customProperties; }
    public void setCustomProperties(Map<String, String> customProperties) { this.customProperties = customProperties; }
}
