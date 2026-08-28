package com.odin.core.network.anonymity.ipleak;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Protects against IPv6 leaks.
 * Enterprise-grade IPv6 leak protection for ODIN.
 */
public class IPv6LeakProtector {
    private static final String TAG = "IPv6LeakProtector";

    private boolean protectionEnabled;
    private final List<String> detectedIPv6Addresses;
    private int leakCount;

    public IPv6LeakProtector() {
        this.protectionEnabled = false;
        this.detectedIPv6Addresses = new ArrayList<>();
        this.leakCount = 0;
    }

    /**
     * Enable IPv6 leak protection
     */
    public void enableProtection() {
        protectionEnabled = true;
        Log.i(TAG, "IPv6 leak protection enabled");
    }

    /**
     * Disable IPv6 leak protection
     */
    public void disableProtection() {
        protectionEnabled = false;
        Log.i(TAG, "IPv6 leak protection disabled");
    }

    /**
     * Check if protection is enabled
     */
    public boolean isProtectionEnabled() {
        return protectionEnabled;
    }

    /**
     * Detect IPv6 leak
     */
    public boolean detectLeak(String ipv6Address) {
        if (!protectionEnabled) {
            return false;
        }

        // Check if IPv6 address is a leak
        boolean isLeak = isValidIPv6Leak(ipv6Address);
        
        if (isLeak) {
            if (!detectedIPv6Addresses.contains(ipv6Address)) {
                detectedIPv6Addresses.add(ipv6Address);
                leakCount++;
                Log.w(TAG, "IPv6 leak detected: " + ipv6Address);
            }
        }
        
        return isLeak;
    }

    /**
     * Check if IPv6 address is a valid leak
     */
    private boolean isValidIPv6Leak(String ipv6Address) {
        // Check if it's a valid IPv6 address
        if (ipv6Address == null || !ipv6Address.contains(":")) {
            return false;
        }
        
        // Skip link-local addresses
        if (ipv6Address.startsWith("fe80") || ipv6Address.startsWith("::1")) {
            return false;
        }
        
        // Skip if already detected
        if (detectedIPv6Addresses.contains(ipv6Address)) {
            return false;
        }
        
        return true;
    }

    /**
     * Get detected IPv6 addresses
     */
    public List<String> getDetectedIPv6Addresses() {
        return new ArrayList<>(detectedIPv6Addresses);
    }

    /**
     * Get leak count
     */
    public int getLeakCount() {
        return leakCount;
    }

    /**
     * Clear detected addresses
     */
    public void clearDetectedAddresses() {
        detectedIPv6Addresses.clear();
        Log.i(TAG, "Cleared all detected IPv6 addresses");
    }

    /**
     * Reset leak count
     */
    public void resetLeakCount() {
        leakCount = 0;
        Log.i(TAG, "Reset leak count");
    }
}
