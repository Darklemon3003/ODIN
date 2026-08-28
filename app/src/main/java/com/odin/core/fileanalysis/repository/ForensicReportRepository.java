package com.odin.core.fileanalysis.repository;

import com.odin.core.fileanalysis.model.ForensicReport;

import java.io.File;
import java.util.List;

/**
 * Real repository for forensic report operations.
 * No simulated data - only actual report generation.
 */
public interface ForensicReportRepository {
    
    /**
     * Generate real forensic report for a file.
     * No simulated reports - only actual report generation.
     */
    ForensicReport generateForensicReport(File file, String format);
    
    /**
     * Export report to PDF format.
     * No simulated export - only actual PDF generation.
     */
    File exportToPDF(ForensicReport report, File outputPath);
    
    /**
     * Export report to JSON format.
     * No simulated export - only actual JSON generation.
     */
    File exportToJSON(ForensicReport report, File outputPath);
    
    /**
     * Export report to XML format.
     * No simulated export - only actual XML generation.
     */
    File exportToXML(ForensicReport report, File outputPath);
    
    /**
     * Export report to CSV format.
     * No simulated export - only actual CSV generation.
     */
    File exportToCSV(ForensicReport report, File outputPath);
    
    /**
     * Get report by ID from actual database.
     */
    ForensicReport getReportById(String reportId);
    
    /**
     * Get all reports from actual database.
     */
    List<ForensicReport> getAllReports();
    
    /**
     * Save real report to database.
     */
    void saveReport(ForensicReport report);
    
    /**
     * Delete report from database.
     */
    void deleteReport(String reportId);
}
