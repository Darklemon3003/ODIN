package com.odin.core.network.anonymity.routing;

import android.content.Context;
import android.util.Log;

import com.odin.core.network.anonymity.model.RoutingStatus;

/**
 * Main engine for network routing management.
 * Enterprise-grade routing engine for ODIN.
 */
public class NetworkRoutingEngine {
    private static final String TAG = "NetworkRoutingEngine";

    private final RoutingSelector routingSelector;
    private final LoadBalancer loadBalancer;
    private final FailoverManager failoverManager;
    private final RoutingMonitor routingMonitor;
    private boolean isEngineActive;

    public NetworkRoutingEngine(Context context) {
        this.routingSelector = new RoutingSelector();
        this.loadBalancer = new LoadBalancer();
        this.failoverManager = new FailoverManager();
        this.routingMonitor = new RoutingMonitor(routingSelector, loadBalancer, failoverManager);
        this.isEngineActive = false;

        // Initialize default routes
        failoverManager.addAvailableRoute("TOR");
        failoverManager.addAvailableRoute("VPN");
        failoverManager.addAvailableRoute("DIRECT");
        failoverManager.setCurrentRoute("DIRECT");
    }

    /**
     * Start routing engine
     */
    public void startEngine() {
        Log.i(TAG, "Starting network routing engine");
        isEngineActive = true;
        routingMonitor.startMonitoring();
    }

    /**
     * Stop routing engine
     */
    public void stopEngine() {
        Log.i(TAG, "Stopping network routing engine");
        isEngineActive = false;
        routingMonitor.stopMonitoring();
    }

    /**
     * Check if engine is active
     */
    public boolean isEngineActive() {
        return isEngineActive;
    }

    /**
     * Get routing selector
     */
    public RoutingSelector getRoutingSelector() {
        return routingSelector;
    }

    /**
     * Get load balancer
     */
    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    /**
     * Get failover manager
     */
    public FailoverManager getFailoverManager() {
        return failoverManager;
    }

    /**
     * Get routing monitor
     */
    public RoutingMonitor getRoutingMonitor() {
        return routingMonitor;
    }

    /**
     * Get routing status
     */
    public RoutingStatus getRoutingStatus() {
        return routingMonitor.getRoutingStatus();
    }

    /**
     * Route a request through the selected path
     */
    public String routeRequest(String destination) {
        if (!isEngineActive) {
            Log.w(TAG, "Routing engine not active, using direct route");
            return "DIRECT";
        }

        String selectedRoute = routingSelector.selectRoute(destination);
        failoverManager.setCurrentRoute(selectedRoute);
        
        Log.d(TAG, "Routing request to " + destination + " via " + selectedRoute);
        return selectedRoute;
    }

    /**
     * Enable load balancing
     */
    public void enableLoadBalancing() {
        loadBalancer.enableLoadBalancing();
    }

    /**
     * Disable load balancing
     */
    public void disableLoadBalancing() {
        loadBalancer.disableLoadBalancing();
    }

    /**
     * Enable failover
     */
    public void enableFailover() {
        failoverManager.enableFailover();
    }

    /**
     * Disable failover
     */
    public void disableFailover() {
        failoverManager.disableFailover();
    }

    /**
     * Set routing mode
     */
    public void setRoutingMode(RoutingSelector.RoutingMode mode) {
        routingSelector.setRoutingMode(mode);
    }

    /**
     * Shutdown engine
     */
    public void shutdown() {
        stopEngine();
        routingMonitor.shutdown();
    }
}
