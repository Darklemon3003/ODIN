package com.odin.core.fileanalysis.model;

import java.util.Date;
import java.util.List;

/**
 * Real analysis result from actual malware analysis.
 * No simulated results - only actual analysis outcomes.
 */
public class AnalysisResult {
    private String analysisId;
    private String fileId;
    private AnalysisType analysisType;
    private Date analysisTimestamp;
    private boolean isMalicious;
    private ThreatLevel threatLevel;
    private List<Threat> threats;
    private HeuristicResult heuristicResult;
    private BehavioralResult behavioralResult;
    private MLResult mlResult;
    private boolean analysisCompleted;
    private String analysisStatus;

    public AnalysisResult() {
        this.analysisTimestamp = new Date();
        this.analysisCompleted = false;
        this.analysisStatus = "pending";
    }

    // Getters and Setters
    public String getAnalysisId() { return analysisId; }
    public void setAnalysisId(String analysisId) { this.analysisId = analysisId; }

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }

    public AnalysisType getAnalysisType() { return analysisType; }
    public void setAnalysisType(AnalysisType analysisType) { this.analysisType = analysisType; }

    public Date getAnalysisTimestamp() { return analysisTimestamp; }
    public void setAnalysisTimestamp(Date analysisTimestamp) { this.analysisTimestamp = analysisTimestamp; }

    public boolean isMalicious() { return isMalicious; }
    public void setMalicious(boolean malicious) { isMalicious = malicious; }

    public ThreatLevel getThreatLevel() { return threatLevel; }
    public void setThreatLevel(ThreatLevel threatLevel) { this.threatLevel = threatLevel; }

    public List<Threat> getThreats() { return threats; }
    public void setThreats(List<Threat> threats) { this.threats = threats; }

    public HeuristicResult getHeuristicResult() { return heuristicResult; }
    public void setHeuristicResult(HeuristicResult heuristicResult) { this.heuristicResult = heuristicResult; }

    public BehavioralResult getBehavioralResult() { return behavioralResult; }
    public void setBehavioralResult(BehavioralResult behavioralResult) { this.behavioralResult = behavioralResult; }

    public MLResult getMlResult() { return mlResult; }
    public void setMlResult(MLResult mlResult) { this.mlResult = mlResult; }

    public boolean isAnalysisCompleted() { return analysisCompleted; }
    public void setAnalysisCompleted(boolean analysisCompleted) { this.analysisCompleted = analysisCompleted; }

    public String getAnalysisStatus() { return analysisStatus; }
    public void setAnalysisStatus(String analysisStatus) { this.analysisStatus = analysisStatus; }
}
