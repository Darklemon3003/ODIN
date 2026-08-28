package com.odin.core.network.anonymity.model;

/**
 * Model representing the current status of network routing.
 * Enterprise-grade routing status tracking for ODIN.
 */
public class RoutingStatus {
    private boolean isActive;
    private String routingMode;
    private String activeRoute;
    private long totalRoutes;
    private long activeConnections;
    private long failedConnections;
    private double loadBalanceRatio;
    private boolean isFailoverActive;
    private String failoverReason;
    private long latency;
    private long throughput;
    private long lastUpdated;

    public RoutingStatus() {
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getRoutingMode() {
        return routingMode;
    }

    public void setRoutingMode(String routingMode) {
        this.routingMode = routingMode;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getActiveRoute() {
        return activeRoute;
    }

    public void setActiveRoute(String activeRoute) {
        this.activeRoute = activeRoute;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getTotalRoutes() {
        return totalRoutes;
    }

    public void setTotalRoutes(long totalRoutes) {
        this.totalRoutes = totalRoutes;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getActiveConnections() {
        return activeConnections;
    }

    public void setActiveConnections(long activeConnections) {
        this.activeConnections = activeConnections;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getFailedConnections() {
        return failedConnections;
    }

    public void setFailedConnections(long failedConnections) {
        this.failedConnections = failedConnections;
        this.lastUpdated = System.currentTimeMillis();
    }

    public double getLoadBalanceRatio() {
        return loadBalanceRatio;
    }

    public void setLoadBalanceRatio(double loadBalanceRatio) {
        this.loadBalanceRatio = loadBalanceRatio;
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isFailoverActive() {
        return isFailoverActive;
    }

    public void setFailoverActive(boolean failoverActive) {
        isFailoverActive = failoverActive;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getFailoverReason() {
        return failoverReason;
    }

    public void setFailoverReason(String failoverReason) {
        this.failoverReason = failoverReason;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getThroughput() {
        return throughput;
    }

    public void setThroughput(long throughput) {
        this.throughput = throughput;
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
        return "RoutingStatus{" +
                "isActive=" + isActive +
                ", routingMode='" + routingMode + '\'' +
                ", activeRoute='" + activeRoute + '\'' +
                ", totalRoutes=" + totalRoutes +
                ", activeConnections=" + activeConnections +
                ", failedConnections=" + failedConnections +
                ", loadBalanceRatio=" + loadBalanceRatio +
                ", isFailoverActive=" + isFailoverActive +
                ", failoverReason='" + failoverReason + '\'' +
                ", latency=" + latency +
                ", throughput=" + throughput +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
