package com.odin.core.network.behavioral;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.odin.core.domain.model.NetworkPacket;
import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatLevel;
import com.odin.core.network.dpi.DeepPacketInspector;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ApplicationFingerprinter: Huella digital de aplicaciones para identificar comportamiento anómalo.
 * 
 * Responsabilidades:
 * - TLS Fingerprinting: Analizar handshake TLS para identificar aplicación
 * - HTTP Fingerprinting: Analizar headers HTTP para identificar aplicación
 * - Protocol Behavior Fingerprinting: Analizar comportamiento de protocolos
 * - Payload Pattern Fingerprinting: Analizar patrones en payload
 * - Fingerprint Matching: Comparar huellas con base de datos conocida
 * 
 * Performance: <3ms por paquete
 * Thread-safe: Concurrent collections + atomic operations
 */
public final class ApplicationFingerprinter {

    // Thresholds configurables
    private static final double FINGERPRINT_SIMILARITY_THRESHOLD = 0.8;
    private static final long FINGERPRINT_RETENTION_MS = 90 * 24 * 60 * 60 * 1000L; // 90 días
    private static final int MIN_SAMPLES_FOR_FINGERPRINT = 20;

    // Estado interno
    private final ConcurrentHashMap<Integer, ApplicationFingerprint> applicationFingerprints;
    private final AtomicBoolean isRunning;
    private final AtomicLong totalFingerprintsCreated;
    private final AtomicLong totalFingerprintsUpdated;
    private final AtomicLong totalAnomaliesDetected;

    /**
     * Constructor.
     */
    public ApplicationFingerprinter() {
        this.applicationFingerprints = new ConcurrentHashMap<>();
        this.isRunning = new AtomicBoolean(false);
        this.totalFingerprintsCreated = new AtomicLong(0);
        this.totalFingerprintsUpdated = new AtomicLong(0);
        this.totalAnomaliesDetected = new AtomicLong(0);
    }

    /**
     * Crea huella digital de una aplicación.
     * 
     * @param packet Paquete a analizar
     * @param dpiAnalysis Análisis DPI del paquete
     * @return DetectionResult si detecta anomalía en huella, null en caso contrario
     */
    @Nullable
    public DetectionResult createFingerprint(
            @NonNull NetworkPacket packet,
            @Nullable DeepPacketInspector.DeepPacketAnalysis dpiAnalysis
    ) {
        if (!isRunning.get()) return null;

        int appUID = packet.getAppUid();
        if (appUID == 0) return null; // No hay UID válida

        // Crear huella actual del paquete
        ApplicationFingerprint currentFingerprint = extractFingerprint(packet, dpiAnalysis);

        // Obtener huella existente
        ApplicationFingerprint existingFingerprint = applicationFingerprints.get(appUID);

        if (existingFingerprint == null) {
            // Primera huella para esta aplicación
            applicationFingerprints.put(appUID, currentFingerprint);
            totalFingerprintsCreated.incrementAndGet();
            return null;
        }

        // Comparar huellas
        double similarity = compareFingerprints(existingFingerprint, currentFingerprint);

        if (similarity < FINGERPRINT_SIMILARITY_THRESHOLD) {
            // Huella anómala detectada
            totalAnomaliesDetected.incrementAndGet();
            
            // Actualizar huella si la nueva es más reciente
            updateFingerprint(appUID, currentFingerprint);
            
            return createFingerprintAnomalyResult(packet, appUID, similarity);
        }

        // Actualizar huella gradualmente
        updateFingerprint(appUID, currentFingerprint);
        totalFingerprintsUpdated.incrementAndGet();

        return null;
    }

    /**
     * Extrae huella digital de un paquete.
     */
    @NonNull
    private ApplicationFingerprint extractFingerprint(
            @NonNull NetworkPacket packet,
            @Nullable DeepPacketInspector.DeepPacketAnalysis dpiAnalysis
    ) {
        ApplicationFingerprint fingerprint = new ApplicationFingerprint();

        // TLS Fingerprint
        if (dpiAnalysis != null && dpiAnalysis.applicationLayer != null) {
            if (dpiAnalysis.applicationLayer.contains("TLS") || 
                dpiAnalysis.applicationLayer.contains("SSL")) {
                fingerprint.tlsFingerprint = extractTLSFingerprint(dpiAnalysis);
            }
        }

        // HTTP Fingerprint
        if (dpiAnalysis != null && dpiAnalysis.applicationLayer != null) {
            if (dpiAnalysis.applicationLayer.contains("HTTP")) {
                fingerprint.httpFingerprint = extractHTTPFingerprint(dpiAnalysis);
            }
        }

        // Protocol Behavior
        fingerprint.protocolBehavior = extractProtocolBehavior(packet, dpiAnalysis);

        // Payload Pattern
        if (dpiAnalysis != null && dpiAnalysis.payloadAnalysis != null) {
            fingerprint.payloadPattern = extractPayloadPattern(dpiAnalysis.payloadAnalysis);
        }

        // Puertos y destinos típicos
        fingerprint.typicalPorts.add(packet.getDestinationPort());
        fingerprint.typicalDestinations.add(packet.getDestinationIp());

        fingerprint.lastUpdatedTime = System.currentTimeMillis();

        return fingerprint;
    }

    /**
     * Extrae huella TLS.
     */
    @NonNull
    private String extractTLSFingerprint(@NonNull DeepPacketInspector.DeepPacketAnalysis dpiAnalysis) {
        // En una implementación real, esto analizaría cipher suites, extensions, etc.
        // Por ahora, usamos metadata del DPI
        return "TLS:" + dpiAnalysis.applicationLayer + ":" + dpiAnalysis.metadata;
    }

    /**
     * Extrae huella HTTP.
     */
    @NonNull
    private String extractHTTPFingerprint(@NonNull DeepPacketInspector.DeepPacketAnalysis dpiAnalysis) {
        // En una implementación real, esto analizaría User-Agent, headers, etc.
        // Por ahora, usamos metadata del DPI
        return "HTTP:" + dpiAnalysis.applicationLayer + ":" + dpiAnalysis.metadata;
    }

    /**
     * Extrae comportamiento de protocolo.
     */
    @NonNull
    private String extractProtocolBehavior(
            @NonNull NetworkPacket packet,
            @Nullable DeepPacketInspector.DeepPacketAnalysis dpiAnalysis
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("PROTO:").append(packet.getProtocol());
        
        if (dpiAnalysis != null && dpiAnalysis.tcpFlags != null) {
            sb.append(":FLAGS:").append(dpiAnalysis.tcpFlags);
        }
        
        return sb.toString();
    }

    /**
     * Extrae patrón de payload.
     */
    @NonNull
    private String extractPayloadPattern(@NonNull DeepPacketInspector.PayloadAnalysis payloadAnalysis) {
        return String.format("PAYLOAD:entropy=%.2f:encrypted=%s:patterns=%d",
            payloadAnalysis.entropyScore,
            payloadAnalysis.isEncrypted,
            payloadAnalysis.suspiciousPatterns
        );
    }

    /**
     * Compara dos huellas digitales.
     */
    private double compareFingerprints(
            @NonNull ApplicationFingerprint fp1,
            @NonNull ApplicationFingerprint fp2
    ) {
        double similarity = 0.0;
        int factors = 0;

        // Comparar TLS fingerprint
        if (fp1.tlsFingerprint != null && fp2.tlsFingerprint != null) {
            if (fp1.tlsFingerprint.equals(fp2.tlsFingerprint)) {
                similarity += 1.0;
            }
            factors++;
        }

        // Comparar HTTP fingerprint
        if (fp1.httpFingerprint != null && fp2.httpFingerprint != null) {
            if (fp1.httpFingerprint.equals(fp2.httpFingerprint)) {
                similarity += 1.0;
            }
            factors++;
        }

        // Comparar protocol behavior
        if (fp1.protocolBehavior != null && fp2.protocolBehavior != null) {
            if (fp1.protocolBehavior.equals(fp2.protocolBehavior)) {
                similarity += 1.0;
            }
            factors++;
        }

        // Comparar payload pattern
        if (fp1.payloadPattern != null && fp2.payloadPattern != null) {
            if (fp1.payloadPattern.equals(fp2.payloadPattern)) {
                similarity += 1.0;
            }
            factors++;
        }

        // Comparar puertos típicos
        int portIntersection = 0;
        for (Integer port : fp2.typicalPorts) {
            if (fp1.typicalPorts.contains(port)) {
                portIntersection++;
            }
        }
        int portUnion = fp1.typicalPorts.size() + fp2.typicalPorts.size() - portIntersection;
        if (portUnion > 0) {
            similarity += (double) portIntersection / portUnion;
            factors++;
        }

        // Normalizar
        return factors > 0 ? similarity / factors : 0.0;
    }

    /**
     * Detecta anomalía en huella de aplicación.
     */
    @Nullable
    public DetectionResult detectFingerprintAnomaly(int appUID, @NonNull ApplicationFingerprint currentFingerprint) {
        ApplicationFingerprint existingFingerprint = applicationFingerprints.get(appUID);
        if (existingFingerprint == null) return null;

        double similarity = compareFingerprints(existingFingerprint, currentFingerprint);
        if (similarity < FINGERPRINT_SIMILARITY_THRESHOLD) {
            return createFingerprintAnomalyResult(null, appUID, similarity);
        }

        return null;
    }

    /**
     * Actualiza huella de aplicación.
     */
    private void updateFingerprint(int appUID, @NonNull ApplicationFingerprint newFingerprint) {
        ApplicationFingerprint existing = applicationFingerprints.get(appUID);
        if (existing == null) {
            applicationFingerprints.put(appUID, newFingerprint);
            return;
        }

        // Actualizar gradualmente (merge de datos)
        if (newFingerprint.tlsFingerprint != null) {
            existing.tlsFingerprint = newFingerprint.tlsFingerprint;
        }
        if (newFingerprint.httpFingerprint != null) {
            existing.httpFingerprint = newFingerprint.httpFingerprint;
        }
        if (newFingerprint.protocolBehavior != null) {
            existing.protocolBehavior = newFingerprint.protocolBehavior;
        }
        if (newFingerprint.payloadPattern != null) {
            existing.payloadPattern = newFingerprint.payloadPattern;
        }
        existing.typicalPorts.addAll(newFingerprint.typicalPorts);
        existing.typicalDestinations.addAll(newFingerprint.typicalDestinations);
        existing.lastUpdatedTime = System.currentTimeMillis();
    }

    /**
     * Obtiene huella de aplicación.
     */
    @Nullable
    public ApplicationFingerprint getApplicationFingerprint(int appUID) {
        return applicationFingerprints.get(appUID);
    }

    /**
     * Crea DetectionResult para anomalía de huella.
     */
    private DetectionResult createFingerprintAnomalyResult(
            @Nullable NetworkPacket packet,
            int appUID,
            double similarity
    ) {
        String description = String.format(
            "Huella digital anómala detectada para UID %d: similitud %.2f",
            appUID, similarity
        );

        return new DetectionResult(
            ThreatLevel.HIGH,
            "FINGERPRINT_ANOMALY",
            description,
            "ApplicationFingerprinter",
            packet != null ? packet.getSourceIp() : "0",
            packet != null ? packet.getDestinationIp() : "0",
            packet != null ? packet.getSourcePort() : 0,
            packet != null ? packet.getDestinationPort() : 0,
            1.0 - similarity,
            "Similarity: " + String.format("%.2f", similarity),
            "AppUID: " + appUID,
            "Investigar posible aplicación disfrazada"
        );
    }

    /**
     * Limpia huellas antiguas.
     * Debe llamarse periódicamente (ej. cada día).
     */
    public void cleanup() {
        long now = System.currentTimeMillis();
        applicationFingerprints.entrySet().removeIf(entry -> {
            ApplicationFingerprint fingerprint = entry.getValue();
            if ((now - fingerprint.lastUpdatedTime) > FINGERPRINT_RETENTION_MS) {
                return true;
            }
            return false;
        });
    }

    /**
     * Inicia el fingerprinter.
     */
    public void start() {
        isRunning.set(true);
    }

    /**
     * Detiene el fingerprinter y libera recursos.
     */
    public void shutdown() {
        isRunning.set(false);
        applicationFingerprints.clear();
    }

    /**
     * Obtiene estadísticas del fingerprinter.
     */
    public FingerprinterStatistics getStatistics() {
        FingerprinterStatistics stats = new FingerprinterStatistics();
        stats.activeFingerprints = applicationFingerprints.size();
        stats.totalFingerprintsCreated = totalFingerprintsCreated.get();
        stats.totalFingerprintsUpdated = totalFingerprintsUpdated.get();
        stats.totalAnomaliesDetected = totalAnomaliesDetected.get();
        return stats;
    }

    /**
     * Huella digital de aplicación.
     */
    public static final class ApplicationFingerprint {
        public volatile String tlsFingerprint;
        public volatile String httpFingerprint;
        public volatile String protocolBehavior;
        public volatile String payloadPattern;
        public final ConcurrentSkipListSet<Integer> typicalPorts = new ConcurrentSkipListSet<>();
        public final ConcurrentSkipListSet<String> typicalDestinations = new ConcurrentSkipListSet<>();
        public final long fingerprintCreatedTime;
        public volatile long lastUpdatedTime;
        public volatile boolean isVerified = false;

        public ApplicationFingerprint() {
            this.fingerprintCreatedTime = System.currentTimeMillis();
            this.lastUpdatedTime = fingerprintCreatedTime;
        }
    }

    /**
     * Estadísticas del fingerprinter.
     */
    public static final class FingerprinterStatistics {
        public int activeFingerprints;
        public long totalFingerprintsCreated;
        public long totalFingerprintsUpdated;
        public long totalAnomaliesDetected;

        @Override
        public String toString() {
            return String.format(
                "ApplicationFingerprinter{fingerprints=%d, created=%d, updated=%d, anomalies=%d}",
                activeFingerprints, totalFingerprintsCreated, totalFingerprintsUpdated, totalAnomaliesDetected
            );
        }
    }
}
