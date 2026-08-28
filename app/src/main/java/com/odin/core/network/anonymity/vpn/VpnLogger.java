package com.odin.core.network.anonymity.vpn;

import android.util.Log;

import com.odin.core.network.anonymity.model.AnonymityLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Logs VPN events and activities.
 * Enterprise-grade VPN logging for ODIN.
 */
public class VpnLogger {
    private static final String TAG = "VpnLogger";
    private static final int MAX_LOG_ENTRIES = 1000;

    private final ConcurrentLinkedQueue<AnonymityLog> logQueue;
    private final List<AnonymityLog> persistentLogs;

    public VpnLogger() {
        this.logQueue = new ConcurrentLinkedQueue<>();
        this.persistentLogs = new ArrayList<>();
    }

    /**
     * Log a VPN event
     */
    public void log(String level, String component, String message) {
        AnonymityLog log = new AnonymityLog(level, component, message);
        log.setCategory("VPN");
        
        logQueue.offer(log);
        
        // Maintain max log size
        while (logQueue.size() > MAX_LOG_ENTRIES) {
            logQueue.poll();
        }

        // Also add to persistent logs for important events
        if (level.equals("ERROR") || level.equals("WARN")) {
            synchronized (persistentLogs) {
                persistentLogs.add(log);
            }
        }

        Log.i(TAG, "[" + level + "] " + component + ": " + message);
    }

    /**
     * Log info event
     */
    public void info(String component, String message) {
        log("INFO", component, message);
    }

    /**
     * Log warning event
     */
    public void warn(String component, String message) {
        log("WARN", component, message);
    }

    /**
     * Log error event
     */
    public void error(String component, String message) {
        log("ERROR", component, message);
    }

    /**
     * Log debug event
     */
    public void debug(String component, String message) {
        log("DEBUG", component, message);
    }

    /**
     * Get recent logs
     */
    public List<AnonymityLog> getRecentLogs(int count) {
        List<AnonymityLog> logs = new ArrayList<>();
        int itemsToTake = Math.min(count, logQueue.size());
        
        for (int i = 0; i < itemsToTake; i++) {
            AnonymityLog log = logQueue.poll();
            if (log != null) {
                logs.add(log);
                logQueue.offer(log); // Put it back
            }
        }
        
        return logs;
    }

    /**
     * Get all persistent logs
     */
    public List<AnonymityLog> getPersistentLogs() {
        synchronized (persistentLogs) {
            return new ArrayList<>(persistentLogs);
        }
    }

    /**
     * Clear all logs
     */
    public void clearLogs() {
        logQueue.clear();
        synchronized (persistentLogs) {
            persistentLogs.clear();
        }
        Log.i(TAG, "VPN logs cleared");
    }

    /**
     * Get log count
     */
    public int getLogCount() {
        return logQueue.size();
    }
}
