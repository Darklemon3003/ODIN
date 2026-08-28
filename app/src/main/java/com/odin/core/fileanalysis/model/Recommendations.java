package com.odin.core.fileanalysis.model;

import java.util.List;

/**
 * Real recommendations from actual analysis.
 * No simulated recommendations - only actual analysis-based recommendations.
 */
public class Recommendations {
    private List<String> immediateActions;
    private List<String> preventiveMeasures;
    private List<String> monitoringRecommendations;
    private String overallAssessment;

    public Recommendations() {}

    // Getters and Setters
    public List<String> getImmediateActions() { return immediateActions; }
    public void setImmediateActions(List<String> immediateActions) { this.immediateActions = immediateActions; }

    public List<String> getPreventiveMeasures() { return preventiveMeasures; }
    public void setPreventiveMeasures(List<String> preventiveMeasures) { this.preventiveMeasures = preventiveMeasures; }

    public List<String> getMonitoringRecommendations() { return monitoringRecommendations; }
    public void setMonitoringRecommendations(List<String> monitoringRecommendations) { this.monitoringRecommendations = monitoringRecommendations; }

    public String getOverallAssessment() { return overallAssessment; }
    public void setOverallAssessment(String overallAssessment) { this.overallAssessment = overallAssessment; }
}
