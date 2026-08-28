package com.odin.core.network.anonymity.routing;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.odin.core.network.anonymity.model.RoutingStatus;

/**
 * ViewModel for routing status and control.
 * Enterprise-grade routing ViewModel for ODIN.
 */
public class RoutingViewModel extends AndroidViewModel {
    private static final String TAG = "RoutingViewModel";

    private final MutableLiveData<RoutingStatus> routingStatus;
    private final MutableLiveData<Boolean> isRoutingEnabled;
    private NetworkRoutingEngine routingEngine;

    public RoutingViewModel(Application application) {
        super(application);
        this.routingStatus = new MutableLiveData<>(new RoutingStatus());
        this.isRoutingEnabled = new MutableLiveData<>(false);
    }

    /**
     * Set routing engine reference
     */
    public void setRoutingEngine(NetworkRoutingEngine routingEngine) {
        this.routingEngine = routingEngine;
    }

    /**
     * Get routing status LiveData
     */
    public LiveData<RoutingStatus> getRoutingStatus() {
        return routingStatus;
    }

    /**
     * Get routing enabled LiveData
     */
    public LiveData<Boolean> isRoutingEnabled() {
        return isRoutingEnabled;
    }

    /**
     * Start routing engine
     */
    public void startRouting() {
        Log.i(TAG, "Starting routing from ViewModel");
        if (routingEngine != null) {
            routingEngine.startEngine();
            isRoutingEnabled.postValue(true);
        } else {
            Log.e(TAG, "RoutingEngine not available");
        }
    }

    /**
     * Stop routing engine
     */
    public void stopRouting() {
        Log.i(TAG, "Stopping routing from ViewModel");
        if (routingEngine != null) {
            routingEngine.stopEngine();
            isRoutingEnabled.postValue(false);
        } else {
            Log.e(TAG, "RoutingEngine not available");
        }
    }

    /**
     * Update routing status
     */
    public void updateRoutingStatus() {
        if (routingEngine != null) {
            RoutingStatus status = routingEngine.getRoutingStatus();
            routingStatus.postValue(status);
        }
    }

    /**
     * Set routing mode
     */
    public void setRoutingMode(RoutingSelector.RoutingMode mode) {
        if (routingEngine != null) {
            routingEngine.setRoutingMode(mode);
            Log.i(TAG, "Routing mode set to: " + mode);
        }
    }

    /**
     * Enable load balancing
     */
    public void enableLoadBalancing() {
        if (routingEngine != null) {
            routingEngine.enableLoadBalancing();
            RoutingStatus status = routingEngine.getRoutingStatus();
            routingStatus.postValue(status);
            Log.i(TAG, "Load balancing enabled");
        }
    }

    /**
     * Disable load balancing
     */
    public void disableLoadBalancing() {
        if (routingEngine != null) {
            routingEngine.disableLoadBalancing();
            RoutingStatus status = routingEngine.getRoutingStatus();
            routingStatus.postValue(status);
            Log.i(TAG, "Load balancing disabled");
        }
    }

    /**
     * Enable failover
     */
    public void enableFailover() {
        if (routingEngine != null) {
            routingEngine.enableFailover();
            RoutingStatus status = routingEngine.getRoutingStatus();
            routingStatus.postValue(status);
            Log.i(TAG, "Failover enabled");
        }
    }

    /**
     * Disable failover
     */
    public void disableFailover() {
        if (routingEngine != null) {
            routingEngine.disableFailover();
            RoutingStatus status = routingEngine.getRoutingStatus();
            routingStatus.postValue(status);
            Log.i(TAG, "Failover disabled");
        }
    }

    /**
     * Route a request
     */
    public String routeRequest(String destination) {
        if (routingEngine != null) {
            return routingEngine.routeRequest(destination);
        }
        return "DIRECT";
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "RoutingViewModel cleared");
    }
}
