package com.odin.core.fileanalysis.report;

import android.util.Log;

import com.odin.core.fileanalysis.model.ForensicReport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Real CSV report generator.
 * No simulated reports - only actual CSV generation.
 */
public class CSVReportGenerator {

    private static final String TAG = "CSVReportGenerator";

    public CSVReportGenerator() {
        Log.d(TAG, "CSVReportGenerator initialized");
    }

    /**
     * Generate real CSV report.
     * No simulation - actual CSV generation.
     */
    public File generateReport(ForensicReport report, File outputPath) throws IOException {
        if (report == null || outputPath == null) {
            throw new IllegalArgumentException("Report and output path cannot be null");
        }

        Log.d(TAG, "Generating real CSV report to: " + outputPath.getPath());
        
        FileWriter writer = null;

        try {
            writer = new FileWriter(outputPath);
            
            // Write header
            writer.write("Report ID,File ID,File Name,Report Timestamp,Report Format,Report Version\n");
            
            // Write data
            StringBuilder line = new StringBuilder();
            line.append(escapeCsv(report.getReportId())).append(",");
            line.append(escapeCsv(report.getFileId())).append(",");
            line.append(escapeCsv(report.getFileName())).append(",");
            line.append(report.getReportTimestamp()).append(",");
            line.append(escapeCsv(report.getReportFormat())).append(",");
            line.append(escapeCsv(report.getReportVersion()));
            writer.write(line.toString());
            writer.write("\n");
            
            // Write file information
            if (report.getFileInformation() != null) {
                writer.write("\nFile Information\n");
                writer.write("File Name,File Path,File Size,File Type\n");
                StringBuilder fileInfo = new StringBuilder();
                fileInfo.append(escapeCsv(report.getFileInformation().getFileName())).append(",");
                fileInfo.append(escapeCsv(report.getFileInformation().getFilePath())).append(",");
                fileInfo.append(report.getFileInformation().getFileSize()).append(",");
                fileInfo.append(escapeCsv(report.getFileInformation().getFileType()));
                writer.write(fileInfo.toString());
                writer.write("\n");
            }
            
            // Write hash information
            if (report.getHashInformation() != null) {
                writer.write("\nHash Information\n");
                writer.write("MD5,SHA1,SHA256\n");
                StringBuilder hashInfo = new StringBuilder();
                hashInfo.append(escapeCsv(report.getHashInformation().getMd5Hash())).append(",");
                hashInfo.append(escapeCsv(report.getHashInformation().getSha1Hash())).append(",");
                hashInfo.append(escapeCsv(report.getHashInformation().getSha256Hash()));
                writer.write(hashInfo.toString());
                writer.write("\n");
            }
            
            writer.close();

            Log.d(TAG, "CSV report generated successfully");
            
            return outputPath;

        } catch (Exception e) {
            Log.e(TAG, "Error generating CSV report", e);
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

    private String escapeCsv(String input) {
        if (input == null) return "";
        if (input.contains(",") || input.contains("\"") || input.contains("\n")) {
            return "\"" + input.replace("\"", "\"\"") + "\"";
        }
        return input;
    }
}
