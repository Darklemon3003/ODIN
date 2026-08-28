package com.odin.core.network.anonymity.ipleak;

import android.content.Context;
import android.util.Log;

import com.odin.core.network.anonymity.model.IPLeakStatus;

/**
 * Main service for IP leak protection.
 * Enterprise-grade IP leak protection for ODIN.
 */
public class IPLeakProtection {
    private static final String TAG = "IPLeakProtection";

    private final LeakMonitor leakMonitor;
    private boolean isProtectionActive;

    public IPLeakProtection(Context context) {
        this.leakMonitor = new LeakMonitor(context);
        this.isProtectionActive = false;
    }

    /**
     * Start IP leak protection
     */
    public void startProtection() {
        Log.i(TAG, "Starting IP leak protection");
        isProtectionActive = true;
        
        // Enable all protection mechanisms
        leakMonitor.getWebRTCLeakProtector().enableProtection();
        leakMonitor.getIPv6LeakProtector().enableProtection();
        leakMonitor.getDNSLeakProtector().enableProtection();
        
        leakMonitor.startMonitoring();
    }

    /**
     * Stop IP leak protection
     */
    public void stopProtection() {
        Log.i(TAG, "Stopping IP leak protection");
        isProtectionActive = false;
        
        // Disable all protection mechanisms
        leakMonitor.getWebRTCLeakProtector().disableProtection();
        leakMonitor.getIPv6LeakProtector().disableProtection();
        leakMonitor.getDNSLeakProtector().disableProtection();
        
        leakMonitor.stopMonitoring();
    }

    /**
     * Check if protection is active
     */
    public boolean isProtectionActive() {
        return isProtectionActive;
    }

    /**
     * Get leak monitor
     */
    public LeakMonitor getLeakMonitor() {
        return leakMonitor;
    }

    /**
     * Get IP leak status
     */
    public IPLeakStatus getIPLeakStatus() {
        return leakMonitor.getIPLeakStatus();
    }

    /**
     * Set expected DNS servers
     */
    public void setExpectedDNSServers(java.util.List<String> servers) {
        leakMonitor.getDNSLeakProtector().setExpectedDNSServers(servers);
        Log.i(TAG, "Set expected DNS servers: " + servers);
    }

    /**
     * Perform manual leak check
     */
    public IPLeakDetector.LeakDetectionResult performLeakCheck() {
        Log.i(TAG, "Performing manual leak check");
        return leakMonitor.getIPLeakDetector().detectLeaks();
    }

    /**
     * Shutdown protection
     */
    public void shutdown() {
        stopProtection();
        leakMonitor.shutdown();
    }
}
