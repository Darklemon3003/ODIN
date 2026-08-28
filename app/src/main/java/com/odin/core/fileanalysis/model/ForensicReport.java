package com.odin.core.fileanalysis.model;

import java.util.Date;

/**
 * Real forensic report from actual file analysis.
 * No simulated reports - only actual forensic reports.
 */
public class ForensicReport {
    private String reportId;
    private String fileId;
    private String fileName;
    private Date reportTimestamp;
    private FileInformation fileInformation;
    private HashInformation hashInformation;
    private Metadata metadata;
    private AnalysisResults analysisResults;
    private ThreatInformation threatInformation;
    private Evidence evidence;
    private Recommendations recommendations;
    private Timeline timeline;
    private String reportFormat;
    private String reportVersion;

    public ForensicReport() {
        this.reportTimestamp = new Date();
        this.reportVersion = "1.0";
    }

    // Getters and Setters
    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Date getReportTimestamp() { return reportTimestamp; }
    public void setReportTimestamp(Date reportTimestamp) { this.reportTimestamp = reportTimestamp; }

    public FileInformation getFileInformation() { return fileInformation; }
    public void setFileInformation(FileInformation fileInformation) { this.fileInformation = fileInformation; }

    public HashInformation getHashInformation() { return hashInformation; }
    public void setHashInformation(HashInformation hashInformation) { this.hashInformation = hashInformation; }

    public Metadata getMetadata() { return metadata; }
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }

    public AnalysisResults getAnalysisResults() { return analysisResults; }
    public void setAnalysisResults(AnalysisResults analysisResults) { this.analysisResults = analysisResults; }

    public ThreatInformation getThreatInformation() { return threatInformation; }
    public void setThreatInformation(ThreatInformation threatInformation) { this.threatInformation = threatInformation; }

    public Evidence getEvidence() { return evidence; }
    public void setEvidence(Evidence evidence) { this.evidence = evidence; }

    public Recommendations getRecommendations() { return recommendations; }
    public void setRecommendations(Recommendations recommendations) { this.recommendations = recommendations; }

    public Timeline getTimeline() { return timeline; }
    public void setTimeline(Timeline timeline) { this.timeline = timeline; }

    public String getReportFormat() { return reportFormat; }
    public void setReportFormat(String reportFormat) { this.reportFormat = reportFormat; }

    public String getReportVersion() { return reportVersion; }
    public void setReportVersion(String reportVersion) { this.reportVersion = reportVersion; }
}
