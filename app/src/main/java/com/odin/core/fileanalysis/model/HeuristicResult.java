package com.odin.core.fileanalysis.model;

import java.util.Map;

/**
 * Real heuristic analysis result from actual heuristic analysis.
 * No simulated results - only actual heuristic analysis outcomes.
 */
public class HeuristicResult {
    private double suspiciousScore;
    private double anomalyScore;
    private boolean isSuspicious;
    private Map<String, Double> featureScores;
    private String analysisDetails;

    public HeuristicResult() {}

    // Getters and Setters
    public double getSuspiciousScore() { return suspiciousScore; }
    public void setSuspiciousScore(double suspiciousScore) { this.suspiciousScore = suspiciousScore; }

    public double getAnomalyScore() { return anomalyScore; }
    public void setAnomalyScore(double anomalyScore) { this.anomalyScore = anomalyScore; }

    public boolean isSuspicious() { return isSuspicious; }
    public void setSuspicious(boolean suspicious) { isSuspicious = suspicious; }

    public Map<String, Double> getFeatureScores() { return featureScores; }
    public void setFeatureScores(Map<String, Double> featureScores) { this.featureScores = featureScores; }

    public String getAnalysisDetails() { return analysisDetails; }
    public void setAnalysisDetails(String analysisDetails) { this.analysisDetails = analysisDetails; }
}
