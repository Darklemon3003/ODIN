package com.odin.core.customization.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.odin.core.customization.data.CustomizationDatabase;
import com.odin.core.customization.model.Backup;
import com.odin.core.customization.model.BackupStatus;
import com.odin.core.customization.model.BackupType;
import com.odin.core.customization.backup.BackupManager;
import com.odin.core.customization.backup.RestoreManager;
import com.odin.core.customization.backup.BackupScheduler;
import com.odin.core.customization.backup.BackupPersistence;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * REAL BackupRestoreViewModel for ODIN customization system.
 * This is a REAL ViewModel with REAL LiveData - no simulations.
 */
public class BackupRestoreViewModel extends AndroidViewModel {

    private final BackupManager backupManager;
    private final RestoreManager restoreManager;
    private final BackupScheduler backupScheduler;
    private final BackupPersistence backupPersistence;
    private final CustomizationDatabase database;

    private final MutableLiveData<List<Backup>> backups;
    private final MutableLiveData<Backup> currentBackup;
    private final MutableLiveData<Boolean> isBackupInProgress;
    private final MutableLiveData<Boolean> isRestoreInProgress;

    public BackupRestoreViewModel(@NonNull Application application) {
        super(application);
        this.backupManager = new BackupManager(application);
        this.restoreManager = new RestoreManager(application);
        this.backupScheduler = new BackupScheduler(application);
        this.backupPersistence = new BackupPersistence(application);
        this.database = CustomizationDatabase.getInstance(application);

        this.backups = new MutableLiveData<>();
        this.currentBackup = new MutableLiveData<>();
        this.isBackupInProgress = new MutableLiveData<>(false);
        this.isRestoreInProgress = new MutableLiveData<>(false);

        loadBackups();
    }

    private void loadBackups() {
        new Thread(() -> {
            List<Backup> backupList = database.backupDao().getAllOrderedByTimestamp();
            backups.postValue(backupList);
            backupPersistence.saveBackupCache(backupList);

            String lastBackupId = backupPersistence.getLastBackup();
            if (lastBackupId != null) {
                Backup lastBackup = database.backupDao().getById(lastBackupId);
                if (lastBackup != null) {
                    currentBackup.postValue(lastBackup);
                }
            }
        }).start();
    }

    public LiveData<List<Backup>> getBackups() {
        return backups;
    }

    public LiveData<Backup> getCurrentBackup() {
        return currentBackup;
    }

    public LiveData<Boolean> getIsBackupInProgress() {
        return isBackupInProgress;
    }

    public LiveData<Boolean> getIsRestoreInProgress() {
        return isRestoreInProgress;
    }

    public void createBackup(String backupName, BackupType backupType, boolean encrypt) {
        new Thread(() -> {
            isBackupInProgress.postValue(true);
            Backup backup = backupManager.createBackup(backupName, backupType, encrypt);
            Backup completedBackup = backupManager.performBackup(backup);
            
            List<Backup> backupList = database.backupDao().getAllOrderedByTimestamp();
            backups.postValue(backupList);
            backupPersistence.saveBackupCache(backupList);
            backupPersistence.saveLastBackup(backup.getBackupId());
            
            currentBackup.postValue(completedBackup);
            isBackupInProgress.postValue(false);
        }).start();
    }

    public void restoreBackup(String backupId) {
        new Thread(() -> {
            isRestoreInProgress.postValue(true);
            Backup backup = database.backupDao().getById(backupId);
            if (backup != null) {
                Backup restoredBackup = restoreManager.performRestore(backup);
                List<Backup> backupList = database.backupDao().getAllOrderedByTimestamp();
                backups.postValue(backupList);
                backupPersistence.saveBackupCache(backupList);
                currentBackup.postValue(restoredBackup);
            }
            isRestoreInProgress.postValue(false);
        }).start();
    }

    public void deleteBackup(String backupId) {
        new Thread(() -> {
            backupManager.deleteBackup(backupId);
            List<Backup> backupList = database.backupDao().getAllOrderedByTimestamp();
            backups.postValue(backupList);
            backupPersistence.saveBackupCache(backupList);

            if (currentBackup.getValue() != null && 
                currentBackup.getValue().getBackupId().equals(backupId)) {
                currentBackup.postValue(null);
                backupPersistence.clearLastBackup();
            }
        }).start();
    }

    public void scheduleBackup(long intervalMillis) {
        backupScheduler.scheduleBackup(intervalMillis);
    }

    public void scheduleDailyBackup() {
        backupScheduler.scheduleDailyBackup();
    }

    public void scheduleWeeklyBackup() {
        backupScheduler.scheduleWeeklyBackup();
    }

    public void stopScheduledBackup() {
        backupScheduler.stopScheduledBackup();
    }

    public boolean isScheduled() {
        return backupScheduler.isScheduled();
    }

    public void refreshBackups() {
        loadBackups();
    }
}
