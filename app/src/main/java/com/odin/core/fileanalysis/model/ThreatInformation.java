package com.odin.core.fileanalysis.model;

import java.util.List;

/**
 * Real threat information from actual threat detection.
 * No simulated threats - only actual detected threats.
 */
public class ThreatInformation {
    private List<Threat> detectedThreats;
    private ThreatLevel overallThreatLevel;
    private int totalThreatCount;
    private String threatSummary;

    public ThreatInformation() {}

    // Getters and Setters
    public List<Threat> getDetectedThreats() { return detectedThreats; }
    public void setDetectedThreats(List<Threat> detectedThreats) { this.detectedThreats = detectedThreats; }

    public ThreatLevel getOverallThreatLevel() { return overallThreatLevel; }
    public void setOverallThreatLevel(ThreatLevel overallThreatLevel) { this.overallThreatLevel = overallThreatLevel; }

    public int getTotalThreatCount() { return totalThreatCount; }
    public void setTotalThreatCount(int totalThreatCount) { this.totalThreatCount = totalThreatCount; }

    public String getThreatSummary() { return threatSummary; }
    public void setThreatSummary(String threatSummary) { this.threatSummary = threatSummary; }
}
