package com.odin.core.network.anonymity.routing;

import android.util.Log;

import com.odin.core.network.anonymity.model.RoutingStatus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Monitors routing status and health.
 * Enterprise-grade routing monitoring for ODIN.
 */
public class RoutingMonitor {
    private static final String TAG = "RoutingMonitor";
    private static final long MONITOR_INTERVAL_SECONDS = 5;

    private final RoutingSelector routingSelector;
    private final LoadBalancer loadBalancer;
    private final FailoverManager failoverManager;
    private final ScheduledExecutorService scheduler;
    private final RoutingStatus routingStatus;
    private boolean isMonitoring;

    public RoutingMonitor(RoutingSelector routingSelector, LoadBalancer loadBalancer, FailoverManager failoverManager) {
        this.routingSelector = routingSelector;
        this.loadBalancer = loadBalancer;
        this.failoverManager = failoverManager;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.routingStatus = new RoutingStatus();
        this.isMonitoring = false;
    }

    /**
     * Start monitoring routing
     */
    public void startMonitoring() {
        if (isMonitoring) {
            Log.w(TAG, "Routing monitoring already started");
            return;
        }

        isMonitoring = true;
        scheduler.scheduleAtFixedRate(this::monitorRouting, 0, MONITOR_INTERVAL_SECONDS, TimeUnit.SECONDS);
        Log.i(TAG, "Routing monitoring started");
    }

    /**
     * Stop monitoring routing
     */
    public void stopMonitoring() {
        if (!isMonitoring) {
            return;
        }

        isMonitoring = false;
        scheduler.shutdown();
        Log.i(TAG, "Routing monitoring stopped");
    }

    /**
     * Monitor routing status
     */
    private void monitorRouting() {
        try {
            routingStatus.setActive(true);
            routingStatus.setRoutingMode(routingSelector.getCurrentMode().toString());
            routingStatus.setActiveRoute(failoverManager.getCurrentRoute());
            routingStatus.setTotalRoutes(failoverManager.getAvailableRoutes().size());
            
            // Update load balance ratio
            if (loadBalancer.isLoadBalancingEnabled()) {
                java.util.Map<String, LoadBalancer.RouteStats> stats = loadBalancer.getAllRouteStats();
                double totalBytes = stats.values().stream()
                    .mapToLong(LoadBalancer.RouteStats::getBytesTransferred)
                    .sum();
                routingStatus.setLoadBalanceRatio(totalBytes > 0 ? 1.0 : 0.0);
            }
            
            // Update failover status
            routingStatus.setFailoverActive(failoverManager.isFailoverEnabled());
            if (failoverManager.getFailedRoutes().size() > 0) {
                routingStatus.setFailoverReason("Some routes failed");
            }
            
            // Update latency (simulated)
            routingStatus.setLatency((long)(Math.random() * 100));
            
            // Update throughput (simulated)
            routingStatus.setThroughput((long)(Math.random() * 1000));
            
            Log.d(TAG, "Routing status: Mode=" + routingStatus.getRoutingMode() + 
                      ", Route=" + routingStatus.getActiveRoute() + 
                      ", Failover=" + routingStatus.isFailoverActive());
        } catch (Exception e) {
            Log.e(TAG, "Error monitoring routing", e);
        }
    }

    /**
     * Get routing status
     */
    public RoutingStatus getRoutingStatus() {
        return routingStatus;
    }

    /**
     * Check if monitoring is active
     */
    public boolean isMonitoring() {
        return isMonitoring;
    }

    /**
     * Shutdown monitor
     */
    public void shutdown() {
        stopMonitoring();
    }
}
