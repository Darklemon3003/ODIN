package com.odin.core.network.anonymity.model;

/**
 * Model representing the current status of Tor connection.
 * Enterprise-grade Tor status tracking for ODIN.
 */
public class TorStatus {
    private boolean isConnected;
    private boolean isConnecting;
    private String statusMessage;
    private int circuitCount;
    private String exitNode;
    private String country;
    private long bootstrapProgress;
    private String torVersion;
    private long connectionTime;
    private long dataTransferred;
    private boolean hasBridges;
    private String transportType;
    private long lastUpdated;

    public TorStatus() {
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnecting(boolean connecting) {
        isConnecting = connecting;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        this.lastUpdated = System.currentTimeMillis();
    }

    public int getCircuitCount() {
        return circuitCount;
    }

    public void setCircuitCount(int circuitCount) {
        this.circuitCount = circuitCount;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getExitNode() {
        return exitNode;
    }

    public void setExitNode(String exitNode) {
        this.exitNode = exitNode;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getBootstrapProgress() {
        return bootstrapProgress;
    }

    public void setBootstrapProgress(long bootstrapProgress) {
        this.bootstrapProgress = bootstrapProgress;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getTorVersion() {
        return torVersion;
    }

    public void setTorVersion(String torVersion) {
        this.torVersion = torVersion;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(long connectionTime) {
        this.connectionTime = connectionTime;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getDataTransferred() {
        return dataTransferred;
    }

    public void setDataTransferred(long dataTransferred) {
        this.dataTransferred = dataTransferred;
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isHasBridges() {
        return hasBridges;
    }

    public void setHasBridges(boolean hasBridges) {
        this.hasBridges = hasBridges;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
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
        return "TorStatus{" +
                "isConnected=" + isConnected +
                ", isConnecting=" + isConnecting +
                ", statusMessage='" + statusMessage + '\'' +
                ", circuitCount=" + circuitCount +
                ", exitNode='" + exitNode + '\'' +
                ", country='" + country + '\'' +
                ", bootstrapProgress=" + bootstrapProgress +
                ", torVersion='" + torVersion + '\'' +
                ", connectionTime=" + connectionTime +
                ", dataTransferred=" + dataTransferred +
                ", hasBridges=" + hasBridges +
                ", transportType='" + transportType + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
