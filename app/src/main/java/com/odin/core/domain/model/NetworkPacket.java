package com.odin.core.domain.model;

/**
 * NetworkPacket representa un paquete de red auditado por NetShield.
 * El modelo es inmutable para preservar la integridad de los registros.
 */
public final class NetworkPacket {

    public static final int PROTOCOL_TCP = 6;
    public static final int PROTOCOL_UDP = 17;
    public static final int PROTOCOL_ICMP = 1;
    public static final int PROTOCOL_UNKNOWN = -1;

    private final String sourceIp;
    private final String destinationIp;
    private final int sourcePort;
    private final int destinationPort;
    private final int protocol;
    private final int size;
    private final long timestamp;
    private final int appUid;
    private final int flags; // TCP flags for protocol 6
    private final byte[] payload; // Packet payload data

    public NetworkPacket(String sourceIp,
                         String destinationIp,
                         int sourcePort,
                         int destinationPort,
                         int protocol,
                         int size,
                         long timestamp,
                         int appUid) {
        this(sourceIp, destinationIp, sourcePort, destinationPort, protocol, size, timestamp, appUid, 0, new byte[0]);
    }

    public NetworkPacket(String sourceIp,
                         String destinationIp,
                         int sourcePort,
                         int destinationPort,
                         int protocol,
                         int size,
                         long timestamp,
                         int appUid,
                         int flags) {
        this(sourceIp, destinationIp, sourcePort, destinationPort, protocol, size, timestamp, appUid, flags, new byte[0]);
    }

    public NetworkPacket(String sourceIp,
                         String destinationIp,
                         int sourcePort,
                         int destinationPort,
                         int protocol,
                         int size,
                         long timestamp,
                         int appUid,
                         int flags,
                         byte[] payload) {
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.protocol = protocol;
        this.size = size;
        this.timestamp = timestamp;
        this.appUid = appUid;
        this.flags = flags;
        this.payload = payload != null ? payload : new byte[0];
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public int getProtocol() {
        return protocol;
    }

    public int getSize() {
        return size;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getAppUid() {
        return appUid;
    }

    public int getFlags() {
        return flags;
    }

    public boolean isSynPacket() {
        return (flags & 0x02) != 0;
    }

    public boolean isAckPacket() {
        return (flags & 0x10) != 0;
    }

    public boolean isFinPacket() {
        return (flags & 0x01) != 0;
    }

    public byte[] getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "NetworkPacket{" +
                "sourceIp='" + sourceIp + '\'' +
                ", destinationIp='" + destinationIp + '\'' +
                ", sourcePort=" + sourcePort +
                ", destinationPort=" + destinationPort +
                ", protocol=" + protocol +
                ", size=" + size +
                ", timestamp=" + timestamp +
                ", appUid=" + appUid +
                '}';
    }
}
