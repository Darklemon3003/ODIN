package com.odin.core.fileanalysis.model;

import java.util.List;

/**
 * Real analysis results container.
 * No simulated results - only actual analysis outcomes.
 */
public class AnalysisResults {
    private List<AnalysisResult> staticAnalysisResults;
    private List<AnalysisResult> dynamicAnalysisResults;
    private List<AnalysisResult> heuristicAnalysisResults;
    private List<AnalysisResult> behavioralAnalysisResults;
    private List<AnalysisResult> mlAnalysisResults;

    public AnalysisResults() {}

    // Getters and Setters
    public List<AnalysisResult> getStaticAnalysisResults() { return staticAnalysisResults; }
    public void setStaticAnalysisResults(List<AnalysisResult> staticAnalysisResults) { this.staticAnalysisResults = staticAnalysisResults; }

    public List<AnalysisResult> getDynamicAnalysisResults() { return dynamicAnalysisResults; }
    public void setDynamicAnalysisResults(List<AnalysisResult> dynamicAnalysisResults) { this.dynamicAnalysisResults = dynamicAnalysisResults; }

    public List<AnalysisResult> getHeuristicAnalysisResults() { return heuristicAnalysisResults; }
    public void setHeuristicAnalysisResults(List<AnalysisResult> heuristicAnalysisResults) { this.heuristicAnalysisResults = heuristicAnalysisResults; }

    public List<AnalysisResult> getBehavioralAnalysisResults() { return behavioralAnalysisResults; }
    public void setBehavioralAnalysisResults(List<AnalysisResult> behavioralAnalysisResults) { this.behavioralAnalysisResults = behavioralAnalysisResults; }

    public List<AnalysisResult> getMlAnalysisResults() { return mlAnalysisResults; }
    public void setMlAnalysisResults(List<AnalysisResult> mlAnalysisResults) { this.mlAnalysisResults = mlAnalysisResults; }
}
