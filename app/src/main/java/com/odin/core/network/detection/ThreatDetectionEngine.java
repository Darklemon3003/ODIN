package com.odin.core.network.detection;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.odin.core.domain.model.LogEvent;
import com.odin.core.domain.model.NetworkPacket;
import com.odin.core.exception.PersistenceException;
import com.odin.core.network.behavioral.AnomalyDetector;
import com.odin.core.network.behavioral.ApplicationFingerprinter;
import com.odin.core.network.behavioral.BehavioralProfiler;
import com.odin.core.network.behavioral.TrafficPatternAnalyzer;
import com.odin.core.network.behavioral.UserBehaviorAnalyzer;
import com.odin.core.network.detection.specialized.BypassDetectionEngine;
import com.odin.core.network.detection.specialized.C2Detector;
import com.odin.core.network.detection.specialized.ExfiltrationDetector;
import com.odin.core.network.detection.specialized.LateralMovementDetector;
import com.odin.core.network.detection.specialized.PortScanDetector;
import com.odin.core.network.dpi.DeepPacketInspector;
import com.odin.core.network.dpi.NetworkSession;
import com.odin.core.network.dpi.SessionTracker;
import com.odin.core.persistence.SentryCore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ThreatDetectionEngine: Orquestador central de detección de amenazas.
 * 
 * Coordina:
 * - Deep Packet Inspection (DPI)
 * - Session Tracking
 * - Detección básica de anomalías
 * - Integración de detectores especializados
 * 
 * Performance: <5ms por paquete (target industrial)
 * Thread-safe: Thread pools + concurrent collections
 */
public final class ThreatDetectionEngine {

    private static final int DETECTOR_THREADS = 4;
    private static final int QUEUE_CAPACITY = 10_000;
    private static final long QUEUE_DRAIN_TIMEOUT_MS = 5_000;
    private static final String TAG = "ThreatDetectionEngine";

    private final DeepPacketInspector dpi;
    private final SessionTracker sessionTracker;
    private final BlockingQueue<NetworkPacket> packetQueue;
    private final ExecutorService detectorPool;
    private final List<ThreatDetectionListener> listeners;
    private final PriorityQueue<DetectionResult> detectionQueue;
    private final AtomicBoolean isRunning;
    private final AtomicLong packetsProcessed;
    private final AtomicLong detectionsGenerated;
    
    // Forensics integration (Sprint 4)
    private SentryCore sentryCore;
    private Context context;

    // Specialized detectors (Sprint 2)
    private final PortScanDetector portScanDetector;
    private final ExfiltrationDetector exfiltrationDetector;
    private final C2Detector c2Detector;
    private final LateralMovementDetector lateralMovementDetector;
    private final BypassDetectionEngine bypassDetectionEngine;

    // Behavioral analysis components (Sprint 3)
    private final BehavioralProfiler behavioralProfiler;
    private final AnomalyDetector anomalyDetector;
    private final TrafficPatternAnalyzer trafficPatternAnalyzer;
    private final ApplicationFingerprinter applicationFingerprinter;
    private final UserBehaviorAnalyzer userBehaviorAnalyzer;

    /**
     * Listener para detecciones de amenazas.
     */
    public interface ThreatDetectionListener {
        void onThreatDetected(DetectionResult detection);
        void onAnomalyDetected(String anomalyType, int severity);
    }

    public ThreatDetectionEngine() throws Exception {
        this(null);
    }

    public ThreatDetectionEngine(Context context) throws Exception {
        this.context = context;
        this.dpi = new DeepPacketInspector();
        this.sessionTracker = new SessionTracker();
        this.packetQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.listeners = new CopyOnWriteArrayList<>();
        this.detectionQueue = new PriorityQueue<>();
        this.isRunning = new AtomicBoolean(false);
        this.packetsProcessed = new AtomicLong(0);
        this.detectionsGenerated = new AtomicLong(0);

        // Thread pool para análisis
        this.detectorPool = Executors.newFixedThreadPool(
                DETECTOR_THREADS,
                r -> {
                    Thread t = new Thread(r, "ThreatDetector-" + Thread.currentThread().getId());
                    t.setDaemon(true);
                    return t;
                }
        );

        // Inicializar detectores especializados (Sprint 2)
        this.portScanDetector = new PortScanDetector(sessionTracker);
        this.exfiltrationDetector = new ExfiltrationDetector();
        this.c2Detector = new C2Detector();
        this.lateralMovementDetector = new LateralMovementDetector();
        this.bypassDetectionEngine = new BypassDetectionEngine();

        // Inicializar componentes de análisis de comportamiento (Sprint 3)
        this.behavioralProfiler = new BehavioralProfiler();
        this.anomalyDetector = new AnomalyDetector();
        this.trafficPatternAnalyzer = new TrafficPatternAnalyzer();
        this.applicationFingerprinter = new ApplicationFingerprinter();
        this.userBehaviorAnalyzer = new UserBehaviorAnalyzer();
        
        // Inicializar SentryCore para forensics (Sprint 4)
        if (context != null) {
            try {
                this.sentryCore = SentryCore.getInstance(context);
            } catch (PersistenceException e) {
                Log.w(TAG, "No se pudo inicializar SentryCore para forensics", e);
            }
        }
    }

    /**
     * Inicia el engine de detección.
     */
    public void start() {
        if (!isRunning.compareAndSet(false, true)) {
            return; // Ya está corriendo
        }

        // Iniciar componentes de análisis de comportamiento (Sprint 3)
        if (behavioralProfiler != null) behavioralProfiler.start();
        if (anomalyDetector != null) anomalyDetector.start();
        if (trafficPatternAnalyzer != null) trafficPatternAnalyzer.start();
        if (applicationFingerprinter != null) applicationFingerprinter.start();
        if (userBehaviorAnalyzer != null) userBehaviorAnalyzer.start();

        // Iniciar workers de análisis
        for (int i = 0; i < DETECTOR_THREADS; i++) {
            detectorPool.submit(this::processingLoop);
        }
    }

    /**
     * Detiene el engine.
     */
    public void stop() {
        if (!isRunning.compareAndSet(true, false)) {
            return; // Ya está detenido
        }

        try {
            // Drain remaining packets
            List<NetworkPacket> remaining = new ArrayList<>();
            packetQueue.drainTo(remaining);
            remaining.forEach(this::analyzePacket);

            detectorPool.shutdown();
            if (!detectorPool.awaitTermination(10, TimeUnit.SECONDS)) {
                detectorPool.shutdownNow();
            }
            sessionTracker.shutdown();

            // Shutdown detectores especializados (Sprint 2)
            if (portScanDetector != null) portScanDetector.shutdown();
            if (exfiltrationDetector != null) exfiltrationDetector.shutdown();
            if (c2Detector != null) c2Detector.shutdown();
            if (lateralMovementDetector != null) lateralMovementDetector.shutdown();
            if (bypassDetectionEngine != null) bypassDetectionEngine.shutdown();

            // Shutdown componentes de análisis de comportamiento (Sprint 3)
            if (behavioralProfiler != null) behavioralProfiler.shutdown();
            if (anomalyDetector != null) anomalyDetector.shutdown();
            if (trafficPatternAnalyzer != null) trafficPatternAnalyzer.shutdown();
            if (applicationFingerprinter != null) applicationFingerprinter.shutdown();
            if (userBehaviorAnalyzer != null) userBehaviorAnalyzer.shutdown();
        } catch (InterruptedException e) {
            detectorPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Envía un paquete para análisis.
     * 
     * @param packet Paquete a analizar
     * @return true si fue encolado, false si queue está llena
     */
    public boolean submitPacket(@NonNull NetworkPacket packet) {
        if (!isRunning.get()) return false;
        return packetQueue.offer(packet);
    }

    /**
     * Loop de procesamiento de paquetes.
     */
    private void processingLoop() {
        while (isRunning.get()) {
            try {
                NetworkPacket packet = packetQueue.poll(100, TimeUnit.MILLISECONDS);
                if (packet != null) {
                    analyzePacket(packet);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Análisis profundo de un paquete.
     */
    private void analyzePacket(@NonNull NetworkPacket packet) {
        long startTime = System.nanoTime();

        try {
            // 1. Deep Packet Inspection
            DeepPacketInspector.DeepPacketAnalysis dpiResult = dpi.analyzePacket(packet);
            if (dpiResult == null) return;

            // 2. Session Tracking (para TCP)
            NetworkSession session = null;
            if (packet.getProtocol() == 6) {
                session = sessionTracker.processPacket(packet);
            }

            // 3. Detecciones básicas

            // --- Anomalía 1: Payload sospechoso ---
            if (dpiResult.payloadAnalysis != null) {
                if (dpiResult.payloadAnalysis.suspiciousPatterns > 0) {
                    DetectionResult detection = new DetectionResult(
                            ThreatLevel.MEDIUM,
                            "MALICIOUS_PAYLOAD",
                            "Payload contiene patrones maliciosos",
                            "ThreatDetectionEngine",
                            packet.getSourceIp(),
                            packet.getDestinationIp(),
                            packet.getSourcePort(),
                            packet.getDestinationPort(),
                            0.7,
                            dpiResult.payloadAnalysis.anomalies,
                            dpiResult.payloadAnalysis.payloadHash,
                            "Bloquear conexión y aislar app origen"
                    );
                    notifyThreatDetected(detection);
                }
            }

            // --- Anomalía 2: Entropía anormalmente alta (encriptación no autorizada) ---
            if (dpiResult.payloadAnalysis != null && 
                dpiResult.payloadAnalysis.isEncrypted && 
                !isKnownEncryptedPort(packet.getDestinationPort())) {
                
                DetectionResult detection = new DetectionResult(
                        ThreatLevel.LOW,
                        "UNEXPECTED_ENCRYPTION",
                        "Tráfico encriptado en puerto no esperado",
                        "ThreatDetectionEngine",
                        packet.getSourceIp(),
                        packet.getDestinationIp(),
                        packet.getSourcePort(),
                        packet.getDestinationPort(),
                        0.5,
                        "High entropy detected: " + String.format("%.2f", dpiResult.payloadAnalysis.entropyScore),
                        "",
                        "Investigar origen del tráfico"
                );
                notifyThreatDetected(detection);
            }

            // --- Anomalía 3: TCP Flags anómalos ---
            if (dpiResult.tcpFlags != null) {
                if (dpiResult.tcpFlags.contains("FIN") && dpiResult.tcpFlags.contains("SYN")) {
                    // FIN-SYN es anómalo
                    DetectionResult detection = new DetectionResult(
                            ThreatLevel.MEDIUM,
                            "ANOMALOUS_TCP_FLAGS",
                            "Combinación anómala de flags TCP (FIN-SYN)",
                            "ThreatDetectionEngine",
                            packet.getSourceIp(),
                            packet.getDestinationIp(),
                            packet.getSourcePort(),
                            packet.getDestinationPort(),
                            0.6,
                            "TCP Flags: " + dpiResult.tcpFlags,
                            "",
                            "Investigar posible escaneo de puertos"
                    );
                    notifyThreatDetected(detection);
                }
            }

            packetsProcessed.incrementAndGet();

            // 4. Detecciones especializadas (Sprint 2)
            DetectionResult specializedDetection = runSpecializedDetectors(packet, session, dpiResult);
            if (specializedDetection != null) {
                notifyThreatDetected(specializedDetection);
            }

            // 5. Análisis de comportamiento (Sprint 3)
            DetectionResult behavioralDetection = runBehavioralAnalysis(packet, dpiResult);
            if (behavioralDetection != null) {
                notifyThreatDetected(behavioralDetection);
            }

        } catch (Exception e) {
            // Log error pero no fallar
        }

        long duration = System.nanoTime() - startTime;
        // Si toma >5ms, registrar anomalía de performance
        if (duration > 5_000_000) { // 5ms en nanosegundos
            notifyAnomalyDetected("SLOW_PACKET_PROCESSING", 1);
        }
    }

    /**
     * Verifica si un puerto es conocidamente encriptado.
     */
    private boolean isKnownEncryptedPort(int port) {
        return port == 443 ||    // HTTPS
               port == 22 ||     // SSH
               port == 465 ||    // SMTPS
               port == 587 ||    // SMTP TLS
               port == 989 ||    // FTPS
               port == 990;      // FTPS Control
    }

    /**
     * Registra un listener.
     */
    public void addListener(@NonNull ThreatDetectionListener listener) {
        listeners.add(listener);
    }

    /**
     * Remueve un listener.
     */
    public void removeListener(@NonNull ThreatDetectionListener listener) {
        listeners.remove(listener);
    }

    private void notifyThreatDetected(DetectionResult detection) {
        detectionsGenerated.incrementAndGet();
        detectionQueue.offer(detection);

        // Log forense a SentryCore (Sprint 4)
        logDetectionToSentryCore(detection);

        for (ThreatDetectionListener listener : listeners) {
            try {
                listener.onThreatDetected(detection);
            } catch (Exception e) {
                // Ignorar errores en listeners
            }
        }
    }
    
    /**
     * Log de detección a SentryCore para forensics (Sprint 4).
     */
    private void logDetectionToSentryCore(DetectionResult detection) {
        if (sentryCore == null) return;
        
        try {
            java.util.Map<String, String> metadata = new java.util.HashMap<>();
            metadata.put("threatType", detection.getThreatType());
            metadata.put("confidence", String.valueOf(detection.getConfidence()));
            metadata.put("sourceIp", detection.getSourceIp());
            metadata.put("sourcePort", String.valueOf(detection.getSourcePort()));
            metadata.put("destIp", detection.getDestinationIp());
            metadata.put("destPort", String.valueOf(detection.getDestinationPort()));
            metadata.put("detector", detection.getDetectorName());
            metadata.put("detectionId", String.valueOf(detection.getForensicsId()));
            
            // Mapear ThreatLevel a LogEvent.Severity
            LogEvent.Severity severity;
            if (detection.getThreatLevel() == ThreatLevel.CRITICAL) {
                severity = LogEvent.Severity.CRITICAL;
            } else if (detection.getThreatLevel() == ThreatLevel.HIGH) {
                severity = LogEvent.Severity.ERROR;
            } else if (detection.getThreatLevel() == ThreatLevel.MEDIUM) {
                severity = LogEvent.Severity.WARNING;
            } else {
                severity = LogEvent.Severity.INFO;
            }
            
            LogEvent event = LogEvent.builder()
                    .timestamp(System.currentTimeMillis())
                    .source("THREAT_DETECTION_ENGINE")
                    .message(String.format(
                            "Threat detected: %s (confidence=%.0f%%)",
                            detection.getThreatType(),
                            detection.getConfidence() * 100
                    ))
                    .severity(severity)
                    .metadata(metadata)
                    .build();
            
            sentryCore.appendEvent(event);
        } catch (Exception e) {
            Log.w(TAG, "Error logging detection to SentryCore", e);
        }
    }

    private void notifyAnomalyDetected(String anomalyType, int severity) {
        for (ThreatDetectionListener listener : listeners) {
            try {
                listener.onAnomalyDetected(anomalyType, severity);
            } catch (Exception e) {
                // Ignorar errores en listeners
            }
        }
    }

    /**
     * Obtiene las detecciones de mayor prioridad.
     */
    public List<DetectionResult> getTopDetections(int limit) {
        List<DetectionResult> result = new ArrayList<>();
        for (int i = 0; i < limit && !detectionQueue.isEmpty(); i++) {
            DetectionResult detection = detectionQueue.poll();
            if (detection != null) {
                result.add(detection);
            }
        }
        return result;
    }

    /**
     * Ejecuta detectores especializados en orden de prioridad.
     */
    private DetectionResult runSpecializedDetectors(
            NetworkPacket packet,
            NetworkSession session,
            DeepPacketInspector.DeepPacketAnalysis dpiResult
    ) {
        DetectionResult result;

        // Port Scan Detection
        if (portScanDetector != null) {
            result = portScanDetector.analyzePacket(packet, session);
            if (result != null) return result;
        }

        // Exfiltration Detection
        if (exfiltrationDetector != null) {
            result = exfiltrationDetector.analyzePacket(packet, dpiResult);
            if (result != null) return result;
        }

        // C2 Detection
        if (c2Detector != null) {
            result = c2Detector.analyzePacket(packet, session, dpiResult);
            if (result != null) return result;
        }

        // Lateral Movement Detection
        if (lateralMovementDetector != null) {
            result = lateralMovementDetector.analyzePacket(packet, session);
            if (result != null) return result;
        }

        // Bypass Detection
        if (bypassDetectionEngine != null) {
            result = bypassDetectionEngine.analyzePacket(packet, dpiResult);
            if (result != null) return result;
        }

        return null;
    }

    /**
     * Ejecuta análisis de comportamiento (Sprint 3).
     */
    private DetectionResult runBehavioralAnalysis(
            NetworkPacket packet,
            DeepPacketInspector.DeepPacketAnalysis dpiResult
    ) {
        DetectionResult result;

        // Behavioral Profiling
        if (behavioralProfiler != null) {
            result = behavioralProfiler.analyzePacket(packet);
            if (result != null) return result;
        }

        // Traffic Pattern Analysis
        if (trafficPatternAnalyzer != null) {
            result = trafficPatternAnalyzer.analyzeTraffic(packet);
            if (result != null) return result;
        }

        // Application Fingerprinting
        if (applicationFingerprinter != null) {
            result = applicationFingerprinter.createFingerprint(packet, dpiResult);
            if (result != null && result.getThreatLevel().isAtLeast(ThreatLevel.HIGH)) {
                return result;
            }
        }

        return null;
    }

    /**
     * Limpia trackers expirados de detectores especializados.
     * Debe llamarse periódicamente (ej. cada 5 minutos).
     */
    public void cleanupSpecializedDetectors() {
        if (portScanDetector != null) portScanDetector.cleanup();
        if (exfiltrationDetector != null) exfiltrationDetector.cleanup();
        if (c2Detector != null) c2Detector.cleanup();
        if (lateralMovementDetector != null) lateralMovementDetector.cleanup();
        if (bypassDetectionEngine != null) bypassDetectionEngine.cleanup();

        // Cleanup componentes de análisis de comportamiento (Sprint 3)
        if (behavioralProfiler != null) behavioralProfiler.cleanup();
        if (anomalyDetector != null) anomalyDetector.cleanup();
        if (trafficPatternAnalyzer != null) trafficPatternAnalyzer.cleanup();
        if (applicationFingerprinter != null) applicationFingerprinter.cleanup();
        if (userBehaviorAnalyzer != null) userBehaviorAnalyzer.cleanup();
    }

    /**
     * Obtiene estadísticas del engine.
     */
    public EngineStatistics getStatistics() {
        EngineStatistics stats = new EngineStatistics();
        stats.packetsProcessed = packetsProcessed.get();
        stats.detectionsGenerated = detectionsGenerated.get();
        stats.queueSize = packetQueue.size();
        stats.activeSessions = sessionTracker.getActiveSessionCount();
        return stats;
    }

    /**
     * Estadísticas del engine.
     */
    public static final class EngineStatistics {
        public long packetsProcessed;
        public long detectionsGenerated;
        public int queueSize;
        public int activeSessions;

        @Override
        public String toString() {
            return String.format(
                    "ThreatEngine{packets=%d, detections=%d, queue=%d, sessions=%d}",
                    packetsProcessed, detectionsGenerated, queueSize, activeSessions
            );
        }
    }
}
