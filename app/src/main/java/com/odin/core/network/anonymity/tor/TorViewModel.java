package com.odin.core.network.anonymity.tor;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.odin.core.network.anonymity.model.TorStatus;

/**
 * ViewModel for Tor status and control.
 * Enterprise-grade Tor ViewModel for ODIN.
 */
public class TorViewModel extends AndroidViewModel {
    private static final String TAG = "TorViewModel";

    private final MutableLiveData<TorStatus> torStatus;
    private final MutableLiveData<Boolean> isTorEnabled;
    private TorService torService;
    private TorLogger torLogger;

    public TorViewModel(Application application) {
        super(application);
        this.torStatus = new MutableLiveData<>(new TorStatus());
        this.isTorEnabled = new MutableLiveData<>(false);
        this.torLogger = new TorLogger();
    }

    /**
     * Set Tor service reference
     */
    public void setTorService(TorService torService) {
        this.torService = torService;
        this.torLogger = torService.getTorLogger();
    }

    /**
     * Get Tor status LiveData
     */
    public LiveData<TorStatus> getTorStatus() {
        return torStatus;
    }

    /**
     * Get Tor enabled LiveData
     */
    public LiveData<Boolean> isTorEnabled() {
        return isTorEnabled;
    }

    /**
     * Start Tor
     */
    public void startTor() {
        Log.i(TAG, "Starting Tor from ViewModel");
        if (torService != null) {
            torService.startTor();
            isTorEnabled.postValue(true);
            torLogger.info("TorViewModel", "Tor started by user");
        } else {
            Log.e(TAG, "TorService not bound");
        }
    }

    /**
     * Stop Tor
     */
    public void stopTor() {
        Log.i(TAG, "Stopping Tor from ViewModel");
        if (torService != null) {
            torService.stopTor();
            isTorEnabled.postValue(false);
            torLogger.info("TorViewModel", "Tor stopped by user");
        } else {
            Log.e(TAG, "TorService not bound");
        }
    }

    /**
     * Update Tor status
     */
    public void updateTorStatus() {
        if (torService != null) {
            TorStatus status = torService.getTorStatus();
            torStatus.postValue(status);
        }
    }

    /**
     * Enable bridges
     */
    public void enableBridges() {
        if (torService != null) {
            torService.getTorBridgeManager().enableBridges();
            TorStatus status = torService.getTorStatus();
            status.setHasBridges(true);
            torStatus.postValue(status);
            torLogger.info("TorViewModel", "Bridges enabled");
        }
    }

    /**
     * Disable bridges
     */
    public void disableBridges() {
        if (torService != null) {
            torService.getTorBridgeManager().disableBridges();
            TorStatus status = torService.getTorStatus();
            status.setHasBridges(false);
            torStatus.postValue(status);
            torLogger.info("TorViewModel", "Bridges disabled");
        }
    }

    /**
     * Set transport type
     */
    public void setTransportType(String transportType) {
        if (torService != null) {
            torService.getTorTransportManager().setActiveTransport(transportType);
            TorStatus status = torService.getTorStatus();
            status.setTransportType(transportType);
            torStatus.postValue(status);
            torLogger.info("TorViewModel", "Transport set to: " + transportType);
        }
    }

    /**
     * Get recent logs
     */
    public java.util.List<com.odin.core.network.anonymity.model.AnonymityLog> getRecentLogs(int count) {
        return torLogger.getRecentLogs(count);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "TorViewModel cleared");
    }
}
