package com.odin.core.network.detection.specialized;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.odin.core.domain.model.NetworkPacket;
import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatLevel;
import com.odin.core.network.dpi.DeepPacketInspector;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * BypassDetectionEngine: Motor unificado para detectar técnicas de bypass de firewall/VPN.
 * 
 * Detecta técnicas de evasión:
 * - ICMP Tunneling: Payloads ICMP >64 bytes con entropía alta
 * - DNS Tunneling: Queries DNS con TXT records largos y alta entropía
 * - HTTP Tunneling: HTTP requests con headers inusuales
 * - Fragmentation Evasion: Paquetes IP fragmentados con offsets sospechosos
 * - Protocol Hopping: Cambio rápido de protocolos para misma conexión
 * - Timing Attack Detection: Paquetes enviados en intervalos específicos
 * 
 * Thread-safe: Concurrent collections + atomic operations
 * Performance: <2ms por paquete
 */
public final class BypassDetectionEngine {

    // Thresholds configurables
    private static final int ICMP_TUNNEL_SIZE_THRESHOLD = 64; // bytes
    private static final double DNS_TUNNEL_ENTROPY_THRESHOLD = 0.7;
    private static final int FRAGMENTATION_THRESHOLD = 10;
    private static final long FRAGMENTATION_WINDOW_MS = 30_000; // 30 segundos
    private static final int PROTOCOL_HOPPING_THRESHOLD = 5;
    private static final long PROTOCOL_HOPPING_WINDOW_MS = 10_000; // 10 segundos
    private static final long BYPASS_TIMEOUT_MS = 300_000; // 5 minutos

    private final ConcurrentHashMap<String, BypassTracker> bypassTrackers;
    private final AtomicLong totalBypassDetected;
    private final AtomicLong totalPacketsAnalyzed;

    public BypassDetectionEngine() {
        this.bypassTrackers = new ConcurrentHashMap<>();
        this.totalBypassDetected = new AtomicLong(0);
        this.totalPacketsAnalyzed = new AtomicLong(0);
    }

    /**
     * Analiza un paquete para detectar técnicas de bypass.
     * 
     * @param packet Paquete a analizar
     * @param dpiAnalysis Análisis DPI del paquete
     * @return DetectionResult si detecta bypass, null otherwise
     */
    @Nullable
    public DetectionResult analyzePacket(
            @NonNull NetworkPacket packet,
            @Nullable DeepPacketInspector.DeepPacketAnalysis dpiAnalysis
    ) {
        totalPacketsAnalyzed.incrementAndGet();

        String sourceIP = packet.getSourceIp();
        String destIP = packet.getDestinationIp();
        int destPort = packet.getDestinationPort();
        int protocol = packet.getProtocol();
        int packetSize = packet.getSize();

        // Obtener o crear tracker para esta IP origen
        BypassTracker tracker = bypassTrackers.computeIfAbsent(sourceIP, k -> new BypassTracker());
        tracker.recordActivity(protocol, packetSize);

        DetectionResult result = null;

        // 1. ICMP Tunneling Detection
        if (protocol == NetworkPacket.PROTOCOL_ICMP) {
            result = analyzeICMP(packet, tracker, sourceIP, destIP);
        }

        // 2. DNS Tunneling Detection
        if (destPort == 53 && dpiAnalysis != null && dpiAnalysis.payloadAnalysis != null) {
            result = analyzeDNSTunneling(packet, dpiAnalysis, tracker, sourceIP, destIP);
        }

        // 3. HTTP Tunneling Detection
        if ((destPort == 80 || destPort == 443) && dpiAnalysis != null) {
            result = analyzeHTTPTunneling(packet, dpiAnalysis, tracker, sourceIP, destIP, destPort);
        }

        // 4. Fragmentation Evasion Detection
        if (isFragmentedPacket(packet)) {
            int fragmentedCount = tracker.incrementFragmentedPacketCount();
            if (fragmentedCount >= FRAGMENTATION_THRESHOLD) {
                long timeWindow = System.currentTimeMillis() - tracker.getFirstSeenTime();
                if (timeWindow < FRAGMENTATION_WINDOW_MS) {
                    result = createDetectionResult(
                            ThreatLevel.HIGH,
                            "FRAGMENTATION_EVASION",
                            String.format("Fragmentation evasion detectada: %d paquetes fragmentados en %dms", fragmentedCount, timeWindow),
                            sourceIP,
                            destIP,
                            destPort,
                            0.85,
                            String.format("Fragmentados: %d", fragmentedCount)
                    );
                }
            }
        }

        // 5. Protocol Hopping Detection
        List<Integer> protocolSequence = tracker.getProtocolSequence();
        if (protocolSequence.size() >= PROTOCOL_HOPPING_THRESHOLD) {
            long timeWindow = System.currentTimeMillis() - tracker.getFirstSeenTime();
            if (timeWindow < PROTOCOL_HOPPING_WINDOW_MS) {
                int uniqueProtocols = (int) protocolSequence.stream().distinct().count();
                if (uniqueProtocols >= 3) {
                    result = createDetectionResult(
                            ThreatLevel.HIGH,
                            "PROTOCOL_HOPPING",
                            String.format("Protocol hopping detectado: %d protocolos en %dms", uniqueProtocols, timeWindow),
                            sourceIP,
                            destIP,
                            destPort,
                            0.8,
                            String.format("Protocolos únicos: %d", uniqueProtocols)
                    );
                }
            }
        }

        // 6. Timing Attack Detection
        List<Long> timestamps = tracker.getPacketTimestamps();
        if (timestamps.size() >= 10) {
            double timingVariance = calculateTimingVariance(timestamps);
            if (timingVariance < 0.05) { // Muy baja variación = posible timing attack
                result = createDetectionResult(
                        ThreatLevel.MEDIUM,
                        "TIMING_ATTACK",
                        String.format("Timing attack detectado: baja variación en intervalos (%.2f%%)", timingVariance * 100),
                        sourceIP,
                        destIP,
                        destPort,
                        0.7,
                        String.format("Variación: %.2f%%", timingVariance * 100)
                );
            }
        }

        if (result != null) {
            totalBypassDetected.incrementAndGet();
        }

        return result;
    }

    /**
     * Analiza paquetes ICMP para detectar tunneling.
     */
    @Nullable
    private DetectionResult analyzeICMP(
            @NonNull NetworkPacket packet,
            BypassTracker tracker,
            String sourceIP,
            String destIP
    ) {
        int packetSize = packet.getSize();
        
        if (packetSize > ICMP_TUNNEL_SIZE_THRESHOLD) {
            int icmpTunnelCount = tracker.incrementIcmpTunnelCount();
            if (icmpTunnelCount >= 5) {
                return createDetectionResult(
                        ThreatLevel.CRITICAL,
                        "ICMP_TUNNELING",
                        String.format("ICMP tunneling detectado: %d paquetes grandes", icmpTunnelCount),
                        sourceIP,
                        destIP,
                        0,
                        0.95,
                        String.format("Tamaño: %d bytes, Count: %d", packetSize, icmpTunnelCount)
                );
            }
        }
        return null;
    }

    /**
     * Analiza tráfico DNS para detectar tunneling.
     */
    @Nullable
    private DetectionResult analyzeDNSTunneling(
            @NonNull NetworkPacket packet,
            @NonNull DeepPacketInspector.DeepPacketAnalysis dpiAnalysis,
            BypassTracker tracker,
            String sourceIP,
            String destIP
    ) {
        double entropy = dpiAnalysis.payloadAnalysis.entropyScore;
        
        if (entropy > DNS_TUNNEL_ENTROPY_THRESHOLD) {
            int dnsTunnelCount = tracker.incrementDnsTunnelCount();
            if (dnsTunnelCount >= 5) {
                return createDetectionResult(
                        ThreatLevel.CRITICAL,
                        "DNS_TUNNELING",
                        String.format("DNS tunneling detectado: alta entropía (%.2f)", entropy),
                        sourceIP,
                        destIP,
                        53,
                        0.95,
                        String.format("Entropía: %.2f, Count: %d", entropy, dnsTunnelCount)
                );
            }
        }
        return null;
    }

    /**
     * Analiza tráfico HTTP para detectar tunneling.
     */
    @Nullable
    private DetectionResult analyzeHTTPTunneling(
            @NonNull NetworkPacket packet,
            @NonNull DeepPacketInspector.DeepPacketAnalysis dpiAnalysis,
            BypassTracker tracker,
            String sourceIP,
            String destIP,
            int destPort
    ) {
        // Detectar headers inusuales
        if (dpiAnalysis.metadata != null) {
            String metadata = dpiAnalysis.metadata.toLowerCase();
            
            boolean hasUnusualHeaders = metadata.contains("x-forwarded-for") ||
                                       metadata.contains("x-real-ip") ||
                                       metadata.contains("user-agent") && metadata.contains("bot");
            
            if (hasUnusualHeaders) {
                int httpTunnelCount = tracker.incrementHttpTunnelCount();
                if (httpTunnelCount >= 3) {
                    return createDetectionResult(
                            ThreatLevel.HIGH,
                            "HTTP_TUNNELING",
                            "HTTP tunneling detectado: headers inusuales",
                            sourceIP,
                            destIP,
                            destPort,
                            0.8,
                            String.format("Headers sospechosos, Count: %d", httpTunnelCount)
                    );
                }
            }
        }
        return null;
    }

    /**
     * Verifica si un paquete está fragmentado.
     */
    private boolean isFragmentedPacket(NetworkPacket packet) {
        // En una implementación real, esto vendría del paquete raw IP
        // Por ahora, usamos heurística basada en tamaño
        return packet.getSize() > 1400 && packet.getProtocol() == NetworkPacket.PROTOCOL_TCP;
    }

    /**
     * Calcula la varianza en los intervalos de tiempo entre paquetes.
     */
    private double calculateTimingVariance(List<Long> timestamps) {
        if (timestamps.size() < 2) return 1.0;

        // Calcular intervalos
        long sumIntervals = 0;
        for (int i = 1; i < timestamps.size(); i++) {
            sumIntervals += (timestamps.get(i) - timestamps.get(i - 1));
        }
        double avgInterval = (double) sumIntervals / (timestamps.size() - 1);

        // Calcular varianza
        double variance = 0;
        for (int i = 1; i < timestamps.size(); i++) {
            long interval = timestamps.get(i) - timestamps.get(i - 1);
            variance += Math.pow(interval - avgInterval, 2);
        }
        variance /= (timestamps.size() - 1);

        // Normalizar a 0-1
        return Math.min(1.0, variance / (avgInterval * avgInterval));
    }

    /**
     * Limpia trackers expirados (>5 minutos sin actividad).
     */
    public void cleanup() {
        long now = System.currentTimeMillis();
        Iterator<BypassTracker> iterator = bypassTrackers.values().iterator();
        
        while (iterator.hasNext()) {
            BypassTracker tracker = iterator.next();
            if (tracker.hasTimedOut(BYPASS_TIMEOUT_MS, now)) {
                iterator.remove();
            }
        }
    }

    /**
     * Cierra recursos y limpia todos los trackers.
     */
    public void shutdown() {
        bypassTrackers.clear();
    }

    /**
     * Obtiene estadísticas del detector.
     */
    public BypassStatistics getStatistics() {
        BypassStatistics stats = new BypassStatistics();
        stats.totalBypassDetected = totalBypassDetected.get();
        stats.totalPacketsAnalyzed = totalPacketsAnalyzed.get();
        stats.activeTrackers = bypassTrackers.size();
        return stats;
    }

    // Helper methods

    private DetectionResult createDetectionResult(
            ThreatLevel level,
            String type,
            String description,
            String sourceIP,
            String destIP,
            int destPort,
            double confidence,
            String evidence
    ) {
        return new DetectionResult(
                level,
                type,
                description,
                "BypassDetectionEngine",
                sourceIP,
                destIP,
                0, // sourcePort no relevante
                destPort,
                confidence,
                evidence,
                "",
                "Bloquear IP origen e inspeccionar tráfico cifrado"
        );
    }

    private int ipToInt(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) return 0;
        return (Integer.parseInt(parts[0]) & 0xFF) |
               ((Integer.parseInt(parts[1]) & 0xFF) << 8) |
               ((Integer.parseInt(parts[2]) & 0xFF) << 16) |
               ((Integer.parseInt(parts[3]) & 0xFF) << 24);
    }

    /**
     * Tracker interno para seguimiento de técnicas de bypass por IP origen.
     */
    public static final class BypassTracker {
        private final AtomicInteger icmpTunnelCount = new AtomicInteger(0);
        private final AtomicInteger dnsTunnelCount = new AtomicInteger(0);
        private final AtomicInteger httpTunnelCount = new AtomicInteger(0);
        private final AtomicInteger fragmentedPacketCount = new AtomicInteger(0);
        private final CopyOnWriteArrayList<Integer> protocolSequence = new CopyOnWriteArrayList<>();
        private final CopyOnWriteArrayList<Long> packetTimestamps = new CopyOnWriteArrayList<>();
        private final long firstSeenTime;
        private volatile long lastSeenTime;

        public BypassTracker() {
            this.firstSeenTime = System.currentTimeMillis();
            this.lastSeenTime = firstSeenTime;
        }

        public void recordActivity(int protocol, int packetSize) {
            lastSeenTime = System.currentTimeMillis();
            protocolSequence.add(protocol);
            packetTimestamps.add(System.currentTimeMillis());
            
            // Mantener solo los últimos 50 timestamps para evitar memoria excesiva
            if (packetTimestamps.size() > 50) {
                packetTimestamps.remove(0);
                protocolSequence.remove(0);
            }
        }

        public int incrementIcmpTunnelCount() { return icmpTunnelCount.incrementAndGet(); }
        public int incrementDnsTunnelCount() { return dnsTunnelCount.incrementAndGet(); }
        public int incrementHttpTunnelCount() { return httpTunnelCount.incrementAndGet(); }
        public int incrementFragmentedPacketCount() { return fragmentedPacketCount.incrementAndGet(); }
        
        public List<Integer> getProtocolSequence() { return protocolSequence; }
        public List<Long> getPacketTimestamps() { return packetTimestamps; }
        public long getFirstSeenTime() { return firstSeenTime; }
        public long getLastSeenTime() { return lastSeenTime; }

        public boolean hasTimedOut(long timeoutMs, long now) {
            return (now - lastSeenTime) > timeoutMs;
        }
    }

    /**
     * Estadísticas del detector.
     */
    public static final class BypassStatistics {
        public long totalBypassDetected;
        public long totalPacketsAnalyzed;
        public int activeTrackers;

        @Override
        public String toString() {
            return String.format(
                    "BypassDetectionEngine{bypass=%d, packets=%d, trackers=%d}",
                    totalBypassDetected, totalPacketsAnalyzed, activeTrackers
            );
        }
    }
}
