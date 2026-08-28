package com.odin.core.network.anonymity.dns;

import android.content.Context;
import android.util.Log;

import com.odin.core.network.anonymity.model.DnsStatus;

/**
 * Main service for DNS privacy management.
 * Enterprise-grade DNS privacy service for ODIN.
 */
public class DNSPrivacyService {
    private static final String TAG = "DNSPrivacyService";

    private final DoHManager dohManager;
    private final DoTManager dotManager;
    private final DNSSECManager dnssecManager;
    private final CustomDNSManager customDNSManager;
    private final DNSMonitor dnsMonitor;
    private boolean isServiceActive;

    public DNSPrivacyService(Context context) {
        this.dohManager = new DoHManager();
        this.dotManager = new DoTManager();
        this.dnssecManager = new DNSSECManager();
        this.customDNSManager = new CustomDNSManager();
        this.dnsMonitor = new DNSMonitor(dohManager, dotManager, dnssecManager, customDNSManager);
        this.isServiceActive = false;
    }

    /**
     * Start DNS privacy service
     */
    public void startService() {
        Log.i(TAG, "Starting DNS privacy service");
        isServiceActive = true;
        dnsMonitor.startMonitoring();
    }

    /**
     * Stop DNS privacy service
     */
    public void stopService() {
        Log.i(TAG, "Stopping DNS privacy service");
        isServiceActive = false;
        dnsMonitor.stopMonitoring();
        
        // Disable all DNS privacy features
        dohManager.disableDoH();
        dotManager.disableDoT();
        dnssecManager.disableDNSSEC();
        customDNSManager.disableCustomDNS();
    }

    /**
     * Check if service is active
     */
    public boolean isServiceActive() {
        return isServiceActive;
    }

    /**
     * Get DoH manager
     */
    public DoHManager getDoHManager() {
        return dohManager;
    }

    /**
     * Get DoT manager
     */
    public DoTManager getDoTManager() {
        return dotManager;
    }

    /**
     * Get DNSSEC manager
     */
    public DNSSECManager getDNSSECManager() {
        return dnssecManager;
    }

    /**
     * Get custom DNS manager
     */
    public CustomDNSManager getCustomDNSManager() {
        return customDNSManager;
    }

    /**
     * Get DNS monitor
     */
    public DNSMonitor getDNSMonitor() {
        return dnsMonitor;
    }

    /**
     * Get DNS status
     */
    public DnsStatus getDnsStatus() {
        return dnsMonitor.getDnsStatus();
    }

    /**
     * Enable DoH
     */
    public void enableDoH(String provider) {
        dohManager.setActiveProvider(provider);
        dohManager.enableDoH();
        Log.i(TAG, "DoH enabled with provider: " + provider);
    }

    /**
     * Enable DoT
     */
    public void enableDoT(String provider) {
        dotManager.setActiveProvider(provider);
        dotManager.enableDoT();
        Log.i(TAG, "DoT enabled with provider: " + provider);
    }

    /**
     * Enable DNSSEC
     */
    public void enableDNSSEC() {
        dnssecManager.enableDNSSEC();
        Log.i(TAG, "DNSSEC enabled");
    }

    /**
     * Perform DNS query
     */
    public String performQuery(String domain) {
        if (dohManager.isDoHEnabled()) {
            return dohManager.performQuery(domain);
        } else if (dotManager.isDoTEnabled()) {
            return dotManager.performQuery(domain);
        } else {
            Log.w(TAG, "No DNS privacy method enabled, using system DNS");
            return null;
        }
    }

    /**
     * Shutdown service
     */
    public void shutdown() {
        stopService();
        dnsMonitor.shutdown();
    }
}
