package com.odin.core.network.anonymity.ipleak;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Protects against WebRTC IP leaks.
 * Enterprise-grade WebRTC leak protection for ODIN.
 */
public class WebRTCLeakProtector {
    private static final String TAG = "WebRTCLeakProtector";

    private boolean protectionEnabled;
    private final List<String> blockedIPs;
    private int leakCount;

    public WebRTCLeakProtector() {
        this.protectionEnabled = false;
        this.blockedIPs = new ArrayList<>();
        this.leakCount = 0;
    }

    /**
     * Enable WebRTC leak protection
     */
    public void enableProtection() {
        protectionEnabled = true;
        Log.i(TAG, "WebRTC leak protection enabled");
    }

    /**
     * Disable WebRTC leak protection
     */
    public void disableProtection() {
        protectionEnabled = false;
        Log.i(TAG, "WebRTC leak protection disabled");
    }

    /**
     * Check if protection is enabled
     */
    public boolean isProtectionEnabled() {
        return protectionEnabled;
    }

    /**
     * Block IP address
     */
    public void blockIP(String ipAddress) {
        if (!blockedIPs.contains(ipAddress)) {
            blockedIPs.add(ipAddress);
            Log.i(TAG, "Blocked IP: " + ipAddress);
        }
    }

    /**
     * Unblock IP address
     */
    public void unblockIP(String ipAddress) {
        blockedIPs.remove(ipAddress);
        Log.i(TAG, "Unblocked IP: " + ipAddress);
    }

    /**
     * Check if IP is blocked
     */
    public boolean isIPBlocked(String ipAddress) {
        return blockedIPs.contains(ipAddress);
    }

    /**
     * Get blocked IPs
     */
    public List<String> getBlockedIPs() {
        return new ArrayList<>(blockedIPs);
    }

    /**
     * Detect WebRTC leak
     */
    public boolean detectLeak(String candidateIP) {
        if (!protectionEnabled) {
            return false;
        }

        // Check if IP is a potential WebRTC leak
        boolean isLeak = isPotentialLeak(candidateIP);
        
        if (isLeak) {
            leakCount++;
            Log.w(TAG, "WebRTC leak detected: " + candidateIP);
            blockIP(candidateIP);
        }
        
        return isLeak;
    }

    /**
     * Check if IP is a potential leak
     */
    private boolean isPotentialLeak(String ipAddress) {
        // Check if IP is local network address
        if (ipAddress.startsWith("192.168.") || 
            ipAddress.startsWith("10.") ||
            ipAddress.startsWith("172.16.") ||
            ipAddress.equals("127.0.0.1")) {
            return true;
        }
        
        // Check if IP is already blocked
        return blockedIPs.contains(ipAddress);
    }

    /**
     * Get leak count
     */
    public int getLeakCount() {
        return leakCount;
    }

    /**
     * Clear blocked IPs
     */
    public void clearBlockedIPs() {
        blockedIPs.clear();
        Log.i(TAG, "Cleared all blocked IPs");
    }

    /**
     * Reset leak count
     */
    public void resetLeakCount() {
        leakCount = 0;
        Log.i(TAG, "Reset leak count");
    }
}
