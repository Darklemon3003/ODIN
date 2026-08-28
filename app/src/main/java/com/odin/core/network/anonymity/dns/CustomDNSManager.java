package com.odin.core.network.anonymity.dns;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages custom DNS server configuration.
 * Enterprise-grade custom DNS management for ODIN.
 */
public class CustomDNSManager {
    private static final String TAG = "CustomDNSManager";

    private final List<DNSServer> customServers;
    private boolean customDNSEnabled;

    public CustomDNSManager() {
        this.customServers = new ArrayList<>();
        this.customDNSEnabled = false;
    }

    /**
     * Enable custom DNS
     */
    public void enableCustomDNS() {
        customDNSEnabled = true;
        Log.i(TAG, "Custom DNS enabled");
    }

    /**
     * Disable custom DNS
     */
    public void disableCustomDNS() {
        customDNSEnabled = false;
        Log.i(TAG, "Custom DNS disabled");
    }

    /**
     * Check if custom DNS is enabled
     */
    public boolean isCustomDNSEnabled() {
        return customDNSEnabled;
    }

    /**
     * Add custom DNS server
     */
    public void addDNSServer(DNSServer server) {
        customServers.add(server);
        Log.i(TAG, "Added custom DNS server: " + server.getIpAddress() + ":" + server.getPort());
    }

    /**
     * Remove custom DNS server
     */
    public void removeDNSServer(DNSServer server) {
        customServers.remove(server);
        Log.i(TAG, "Removed custom DNS server: " + server.getIpAddress() + ":" + server.getPort());
    }

    /**
     * Get all custom DNS servers
     */
    public List<DNSServer> getDNSServers() {
        return new ArrayList<>(customServers);
    }

    /**
     * Get primary DNS server
     */
    public DNSServer getPrimaryServer() {
        return customServers.isEmpty() ? null : customServers.get(0);
    }

    /**
     * Clear all custom DNS servers
     */
    public void clearServers() {
        customServers.clear();
        Log.i(TAG, "Cleared all custom DNS servers");
    }

    /**
     * DNS server data class
     */
    public static class DNSServer {
        private final String ipAddress;
        private final int port;
        private final String name;
        private boolean isPrimary;

        public DNSServer(String ipAddress, int port, String name) {
            this.ipAddress = ipAddress;
            this.port = port;
            this.name = name;
            this.isPrimary = false;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public int getPort() {
            return port;
        }

        public String getName() {
            return name;
        }

        public boolean isPrimary() {
            return isPrimary;
        }

        public void setPrimary(boolean primary) {
            isPrimary = primary;
        }

        @Override
        public String toString() {
            return name + " (" + ipAddress + ":" + port + ")";
        }
    }
}
