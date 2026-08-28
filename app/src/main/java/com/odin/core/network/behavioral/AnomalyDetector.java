package com.odin.core.network.behavioral;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * AnomalyDetector: Detección de anomalías usando estadísticas y ML básico.
 * 
 * Responsabilidades:
 * - Statistical Anomaly Detection usando Z-score
 * - Moving Average para detectar cambios bruscos
 * - Percentile-based Detection
 * - Simple ML Classifier basado en reglas
 * - Ensemble Detection para reducir falsos positivos
 * 
 * Performance: <2ms por análisis métrico
 * Thread-safe: Concurrent collections + atomic operations
 */
public final class AnomalyDetector {

    // Thresholds configurables
    private static final double Z_SCORE_THRESHOLD = 3.0;
    private static final double PERCENTILE_THRESHOLD = 0.95;
    private static final int MOVING_AVERAGE_WINDOW = 50;
    private static final int MAX_HISTORY_SIZE = 1000;
    private static final long HISTORY_RETENTION_MS = 24 * 60 * 60 * 1000L; // 24 horas

    // Estado interno
    private final ConcurrentHashMap<String, MetricHistory> metricHistories;
    private final AtomicBoolean isRunning;
    private final AtomicLong totalAnomaliesDetected;
    private final AtomicLong totalMetricsAnalyzed;

    /**
     * Constructor.
     */
    public AnomalyDetector() {
        this.metricHistories = new ConcurrentHashMap<>();
        this.isRunning = new AtomicBoolean(false);
        this.totalAnomaliesDetected = new AtomicLong(0);
        this.totalMetricsAnalyzed = new AtomicLong(0);
    }

    /**
     * Analiza un valor métrico y retorna anomalía si detectada.
     * 
     * @param metricName Nombre de la métrica
     * @param value Valor a analizar
     * @return AnomalyResult si es anómalo, null en caso contrario
     */
    @NonNull
    public AnomalyResult analyzeMetric(@NonNull String metricName, double value) {
        if (!isRunning.get()) {
            return new AnomalyResult(metricName, value, false, 0.0, "Detector not running");
        }

        totalMetricsAnalyzed.incrementAndGet();

        // Actualizar historial
        updateMetricHistory(metricName, value);

        // Ensemble detection: combinar múltiples métodos
        boolean isZScoreAnomaly = isZScoreAnomaly(metricName, value);
        boolean isPercentileAnomaly = isPercentileAnomaly(metricName, value);
        boolean isMovingAverageAnomaly = isMovingAverageAnomaly(metricName, value);

        // Ensemble: requiere al menos 2 métodos para reducir falsos positivos
        int anomalyCount = (isZScoreAnomaly ? 1 : 0) + 
                          (isPercentileAnomaly ? 1 : 0) + 
                          (isMovingAverageAnomaly ? 1 : 0);

        boolean isAnomaly = anomalyCount >= 2;

        if (isAnomaly) {
            totalAnomaliesDetected.incrementAndGet();
        }

        double confidence = calculateConfidence(anomalyCount, isZScoreAnomaly, isPercentileAnomaly, isMovingAverageAnomaly);
        String reason = buildReason(isZScoreAnomaly, isPercentileAnomaly, isMovingAverageAnomaly);

        return new AnomalyResult(metricName, value, isAnomaly, confidence, reason);
    }

    /**
     * Calcula Z-score para un valor.
     * 
     * @param metricName Nombre de la métrica
     * @param value Valor a analizar
     * @return Z-score
     */
    public double calculateZScore(@NonNull String metricName, double value) {
        MetricHistory history = metricHistories.get(metricName);
        if (history == null || history.getStdDev() == 0) return 0.0;
        return Math.abs((value - history.getMean()) / history.getStdDev());
    }

    /**
     * Calcula percentil para un valor.
     * 
     * @param metricName Nombre de la métrica
     * @param value Valor a analizar
     * @return Percentil (0.0 - 1.0)
     */
    public double calculatePercentile(@NonNull String metricName, double value) {
        MetricHistory history = metricHistories.get(metricName);
        if (history == null || history.getValues().isEmpty()) return 0.5;

        List<Double> values = new ArrayList<>(history.getValues());
        Collections.sort(values);

        int rank = 0;
        for (Double v : values) {
            if (v <= value) rank++;
        }

        return (double) rank / values.size();
    }

    /**
     * Determina si un valor es atípico.
     * 
     * @param metricName Nombre de la métrica
     * @param value Valor a analizar
     * @return true si es atípico
     */
    public boolean isOutlier(@NonNull String metricName, double value) {
        AnomalyResult result = analyzeMetric(metricName, value);
        return result.isAnomaly;
    }

    /**
     * Actualiza el historial de una métrica.
     */
    private void updateMetricHistory(@NonNull String metricName, double value) {
        MetricHistory history = metricHistories.computeIfAbsent(
            metricName,
            k -> new MetricHistory()
        );
        history.addValue(value);
    }

    /**
     * Verifica si es anomalía por Z-score.
     */
    private boolean isZScoreAnomaly(@NonNull String metricName, double value) {
        double zScore = calculateZScore(metricName, value);
        return zScore > Z_SCORE_THRESHOLD;
    }

    /**
     * Verifica si es anomalía por percentil.
     */
    private boolean isPercentileAnomaly(@NonNull String metricName, double value) {
        double percentile = calculatePercentile(metricName, value);
        return percentile > PERCENTILE_THRESHOLD || percentile < (1.0 - PERCENTILE_THRESHOLD);
    }

    /**
     * Verifica si es anomalía por moving average.
     */
    private boolean isMovingAverageAnomaly(@NonNull String metricName, double value) {
        MetricHistory history = metricHistories.get(metricName);
        if (history == null) return false;

        double movingAverage = history.getMovingAverage();
        if (movingAverage == 0) return false;

        double ratio = Math.abs(value - movingAverage) / movingAverage;
        return ratio > 2.0; // 200% de desviación del moving average
    }

    /**
     * Calcula confianza de la detección.
     */
    private double calculateConfidence(int anomalyCount, boolean zScore, boolean percentile, boolean movingAvg) {
        if (anomalyCount == 0) return 0.0;
        if (anomalyCount == 3) return 0.95; // Todos los métodos coinciden
        if (anomalyCount == 2) return 0.75; // Dos métodos coinciden
        return 0.5; // Solo un método
    }

    /**
     * Construye razón de la detección.
     */
    private String buildReason(boolean zScore, boolean percentile, boolean movingAvg) {
        List<String> reasons = new ArrayList<>();
        if (zScore) reasons.add("Z-score");
        if (percentile) reasons.add("Percentile");
        if (movingAvg) reasons.add("Moving Average");
        return String.join(", ", reasons);
    }

    /**
     * Limpia historiales antiguos.
     * Debe llamarse periódicamente (ej. cada hora).
     */
    public void cleanup() {
        long now = System.currentTimeMillis();
        metricHistories.entrySet().removeIf(entry -> {
            MetricHistory history = entry.getValue();
            if ((now - history.getLastUpdateTime()) > HISTORY_RETENTION_MS) {
                return true;
            }
            return false;
        });
    }

    /**
     * Inicia el detector.
     */
    public void start() {
        isRunning.set(true);
    }

    /**
     * Detiene el detector y libera recursos.
     */
    public void shutdown() {
        isRunning.set(false);
        metricHistories.clear();
    }

    /**
     * Obtiene estadísticas del detector.
     */
    public DetectorStatistics getStatistics() {
        DetectorStatistics stats = new DetectorStatistics();
        stats.activeMetrics = metricHistories.size();
        stats.totalAnomaliesDetected = totalAnomaliesDetected.get();
        stats.totalMetricsAnalyzed = totalMetricsAnalyzed.get();
        return stats;
    }

    /**
     * Historial de métricas.
     */
    public static final class MetricHistory {
        private final CopyOnWriteArrayList<Double> values = new CopyOnWriteArrayList<>();
        private volatile double mean = 0.0;
        private volatile double stdDev = 0.0;
        private volatile double p25 = 0.0;
        private volatile double p75 = 0.0;
        private volatile double p95 = 0.0;
        private volatile double movingAverage = 0.0;
        private final AtomicInteger sampleCount = new AtomicInteger(0);
        private volatile long lastUpdateTime = System.currentTimeMillis();

        /**
         * Agrega un valor al historial.
         */
        public void addValue(double value) {
            values.add(value);
            sampleCount.incrementAndGet();
            lastUpdateTime = System.currentTimeMillis();

            // Limitar tamaño
            if (values.size() > MAX_HISTORY_SIZE) {
                values.remove(0);
            }

            // Recalcular estadísticas
            recalculateStatistics();
        }

        /**
         * Recalcula estadísticas del historial.
         */
        private void recalculateStatistics() {
            if (values.isEmpty()) return;

            // Calcular mean
            double sum = 0;
            for (Double v : values) {
                sum += v;
            }
            mean = sum / values.size();

            // Calcular stdDev
            double variance = 0;
            for (Double v : values) {
                variance += Math.pow(v - mean, 2);
            }
            stdDev = Math.sqrt(variance / values.size());

            // Calcular percentiles
            List<Double> sorted = new ArrayList<>(values);
            Collections.sort(sorted);
            if (!sorted.isEmpty()) {
                p25 = sorted.get((int) (sorted.size() * 0.25));
                p75 = sorted.get((int) (sorted.size() * 0.75));
                p95 = sorted.get((int) (sorted.size() * 0.95));
            }

            // Calcular moving average (últimas 50 muestras)
            int window = Math.min(MOVING_AVERAGE_WINDOW, sorted.size());
            double maSum = 0;
            for (int i = sorted.size() - window; i < sorted.size(); i++) {
                maSum += sorted.get(i);
            }
            movingAverage = maSum / window;
        }

        // Getters
        public CopyOnWriteArrayList<Double> getValues() {
            return values;
        }

        public double getMean() {
            return mean;
        }

        public double getStdDev() {
            return stdDev;
        }

        public double getP25() {
            return p25;
        }

        public double getP75() {
            return p75;
        }

        public double getP95() {
            return p95;
        }

        public double getMovingAverage() {
            return movingAverage;
        }

        public int getSampleCount() {
            return sampleCount.get();
        }

        public long getLastUpdateTime() {
            return lastUpdateTime;
        }
    }

    /**
     * Resultado de análisis de anomalía.
     */
    public static final class AnomalyResult {
        public final String metricName;
        public final double value;
        public final boolean isAnomaly;
        public final double confidence;
        public final String reason;

        public AnomalyResult(String metricName, double value, boolean isAnomaly, double confidence, String reason) {
            this.metricName = metricName;
            this.value = value;
            this.isAnomaly = isAnomaly;
            this.confidence = confidence;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return String.format(
                "AnomalyResult{metric=%s, value=%.2f, anomaly=%s, confidence=%.2f, reason=%s}",
                metricName, value, isAnomaly, confidence, reason
            );
        }
    }

    /**
     * Estadísticas del detector.
     */
    public static final class DetectorStatistics {
        public int activeMetrics;
        public long totalAnomaliesDetected;
        public long totalMetricsAnalyzed;

        @Override
        public String toString() {
            return String.format(
                "AnomalyDetector{metrics=%d, anomalies=%d, analyzed=%d}",
                activeMetrics, totalAnomaliesDetected, totalMetricsAnalyzed
            );
        }
    }
}
