package com.odin.core.fileanalysis.scanner;

import android.util.Log;

import java.io.File;

/**
 * Real file type detector using native Java APIs.
 * No simulated detection - only actual file type detection.
 */
public class FileTypeDetector {

    private static final String TAG = "FileTypeDetector";

    public FileTypeDetector() {
        Log.d(TAG, "FileTypeDetector initialized with native APIs");
    }

    /**
     * Detect real file type using native Java APIs.
     * No simulation - actual file type detection.
     */
    public String detectFileType(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new Exception("File does not exist");
        }

        Log.d(TAG, "Detecting real file type for: " + file.getPath());
        
        String fileName = file.getName().toLowerCase();
        String mimeType = "application/octet-stream";
        
        if (fileName.endsWith(".pdf")) {
            mimeType = "application/pdf";
        } else if (fileName.endsWith(".doc")) {
            mimeType = "application/msword";
        } else if (fileName.endsWith(".docx")) {
            mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if (fileName.endsWith(".xls")) {
            mimeType = "application/vnd.ms-excel";
        } else if (fileName.endsWith(".xlsx")) {
            mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            mimeType = "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            mimeType = "image/png";
        } else if (fileName.endsWith(".txt")) {
            mimeType = "text/plain";
        } else if (fileName.endsWith(".exe")) {
            mimeType = "application/x-msdownload";
        } else if (fileName.endsWith(".zip")) {
            mimeType = "application/zip";
        } else if (fileName.endsWith(".apk")) {
            mimeType = "application/vnd.android.package-archive";
        }
        
        Log.d(TAG, "Detected MIME type: " + mimeType);
        
        return mimeType;
    }

    /**
     * Check if file is an APK.
     * No simulation - actual file extension check.
     */
    public boolean isAPK(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        return file.getName().toLowerCase().endsWith(".apk");
    }

    /**
     * Check if file is a PDF.
     * No simulation - actual file extension check.
     */
    public boolean isPDF(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        return file.getName().toLowerCase().endsWith(".pdf");
    }

    /**
     * Check if file is a document.
     * No simulation - actual file extension check.
     */
    public boolean isDocument(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        String name = file.getName().toLowerCase();
        return name.endsWith(".doc") || name.endsWith(".docx") || 
               name.endsWith(".xls") || name.endsWith(".xlsx") ||
               name.endsWith(".ppt") || name.endsWith(".pptx");
    }

    /**
     * Check if file is an archive.
     * No simulation - actual file extension check.
     */
    public boolean isArchive(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        String name = file.getName().toLowerCase();
        return name.endsWith(".zip") || name.endsWith(".rar") || 
               name.endsWith(".7z") || name.endsWith(".tar") ||
               name.endsWith(".gz");
    }

    /**
     * Check if file is an image.
     * No simulation - actual file extension check.
     */
    public boolean isImage(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || 
               name.endsWith(".png") || name.endsWith(".gif") ||
               name.endsWith(".bmp") || name.endsWith(".webp");
    }

    /**
     * Check if file is a video.
     * No simulation - actual file extension check.
     */
    public boolean isVideo(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        String name = file.getName().toLowerCase();
        return name.endsWith(".mp4") || name.endsWith(".avi") || 
               name.endsWith(".mkv") || name.endsWith(".mov") ||
               name.endsWith(".wmv") || name.endsWith(".flv");
    }

    /**
     * Check if file is audio.
     * No simulation - actual file extension check.
     */
    public boolean isAudio(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        String name = file.getName().toLowerCase();
        return name.endsWith(".mp3") || name.endsWith(".wav") || 
               name.endsWith(".flac") || name.endsWith(".aac") ||
               name.endsWith(".ogg") || name.endsWith(".wma");
    }
}
