package com.odin.core.fileanalysis.model;

import java.util.Date;

/**
 * Real threat representation from actual analysis.
 * No simulated threats - only actual detected threats.
 */
public class Threat {
    private String threatId;
    private String threatName;
    private String threatType;
    private String description;
    private ThreatLevel threatLevel;
    private String signature;
    private Date detectionTimestamp;
    private String sourceComponent;

    public Threat() {
        this.detectionTimestamp = new Date();
    }

    public Threat(String threatId, String threatName, String threatType, 
                  ThreatLevel threatLevel, String signature) {
        this.threatId = threatId;
        this.threatName = threatName;
        this.threatType = threatType;
        this.threatLevel = threatLevel;
        this.signature = signature;
        this.detectionTimestamp = new Date();
    }

    // Getters and Setters
    public String getThreatId() { return threatId; }
    public void setThreatId(String threatId) { this.threatId = threatId; }

    public String getThreatName() { return threatName; }
    public void setThreatName(String threatName) { this.threatName = threatName; }

    public String getThreatType() { return threatType; }
    public void setThreatType(String threatType) { this.threatType = threatType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ThreatLevel getThreatLevel() { return threatLevel; }
    public void setThreatLevel(ThreatLevel threatLevel) { this.threatLevel = threatLevel; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public Date getDetectionTimestamp() { return detectionTimestamp; }
    public void setDetectionTimestamp(Date detectionTimestamp) { this.detectionTimestamp = detectionTimestamp; }

    public String getSourceComponent() { return sourceComponent; }
    public void setSourceComponent(String sourceComponent) { this.sourceComponent = sourceComponent; }
}
