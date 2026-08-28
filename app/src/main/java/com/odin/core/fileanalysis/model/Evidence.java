package com.odin.core.fileanalysis.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Real evidence collected from actual file analysis.
 * No simulated evidence - only actual collected evidence.
 */
public class Evidence {
    private String evidenceId;
    private List<String> extractedStrings;
    private Map<String, String> networkIndicators;
    private Map<String, String> fileIndicators;
    private List<String> suspiciousUrls;
    private List<String> suspiciousDomains;
    private List<String> suspiciousIPs;
    private Date collectionTimestamp;

    public Evidence() {
        this.collectionTimestamp = new Date();
    }

    // Getters and Setters
    public String getEvidenceId() { return evidenceId; }
    public void setEvidenceId(String evidenceId) { this.evidenceId = evidenceId; }

    public List<String> getExtractedStrings() { return extractedStrings; }
    public void setExtractedStrings(List<String> extractedStrings) { this.extractedStrings = extractedStrings; }

    public Map<String, String> getNetworkIndicators() { return networkIndicators; }
    public void setNetworkIndicators(Map<String, String> networkIndicators) { this.networkIndicators = networkIndicators; }

    public Map<String, String> getFileIndicators() { return fileIndicators; }
    public void setFileIndicators(Map<String, String> fileIndicators) { this.fileIndicators = fileIndicators; }

    public List<String> getSuspiciousUrls() { return suspiciousUrls; }
    public void setSuspiciousUrls(List<String> suspiciousUrls) { this.suspiciousUrls = suspiciousUrls; }

    public List<String> getSuspiciousDomains() { return suspiciousDomains; }
    public void setSuspiciousDomains(List<String> suspiciousDomains) { this.suspiciousDomains = suspiciousDomains; }

    public List<String> getSuspiciousIPs() { return suspiciousIPs; }
    public void setSuspiciousIPs(List<String> suspiciousIPs) { this.suspiciousIPs = suspiciousIPs; }

    public Date getCollectionTimestamp() { return collectionTimestamp; }
    public void setCollectionTimestamp(Date collectionTimestamp) { this.collectionTimestamp = collectionTimestamp; }
}
