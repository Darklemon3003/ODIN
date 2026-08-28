package com.odin.core.fileanalysis.report;

import android.util.Log;

import com.odin.core.fileanalysis.model.ForensicReport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Real XML report generator.
 * No simulated reports - only actual XML generation.
 */
public class XMLReportGenerator {

    private static final String TAG = "XMLReportGenerator";

    public XMLReportGenerator() {
        Log.d(TAG, "XMLReportGenerator initialized");
    }

    /**
     * Generate real XML report.
     * No simulation - actual XML generation.
     */
    public File generateReport(ForensicReport report, File outputPath) throws IOException {
        if (report == null || outputPath == null) {
            throw new IllegalArgumentException("Report and output path cannot be null");
        }

        Log.d(TAG, "Generating real XML report to: " + outputPath.getPath());
        
        FileWriter writer = null;

        try {
            writer = new FileWriter(outputPath);
            
            // Build XML document
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xml.append("<ForensicReport>\n");
            xml.append("  <ReportId>").append(escapeXml(report.getReportId())).append("</ReportId>\n");
            xml.append("  <FileId>").append(escapeXml(report.getFileId())).append("</FileId>\n");
            xml.append("  <FileName>").append(escapeXml(report.getFileName())).append("</FileName>\n");
            xml.append("  <ReportTimestamp>").append(report.getReportTimestamp()).append("</ReportTimestamp>\n");
            xml.append("  <ReportFormat>").append(escapeXml(report.getReportFormat())).append("</ReportFormat>\n");
            xml.append("  <ReportVersion>").append(escapeXml(report.getReportVersion())).append("</ReportVersion>\n");
            
            // File Information
            if (report.getFileInformation() != null) {
                xml.append("  <FileInformation>\n");
                xml.append("    <FileName>").append(escapeXml(report.getFileInformation().getFileName())).append("</FileName>\n");
                xml.append("    <FilePath>").append(escapeXml(report.getFileInformation().getFilePath())).append("</FilePath>\n");
                xml.append("    <FileSize>").append(report.getFileInformation().getFileSize()).append("</FileSize>\n");
                xml.append("    <FileType>").append(escapeXml(report.getFileInformation().getFileType())).append("</FileType>\n");
                xml.append("  </FileInformation>\n");
            }
            
            // Hash Information
            if (report.getHashInformation() != null) {
                xml.append("  <HashInformation>\n");
                xml.append("    <MD5>").append(escapeXml(report.getHashInformation().getMd5Hash())).append("</MD5>\n");
                xml.append("    <SHA1>").append(escapeXml(report.getHashInformation().getSha1Hash())).append("</SHA1>\n");
                xml.append("    <SHA256>").append(escapeXml(report.getHashInformation().getSha256Hash())).append("</SHA256>\n");
                xml.append("  </HashInformation>\n");
            }
            
            xml.append("</ForensicReport>\n");
            
            writer.write(xml.toString());
            writer.close();

            Log.d(TAG, "XML report generated successfully");
            
            return outputPath;

        } catch (Exception e) {
            Log.e(TAG, "Error generating XML report", e);
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ex) {
                    Log.e(TAG, "Error closing writer", ex);
                }
            }
            throw e;
        }
    }

    private String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
}
