package com.odin.core.network.behavioral;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.odin.core.domain.model.NetworkPacket;
import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TrafficPatternAnalyzer: Análisis de patrones de tráfico a largo plazo.
 * 
 * Responsabilidades:
 * - Time Series Analysis de tráfico
 * - Peak Detection para detectar picos inusuales
 * - Trend Analysis para detectar tendencias crecientes sospechosas
 * - Periodicity Detection para detectar patrones periódicos (beaconing)
 * - Correlation Analysis entre métricas
 * 
 * Performance: <3ms por paquete
 * Thread-safe: Concurrent collections + atomic operations
 */
public final class TrafficPatternAnalyzer {

    // Thresholds configurables
    private static final double PEAK_MULTIPLIER = 3.0;
    private static final double TREND_THRESHOLD = 0.5;
    private static final double PERIODICITY_TOLERANCE = 0.1;
    private static final long SAMPLE_RETENTION_MS = 7 * 24 * 60 * 60 * 1000L; // 7 días
    private static final int MIN_SAMPLES_FOR_ANALYSIS = 50;

    // Estado interno
    private final ConcurrentHashMap<String, TrafficPattern> trafficPatterns;
    private final AnomalyDetector anomalyDetector;
    private final AtomicBoolean isRunning;
    private final AtomicLong totalPatternsAnalyzed;
    private final AtomicLong totalAnomaliesDetected;

    /**
     * Constructor.
     */
    public TrafficPatternAnalyzer() {
        this.trafficPatterns = new ConcurrentHashMap<>();
        this.anomalyDetector = new AnomalyDetector();
        this.isRunning = new AtomicBoolean(false);
        this.totalPatternsAnalyzed = new AtomicLong(0);
        this.totalAnomaliesDetected = new AtomicLong(0);
    }

    /**
     * Analiza tráfico y retorna DetectionResult si detecta patrón sospechoso.
     * 
     * @param packet Paquete a analizar
     * @return DetectionResult si detecta patrón sospechoso, null en caso contrario
     */
    @Nullable
    public DetectionResult analyzeTraffic(@NonNull NetworkPacket packet) {
        if (!isRunning.get()) return null;

        // Crear clave basada en source/destination IP
        String key = generateKey(packet);

        // Actualizar patrón de tráfico
        updateTrafficPattern(key, packet);

        TrafficPattern pattern = trafficPatterns.get(key);
        if (pattern == null || pattern.getSamples().size() < MIN_SAMPLES_FOR_ANALYSIS) {
            return null;
        }

        totalPatternsAnalyzed.incrementAndGet();

        // Detectar pico inusual
        if (detectPeak(key, packet.getSize())) {
            totalAnomaliesDetected.incrementAndGet();
            return createPeakDetectionResult(packet, key);
        }

        // Detectar tendencia sospechosa
        double trend = calculateTrend(key);
        if (Math.abs(trend) > TREND_THRESHOLD) {
            totalAnomaliesDetected.incrementAndGet();
            return createTrendDetectionResult(packet, key, trend);
        }

        // Detectar periodicidad (beaconing)
        double periodicity = detectPeriodicity(key);
        if (periodicity > 0.8) {
            totalAnomaliesDetected.incrementAndGet();
            return createPeriodicityDetectionResult(packet, key, periodicity);
        }

        return null;
    }

    /**
     * Detecta pico inusual en volumen.
     */
    private boolean detectPeak(@NonNull String key, double currentVolume) {
        TrafficPattern pattern = trafficPatterns.get(key);
        if (pattern == null) return false;

        double baseline = pattern.getBaselineVolume();
        if (baseline == 0) return false;

        return currentVolume > (baseline * PEAK_MULTIPLIER);
    }

    /**
     * Calcula tendencia del tráfico.
     */
    private double calculateTrend(@NonNull String key) {
        TrafficPattern pattern = trafficPatterns.get(key);
        if (pattern == null || pattern.getSamples().size() < MIN_SAMPLES_FOR_ANALYSIS) return 0.0;

        List<TrafficSample> samples = new ArrayList<>(pattern.getSamples());
        if (samples.size() < 2) return 0.0;

        // Calcular pendiente usando regresión lineal simple
        double n = samples.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < samples.size(); i++) {
            double x = i;
            double y = samples.get(i).volume;
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        
        pattern.setTrendSlope(slope);
        return slope;
    }

    /**
     * Detecta periodicidad en el tráfico (beaconing).
     */
    private double detectPeriodicity(@NonNull String key) {
        TrafficPattern pattern = trafficPatterns.get(key);
        if (pattern == null || pattern.getSamples().size() < MIN_SAMPLES_FOR_ANALYSIS) return 0.0;

        List<TrafficSample> samples = new ArrayList<>(pattern.getSamples());
        if (samples.size() < 10) return 0.0;

        // Calcular intervalos entre muestras
        List<Long> intervals = new ArrayList<>();
        for (int i = 1; i < samples.size(); i++) {
            long interval = samples.get(i).timestamp - samples.get(i - 1).timestamp;
            intervals.add(interval);
        }

        if (intervals.isEmpty()) return 0.0;

        // Calcular desviación estándar de intervalos
        double mean = intervals.stream().mapToLong(Long::longValue).average().orElse(0);
        double variance = intervals.stream()
            .mapToLong(Long::longValue)
            .mapToDouble(v -> Math.pow(v - mean, 2))
            .average()
            .orElse(0);
        double stdDev = Math.sqrt(variance);

        // Periodicidad alta si stdDev es bajo (intervalos consistentes)
        double coefficientOfVariation = mean > 0 ? stdDev / mean : 1.0;
        double periodicity = 1.0 - coefficientOfVariation;
        
        pattern.setPeriodicity(periodicity);
        return periodicity;
    }

    /**
     * Actualiza patrón de tráfico.
     */
    private void updateTrafficPattern(@NonNull String key, @NonNull NetworkPacket packet) {
        TrafficPattern pattern = trafficPatterns.computeIfAbsent(
            key,
            k -> new TrafficPattern()
        );

        TrafficSample sample = new TrafficSample(
            System.currentTimeMillis(),
            packet.getSize(),
            1, // packetCount
            1  // connectionCount
        );

        pattern.addSample(sample);

        // Actualizar baseline
        updateBaseline(pattern);
    }

    /**
     * Actualiza baseline de volumen.
     */
    private void updateBaseline(@NonNull TrafficPattern pattern) {
        List<TrafficSample> samples = pattern.getSamples();
        if (samples.isEmpty()) return;

        double sum = 0;
        for (TrafficSample sample : samples) {
            sum += sample.volume;
        }
        pattern.setBaselineVolume(sum / samples.size());
    }

    /**
     * Genera clave para patrón de tráfico.
     */
    private String generateKey(@NonNull NetworkPacket packet) {
        return packet.getSourceIp() + "->" + packet.getDestinationIp();
    }

    /**
     * Crea DetectionResult para pico detectado.
     */
    private DetectionResult createPeakDetectionResult(@NonNull NetworkPacket packet, @NonNull String key) {
        TrafficPattern pattern = trafficPatterns.get(key);
        double baseline = pattern != null ? pattern.getBaselineVolume() : 0;
        double ratio = baseline > 0 ? packet.getSize() / baseline : 0;

        return new DetectionResult(
            ThreatLevel.HIGH,
            "TRAFFIC_PEAK",
            String.format("Pico inusual de tráfico detectado: %.2fx baseline", ratio),
            "TrafficPatternAnalyzer",
            packet.getSourceIp(),
            packet.getDestinationIp(),
            packet.getSourcePort(),
            packet.getDestinationPort(),
            0.8,
            String.format("Volume: %d, Baseline: %.2f", packet.getSize(), baseline),
            "Key: " + key,
            "Investigar origen del pico de tráfico"
        );
    }

    /**
     * Crea DetectionResult para tendencia detectada.
     */
    private DetectionResult createTrendDetectionResult(@NonNull NetworkPacket packet, @NonNull String key, double trend) {
        return new DetectionResult(
            ThreatLevel.MEDIUM,
            "TRAFFIC_TREND",
            String.format("Tendencia sospechosa detectada: slope=%.2f", trend),
            "TrafficPatternAnalyzer",
            packet.getSourceIp(),
            packet.getDestinationIp(),
            packet.getSourcePort(),
            packet.getDestinationPort(),
            0.7,
            String.format("Trend slope: %.2f", trend),
            "Key: " + key,
            "Investigar tendencia de tráfico"
        );
    }

    /**
     * Crea DetectionResult para periodicidad detectada.
     */
    private DetectionResult createPeriodicityDetectionResult(@NonNull NetworkPacket packet, @NonNull String key, double periodicity) {
        return new DetectionResult(
            ThreatLevel.HIGH,
            "BEACONING_PATTERN",
            String.format("Patrón periódico detectado (beaconing): %.2f", periodicity),
            "TrafficPatternAnalyzer",
            packet.getSourceIp(),
            packet.getDestinationIp(),
            packet.getSourcePort(),
            packet.getDestinationPort(),
            0.85,
            String.format("Periodicity: %.2f", periodicity),
            "Key: " + key,
            "Investigar posible comunicación C2"
        );
    }

    /**
     * Limpia patrones antiguos.
     * Debe llamarse periódicamente (ej. cada hora).
     */
    public void cleanup() {
        long now = System.currentTimeMillis();
        trafficPatterns.entrySet().removeIf(entry -> {
            TrafficPattern pattern = entry.getValue();
            if ((now - pattern.getLastSeenTime()) > SAMPLE_RETENTION_MS) {
                return true;
            }
            // Limpiar muestras antiguas
            pattern.cleanupOldSamples(now - SAMPLE_RETENTION_MS);
            return false;
        });
    }

    /**
     * Inicia el analyzer.
     */
    public void start() {
        isRunning.set(true);
        anomalyDetector.start();
    }

    /**
     * Detiene el analyzer y libera recursos.
     */
    public void shutdown() {
        isRunning.set(false);
        anomalyDetector.shutdown();
        trafficPatterns.clear();
    }

    /**
     * Obtiene estadísticas del analyzer.
     */
    public AnalyzerStatistics getStatistics() {
        AnalyzerStatistics stats = new AnalyzerStatistics();
        stats.activePatterns = trafficPatterns.size();
        stats.totalPatternsAnalyzed = totalPatternsAnalyzed.get();
        stats.totalAnomaliesDetected = totalAnomaliesDetected.get();
        return stats;
    }

    /**
     * Patrón de tráfico.
     */
    public static final class TrafficPattern {
        private final CopyOnWriteArrayList<TrafficSample> samples = new CopyOnWriteArrayList<>();
        private volatile double baselineVolume = 0.0;
        private volatile double peakVolume = 0.0;
        private volatile double trendSlope = 0.0;
        private volatile double periodicity = 0.0;
        private volatile long lastPeakTime = 0;
        private final AtomicInteger peakCount = new AtomicInteger(0);
        private final long firstSeenTime;
        private volatile long lastSeenTime;

        public TrafficPattern() {
            this.firstSeenTime = System.currentTimeMillis();
            this.lastSeenTime = firstSeenTime;
        }

        /**
         * Agrega una muestra al patrón.
         */
        public void addSample(@NonNull TrafficSample sample) {
            samples.add(sample);
            lastSeenTime = sample.timestamp;

            // Limitar tamaño
            if (samples.size() > 1000) {
                samples.remove(0);
            }

            // Actualizar peak
            if (sample.volume > peakVolume) {
                peakVolume = sample.volume;
                lastPeakTime = sample.timestamp;
                peakCount.incrementAndGet();
            }
        }

        /**
         * Limpia muestras antiguas.
         */
        public void cleanupOldSamples(long cutoffTime) {
            samples.removeIf(sample -> sample.timestamp < cutoffTime);
        }

        // Getters
        public CopyOnWriteArrayList<TrafficSample> getSamples() {
            return samples;
        }

        public double getBaselineVolume() {
            return baselineVolume;
        }

        public void setBaselineVolume(double baselineVolume) {
            this.baselineVolume = baselineVolume;
        }

        public double getPeakVolume() {
            return peakVolume;
        }

        public double getTrendSlope() {
            return trendSlope;
        }

        public void setTrendSlope(double trendSlope) {
            this.trendSlope = trendSlope;
        }

        public double getPeriodicity() {
            return periodicity;
        }

        public void setPeriodicity(double periodicity) {
            this.periodicity = periodicity;
        }

        public long getLastPeakTime() {
            return lastPeakTime;
        }

        public int getPeakCount() {
            return peakCount.get();
        }

        public long getFirstSeenTime() {
            return firstSeenTime;
        }

        public long getLastSeenTime() {
            return lastSeenTime;
        }
    }

    /**
     * Muestra de tráfico.
     */
    public static final class TrafficSample {
        public final long timestamp;
        public final double volume;
        public final int packetCount;
        public final int connectionCount;

        public TrafficSample(long timestamp, double volume, int packetCount, int connectionCount) {
            this.timestamp = timestamp;
            this.volume = volume;
            this.packetCount = packetCount;
            this.connectionCount = connectionCount;
        }
    }

    /**
     * Estadísticas del analyzer.
     */
    public static final class AnalyzerStatistics {
        public int activePatterns;
        public long totalPatternsAnalyzed;
        public long totalAnomaliesDetected;

        @Override
        public String toString() {
            return String.format(
                "TrafficPatternAnalyzer{patterns=%d, analyzed=%d, anomalies=%d}",
                activePatterns, totalPatternsAnalyzed, totalAnomaliesDetected
            );
        }
    }
}
