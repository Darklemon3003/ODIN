package com.odin.core.service.network;

import com.odin.core.domain.model.NetworkPacket;

import java.nio.ByteBuffer;

/**
 * PacketAuditor parsea paquetes IPv4 básicos e extrae la metadata necesaria para auditoría.
 */
public final class PacketAuditor {

    public NetworkPacket auditPacket(byte[] rawPacket, int length) {
        if (rawPacket == null || length < 20) {
            return createUnknownPacket(length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(rawPacket, 0, length);
        int firstByte = buffer.get(0) & 0xFF;
        int version = (firstByte >> 4) & 0x0F;
        if (version != 4) {
            return createUnknownPacket(length);
        }

        int headerLength = (firstByte & 0x0F) * 4;
        if (headerLength < 20 || length < headerLength) {
            return createUnknownPacket(length);
        }

        int protocol = buffer.get(9) & 0xFF;
        String sourceIp = formatIp(buffer.get(12), buffer.get(13), buffer.get(14), buffer.get(15));
        String destinationIp = formatIp(buffer.get(16), buffer.get(17), buffer.get(18), buffer.get(19));

        int sourcePort = -1;
        int destinationPort = -1;

        if (protocol == NetworkPacket.PROTOCOL_TCP || protocol == NetworkPacket.PROTOCOL_UDP) {
            if (length >= headerLength + 4) {
                sourcePort = (buffer.get(headerLength) & 0xFF) << 8 | (buffer.get(headerLength + 1) & 0xFF);
                destinationPort = (buffer.get(headerLength + 2) & 0xFF) << 8 | (buffer.get(headerLength + 3) & 0xFF);
            }
        }

        return new NetworkPacket(
                sourceIp,
                destinationIp,
                sourcePort,
                destinationPort,
                protocol,
                length,
                System.currentTimeMillis(),
                -1
        );
    }

    private NetworkPacket createUnknownPacket(int length) {
        return new NetworkPacket(
                "0.0.0.0",
                "0.0.0.0",
                -1,
                -1,
                NetworkPacket.PROTOCOL_UNKNOWN,
                length,
                System.currentTimeMillis(),
                -1
        );
    }

    private String formatIp(byte b1, byte b2, byte b3, byte b4) {
        return (b1 & 0xFF) + "." +
                (b2 & 0xFF) + "." +
                (b3 & 0xFF) + "." +
                (b4 & 0xFF);
    }
}
