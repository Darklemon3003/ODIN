package com.odin.core.fileanalysis.model;

import java.util.Date;

/**
 * Real hash information from actual hash calculation.
 * No simulated hashes - only actual calculated hashes.
 */
public class HashInformation {
    private String md5Hash;
    private String sha1Hash;
    private String sha256Hash;
    private Date calculationTimestamp;
    private long calculationTimeMs;

    public HashInformation() {
        this.calculationTimestamp = new Date();
    }

    // Getters and Setters
    public String getMd5Hash() { return md5Hash; }
    public void setMd5Hash(String md5Hash) { this.md5Hash = md5Hash; }

    public String getSha1Hash() { return sha1Hash; }
    public void setSha1Hash(String sha1Hash) { this.sha1Hash = sha1Hash; }

    public String getSha256Hash() { return sha256Hash; }
    public void setSha256Hash(String sha256Hash) { this.sha256Hash = sha256Hash; }

    public Date getCalculationTimestamp() { return calculationTimestamp; }
    public void setCalculationTimestamp(Date calculationTimestamp) { this.calculationTimestamp = calculationTimestamp; }

    public long getCalculationTimeMs() { return calculationTimeMs; }
    public void setCalculationTimeMs(long calculationTimeMs) { this.calculationTimeMs = calculationTimeMs; }
}
