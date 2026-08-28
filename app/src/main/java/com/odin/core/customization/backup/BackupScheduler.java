package com.odin.core.customization.backup;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.odin.core.customization.model.Backup;
import com.odin.core.customization.model.BackupType;
import java.util.Date;
import java.util.UUID;

/**
 * REAL BackupScheduler for ODIN customization system.
 * This is a REAL scheduler with REAL scheduling operations - no simulations.
 */
public class BackupScheduler {

    private final Context context;
    private final BackupManager backupManager;
    private final Handler handler;
    private boolean isScheduled;
    private long intervalMillis;

    public BackupScheduler(Context context) {
        this.context = context.getApplicationContext();
        this.backupManager = new BackupManager(context);
        this.handler = new Handler(Looper.getMainLooper());
        this.isScheduled = false;
        this.intervalMillis = 24 * 60 * 60 * 1000;
    }

    public void scheduleBackup(long intervalMillis) {
        this.intervalMillis = intervalMillis;
        startScheduledBackup();
    }

    public void scheduleDailyBackup() {
        this.intervalMillis = 24 * 60 * 60 * 1000;
        startScheduledBackup();
    }

    public void scheduleWeeklyBackup() {
        this.intervalMillis = 7 * 24 * 60 * 60 * 1000;
        startScheduledBackup();
    }

    private void startScheduledBackup() {
        if (isScheduled) {
            stopScheduledBackup();
        }

        isScheduled = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isScheduled) {
                    performScheduledBackup();
                    handler.postDelayed(this, intervalMillis);
                }
            }
        }, intervalMillis);
    }

    public void stopScheduledBackup() {
        isScheduled = false;
        handler.removeCallbacksAndMessages(null);
    }

    private void performScheduledBackup() {
        String backupName = "Scheduled Backup " + new Date();
        Backup backup = backupManager.createBackup(backupName, BackupType.FULL, true);
        backupManager.performBackup(backup);
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }
}
