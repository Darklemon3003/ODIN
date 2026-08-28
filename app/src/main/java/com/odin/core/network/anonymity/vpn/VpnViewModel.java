package com.odin.core.network.anonymity.vpn;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.odin.core.network.anonymity.model.VpnStatus;

/**
 * ViewModel for VPN status and control.
 * Enterprise-grade VPN ViewModel for ODIN.
 */
public class VpnViewModel extends AndroidViewModel {
    private static final String TAG = "VpnViewModel";

    private final MutableLiveData<VpnStatus> vpnStatus;
    private final MutableLiveData<Boolean> isVpnEnabled;
    private VpnAnonymityService vpnService;
    private VpnLogger vpnLogger;

    public VpnViewModel(Application application) {
        super(application);
        this.vpnStatus = new MutableLiveData<>(new VpnStatus());
        this.isVpnEnabled = new MutableLiveData<>(false);
        this.vpnLogger = new VpnLogger();
    }

    /**
     * Set VPN service reference
     */
    public void setVpnService(VpnAnonymityService vpnService) {
        this.vpnService = vpnService;
        this.vpnLogger = vpnService.getVpnLogger();
    }

    /**
     * Get VPN status LiveData
     */
    public LiveData<VpnStatus> getVpnStatus() {
        return vpnStatus;
    }

    /**
     * Get VPN enabled LiveData
     */
    public LiveData<Boolean> isVpnEnabled() {
        return isVpnEnabled;
    }

    /**
     * Start VPN
     */
    public void startVpn() {
        Log.i(TAG, "Starting VPN from ViewModel");
        if (vpnService != null) {
            vpnService.startVpn();
            isVpnEnabled.postValue(true);
            vpnLogger.info("VpnViewModel", "VPN started by user");
        } else {
            Log.e(TAG, "VpnService not bound");
        }
    }

    /**
     * Stop VPN
     */
    public void stopVpn() {
        Log.i(TAG, "Stopping VPN from ViewModel");
        if (vpnService != null) {
            vpnService.stopVpn();
            isVpnEnabled.postValue(false);
            vpnLogger.info("VpnViewModel", "VPN stopped by user");
        } else {
            Log.e(TAG, "VpnService not bound");
        }
    }

    /**
     * Update VPN status
     */
    public void updateVpnStatus() {
        if (vpnService != null) {
            VpnStatus status = vpnService.getVpnStatus();
            vpnStatus.postValue(status);
        }
    }

    /**
     * Enable split tunneling
     */
    public void enableSplitTunneling() {
        if (vpnService != null) {
            vpnService.getVpnSplitTunnelManager().enableSplitTunneling();
            VpnStatus status = vpnService.getVpnStatus();
            status.setSplitTunnelingEnabled(true);
            vpnStatus.postValue(status);
            vpnLogger.info("VpnViewModel", "Split tunneling enabled");
        }
    }

    /**
     * Disable split tunneling
     */
    public void disableSplitTunneling() {
        if (vpnService != null) {
            vpnService.getVpnSplitTunnelManager().disableSplitTunneling();
            VpnStatus status = vpnService.getVpnStatus();
            status.setSplitTunnelingEnabled(false);
            vpnStatus.postValue(status);
            vpnLogger.info("VpnViewModel", "Split tunneling disabled");
        }
    }

    /**
     * Enable kill switch
     */
    public void enableKillSwitch() {
        if (vpnService != null) {
            vpnService.getVpnKillSwitch().enableKillSwitch();
            VpnStatus status = vpnService.getVpnStatus();
            status.setKillSwitchEnabled(true);
            vpnStatus.postValue(status);
            vpnLogger.info("VpnViewModel", "Kill switch enabled");
        }
    }

    /**
     * Disable kill switch
     */
    public void disableKillSwitch() {
        if (vpnService != null) {
            vpnService.getVpnKillSwitch().disableKillSwitch();
            VpnStatus status = vpnService.getVpnStatus();
            status.setKillSwitchEnabled(false);
            vpnStatus.postValue(status);
            vpnLogger.info("VpnViewModel", "Kill switch disabled");
        }
    }

    /**
     * Set VPN server address
     */
    public void setServerAddress(String serverAddress) {
        if (vpnService != null) {
            vpnService.getVpnConnectionManager().setServerAddress(serverAddress);
            VpnStatus status = vpnService.getVpnStatus();
            status.setServerAddress(serverAddress);
            vpnStatus.postValue(status);
            vpnLogger.info("VpnViewModel", "Server address set to: " + serverAddress);
        }
    }

    /**
     * Get recent logs
     */
    public java.util.List<com.odin.core.network.anonymity.model.AnonymityLog> getRecentLogs(int count) {
        return vpnLogger.getRecentLogs(count);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "VpnViewModel cleared");
    }
}
