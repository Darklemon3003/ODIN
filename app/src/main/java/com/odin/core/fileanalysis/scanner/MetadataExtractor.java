package com.odin.core.fileanalysis.scanner;

import android.util.Log;

import com.odin.core.fileanalysis.model.Metadata;

import java.io.File;
import java.util.Date;

/**
 * Real metadata extractor using native Java APIs.
 * No simulated metadata - only actual metadata extraction from files.
 */
public class MetadataExtractor {

    private static final String TAG = "MetadataExtractor";

    public MetadataExtractor() {
        Log.d(TAG, "MetadataExtractor initialized with native APIs");
    }

    /**
     * Extract real metadata from a file using native Java APIs.
     * No simulation - actual metadata extraction.
     */
    public Metadata extractMetadata(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new Exception("File does not exist");
        }

        Log.d(TAG, "Extracting real metadata from: " + file.getPath());
        
        Metadata metadata = new Metadata();
        metadata.setFileSize(file.length());
        metadata.setFileType(getFileType(file));
        metadata.setModificationDate(new Date(file.lastModified()));
        
        Log.d(TAG, "Metadata extraction completed for: " + file.getName());
        return metadata;
    }

    private String getFileType(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".apk")) return "APK";
        if (fileName.endsWith(".pdf")) return "PDF";
        if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) return "DOC";
        if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) return "XLS";
        if (fileName.endsWith(".zip") || fileName.endsWith(".rar")) return "ARCHIVE";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) return "IMAGE";
        if (fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".mkv")) return "VIDEO";
        if (fileName.endsWith(".mp3") || fileName.endsWith(".wav")) return "AUDIO";
        return "UNKNOWN";
    }
}
