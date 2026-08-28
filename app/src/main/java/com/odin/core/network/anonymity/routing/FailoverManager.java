package com.odin.core.network.anonymity.routing;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages automatic failover between routes.
 * Enterprise-grade failover management for ODIN.
 */
public class FailoverManager {
    private static final String TAG = "FailoverManager";
    private static final int MAX_FAILURES = 3;
    private static final long FAILURE_TIMEOUT_MS = 30000;

    private final List<String> availableRoutes;
    private final List<String> failedRoutes;
    private final List<FailureRecord> failureRecords;
    private boolean failoverEnabled;
    private String currentRoute;

    public FailoverManager() {
        this.availableRoutes = new ArrayList<>();
        this.failedRoutes = new ArrayList<>();
        this.failureRecords = new ArrayList<>();
        this.failoverEnabled = true;
        this.currentRoute = "DIRECT";
    }

    /**
     * Enable failover
     */
    public void enableFailover() {
        failoverEnabled = true;
        Log.i(TAG, "Failover enabled");
    }

    /**
     * Disable failover
     */
    public void disableFailover() {
        failoverEnabled = false;
        Log.i(TAG, "Failover disabled");
    }

    /**
     * Check if failover is enabled
     */
    public boolean isFailoverEnabled() {
        return failoverEnabled;
    }

    /**
     * Add available route
     */
    public void addAvailableRoute(String route) {
        if (!availableRoutes.contains(route)) {
            availableRoutes.add(route);
            Log.d(TAG, "Added available route: " + route);
        }
    }

    /**
     * Remove available route
     */
    public void removeAvailableRoute(String route) {
        availableRoutes.remove(route);
        Log.d(TAG, "Removed available route: " + route);
    }

    /**
     * Set current route
     */
    public void setCurrentRoute(String route) {
        this.currentRoute = route;
        Log.d(TAG, "Current route set to: " + route);
    }

    /**
     * Get current route
     */
    public String getCurrentRoute() {
        return currentRoute;
    }

    /**
     * Record route failure
     */
    public void recordFailure(String route, String reason) {
        if (!failoverEnabled) {
            return;
        }

        FailureRecord record = new FailureRecord(route, reason, System.currentTimeMillis());
        failureRecords.add(record);

        // Check if route should be marked as failed
        int recentFailures = countRecentFailures(route);
        if (recentFailures >= MAX_FAILURES) {
            markRouteAsFailed(route);
            triggerFailover(reason);
        }

        Log.w(TAG, "Recorded failure for route " + route + ": " + reason);
    }

    /**
     * Count recent failures for a route
     */
    private int countRecentFailures(String route) {
        long now = System.currentTimeMillis();
        int count = 0;
        
        for (FailureRecord record : failureRecords) {
            if (record.getRoute().equals(route) && 
                (now - record.getTimestamp()) < FAILURE_TIMEOUT_MS) {
                count++;
            }
        }
        
        return count;
    }

    /**
     * Mark route as failed
     */
    private void markRouteAsFailed(String route) {
        if (!failedRoutes.contains(route)) {
            failedRoutes.add(route);
            availableRoutes.remove(route);
            Log.w(TAG, "Route marked as failed: " + route);
        }
    }

    /**
     * Trigger failover to next available route
     */
    private void triggerFailover(String reason) {
        if (availableRoutes.isEmpty()) {
            Log.e(TAG, "No available routes for failover");
            return;
        }

        String nextRoute = availableRoutes.get(0);
        setCurrentRoute(nextRoute);
        Log.i(TAG, "Failover triggered: " + currentRoute + " -> " + nextRoute + " (Reason: " + reason + ")");
    }

    /**
     * Recover failed route
     */
    public void recoverRoute(String route) {
        if (failedRoutes.contains(route)) {
            failedRoutes.remove(route);
            addAvailableRoute(route);
            
            // Clear failure records for this route
            failureRecords.removeIf(record -> record.getRoute().equals(route));
            
            Log.i(TAG, "Route recovered: " + route);
        }
    }

    /**
     * Get available routes
     */
    public List<String> getAvailableRoutes() {
        return new ArrayList<>(availableRoutes);
    }

    /**
     * Get failed routes
     */
    public List<String> getFailedRoutes() {
        return new ArrayList<>(failedRoutes);
    }

    /**
     * Get failure records
     */
    public List<FailureRecord> getFailureRecords() {
        return new ArrayList<>(failureRecords);
    }

    /**
     * Clear failure records
     */
    public void clearFailureRecords() {
        failureRecords.clear();
        failedRoutes.clear();
        Log.i(TAG, "Failure records cleared");
    }

    /**
     * Failure record data class
     */
    public static class FailureRecord {
        private final String route;
        private final String reason;
        private final long timestamp;

        public FailureRecord(String route, String reason, long timestamp) {
            this.route = route;
            this.reason = reason;
            this.timestamp = timestamp;
        }

        public String getRoute() {
            return route;
        }

        public String getReason() {
            return reason;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
