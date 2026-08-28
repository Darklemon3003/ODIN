package com.odin.core.network.anonymity.ipleak;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Protects against DNS leaks.
 * Enterprise-grade DNS leak protection for ODIN.
 */
public class DNSLeakProtector {
    private static final String TAG = "DNSLeakProtector";

    private boolean protectionEnabled;
    private final List<String> detectedDNSServers;
    private final List<String> expectedDNSServers;
    private int leakCount;

    public DNSLeakProtector() {
        this.protectionEnabled = false;
        this.detectedDNSServers = new ArrayList<>();
        this.expectedDNSServers = new ArrayList<>();
        this.leakCount = 0;
    }

    /**
     * Enable DNS leak protection
     */
    public void enableProtection() {
        protectionEnabled = true;
        Log.i(TAG, "DNS leak protection enabled");
    }

    /**
     * Disable DNS leak protection
     */
    public void disableProtection() {
        protectionEnabled = false;
        Log.i(TAG, "DNS leak protection disabled");
    }

    /**
     * Check if protection is enabled
     */
    public boolean isProtectionEnabled() {
        return protectionEnabled;
    }

    /**
     * Set expected DNS servers
     */
    public void setExpectedDNSServers(List<String> servers) {
        expectedDNSServers.clear();
        expectedDNSServers.addAll(servers);
        Log.i(TAG, "Set expected DNS servers: " + servers);
    }

    /**
     * Detect DNS leak
     */
    public boolean detectLeak(List<String> detectedServers) {
        if (!protectionEnabled) {
            return false;
        }

        boolean hasLeak = false;
        
        for (String server : detectedServers) {
            if (!expectedDNSServers.contains(server)) {
                if (!detectedDNSServers.contains(server)) {
                    detectedDNSServers.add(server);
                    leakCount++;
                    Log.w(TAG, "DNS leak detected: " + server);
                    hasLeak = true;
                }
            }
        }
        
        return hasLeak;
    }

    /**
     * Get detected DNS servers
     */
    public List<String> getDetectedDNSServers() {
        return new ArrayList<>(detectedDNSServers);
    }

    /**
     * Get expected DNS servers
     */
    public List<String> getExpectedDNSServers() {
        return new ArrayList<>(expectedDNSServers);
    }

    /**
     * Get leak count
     */
    public int getLeakCount() {
        return leakCount;
    }

    /**
     * Clear detected DNS servers
     */
    public void clearDetectedServers() {
        detectedDNSServers.clear();
        Log.i(TAG, "Cleared all detected DNS servers");
    }

    /**
     * Reset leak count
     */
    public void resetLeakCount() {
        leakCount = 0;
        Log.i(TAG, "Reset leak count");
    }
}
