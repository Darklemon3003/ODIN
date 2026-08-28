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
 * ExfiltrationDetector: Detector especializado de exfiltración de datos.
 * 
 * Detecta patrones de exfiltración mediante análisis de volumen y comportamiento:
 * - Volume Anomaly: >10MB de datos salientes en <1 minuto a misma IP
 * - High Frequency Small Packets: >1000 paquetes pequeños (<1KB) en <30 segundos
 * - Unusual Port Data Transfer: Datos grandes (>5MB) en puertos no estándar
 * - Time-Based Anomaly: Transferencia en horas no habituales (2 AM - 5 AM)
 * - Entropy-Based Detection: Payloads con entropía >0.8 (posible cifrado)
 * 
 * Thread-safe: Concurrent collections + atomic operations
 * Performance: <2ms por paquete
 */
public final class ExfiltrationDetector {

    // Thresholds configurables
    private static final long VOLUME_THRESHOLD_BYTES = 10_485_760; // 10 MB
    private static final long VOLUME_WINDOW_MS = 60_000; // 1 minuto
    private static final int SMALL_PACKET_THRESHOLD = 1000;
    private static final long SMALL_PACKET_WINDOW_MS = 30_000; // 30 segundos
    private static final int SMALL_PACKET_SIZE = 1024; // 1 KB
    private static final long UNUSUAL_PORT_THRESHOLD = 5_242_880; // 5 MB
    private static final int UNUSUAL_PORT_MIN = 1024;
    private static final double HIGH_ENTROPY_THRESHOLD = 0.8;
    private static final long EXFIL_TIMEOUT_MS = 300_000; // 5 minutos

    private final ConcurrentHashMap<String, ExfilTracker> exfilTrackers;
    private final AtomicLong totalExfilDetected;
    private final AtomicLong totalPacketsAnalyzed;

    public ExfiltrationDetector() {
        this.exfilTrackers = new ConcurrentHashMap<>();
        this.totalExfilDetected = new AtomicLong(0);
        this.totalPacketsAnalyzed = new AtomicLong(0);
    }

    /**
     * Analiza un paquete para detectar patrones de exfiltración.
     * 
     * @param packet Paquete a analizar
     * @param dpiAnalysis Análisis DPI del paquete
     * @return DetectionResult si detecta exfiltración, null otherwise
     */
    @Nullable
    public DetectionResult analyzePacket(
            @NonNull NetworkPacket packet,
            @Nullable DeepPacketInspector.DeepPacketAnalysis dpiAnalysis
    ) {
        totalPacketsAnalyzed.incrementAndGet();

        String destIP = packet.getDestinationIp();
        int destPort = packet.getDestinationPort();
        int packetSize = packet.getSize();
        double entropy = 0.0;

        // Extraer entropía del análisis DPI si está disponible
        if (dpiAnalysis != null && dpiAnalysis.payloadAnalysis != null) {
            entropy = dpiAnalysis.payloadAnalysis.entropyScore;
        }

        // Obtener o crear tracker para esta IP destino
        ExfilTracker tracker = exfilTrackers.computeIfAbsent(destIP, k -> new ExfilTracker());
        tracker.recordTransfer(packetSize, entropy);

        DetectionResult result = null;

        // 1. Volume Anomaly Detection
        long totalBytes = tracker.getBytesTransferred();
        if (totalBytes >= VOLUME_THRESHOLD_BYTES) {
            long timeWindow = System.currentTimeMillis() - tracker.getFirstTransferTime();
            if (timeWindow < VOLUME_WINDOW_MS) {
                result = createDetectionResult(
                        ThreatLevel.HIGH,
                        "VOLUME_ANOMALY",
                        String.format("Volumen anormal: %d MB en %dms", totalBytes / 1_048_576, timeWindow),
                        destIP,
                        destPort,
                        0.9,
                        String.format("Bytes transferidos: %d, Paquetes: %d", totalBytes, tracker.getPacketCount())
                );
            }
        }

        // 2. High Frequency Small Packets
        if (packetSize < SMALL_PACKET_SIZE) {
            int smallPacketCount = tracker.getSmallPacketCount();
            if (smallPacketCount >= SMALL_PACKET_THRESHOLD) {
                long timeWindow = System.currentTimeMillis() - tracker.getFirstTransferTime();
                if (timeWindow < SMALL_PACKET_WINDOW_MS) {
                    result = createDetectionResult(
                            ThreatLevel.MEDIUM,
                            "HIGH_FREQUENCY_SMALL_PACKETS",
                            String.format("Alta frecuencia de paquetes pequeños: %d en %dms", smallPacketCount, timeWindow),
                            destIP,
                            destPort,
                            0.75,
                            String.format("Small packets: %d, Total bytes: %d", smallPacketCount, totalBytes)
                    );
                }
            }
        }

        // 3. Unusual Port Data Transfer
        if (destPort >= UNUSUAL_PORT_MIN && totalBytes >= UNUSUAL_PORT_THRESHOLD) {
            result = createDetectionResult(
                    ThreatLevel.MEDIUM,
                    "UNUSUAL_PORT_TRANSFER",
                    String.format("Transferencia grande en puerto no estándar: puerto %d, %d MB", destPort, totalBytes / 1_048_576),
                    destIP,
                    destPort,
                    0.7,
                    String.format("Puerto: %d, Bytes: %d", destPort, totalBytes)
            );
        }

        // 4. Time-Based Anomaly (2 AM - 5 AM)
        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        if (hour >= 2 && hour <= 5 && totalBytes > 1_048_576) { // >1MB en horas no habituales
            result = createDetectionResult(
                    ThreatLevel.LOW,
                    "TIME_BASED_ANOMALY",
                    String.format("Transferencia en hora no habitual: %d:00, %d MB", hour, totalBytes / 1_048_576),
                    destIP,
                    destPort,
                    0.5,
                    String.format("Hora: %d:00, Bytes: %d", hour, totalBytes)
            );
        }

        // 5. Entropy-Based Detection
        double avgEntropy = tracker.getAvgEntropy();
        if (avgEntropy > HIGH_ENTROPY_THRESHOLD && totalBytes > 1_048_576) {
            result = createDetectionResult(
                    ThreatLevel.HIGH,
                    "HIGH_ENTROPY_TRANSFER",
                    String.format("Transferencia con alta entropía: %.2f, %d MB", avgEntropy, totalBytes / 1_048_576),
                    destIP,
                    destPort,
                    0.85,
                    String.format("Entropía promedio: %.2f, Bytes: %d", avgEntropy, totalBytes)
            );
        }

        if (result != null) {
            totalExfilDetected.incrementAndGet();
        }

        return result;
    }

    /**
     * Limpia trackers expirados (>5 minutos sin actividad).
     */
    public void cleanup() {
        long now = System.currentTimeMillis();
        Iterator<ExfilTracker> iterator = exfilTrackers.values().iterator();
        
        while (iterator.hasNext()) {
            ExfilTracker tracker = iterator.next();
            if (tracker.hasTimedOut(EXFIL_TIMEOUT_MS, now)) {
                iterator.remove();
            }
        }
    }

    /**
     * Cierra recursos y limpia todos los trackers.
     */
    public void shutdown() {
        exfilTrackers.clear();
    }

    /**
     * Obtiene estadísticas del detector.
     */
    public ExfilStatistics getStatistics() {
        ExfilStatistics stats = new ExfilStatistics();
        stats.totalExfilDetected = totalExfilDetected.get();
        stats.totalPacketsAnalyzed = totalPacketsAnalyzed.get();
        stats.activeTrackers = exfilTrackers.size();
        return stats;
    }

    // Helper methods

    private DetectionResult createDetectionResult(
            ThreatLevel level,
            String type,
            String description,
            String destIP,
            int destPort,
            double confidence,
            String evidence
    ) {
        return new DetectionResult(
                level,
                type,
                description,
                "ExfiltrationDetector",
                "0", // sourceIP no relevante
                destIP,
                0, // sourcePort no relevante
                destPort,
                confidence,
                evidence,
                "",
                "Bloquear conexión y notificar administrador"
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
     * Tracker interno para seguimiento de exfiltración por IP destino.
     */
    public static final class ExfilTracker {
        private final AtomicLong bytesTransferred = new AtomicLong(0);
        private final AtomicInteger packetCount = new AtomicInteger(0);
        private final AtomicLong smallPacketCount = new AtomicLong(0);
        private final CopyOnWriteArrayList<Long> transferTimestamps = new CopyOnWriteArrayList<>();
        private volatile double avgEntropy = 0.0;
        private final long firstTransferTime;
        private volatile long lastTransferTime;

        public ExfilTracker() {
            this.firstTransferTime = System.currentTimeMillis();
            this.lastTransferTime = firstTransferTime;
        }

        public void recordTransfer(int bytes, double entropy) {
            bytesTransferred.addAndGet(bytes);
            packetCount.incrementAndGet();
            if (bytes < SMALL_PACKET_SIZE) {
                smallPacketCount.incrementAndGet();
            }
            transferTimestamps.add(System.currentTimeMillis());
            lastTransferTime = System.currentTimeMillis();
            
            // Calcular avg entropy (moving average)
            avgEntropy = (avgEntropy * 0.9) + (entropy * 0.1);
        }

        public long getBytesTransferred() { return bytesTransferred.get(); }
        public int getPacketCount() { return packetCount.get(); }
        public int getSmallPacketCount() { return (int) smallPacketCount.get(); }
        public double getAvgEntropy() { return avgEntropy; }
        public long getFirstTransferTime() { return firstTransferTime; }
        public long getLastTransferTime() { return lastTransferTime; }

        public boolean hasTimedOut(long timeoutMs, long now) {
            return (now - lastTransferTime) > timeoutMs;
        }
    }

    /**
     * Estadísticas del detector.
     */
    public static final class ExfilStatistics {
        public long totalExfilDetected;
        public long totalPacketsAnalyzed;
        public int activeTrackers;

        @Override
        public String toString() {
            return String.format(
                    "ExfiltrationDetector{exfil=%d, packets=%d, trackers=%d}",
                    totalExfilDetected, totalPacketsAnalyzed, activeTrackers
            );
        }
    }
}
