package com.odin.core.network.detection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

/**
 * DetectionResult encapsula el resultado de un análisis de detección de amenaza.
 * 
 * Contiene toda la información necesaria para logging forense, respuesta y alertas.
 */
public final class DetectionResult implements Serializable, Comparable<DetectionResult> {

    private static final long serialVersionUID = 1L;

    private final long detectionTimestamp;
    private final ThreatLevel threatLevel;
    private final String threatType;
    private final String threatDescription;
    private final String detectorName;
    private final String sourceIP;
    private final String destinationIP;
    private final int sourcePort;
    private final int destinationPort;
    private final double confidence; // 0.0 - 1.0
    private final String evidence; // Evidencia legible
    private final String forensicsData; // Datos técnicos completos (será encriptado)
    private final String recommendedAction; // Acción recomendada
    private transient long forensicsId; // ID único para referencia en logs

    public DetectionResult(
            ThreatLevel threatLevel,
            String threatType,
            String threatDescription,
            String detectorName,
            String sourceIP,
            String destinationIP,
            int sourcePort,
            int destinationPort,
            double confidence,
            String evidence,
            String forensicsData,
            String recommendedAction
    ) {
        this.detectionTimestamp = System.currentTimeMillis();
        this.threatLevel = threatLevel;
        this.threatType = threatType;
        this.threatDescription = threatDescription;
        this.detectorName = detectorName;
        this.sourceIP = sourceIP;
        this.destinationIP = destinationIP;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.confidence = Math.max(0.0, Math.min(1.0, confidence)); // Normalizar
        this.evidence = evidence;
        this.forensicsData = forensicsData;
        this.recommendedAction = recommendedAction;
        this.forensicsId = System.nanoTime();
    }

    // Getters
    public long getDetectionTimestamp() { return detectionTimestamp; }
    public ThreatLevel getThreatLevel() { return threatLevel; }
    public String getThreatType() { return threatType; }
    public String getThreatDescription() { return threatDescription; }
    public String getDetectorName() { return detectorName; }
    public String getSourceIp() { return sourceIP; }
    public String getDestinationIp() { return destinationIP; }
    public int getSourcePort() { return sourcePort; }
    public int getDestinationPort() { return destinationPort; }
    public double getConfidence() { return confidence; }
    public String getEvidence() { return evidence; }
    public String getForensicsData() { return forensicsData; }
    public String getRecommendedAction() { return recommendedAction; }
    public long getForensicsId() { return forensicsId; }

    /**
     * Compara dos resultados de detección por severidad (para prioritization).
     */
    @Override
    public int compareTo(@NonNull DetectionResult other) {
        // Comparar por threat level primero (descendente)
        int levelComparison = Integer.compare(
                other.threatLevel.getSeverity(),
                this.threatLevel.getSeverity()
        );
        if (levelComparison != 0) return levelComparison;

        // Si mismo nivel, comparar por timestamp (descendente)
        return Long.compare(other.detectionTimestamp, this.detectionTimestamp);
    }

    /**
     * Verifica si esta amenaza requiere respuesta inmediata.
     */
    public boolean requiresImmediateResponse() {
        return threatLevel.isAtLeast(ThreatLevel.HIGH) && confidence > 0.7;
    }

    @Override
    public String toString() {
        return String.format(
                "Detection{%s: %s (%.0f%%) - %s -> %s by %s}",
                threatLevel.getLabel(),
                threatType,
                confidence * 100,
                sourceIP + ":" + sourcePort,
                destinationIP + ":" + destinationPort,
                detectorName
        );
    }

    private static String formatIP(String ip) {
        return ip;
    }
}
