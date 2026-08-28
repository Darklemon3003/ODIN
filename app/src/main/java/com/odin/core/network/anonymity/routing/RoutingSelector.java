package com.odin.core.network.anonymity.routing;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Selects the optimal routing path (Tor/VPN/Direct).
 * Enterprise-grade routing selection for ODIN.
 */
public class RoutingSelector {
    private static final String TAG = "RoutingSelector";

    public enum RoutingMode {
        TOR_ONLY,
        VPN_ONLY,
        TOR_OVER_VPN,
        VPN_OVER_TOR,
        DIRECT,
        AUTO
    }

    private RoutingMode currentMode;
    private final Map<String, Double> routeLatencies;
    private final Map<String, Double> routeReliability;

    public RoutingSelector() {
        this.currentMode = RoutingMode.AUTO;
        this.routeLatencies = new HashMap<>();
        this.routeReliability = new HashMap<>();
        initializeDefaultMetrics();
    }

    /**
     * Initialize default route metrics
     */
    private void initializeDefaultMetrics() {
        routeLatencies.put("TOR", 500.0);
        routeLatencies.put("VPN", 50.0);
        routeLatencies.put("DIRECT", 10.0);

        routeReliability.put("TOR", 0.95);
        routeReliability.put("VPN", 0.99);
        routeReliability.put("DIRECT", 1.0);
    }

    /**
     * Set routing mode
     */
    public void setRoutingMode(RoutingMode mode) {
        this.currentMode = mode;
        Log.i(TAG, "Routing mode set to: " + mode);
    }

    /**
     * Get current routing mode
     */
    public RoutingMode getCurrentMode() {
        return currentMode;
    }

    /**
     * Select optimal route based on mode and metrics
     */
    public String selectRoute(String destination) {
        switch (currentMode) {
            case TOR_ONLY:
                return "TOR";
            case VPN_ONLY:
                return "VPN";
            case TOR_OVER_VPN:
                return "TOR_OVER_VPN";
            case VPN_OVER_TOR:
                return "VPN_OVER_TOR";
            case DIRECT:
                return "DIRECT";
            case AUTO:
                return selectAutoRoute(destination);
            default:
                return "DIRECT";
        }
    }

    /**
     * Auto-select route based on metrics
     */
    private String selectAutoRoute(String destination) {
        // Simple heuristic: choose based on latency and reliability
        double torScore = calculateScore("TOR");
        double vpnScore = calculateScore("VPN");
        double directScore = calculateScore("DIRECT");

        if (torScore > vpnScore && torScore > directScore) {
            return "TOR";
        } else if (vpnScore > directScore) {
            return "VPN";
        } else {
            return "DIRECT";
        }
    }

    /**
     * Calculate route score
     */
    private double calculateScore(String route) {
        double latency = routeLatencies.getOrDefault(route, 1000.0);
        double reliability = routeReliability.getOrDefault(route, 0.9);
        
        // Lower latency and higher reliability = higher score
        return (reliability * 1000) / latency;
    }

    /**
     * Update route latency
     */
    public void updateRouteLatency(String route, double latency) {
        routeLatencies.put(route, latency);
        Log.d(TAG, "Updated " + route + " latency: " + latency + "ms");
    }

    /**
     * Update route reliability
     */
    public void updateRouteReliability(String route, double reliability) {
        routeReliability.put(route, reliability);
        Log.d(TAG, "Updated " + route + " reliability: " + reliability);
    }

    /**
     * Get route latency
     */
    public double getRouteLatency(String route) {
        return routeLatencies.getOrDefault(route, 0.0);
    }

    /**
     * Get route reliability
     */
    public double getRouteReliability(String route) {
        return routeReliability.getOrDefault(route, 0.0);
    }
}
