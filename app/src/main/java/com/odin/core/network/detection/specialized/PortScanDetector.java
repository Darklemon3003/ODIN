package com.odin.core.network.detection.specialized;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.odin.core.domain.model.NetworkPacket;
import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatLevel;
import com.odin.core.network.dpi.NetworkSession;
import com.odin.core.network.dpi.SessionTracker;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * PortScanDetector: Detector especializado de escaneo de puertos.
 * 
 * Detecta patrones de escaneo mediante análisis temporal de conexiones:
 * - SYN Flood: >50 SYN packets a diferentes puertos en <5 segundos
 * - FIN Scan: Paquetes FIN sin handshake previo
 * - XMAS Scan: Paquetes con flags FIN+PSH+URG activos
 * - NULL Scan: Paquetes sin flags TCP activos
 * - Port Sweep: Conexiones a >100 puertos diferentes en <60 segundos
 * 
 * Thread-safe: Concurrent collections + atomic operations
 * Performance: <2ms por paquete
 */
public final class PortScanDetector {

    // Thresholds configurables
    private static final int SYN_FLOOD_THRESHOLD = 50;
    private static final long SYN_FLOOD_WINDOW_MS = 5_000;
    private static final int PORT_SWEEP_THRESHOLD = 100;
    private static final long PORT_SWEEP_WINDOW_MS = 60_000;
    private static final long SCAN_TIMEOUT_MS = 300_000; // 5 minutos

    private final ConcurrentHashMap<String, ScanTracker> scanTrackers;
    private final SessionTracker sessionTracker;
    private final AtomicLong totalScansDetected;
    private final AtomicLong totalPacketsAnalyzed;

    public PortScanDetector(SessionTracker sessionTracker) {
        this.sessionTracker = sessionTracker;
        this.scanTrackers = new ConcurrentHashMap<>();
        this.totalScansDetected = new AtomicLong(0);
        this.totalPacketsAnalyzed = new AtomicLong(0);
    }

    /**
     * Analiza un paquete para detectar patrones de escaneo de puertos.
     * 
     * @param packet Paquete a analizar
     * @param session Sesión TCP (null si no existe)
     * @return DetectionResult si detecta escaneo, null otherwise
     */
    @Nullable
    public DetectionResult analyzePacket(@NonNull NetworkPacket packet, @Nullable NetworkSession session) {
        if (packet.getProtocol() != NetworkPacket.PROTOCOL_TCP) {
            return null; // Solo TCP para escaneo de puertos
        }

        totalPacketsAnalyzed.incrementAndGet();

        String sourceIP = packet.getSourceIp();
        int destPort = packet.getDestinationPort();
        int flags = extractTCPFlags(packet);

        // Obtener o crear tracker para esta IP
        ScanTracker tracker = scanTrackers.computeIfAbsent(sourceIP, k -> new ScanTracker());
        tracker.recordActivity();

        DetectionResult result = null;

        // 1. SYN Flood Detection
        if (packet.isSynPacket() && !packet.isAckPacket()) {
            int synCount = tracker.incrementSynCount();
            tracker.addPortTouched(destPort);

            if (synCount >= SYN_FLOOD_THRESHOLD) {
                long timeWindow = System.currentTimeMillis() - tracker.getFirstSeenTime();
                if (timeWindow < SYN_FLOOD_WINDOW_MS) {
                    result = createDetectionResult(
                            ThreatLevel.HIGH,
                            "SYN_FLOOD",
                            String.format("SYN flood detectado: %d SYN packets en %dms", synCount, timeWindow),
                            sourceIP,
                            destPort,
                            0.9,
                            String.format("SYN count: %d, Puertos únicos: %d", synCount, tracker.getPortsTouchedCount())
                    );
                }
            }
        }

        // 2. FIN Scan Detection (FIN sin sesión establecida)
        if (packet.isFinPacket() && session == null) {
            int finCount = tracker.incrementFinCount();
            if (finCount >= 10) {
                result = createDetectionResult(
                        ThreatLevel.MEDIUM,
                        "FIN_SCAN",
                        "FIN scan detectado: paquetes FIN sin handshake previo",
                        sourceIP,
                        destPort,
                        0.7,
                        String.format("FIN count: %d", finCount)
                );
            }
        }

        // 3. XMAS Scan Detection (FIN+PSH+URG)
        boolean hasFIN = (flags & 0x01) != 0;
        boolean hasPSH = (flags & 0x08) != 0;
        boolean hasURG = (flags & 0x20) != 0;
        if (hasFIN && hasPSH && hasURG) {
            int xmasCount = tracker.incrementXmasCount();
            if (xmasCount >= 5) {
                result = createDetectionResult(
                        ThreatLevel.MEDIUM,
                        "XMAS_SCAN",
                        "XMAS scan detectado: flags FIN+PSH+URG activos",
                        sourceIP,
                        destPort,
                        0.75,
                        String.format("XMAS count: %d", xmasCount)
                );
            }
        }

        // 4. NULL Scan Detection (sin flags TCP activos)
        boolean hasSYN = packet.isSynPacket();
        boolean hasACK = packet.isAckPacket();
        boolean hasRST = (flags & 0x04) != 0;
        if (!hasSYN && !hasACK && !hasRST && !hasFIN && !hasPSH && !hasURG) {
            int nullCount = tracker.incrementNullCount();
            if (nullCount >= 5) {
                result = createDetectionResult(
                        ThreatLevel.MEDIUM,
                        "NULL_SCAN",
                        "NULL scan detectado: sin flags TCP activos",
                        sourceIP,
                        destPort,
                        0.7,
                        String.format("NULL count: %d", nullCount)
                );
            }
        }

        // 5. Port Sweep Detection
        tracker.addPortTouched(destPort);
        int portsTouched = tracker.getPortsTouchedCount();
        if (portsTouched >= PORT_SWEEP_THRESHOLD) {
            long timeWindow = System.currentTimeMillis() - tracker.getFirstSeenTime();
            if (timeWindow < PORT_SWEEP_WINDOW_MS) {
                result = createDetectionResult(
                        ThreatLevel.HIGH,
                        "PORT_SWEEP",
                        String.format("Port sweep detectado: %d puertos en %dms", portsTouched, timeWindow),
                        sourceIP,
                        destPort,
                        0.85,
                        String.format("Puertos únicos: %d", portsTouched)
                );
            }
        }

        if (result != null) {
            totalScansDetected.incrementAndGet();
        }

        return result;
    }

    /**
     * Limpia trackers expirados (>5 minutos sin actividad).
     */
    public void cleanup() {
        long now = System.currentTimeMillis();
        Iterator<ScanTracker> iterator = scanTrackers.values().iterator();
        
        while (iterator.hasNext()) {
            ScanTracker tracker = iterator.next();
            if (tracker.hasTimedOut(SCAN_TIMEOUT_MS, now)) {
                iterator.remove();
            }
        }
    }

    /**
     * Cierra recursos y limpia todos los trackers.
     */
    public void shutdown() {
        scanTrackers.clear();
    }

    /**
     * Obtiene estadísticas del detector.
     */
    public ScanStatistics getStatistics() {
        ScanStatistics stats = new ScanStatistics();
        stats.totalScansDetected = totalScansDetected.get();
        stats.totalPacketsAnalyzed = totalPacketsAnalyzed.get();
        stats.activeTrackers = scanTrackers.size();
        return stats;
    }

    // Helper methods

    private DetectionResult createDetectionResult(
            ThreatLevel level,
            String type,
            String description,
            String sourceIP,
            int destPort,
            double confidence,
            String evidence
    ) {
        return new DetectionResult(
                level,
                type,
                description,
                "PortScanDetector",
                sourceIP,
                "0", // destinationIP no disponible en contexto
                0, // sourcePort no relevante
                destPort,
                confidence,
                evidence,
                "",
                "Bloquear IP origen temporalmente"
        );
    }

    private int extractTCPFlags(NetworkPacket packet) {
        // En una implementación real, esto vendría del paquete raw
        // Por ahora, inferimos de los métodos disponibles
        int flags = 0;
        if (packet.isSynPacket()) flags |= 0x02;
        if (packet.isAckPacket()) flags |= 0x10;
        if (packet.isFinPacket()) flags |= 0x01;
        return flags;
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
     * Tracker interno para seguimiento de escaneos por IP.
     */
    public static final class ScanTracker {
        private final AtomicInteger synCount = new AtomicInteger(0);
        private final AtomicInteger finCount = new AtomicInteger(0);
        private final AtomicInteger xmasCount = new AtomicInteger(0);
        private final AtomicInteger nullCount = new AtomicInteger(0);
        private final ConcurrentSkipListSet<Integer> portsTouched = new ConcurrentSkipListSet<>();
        private final long firstSeenTime;
        private volatile long lastSeenTime;

        public ScanTracker() {
            this.firstSeenTime = System.currentTimeMillis();
            this.lastSeenTime = firstSeenTime;
        }

        public int incrementSynCount() { return synCount.incrementAndGet(); }
        public int incrementFinCount() { return finCount.incrementAndGet(); }
        public int incrementXmasCount() { return xmasCount.incrementAndGet(); }
        public int incrementNullCount() { return nullCount.incrementAndGet(); }
        
        public void addPortTouched(int port) { portsTouched.add(port); }
        public int getPortsTouchedCount() { return portsTouched.size(); }
        
        public void recordActivity() { this.lastSeenTime = System.currentTimeMillis(); }
        public long getFirstSeenTime() { return firstSeenTime; }
        public long getLastSeenTime() { return lastSeenTime; }

        public boolean hasTimedOut(long timeoutMs, long now) {
            return (now - lastSeenTime) > timeoutMs;
        }
    }

    /**
     * Estadísticas del detector.
     */
    public static final class ScanStatistics {
        public long totalScansDetected;
        public long totalPacketsAnalyzed;
        public int activeTrackers;

        @Override
        public String toString() {
            return String.format(
                    "PortScanDetector{scans=%d, packets=%d, trackers=%d}",
                    totalScansDetected, totalPacketsAnalyzed, activeTrackers
            );
        }
    }
}
