package com.odin.core.network.anonymity.model;

/**
 * Model representing the current status of DNS privacy.
 * Enterprise-grade DNS status tracking for ODIN.
 */
public class DnsStatus {
    private boolean isActive;
    private String dnsMode;
    private String dohProvider;
    private String dotProvider;
    private boolean isDnssecEnabled;
    private String customDnsServer;
    private long queriesTotal;
    private long queriesCached;
    private long queriesEncrypted;
    private long latency;
    private boolean isLeaking;
    private String leakStatus;
    private long lastUpdated;

    public DnsStatus() {
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getDnsMode() {
        return dnsMode;
    }

    public void setDnsMode(String dnsMode) {
        this.dnsMode = dnsMode;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getDohProvider() {
        return dohProvider;
    }

    public void setDohProvider(String dohProvider) {
        this.dohProvider = dohProvider;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getDotProvider() {
        return dotProvider;
    }

    public void setDotProvider(String dotProvider) {
        this.dotProvider = dotProvider;
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isDnssecEnabled() {
        return isDnssecEnabled;
    }

    public void setDnssecEnabled(boolean dnssecEnabled) {
        isDnssecEnabled = dnssecEnabled;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getCustomDnsServer() {
        return customDnsServer;
    }

    public void setCustomDnsServer(String customDnsServer) {
        this.customDnsServer = customDnsServer;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getQueriesTotal() {
        return queriesTotal;
    }

    public void setQueriesTotal(long queriesTotal) {
        this.queriesTotal = queriesTotal;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getQueriesCached() {
        return queriesCached;
    }

    public void setQueriesCached(long queriesCached) {
        this.queriesCached = queriesCached;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getQueriesEncrypted() {
        return queriesEncrypted;
    }

    public void setQueriesEncrypted(long queriesEncrypted) {
        this.queriesEncrypted = queriesEncrypted;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isLeaking() {
        return isLeaking;
    }

    public void setLeaking(boolean leaking) {
        isLeaking = leaking;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getLeakStatus() {
        return leakStatus;
    }

    public void setLeakStatus(String leakStatus) {
        this.leakStatus = leakStatus;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "DnsStatus{" +
                "isActive=" + isActive +
                ", dnsMode='" + dnsMode + '\'' +
                ", dohProvider='" + dohProvider + '\'' +
                ", dotProvider='" + dotProvider + '\'' +
                ", isDnssecEnabled=" + isDnssecEnabled +
                ", customDnsServer='" + customDnsServer + '\'' +
                ", queriesTotal=" + queriesTotal +
                ", queriesCached=" + queriesCached +
                ", queriesEncrypted=" + queriesEncrypted +
                ", latency=" + latency +
                ", isLeaking=" + isLeaking +
                ", leakStatus='" + leakStatus + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
