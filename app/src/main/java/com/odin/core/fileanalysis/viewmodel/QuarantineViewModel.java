package com.odin.core.fileanalysis.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.odin.core.fileanalysis.model.QuarantinedFile;
import com.odin.core.fileanalysis.repository.QuarantineRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Real ViewModel for quarantine operations.
 * No simulated data - only actual quarantine operations.
 */
public class QuarantineViewModel extends ViewModel {

    private static final String TAG = "QuarantineViewModel";
    private final QuarantineRepository quarantineRepository;

    private final MutableLiveData<QuarantineStatus> quarantineStatus = new MutableLiveData<>(QuarantineStatus.IDLE);
    private final MutableLiveData<QuarantinedFile> quarantinedFile = new MutableLiveData<>();
    private final MutableLiveData<IsolationStatus> isolationStatus = new MutableLiveData<>(IsolationStatus.NOT_ISOLATED);
    private final MutableLiveData<List<QuarantinedFile>> allQuarantinedFiles = new MutableLiveData<>(new ArrayList<>());

    public QuarantineViewModel(QuarantineRepository quarantineRepository) {
        this.quarantineRepository = quarantineRepository;
        Log.d(TAG, "QuarantineViewModel initialized");
    }

    /**
     * Quarantine a file.
     * No simulation - only actual file quarantine.
     */
    public void quarantineFile(File file, String reason) {
        if (file == null || !file.exists()) {
            Log.e(TAG, "Invalid file for quarantine");
            quarantineStatus.setValue(QuarantineStatus.ERROR);
            return;
        }

        Log.d(TAG, "Quarantining file: " + file.getPath() + ", Reason: " + reason);
        quarantineStatus.setValue(QuarantineStatus.QUARANTINING);

        new Thread(() -> {
            try {
                QuarantinedFile result = quarantineRepository.quarantineFile(file, reason);
                
                if (result != null) {
                    quarantinedFile.postValue(result);
                    
                    // Add to all quarantined files
                    List<QuarantinedFile> currentFiles = allQuarantinedFiles.getValue();
                    if (currentFiles == null) {
                        currentFiles = new ArrayList<>();
                    }
                    currentFiles.add(result);
                    allQuarantinedFiles.postValue(currentFiles);
                    
                    quarantineStatus.postValue(QuarantineStatus.QUARANTINED);
                    isolationStatus.postValue(IsolationStatus.ISOLATED);
                    
                    Log.d(TAG, "File quarantined successfully: " + file.getName());
                } else {
                    quarantineStatus.postValue(QuarantineStatus.ERROR);
                    Log.e(TAG, "Quarantine result is null");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error quarantining file", e);
                quarantineStatus.postValue(QuarantineStatus.ERROR);
            }
        }).start();
    }

    /**
     * Restore a quarantined file.
     * No simulation - only actual file restoration.
     */
    public void restoreFile(String quarantinedFileId) {
        if (quarantinedFileId == null || quarantinedFileId.isEmpty()) {
            Log.e(TAG, "Invalid quarantined file ID");
            quarantineStatus.setValue(QuarantineStatus.ERROR);
            return;
        }

        Log.d(TAG, "Restoring quarantined file: " + quarantinedFileId);
        quarantineStatus.setValue(QuarantineStatus.RESTORING);

        new Thread(() -> {
            try {
                boolean success = quarantineRepository.restoreFile(quarantinedFileId);
                
                if (success) {
                    quarantineStatus.postValue(QuarantineStatus.RESTORED);
                    isolationStatus.postValue(IsolationStatus.NOT_ISOLATED);
                    
                    Log.d(TAG, "File restored successfully: " + quarantinedFileId);
                } else {
                    quarantineStatus.postValue(QuarantineStatus.ERROR);
                    Log.e(TAG, "Failed to restore file");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error restoring file", e);
                quarantineStatus.postValue(QuarantineStatus.ERROR);
            }
        }).start();
    }

    /**
     * Securely delete a quarantined file.
     * No simulation - only actual secure deletion.
     */
    public void securelyDeleteFile(String quarantinedFileId) {
        if (quarantinedFileId == null || quarantinedFileId.isEmpty()) {
            Log.e(TAG, "Invalid quarantined file ID");
            quarantineStatus.setValue(QuarantineStatus.ERROR);
            return;
        }

        Log.d(TAG, "Securely deleting quarantined file: " + quarantinedFileId);
        quarantineStatus.setValue(QuarantineStatus.DELETING);

        new Thread(() -> {
            try {
                boolean success = quarantineRepository.securelyDeleteFile(quarantinedFileId);
                
                if (success) {
                    quarantineStatus.postValue(QuarantineStatus.DELETED);
                    
                    Log.d(TAG, "File securely deleted: " + quarantinedFileId);
                } else {
                    quarantineStatus.postValue(QuarantineStatus.ERROR);
                    Log.e(TAG, "Failed to delete file");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error deleting file", e);
                quarantineStatus.postValue(QuarantineStatus.ERROR);
            }
        }).start();
    }

    /**
     * Get quarantine status.
     */
    public LiveData<QuarantineStatus> getQuarantineStatus() {
        return quarantineStatus;
    }

    /**
     * Get quarantined file.
     */
    public LiveData<QuarantinedFile> getQuarantinedFile() {
        return quarantinedFile;
    }

    /**
     * Get isolation status.
     */
    public LiveData<IsolationStatus> getIsolationStatus() {
        return isolationStatus;
    }

    /**
     * Get all quarantined files.
     */
    public LiveData<List<QuarantinedFile>> getAllQuarantinedFiles() {
        return allQuarantinedFiles;
    }

    /**
     * Load all quarantined files.
     * No simulation - only actual database retrieval.
     */
    public void loadAllQuarantinedFiles() {
        Log.d(TAG, "Loading all quarantined files");
        
        new Thread(() -> {
            try {
                List<QuarantinedFile> files = quarantineRepository.getAllQuarantinedFiles();
                allQuarantinedFiles.postValue(files);
                Log.d(TAG, "Loaded " + (files != null ? files.size() : 0) + " quarantined files");
            } catch (Exception e) {
                Log.e(TAG, "Error loading quarantined files", e);
            }
        }).start();
    }

    /**
     * Clear quarantined file.
     */
    public void clearQuarantinedFile() {
        quarantinedFile.setValue(null);
        quarantineStatus.setValue(QuarantineStatus.IDLE);
        isolationStatus.setValue(IsolationStatus.NOT_ISOLATED);
        Log.d(TAG, "Quarantined file cleared");
    }

    /**
     * Quarantine status enumeration.
     */
    public enum QuarantineStatus {
        IDLE,
        QUARANTINING,
        QUARANTINED,
        RESTORING,
        RESTORED,
        DELETING,
        DELETED,
        ERROR
    }

    /**
     * Isolation status enumeration.
     */
    public enum IsolationStatus {
        NOT_ISOLATED,
        ISOLATED,
        ERROR
    }
}
