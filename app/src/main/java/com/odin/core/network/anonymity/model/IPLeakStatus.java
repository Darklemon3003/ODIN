package com.odin.core.network.anonymity.model;

/**
 * Model representing the current status of IP leak protection.
 * Enterprise-grade IP leak status tracking for ODIN.
 */
public class IPLeakStatus {
    private boolean isActive;
    private boolean hasIpLeak;
    private boolean hasDnsLeak;
    private boolean hasWebRTCLeak;
    private boolean hasIPv6Leak;
    private String detectedIp;
    private String detectedDns;
    private String webRTCIp;
    private String ipv6Address;
    private long lastCheckTime;
    private int leakCount;
    private String protectionStatus;
    private long lastUpdated;

    public IPLeakStatus() {
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isHasIpLeak() {
        return hasIpLeak;
    }

    public void setHasIpLeak(boolean hasIpLeak) {
        this.hasIpLeak = hasIpLeak;
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isHasDnsLeak() {
        return hasDnsLeak;
    }

    public void setHasDnsLeak(boolean hasDnsLeak) {
        this.hasDnsLeak = hasDnsLeak;
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isHasWebRTCLeak() {
        return hasWebRTCLeak;
    }

    public void setHasWebRTCLeak(boolean hasWebRTCLeak) {
        this.hasWebRTCLeak = hasWebRTCLeak;
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isHasIPv6Leak() {
        return hasIPv6Leak;
    }

    public void setHasIPv6Leak(boolean hasIPv6Leak) {
        this.hasIPv6Leak = hasIPv6Leak;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getDetectedIp() {
        return detectedIp;
    }

    public void setDetectedIp(String detectedIp) {
        this.detectedIp = detectedIp;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getDetectedDns() {
        return detectedDns;
    }

    public void setDetectedDns(String detectedDns) {
        this.detectedDns = detectedDns;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getWebRTCIp() {
        return webRTCIp;
    }

    public void setWebRTCIp(String webRTCIp) {
        this.webRTCIp = webRTCIp;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getIpv6Address() {
        return ipv6Address;
    }

    public void setIpv6Address(String ipv6Address) {
        this.ipv6Address = ipv6Address;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(long lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
        this.lastUpdated = System.currentTimeMillis();
    }

    public int getLeakCount() {
        return leakCount;
    }

    public void setLeakCount(int leakCount) {
        this.leakCount = leakCount;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getProtectionStatus() {
        return protectionStatus;
    }

    public void setProtectionStatus(String protectionStatus) {
        this.protectionStatus = protectionStatus;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean hasAnyLeaks() {
        return hasIpLeak || hasDnsLeak || hasWebRTCLeak || hasIPv6Leak;
    }

    @Override
    public String toString() {
        return "IPLeakStatus{" +
                "isActive=" + isActive +
                ", hasIpLeak=" + hasIpLeak +
                ", hasDnsLeak=" + hasDnsLeak +
                ", hasWebRTCLeak=" + hasWebRTCLeak +
                ", hasIPv6Leak=" + hasIPv6Leak +
                ", detectedIp='" + detectedIp + '\'' +
                ", detectedDns='" + detectedDns + '\'' +
                ", webRTCIp='" + webRTCIp + '\'' +
                ", ipv6Address='" + ipv6Address + '\'' +
                ", lastCheckTime=" + lastCheckTime +
                ", leakCount=" + leakCount +
                ", protectionStatus='" + protectionStatus + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
