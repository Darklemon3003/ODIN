package com.odin.core.network.anonymity.ipleak;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.odin.core.network.anonymity.model.IPLeakStatus;

/**
 * ViewModel for IP leak protection status and control.
 * Enterprise-grade IP leak ViewModel for ODIN.
 */
public class IPLeakViewModel extends AndroidViewModel {
    private static final String TAG = "IPLeakViewModel";

    private final MutableLiveData<IPLeakStatus> ipLeakStatus;
    private final MutableLiveData<Boolean> isProtectionEnabled;
    private IPLeakProtection ipLeakProtection;

    public IPLeakViewModel(Application application) {
        super(application);
        this.ipLeakStatus = new MutableLiveData<>(new IPLeakStatus());
        this.isProtectionEnabled = new MutableLiveData<>(false);
    }

    /**
     * Set IP leak protection reference
     */
    public void setIPLeakProtection(IPLeakProtection ipLeakProtection) {
        this.ipLeakProtection = ipLeakProtection;
    }

    /**
     * Get IP leak status LiveData
     */
    public LiveData<IPLeakStatus> getIPLeakStatus() {
        return ipLeakStatus;
    }

    /**
     * Get protection enabled LiveData
     */
    public LiveData<Boolean> isProtectionEnabled() {
        return isProtectionEnabled;
    }

    /**
     * Start IP leak protection
     */
    public void startProtection() {
        Log.i(TAG, "Starting IP leak protection from ViewModel");
        if (ipLeakProtection != null) {
            ipLeakProtection.startProtection();
            isProtectionEnabled.postValue(true);
        } else {
            Log.e(TAG, "IPLeakProtection not available");
        }
    }

    /**
     * Stop IP leak protection
     */
    public void stopProtection() {
        Log.i(TAG, "Stopping IP leak protection from ViewModel");
        if (ipLeakProtection != null) {
            ipLeakProtection.stopProtection();
            isProtectionEnabled.postValue(false);
        } else {
            Log.e(TAG, "IPLeakProtection not available");
        }
    }

    /**
     * Update IP leak status
     */
    public void updateIPLeakStatus() {
        if (ipLeakProtection != null) {
            IPLeakStatus status = ipLeakProtection.getIPLeakStatus();
            ipLeakStatus.postValue(status);
        }
    }

    /**
     * Perform manual leak check
     */
    public IPLeakDetector.LeakDetectionResult performLeakCheck() {
        if (ipLeakProtection != null) {
            return ipLeakProtection.performLeakCheck();
        }
        return null;
    }

    /**
     * Set expected DNS servers
     */
    public void setExpectedDNSServers(java.util.List<String> servers) {
        if (ipLeakProtection != null) {
            ipLeakProtection.setExpectedDNSServers(servers);
            Log.i(TAG, "Expected DNS servers set");
        }
    }

    /**
     * Enable WebRTC protection
     */
    public void enableWebRTCProtection() {
        if (ipLeakProtection != null) {
            ipLeakProtection.getLeakMonitor().getWebRTCLeakProtector().enableProtection();
            Log.i(TAG, "WebRTC protection enabled");
        }
    }

    /**
     * Disable WebRTC protection
     */
    public void disableWebRTCProtection() {
        if (ipLeakProtection != null) {
            ipLeakProtection.getLeakMonitor().getWebRTCLeakProtector().disableProtection();
            Log.i(TAG, "WebRTC protection disabled");
        }
    }

    /**
     * Enable IPv6 protection
     */
    public void enableIPv6Protection() {
        if (ipLeakProtection != null) {
            ipLeakProtection.getLeakMonitor().getIPv6LeakProtector().enableProtection();
            Log.i(TAG, "IPv6 protection enabled");
        }
    }

    /**
     * Disable IPv6 protection
     */
    public void disableIPv6Protection() {
        if (ipLeakProtection != null) {
            ipLeakProtection.getLeakMonitor().getIPv6LeakProtector().disableProtection();
            Log.i(TAG, "IPv6 protection disabled");
        }
    }

    /**
     * Enable DNS leak protection
     */
    public void enableDNSLeakProtection() {
        if (ipLeakProtection != null) {
            ipLeakProtection.getLeakMonitor().getDNSLeakProtector().enableProtection();
            Log.i(TAG, "DNS leak protection enabled");
        }
    }

    /**
     * Disable DNS leak protection
     */
    public void disableDNSLeakProtection() {
        if (ipLeakProtection != null) {
            ipLeakProtection.getLeakMonitor().getDNSLeakProtector().disableProtection();
            Log.i(TAG, "DNS leak protection disabled");
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "IPLeakViewModel cleared");
    }
}
