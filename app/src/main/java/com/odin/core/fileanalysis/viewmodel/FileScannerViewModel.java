package com.odin.core.fileanalysis.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.odin.core.fileanalysis.model.ScanResult;
import com.odin.core.fileanalysis.model.ThreatLevel;
import com.odin.core.fileanalysis.repository.FileAnalysisRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Real ViewModel for file scanning operations.
 * No simulated data - only actual scan results.
 */
public class FileScannerViewModel extends ViewModel {

    private static final String TAG = "FileScannerViewModel";
    private final FileAnalysisRepository fileAnalysisRepository;

    private final MutableLiveData<ScanStatus> scanStatus = new MutableLiveData<>(ScanStatus.IDLE);
    private final MutableLiveData<ScanResult> scanResults = new MutableLiveData<>();
    private final MutableLiveData<Integer> scanProgress = new MutableLiveData<>(0);
    private final MutableLiveData<List<ScanResult>> allScanResults = new MutableLiveData<>(new ArrayList<>());

    public FileScannerViewModel(FileAnalysisRepository fileAnalysisRepository) {
        this.fileAnalysisRepository = fileAnalysisRepository;
        Log.d(TAG, "FileScannerViewModel initialized");
    }

    /**
     * Scan a file.
     * No simulation - only actual file scanning.
     */
    public void scanFile(File file) {
        if (file == null || !file.exists()) {
            Log.e(TAG, "Invalid file for scanning");
            scanStatus.setValue(ScanStatus.ERROR);
            return;
        }

        Log.d(TAG, "Starting file scan: " + file.getPath());
        scanStatus.setValue(ScanStatus.SCANNING);
        scanProgress.setValue(0);

        new Thread(() -> {
            try {
                scanProgress.postValue(25);
                
                ScanResult result = fileAnalysisRepository.scanFile(file);
                
                scanProgress.postValue(75);
                
                if (result != null) {
                    scanResults.postValue(result);
                    
                    // Add to all results
                    List<ScanResult> currentResults = allScanResults.getValue();
                    if (currentResults == null) {
                        currentResults = new ArrayList<>();
                    }
                    currentResults.add(result);
                    allScanResults.postValue(currentResults);
                    
                    scanProgress.postValue(100);
                    
                    if (result.isMalicious()) {
                        scanStatus.postValue(ScanStatus.THREAT_DETECTED);
                    } else {
                        scanStatus.postValue(ScanStatus.COMPLETED);
                    }
                    
                    Log.d(TAG, "File scan completed: " + file.getName() + 
                               ", Malicious: " + result.isMalicious());
                } else {
                    scanStatus.postValue(ScanStatus.ERROR);
                    Log.e(TAG, "Scan result is null");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error scanning file", e);
                scanStatus.postValue(ScanStatus.ERROR);
            }
        }).start();
    }

    /**
     * Get scan status.
     */
    public LiveData<ScanStatus> getScanStatus() {
        return scanStatus;
    }

    /**
     * Get scan results.
     */
    public LiveData<ScanResult> getScanResults() {
        return scanResults;
    }

    /**
     * Get scan progress.
     */
    public LiveData<Integer> getScanProgress() {
        return scanProgress;
    }

    /**
     * Get all scan results.
     */
    public LiveData<List<ScanResult>> getAllScanResults() {
        return allScanResults;
    }

    /**
     * Clear scan results.
     */
    public void clearResults() {
        scanResults.setValue(null);
        scanStatus.setValue(ScanStatus.IDLE);
        scanProgress.setValue(0);
        Log.d(TAG, "Scan results cleared");
    }

    /**
     * Reset scan status.
     */
    public void resetStatus() {
        scanStatus.setValue(ScanStatus.IDLE);
        scanProgress.setValue(0);
    }

    /**
     * Scan status enumeration.
     */
    public enum ScanStatus {
        IDLE,
        SCANNING,
        COMPLETED,
        THREAT_DETECTED,
        ERROR
    }
}
