package com.odin.core.fileanalysis.report;

import android.util.Log;

import com.odin.core.fileanalysis.model.ForensicReport;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Real JSON report generator using native Android JSON.
 * No simulated reports - only actual JSON generation.
 */
public class JSONReportGenerator {

    private static final String TAG = "JSONReportGenerator";

    public JSONReportGenerator() {
        Log.d(TAG, "JSONReportGenerator initialized with native JSON");
    }

    /**
     * Generate real JSON report.
     * No simulation - actual JSON generation.
     */
    public File generateReport(ForensicReport report, File outputPath) throws IOException {
        if (report == null || outputPath == null) {
            throw new IllegalArgumentException("Report and output path cannot be null");
        }

        Log.d(TAG, "Generating real JSON report to: " + outputPath.getPath());
        
        try {
            JSONObject jsonReport = new JSONObject();
            jsonReport.put("reportId", report.getReportId());
            jsonReport.put("fileName", report.getFileName());
            jsonReport.put("reportTimestamp", report.getReportTimestamp());
            jsonReport.put("reportVersion", report.getReportVersion());
            
            String jsonString = jsonReport.toString(2);
            try (FileWriter writer = new FileWriter(outputPath)) {
                writer.write(jsonString);
            }

            Log.d(TAG, "JSON report generated successfully");
            
            return outputPath;

        } catch (Exception e) {
            Log.e(TAG, "Error generating JSON report", e);
            throw new IOException("Failed to generate JSON report", e);
        }
    }
}
