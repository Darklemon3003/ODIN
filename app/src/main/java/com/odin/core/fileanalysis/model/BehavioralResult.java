package com.odin.core.fileanalysis.model;

import java.util.List;
import java.util.Map;

/**
 * Real behavioral analysis result from actual behavioral analysis.
 * No simulated results - only actual behavioral analysis outcomes.
 */
public class BehavioralResult {
    private List<String> suspiciousBehaviors;
    private Map<String, Integer> behaviorFrequency;
    private boolean hasMaliciousBehavior;
    private double riskScore;
    private String analysisDetails;

    public BehavioralResult() {}

    // Getters and Setters
    public List<String> getSuspiciousBehaviors() { return suspiciousBehaviors; }
    public void setSuspiciousBehaviors(List<String> suspiciousBehaviors) { this.suspiciousBehaviors = suspiciousBehaviors; }

    public Map<String, Integer> getBehaviorFrequency() { return behaviorFrequency; }
    public void setBehaviorFrequency(Map<String, Integer> behaviorFrequency) { this.behaviorFrequency = behaviorFrequency; }

    public boolean isHasMaliciousBehavior() { return hasMaliciousBehavior; }
    public void setHasMaliciousBehavior(boolean hasMaliciousBehavior) { this.hasMaliciousBehavior = hasMaliciousBehavior; }

    public double getRiskScore() { return riskScore; }
    public void setRiskScore(double riskScore) { this.riskScore = riskScore; }

    public String getAnalysisDetails() { return analysisDetails; }
    public void setAnalysisDetails(String analysisDetails) { this.analysisDetails = analysisDetails; }
}
