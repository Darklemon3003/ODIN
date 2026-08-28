package com.odin.core.network.anonymity.model;

/**
 * Model representing the current status of VPN connection.
 * Enterprise-grade VPN status tracking for ODIN.
 */
public class VpnStatus {
    private boolean isConnected;
    private boolean isConnecting;
    private String statusMessage;
    private String serverAddress;
    private String protocol;
    private String encryption;
    private long connectionTime;
    private long dataTransferred;
    private long dataReceived;
    private long dataSent;
    private int mtu;
    private boolean isSplitTunnelingEnabled;
    private boolean isKillSwitchEnabled;
    private String vpnType;
    private long lastUpdated;

    public VpnStatus() {
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

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
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

    public long getDataReceived() {
        return dataReceived;
    }

    public void setDataReceived(long dataReceived) {
        this.dataReceived = dataReceived;
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getDataSent() {
        return dataSent;
    }

    public void setDataSent(long dataSent) {
        this.dataSent = dataSent;
        this.lastUpdated = System.currentTimeMillis();
    }

    public int getMtu() {
        return mtu;
    }

    public void setMtu(int mtu) {
        this.mtu = mtu;
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isSplitTunnelingEnabled() {
        return isSplitTunnelingEnabled;
    }

    public void setSplitTunnelingEnabled(boolean splitTunnelingEnabled) {
        isSplitTunnelingEnabled = splitTunnelingEnabled;
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isKillSwitchEnabled() {
        return isKillSwitchEnabled;
    }

    public void setKillSwitchEnabled(boolean killSwitchEnabled) {
        isKillSwitchEnabled = killSwitchEnabled;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getVpnType() {
        return vpnType;
    }

    public void setVpnType(String vpnType) {
        this.vpnType = vpnType;
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
        return "VpnStatus{" +
                "isConnected=" + isConnected +
                ", isConnecting=" + isConnecting +
                ", statusMessage='" + statusMessage + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", protocol='" + protocol + '\'' +
                ", encryption='" + encryption + '\'' +
                ", connectionTime=" + connectionTime +
                ", dataTransferred=" + dataTransferred +
                ", dataReceived=" + dataReceived +
                ", dataSent=" + dataSent +
                ", mtu=" + mtu +
                ", isSplitTunnelingEnabled=" + isSplitTunnelingEnabled +
                ", isKillSwitchEnabled=" + isKillSwitchEnabled +
                ", vpnType='" + vpnType + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
