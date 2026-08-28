package com.odin.core.domain.model;

import java.util.UUID;

/**
 * FirewallRule define un criterio de bloqueo para NetShield.
 * Puede bloquear por IP, rango de IPs y/o puerto de destino.
 */
public final class FirewallRule {

    private final String id;
    private final String name;
    private final String ipPattern;
    private final String sourceIp;
    private final int sourcePort;
    private final String destinationIp;
    private final int destinationPort;
    private final String protocol;
    private final boolean enabled;
    private final long createdAt;
    private final Integer port;

    public FirewallRule(
        String id,
        String name,
        String ipPattern,
        String sourceIp,
        int sourcePort,
        String destinationIp,
        int destinationPort,
        String protocol,
        boolean enabled,
        long createdAt,
        Integer port
    ) {
        this.id = id;
        this.name = name;
        this.ipPattern = ipPattern;
        this.sourceIp = sourceIp;
        this.sourcePort = sourcePort;
        this.destinationIp = destinationIp;
        this.destinationPort = destinationPort;
        this.protocol = protocol;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.port = port;
    }

    public FirewallRule(String ipPattern, Integer port) {
        this.id = UUID.randomUUID().toString();
        this.name = "Unnamed Rule";
        this.sourceIp = "";
        this.sourcePort = 0;
        this.destinationIp = ipPattern != null ? ipPattern : "";
        this.destinationPort = port != null ? port : 0;
        this.protocol = "TCP";
        this.enabled = true;
        this.createdAt = System.currentTimeMillis();
        this.ipPattern = ipPattern;
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIpPattern() {
        return ipPattern;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public Integer getPort() {
        return port;
    }

    public boolean matches(NetworkPacket packet) {
        if (packet == null) {
            return false;
        }

        boolean ipMatches = destinationIp == null || destinationIp.isEmpty() || matchesIp(packet.getDestinationIp());
        boolean portMatches = destinationPort == 0 || packet.getDestinationPort() == destinationPort;
        return ipMatches && portMatches;
    }

    private boolean matchesIp(String destinationIp) {
        if (destinationIp == null || destinationIp.isEmpty()) {
            return false;
        }
        if (ipPattern.contains("/")) {
            return matchesCidrRange(destinationIp, ipPattern);
        }
        if (ipPattern.contains("-")) {
            return matchesIpRange(destinationIp, ipPattern);
        }
        return destinationIp.equals(ipPattern);
    }

    private boolean matchesCidrRange(String ip, String cidr) {
        String[] parts = cidr.split("/");
        if (parts.length != 2) {
            return false;
        }
        int maskBits;
        try {
            maskBits = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        int ipAddress = ipToInt(ip);
        int networkAddress = ipToInt(parts[0]);
        if (ipAddress < 0 || networkAddress < 0) {
            return false;
        }
        int mask = maskBits == 0 ? 0 : 0xFFFFFFFF << (32 - maskBits);
        return (ipAddress & mask) == (networkAddress & mask);
    }

    private boolean matchesIpRange(String ip, String range) {
        String[] bounds = range.split("-");
        if (bounds.length != 2) {
            return false;
        }
        int value = ipToInt(ip);
        int start = ipToInt(bounds[0]);
        int end = ipToInt(bounds[1]);
        return value >= 0 && start >= 0 && end >= 0 && value >= start && value <= end;
    }

    private int ipToInt(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        if (octets.length != 4) {
            return -1;
        }
        int value = 0;
        try {
            for (String octet : octets) {
                int part = Integer.parseInt(octet);
                if (part < 0 || part > 255) {
                    return -1;
                }
                value = (value << 8) | part;
            }
            return value;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "FirewallRule{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ipPattern='" + ipPattern + '\'' +
                ", sourceIp='" + sourceIp + '\'' +
                ", sourcePort=" + sourcePort +
                ", destinationIp='" + destinationIp + '\'' +
                ", destinationPort=" + destinationPort +
                ", protocol='" + protocol + '\'' +
                ", enabled=" + enabled +
                ", createdAt=" + createdAt +
                ", port=" + port +
                '}';
    }
}
