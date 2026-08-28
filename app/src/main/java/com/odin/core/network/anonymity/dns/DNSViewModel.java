package com.odin.core.network.anonymity.dns;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.odin.core.network.anonymity.model.DnsStatus;

/**
 * ViewModel for DNS privacy status and control.
 * Enterprise-grade DNS ViewModel for ODIN.
 */
public class DNSViewModel extends AndroidViewModel {
    private static final String TAG = "DNSViewModel";

    private final MutableLiveData<DnsStatus> dnsStatus;
    private final MutableLiveData<Boolean> isDNSEnabled;
    private DNSPrivacyService dnsPrivacyService;

    public DNSViewModel(Application application) {
        super(application);
        this.dnsStatus = new MutableLiveData<>(new DnsStatus());
        this.isDNSEnabled = new MutableLiveData<>(false);
    }

    /**
     * Set DNS privacy service reference
     */
    public void setDNSPrivacyService(DNSPrivacyService dnsPrivacyService) {
        this.dnsPrivacyService = dnsPrivacyService;
    }

    /**
     * Get DNS status LiveData
     */
    public LiveData<DnsStatus> getDnsStatus() {
        return dnsStatus;
    }

    /**
     * Get DNS enabled LiveData
     */
    public LiveData<Boolean> isDNSEnabled() {
        return isDNSEnabled;
    }

    /**
     * Start DNS privacy service
     */
    public void startDNSPrivacy() {
        Log.i(TAG, "Starting DNS privacy from ViewModel");
        if (dnsPrivacyService != null) {
            dnsPrivacyService.startService();
            isDNSEnabled.postValue(true);
        } else {
            Log.e(TAG, "DNSPrivacyService not available");
        }
    }

    /**
     * Stop DNS privacy service
     */
    public void stopDNSPrivacy() {
        Log.i(TAG, "Stopping DNS privacy from ViewModel");
        if (dnsPrivacyService != null) {
            dnsPrivacyService.stopService();
            isDNSEnabled.postValue(false);
        } else {
            Log.e(TAG, "DNSPrivacyService not available");
        }
    }

    /**
     * Update DNS status
     */
    public void updateDnsStatus() {
        if (dnsPrivacyService != null) {
            DnsStatus status = dnsPrivacyService.getDnsStatus();
            dnsStatus.postValue(status);
        }
    }

    /**
     * Enable DoH
     */
    public void enableDoH(String provider) {
        if (dnsPrivacyService != null) {
            dnsPrivacyService.enableDoH(provider);
            DnsStatus status = dnsPrivacyService.getDnsStatus();
            dnsStatus.postValue(status);
            Log.i(TAG, "DoH enabled with provider: " + provider);
        }
    }

    /**
     * Enable DoT
     */
    public void enableDoT(String provider) {
        if (dnsPrivacyService != null) {
            dnsPrivacyService.enableDoT(provider);
            DnsStatus status = dnsPrivacyService.getDnsStatus();
            dnsStatus.postValue(status);
            Log.i(TAG, "DoT enabled with provider: " + provider);
        }
    }

    /**
     * Enable DNSSEC
     */
    public void enableDNSSEC() {
        if (dnsPrivacyService != null) {
            dnsPrivacyService.enableDNSSEC();
            DnsStatus status = dnsPrivacyService.getDnsStatus();
            dnsStatus.postValue(status);
            Log.i(TAG, "DNSSEC enabled");
        }
    }

    /**
     * Disable DoH
     */
    public void disableDoH() {
        if (dnsPrivacyService != null) {
            dnsPrivacyService.getDoHManager().disableDoH();
            DnsStatus status = dnsPrivacyService.getDnsStatus();
            dnsStatus.postValue(status);
            Log.i(TAG, "DoH disabled");
        }
    }

    /**
     * Disable DoT
     */
    public void disableDoT() {
        if (dnsPrivacyService != null) {
            dnsPrivacyService.getDoTManager().disableDoT();
            DnsStatus status = dnsPrivacyService.getDnsStatus();
            dnsStatus.postValue(status);
            Log.i(TAG, "DoT disabled");
        }
    }

    /**
     * Disable DNSSEC
     */
    public void disableDNSSEC() {
        if (dnsPrivacyService != null) {
            dnsPrivacyService.getDNSSECManager().disableDNSSEC();
            DnsStatus status = dnsPrivacyService.getDnsStatus();
            dnsStatus.postValue(status);
            Log.i(TAG, "DNSSEC disabled");
        }
    }

    /**
     * Perform DNS query
     */
    public String performQuery(String domain) {
        if (dnsPrivacyService != null) {
            return dnsPrivacyService.performQuery(domain);
        }
        return null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "DNSViewModel cleared");
    }
}
