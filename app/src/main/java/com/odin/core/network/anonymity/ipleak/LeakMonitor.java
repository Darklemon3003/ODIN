package com.odin.core.network.anonymity.ipleak;

import android.content.Context;
import android.util.Log;

import com.odin.core.network.anonymity.model.IPLeakStatus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Monitors IP leaks and protection status.
 * Enterprise-grade leak monitoring for ODIN.
 */
public class LeakMonitor {
    private static final String TAG = "LeakMonitor";
    private static final long MONITOR_INTERVAL_SECONDS = 10;

    private final IPLeakDetector ipLeakDetector;
    private final WebRTCLeakProtector webRTCLeakProtector;
    private final IPv6LeakProtector ipv6LeakProtector;
    private final DNSLeakProtector dnsLeakProtector;
    private final ScheduledExecutorService scheduler;
    private final IPLeakStatus ipLeakStatus;
    private boolean isMonitoring;

    public LeakMonitor(Context context) {
        this.ipLeakDetector = new IPLeakDetector(context);
        this.webRTCLeakProtector = new WebRTCLeakProtector();
        this.ipv6LeakProtector = new IPv6LeakProtector();
        this.dnsLeakProtector = new DNSLeakProtector();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.ipLeakStatus = new IPLeakStatus();
        this.isMonitoring = false;
    }

    /**
     * Start monitoring leaks
     */
    public void startMonitoring() {
        if (isMonitoring) {
            Log.w(TAG, "Leak monitoring already started");
            return;
        }

        isMonitoring = true;
        scheduler.scheduleAtFixedRate(this::monitorLeaks, 0, MONITOR_INTERVAL_SECONDS, TimeUnit.SECONDS);
        Log.i(TAG, "Leak monitoring started");
    }

    /**
     * Stop monitoring leaks
     */
    public void stopMonitoring() {
        if (!isMonitoring) {
            return;
        }

        isMonitoring = false;
        scheduler.shutdown();
        Log.i(TAG, "Leak monitoring stopped");
    }

    /**
     * Monitor for leaks
     */
    private void monitorLeaks() {
        try {
            ipLeakStatus.setActive(true);
            ipLeakStatus.setLastCheckTime(System.currentTimeMillis());
            
            // Detect IP leaks
            IPLeakDetector.LeakDetectionResult result = ipLeakDetector.detectLeaks();
            
            // Update IP leak status
            ipLeakStatus.setHasIpLeak(result.isHasIPLeak());
            if (!result.getDetectedIPs().isEmpty()) {
                ipLeakStatus.setDetectedIp(result.getDetectedIPs().get(0));
            }
            
            // Update IPv6 leak status
            ipLeakStatus.setHasIPv6Leak(result.isHasIPv6Leak());
            if (!result.getIPv6Addresses().isEmpty()) {
                ipLeakStatus.setIpv6Address(result.getIPv6Addresses().get(0));
            }
            
            // Update DNS leak status
            ipLeakStatus.setHasDnsLeak(result.isHasDNSLeak());
            if (!result.getDNSServers().isEmpty()) {
                ipLeakStatus.setDetectedDns(result.getDNSServers().get(0));
            }
            
            // Update WebRTC leak status
            ipLeakStatus.setHasWebRTCLeak(webRTCLeakProtector.getLeakCount() > 0);
            
            // Update leak count
            int totalLeaks = (ipLeakStatus.isHasIpLeak() ? 1 : 0) +
                           (ipLeakStatus.isHasDnsLeak() ? 1 : 0) +
                           (ipLeakStatus.isHasWebRTCLeak() ? 1 : 0) +
                           (ipLeakStatus.isHasIPv6Leak() ? 1 : 0);
            ipLeakStatus.setLeakCount(totalLeaks);
            
            // Update protection status
            if (ipLeakStatus.hasAnyLeaks()) {
                ipLeakStatus.setProtectionStatus("Leaks detected - Protection active");
            } else {
                ipLeakStatus.setProtectionStatus("No leaks detected - Protected");
            }
            
            Log.d(TAG, "Leak status: IP=" + ipLeakStatus.isHasIpLeak() + 
                      ", DNS=" + ipLeakStatus.isHasDnsLeak() + 
                      ", WebRTC=" + ipLeakStatus.isHasWebRTCLeak() + 
                      ", IPv6=" + ipLeakStatus.isHasIPv6Leak());
        } catch (Exception e) {
            Log.e(TAG, "Error monitoring leaks", e);
        }
    }

    /**
     * Get IP leak status
     */
    public IPLeakStatus getIPLeakStatus() {
        return ipLeakStatus;
    }

    /**
     * Get IP leak detector
     */
    public IPLeakDetector getIPLeakDetector() {
        return ipLeakDetector;
    }

    /**
     * Get WebRTC leak protector
     */
    public WebRTCLeakProtector getWebRTCLeakProtector() {
        return webRTCLeakProtector;
    }

    /**
     * Get IPv6 leak protector
     */
    public IPv6LeakProtector getIPv6LeakProtector() {
        return ipv6LeakProtector;
    }

    /**
     * Get DNS leak protector
     */
    public DNSLeakProtector getDNSLeakProtector() {
        return dnsLeakProtector;
    }

    /**
     * Check if monitoring is active
     */
    public boolean isMonitoring() {
        return isMonitoring;
    }

    /**
     * Shutdown monitor
     */
    public void shutdown() {
        stopMonitoring();
    }
}
