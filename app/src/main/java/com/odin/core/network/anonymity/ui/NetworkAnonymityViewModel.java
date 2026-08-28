package com.odin.core.network.anonymity.ui;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.odin.core.network.anonymity.dns.DNSPrivacyService;
import com.odin.core.network.anonymity.dns.DNSViewModel;
import com.odin.core.network.anonymity.ipleak.IPLeakProtection;
import com.odin.core.network.anonymity.ipleak.IPLeakViewModel;
import com.odin.core.network.anonymity.model.AnonymityLog;
import com.odin.core.network.anonymity.model.DnsStatus;
import com.odin.core.network.anonymity.model.IPLeakStatus;
import com.odin.core.network.anonymity.model.RoutingStatus;
import com.odin.core.network.anonymity.model.TorStatus;
import com.odin.core.network.anonymity.model.VpnStatus;
import com.odin.core.network.anonymity.routing.NetworkRoutingEngine;
import com.odin.core.network.anonymity.routing.RoutingViewModel;
import com.odin.core.network.anonymity.tor.TorService;
import com.odin.core.network.anonymity.tor.TorViewModel;
import com.odin.core.network.anonymity.vpn.VpnAnonymityService;
import com.odin.core.network.anonymity.vpn.VpnViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Main ViewModel for Network Anonymity system.
 * Enterprise-grade main ViewModel for ODIN.
 */
public class NetworkAnonymityViewModel extends AndroidViewModel {
    private static final String TAG = "NetworkAnonymityViewModel";

    private final MutableLiveData<TorStatus> torStatus;
    private final MutableLiveData<VpnStatus> vpnStatus;
    private final MutableLiveData<RoutingStatus> routingStatus;
    private final MutableLiveData<DnsStatus> dnsStatus;
    private final MutableLiveData<IPLeakStatus> ipLeakStatus;
    private final MutableLiveData<List<AnonymityLog>> anonymityLogs;
    private final MutableLiveData<Boolean> isAnonymityActive;

    private TorViewModel torViewModel;
    private VpnViewModel vpnViewModel;
    private RoutingViewModel routingViewModel;
    private DNSViewModel dnsViewModel;
    private IPLeakViewModel ipLeakViewModel;

    public NetworkAnonymityViewModel(Application application) {
        super(application);
        this.torStatus = new MutableLiveData<>(new TorStatus());
        this.vpnStatus = new MutableLiveData<>(new VpnStatus());
        this.routingStatus = new MutableLiveData<>(new RoutingStatus());
        this.dnsStatus = new MutableLiveData<>(new DnsStatus());
        this.ipLeakStatus = new MutableLiveData<>(new IPLeakStatus());
        this.anonymityLogs = new MutableLiveData<>(new ArrayList<>());
        this.isAnonymityActive = new MutableLiveData<>(false);
    }

    /**
     * Set child ViewModels
     */
    public void setChildViewModels(TorViewModel torViewModel, VpnViewModel vpnViewModel,
                                   RoutingViewModel routingViewModel, DNSViewModel dnsViewModel,
                                   IPLeakViewModel ipLeakViewModel) {
        this.torViewModel = torViewModel;
        this.vpnViewModel = vpnViewModel;
        this.routingViewModel = routingViewModel;
        this.dnsViewModel = dnsViewModel;
        this.ipLeakViewModel = ipLeakViewModel;
    }

    /**
     * Get Tor status LiveData
     */
    public LiveData<TorStatus> getTorStatus() {
        return torStatus;
    }

    /**
     * Get VPN status LiveData
     */
    public LiveData<VpnStatus> getVpnStatus() {
        return vpnStatus;
    }

    /**
     * Get Routing status LiveData
     */
    public LiveData<RoutingStatus> getRoutingStatus() {
        return routingStatus;
    }

    /**
     * Get DNS status LiveData
     */
    public LiveData<DnsStatus> getDnsStatus() {
        return dnsStatus;
    }

    /**
     * Get IP leak status LiveData
     */
    public LiveData<IPLeakStatus> getIPLeakStatus() {
        return ipLeakStatus;
    }

    /**
     * Get anonymity logs LiveData
     */
    public LiveData<List<AnonymityLog>> getAnonymityLogs() {
        return anonymityLogs;
    }

    /**
     * Get anonymity active LiveData
     */
    public LiveData<Boolean> isAnonymityActive() {
        return isAnonymityActive;
    }

    /**
     * Activate complete network anonymity
     */
    public void activateAnonymity() {
        Log.i(TAG, "Activating complete network anonymity");
        
        // Start all services
        if (torViewModel != null) {
            torViewModel.startTor();
        }
        if (vpnViewModel != null) {
            vpnViewModel.startVpn();
        }
        if (routingViewModel != null) {
            routingViewModel.startRouting();
        }
        if (dnsViewModel != null) {
            dnsViewModel.startDNSPrivacy();
        }
        if (ipLeakViewModel != null) {
            ipLeakViewModel.startProtection();
        }
        
        isAnonymityActive.postValue(true);
        addLog("INFO", "NetworkAnonymity", "Complete anonymity activated");
    }

    /**
     * Deactivate complete network anonymity
     */
    public void deactivateAnonymity() {
        Log.i(TAG, "Deactivating complete network anonymity");
        
        // Stop all services
        if (torViewModel != null) {
            torViewModel.stopTor();
        }
        if (vpnViewModel != null) {
            vpnViewModel.stopVpn();
        }
        if (routingViewModel != null) {
            routingViewModel.stopRouting();
        }
        if (dnsViewModel != null) {
            dnsViewModel.stopDNSPrivacy();
        }
        if (ipLeakViewModel != null) {
            ipLeakViewModel.stopProtection();
        }
        
        isAnonymityActive.postValue(false);
        addLog("INFO", "NetworkAnonymity", "Complete anonymity deactivated");
    }

    /**
     * Update all statuses
     */
    public void updateAllStatuses() {
        if (torViewModel != null) {
            torViewModel.updateTorStatus();
            torStatus.postValue(torViewModel.getTorStatus().getValue());
        }
        if (vpnViewModel != null) {
            vpnViewModel.updateVpnStatus();
            vpnStatus.postValue(vpnViewModel.getVpnStatus().getValue());
        }
        if (routingViewModel != null) {
            routingViewModel.updateRoutingStatus();
            routingStatus.postValue(routingViewModel.getRoutingStatus().getValue());
        }
        if (dnsViewModel != null) {
            dnsViewModel.updateDnsStatus();
            dnsStatus.postValue(dnsViewModel.getDnsStatus().getValue());
        }
        if (ipLeakViewModel != null) {
            ipLeakViewModel.updateIPLeakStatus();
            ipLeakStatus.postValue(ipLeakViewModel.getIPLeakStatus().getValue());
        }
    }

    /**
     * Add log entry
     */
    public void addLog(String level, String component, String message) {
        AnonymityLog log = new AnonymityLog(level, component, message);
        List<AnonymityLog> logs = anonymityLogs.getValue();
        if (logs == null) {
            logs = new ArrayList<>();
        }
        logs.add(0, log); // Add to beginning
        
        // Keep only last 100 logs
        if (logs.size() > 100) {
            logs = new ArrayList<>(logs.subList(0, 100));
        }
        
        anonymityLogs.postValue(logs);
    }

    /**
     * Clear logs
     */
    public void clearLogs() {
        anonymityLogs.postValue(new ArrayList<>());
        Log.i(TAG, "Anonymity logs cleared");
    }

    /**
     * Get overall anonymity status
     */
    public String getOverallStatus() {
        if (!isAnonymityActive.getValue()) {
            return "Inactive";
        }
        
        boolean allActive = true;
        if (torViewModel != null && !torViewModel.isTorEnabled().getValue()) {
            allActive = false;
        }
        if (vpnViewModel != null && !vpnViewModel.isVpnEnabled().getValue()) {
            allActive = false;
        }
        if (routingViewModel != null && !routingViewModel.isRoutingEnabled().getValue()) {
            allActive = false;
        }
        if (dnsViewModel != null && !dnsViewModel.isDNSEnabled().getValue()) {
            allActive = false;
        }
        if (ipLeakViewModel != null && !ipLeakViewModel.isProtectionEnabled().getValue()) {
            allActive = false;
        }
        
        return allActive ? "Fully Protected" : "Partially Protected";
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "NetworkAnonymityViewModel cleared");
    }
}
