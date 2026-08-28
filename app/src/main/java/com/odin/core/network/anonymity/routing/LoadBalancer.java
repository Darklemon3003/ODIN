package com.odin.core.network.anonymity.routing;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Manages load balancing across multiple routes.
 * Enterprise-grade load balancing for ODIN.
 */
public class LoadBalancer {
    private static final String TAG = "LoadBalancer";

    private final Map<String, RouteStats> routeStats;
    private boolean loadBalancingEnabled;

    public LoadBalancer() {
        this.routeStats = new HashMap<>();
        this.loadBalancingEnabled = false;
    }

    /**
     * Enable load balancing
     */
    public void enableLoadBalancing() {
        loadBalancingEnabled = true;
        Log.i(TAG, "Load balancing enabled");
    }

    /**
     * Disable load balancing
     */
    public void disableLoadBalancing() {
        loadBalancingEnabled = false;
        Log.i(TAG, "Load balancing disabled");
    }

    /**
     * Check if load balancing is enabled
     */
    public boolean isLoadBalancingEnabled() {
        return loadBalancingEnabled;
    }

    /**
     * Select route based on load
     */
    public String selectRoute(String[] availableRoutes) {
        if (!loadBalancingEnabled || availableRoutes.length == 0) {
            return availableRoutes.length > 0 ? availableRoutes[0] : "DIRECT";
        }

        String selectedRoute = availableRoutes[0];
        double minLoad = Double.MAX_VALUE;

        for (String route : availableRoutes) {
            RouteStats stats = routeStats.get(route);
            double load = stats != null ? stats.getCurrentLoad() : 0.0;
            
            if (load < minLoad) {
                minLoad = load;
                selectedRoute = route;
            }
        }

        return selectedRoute;
    }

    /**
     * Record route usage
     */
    public void recordRouteUsage(String route, long bytes) {
        RouteStats stats = routeStats.computeIfAbsent(route, k -> new RouteStats());
        stats.addBytesTransferred(bytes);
        stats.incrementRequestCount();
        Log.d(TAG, "Recorded " + bytes + " bytes for route: " + route);
    }

    /**
     * Get route statistics
     */
    public RouteStats getRouteStats(String route) {
        return routeStats.get(route);
    }

    /**
     * Get all route statistics
     */
    public Map<String, RouteStats> getAllRouteStats() {
        return new HashMap<>(routeStats);
    }

    /**
     * Clear route statistics
     */
    public void clearStats() {
        routeStats.clear();
        Log.i(TAG, "Route statistics cleared");
    }

    /**
     * Route statistics data class
     */
    public static class RouteStats {
        private final AtomicLong bytesTransferred;
        private final AtomicLong requestCount;
        private final long startTime;

        public RouteStats() {
            this.bytesTransferred = new AtomicLong(0);
            this.requestCount = new AtomicLong(0);
            this.startTime = System.currentTimeMillis();
        }

        public void addBytesTransferred(long bytes) {
            bytesTransferred.addAndGet(bytes);
        }

        public void incrementRequestCount() {
            requestCount.incrementAndGet();
        }

        public long getBytesTransferred() {
            return bytesTransferred.get();
        }

        public long getRequestCount() {
            return requestCount.get();
        }

        public double getCurrentLoad() {
            long duration = System.currentTimeMillis() - startTime;
            if (duration == 0) return 0.0;
            return (double) bytesTransferred.get() / duration;
        }

        public long getStartTime() {
            return startTime;
        }
    }
}
