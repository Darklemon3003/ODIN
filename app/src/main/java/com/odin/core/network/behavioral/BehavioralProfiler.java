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
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * BehavioralProfiler: Perfilado de comportamiento de aplicaciones y usuarios.
 * 
 * Responsabilidades:
 * - Crear perfiles de comportamiento para aplicaciones basados en patrones de tráfico
 * - Establecer líneas base de comportamiento normal
 * - Detectar desviaciones significativas de la línea base
 * - Actualizar perfiles gradualmente con nuevo comportamiento
 * - Expirar perfiles antiguos
 * 
 * Performance: <3ms por paquete (target adicional)
 * Thread-safe: Concurrent collections + atomic operations
 */
public final class BehavioralProfiler {

    // Thresholds configurables
    private static final int BASELINE_MIN_CONNECTIONS = 100;
    private static final double DEVIATION_THRESHOLD = 2.5;
    private static final long PROFILE_EXPIRATION_MS = 30 * 24 * 60 * 60 * 1000L; // 30 días
    private static final double PROFILE_UPDATE_RATE = 0.1; // 10% peso a nuevos datos

    // Estado interno
    private final ConcurrentHashMap<Integer, ApplicationProfile> applicationProfiles;
    private final AtomicBoolean isRunning;
    private final AtomicLong totalProfilesCreated;
    private final AtomicLong totalProfilesExpired;

    /**
     * Constructor.
     */
    public BehavioralProfiler() {
        this.applicationProfiles = new ConcurrentHashMap<>();
        this.isRunning = new AtomicBoolean(false);
        this.totalProfilesCreated = new AtomicLong(0);
        this.totalProfilesExpired = new AtomicLong(0);
    }

    /**
     * Analiza un paquete y actualiza el perfil de la aplicación.
     * 
     * @param packet Paquete a analizar
     * @return DetectionResult si hay desviación significativa, null en caso contrario
     */
    @Nullable
    public DetectionResult analyzePacket(@NonNull NetworkPacket packet) {
        if (!isRunning.get()) return null;

        int appUID = packet.getAppUid();
        if (appUID == 0) return null; // No hay UID válida

        ApplicationProfile profile = applicationProfiles.computeIfAbsent(
            appUID,
            k -> {
                totalProfilesCreated.incrementAndGet();
                return new ApplicationProfile();
            }
        );

        // Actualizar perfil
        updateProfile(appUID, packet);

        // Si línea base está establecida, verificar desviación
        if (profile.isBaselineEstablished) {
            double deviation = calculateDeviation(appUID, packet);
            if (deviation > DEVIATION_THRESHOLD) {
                return createDetectionResult(packet, appUID, deviation);
            }
        } else {
            // Verificar si se puede establecer línea base
            if (profile.totalConnections.get() >= BASELINE_MIN_CONNECTIONS) {
                establishBaseline(appUID);
            }
        }

        return null;
    }

    /**
     * Obtiene el perfil de una aplicación.
     * 
     * @param appUID UID de la aplicación
     * @return ApplicationProfile o null si no existe
     */
    @Nullable
    public ApplicationProfile getApplicationProfile(int appUID) {
        return applicationProfiles.get(appUID);
    }

    /**
     * Fuerza el establecimiento de línea base para una aplicación.
     * 
     * @param appUID UID de la aplicación
     */
    public void establishBaseline(int appUID) {
        ApplicationProfile profile = applicationProfiles.get(appUID);
        if (profile != null) {
            profile.isBaselineEstablished = true;
        }
    }

    /**
     * Actualiza el perfil de una aplicación gradualmente.
     * 
     * @param appUID UID de la aplicación
     * @param packet Paquete a procesar
     */
    private void updateProfile(int appUID, @NonNull NetworkPacket packet) {
        ApplicationProfile profile = applicationProfiles.get(appUID);
        if (profile == null) return;

        profile.recordActivity(packet);
    }

    /**
     * Calcula la desviación de un paquete respecto a la línea base.
     * 
     * @param appUID UID de la aplicación
     * @param packet Paquete a analizar
     * @return Desviación en desviaciones estándar
     */
    private double calculateDeviation(int appUID, @NonNull NetworkPacket packet) {
        ApplicationProfile profile = applicationProfiles.get(appUID);
        if (profile == null || !profile.isBaselineEstablished) return 0.0;

        double deviation = 0.0;
        int factors = 0;

        // Factor 1: Destino IP típico
        if (!profile.typicalDestinations.contains(packet.getDestinationIp())) {
            deviation += 1.0;
            factors++;
        }

        // Factor 2: Puerto típico
        if (!profile.typicalPorts.contains(packet.getDestinationPort())) {
            deviation += 1.0;
            factors++;
        }

        // Factor 3: Volumen de datos
        if (profile.avgBytesPerConnection > 0) {
            double sizeRatio = (double) packet.getSize() / profile.avgBytesPerConnection;
            if (sizeRatio > 3.0 || sizeRatio < 0.33) {
                deviation += 1.0;
                factors++;
            }
        }

        // Factor 4: Horario de actividad
        long currentHour = System.currentTimeMillis() / (60 * 60 * 1000);
        boolean isTypicalHour = profile.activityTimestamps.stream()
            .anyMatch(ts -> {
                long hour = ts / (60 * 60 * 1000);
                return Math.abs(hour - currentHour) <= 1; // Dentro de 1 hora
            });
        if (!isTypicalHour && profile.activityTimestamps.size() > 10) {
            deviation += 0.5;
            factors++;
        }

        // Normalizar desviación
        return factors > 0 ? deviation / factors : 0.0;
    }

    /**
     * Crea un DetectionResult para una desviación detectada.
     */
    private DetectionResult createDetectionResult(@NonNull NetworkPacket packet, int appUID, double deviation) {
        String description = String.format(
            "Desviación de comportamiento detectada para UID %d: %.2f desviaciones estándar",
            appUID, deviation
        );

        return new DetectionResult(
            deviation > 3.0 ? ThreatLevel.HIGH : ThreatLevel.MEDIUM,
            "BEHAVIORAL_DEVIATION",
            description,
            "BehavioralProfiler",
            packet.getSourceIp(),
            packet.getDestinationIp(),
            packet.getSourcePort(),
            packet.getDestinationPort(),
            Math.min(0.9, deviation / 5.0),
            "Deviation: " + String.format("%.2f", deviation),
            "AppUID: " + appUID,
            "Investigar comportamiento anómalo de aplicación"
        );
    }

    /**
     * Limpia perfiles expirados.
     * Debe llamarse periódicamente (ej. cada hora).
     */
    public void cleanup() {
        long now = System.currentTimeMillis();
        applicationProfiles.entrySet().removeIf(entry -> {
            ApplicationProfile profile = entry.getValue();
            if (profile.hasTimedOut(PROFILE_EXPIRATION_MS)) {
                totalProfilesExpired.incrementAndGet();
                return true;
            }
            return false;
        });
    }

    /**
     * Inicia el profiler.
     */
    public void start() {
        isRunning.set(true);
    }

    /**
     * Detiene el profiler y libera recursos.
     */
    public void shutdown() {
        isRunning.set(false);
        applicationProfiles.clear();
    }

    /**
     * Obtiene estadísticas del profiler.
     */
    public ProfilerStatistics getStatistics() {
        ProfilerStatistics stats = new ProfilerStatistics();
        stats.activeProfiles = applicationProfiles.size();
        stats.totalProfilesCreated = totalProfilesCreated.get();
        stats.totalProfilesExpired = totalProfilesExpired.get();
        stats.baselineEstablishedCount = (int) applicationProfiles.values().stream()
            .filter(p -> p.isBaselineEstablished)
            .count();
        return stats;
    }

    /**
     * Perfil de comportamiento de una aplicación.
     */
    public static final class ApplicationProfile {
        private final ConcurrentSkipListSet<String> typicalDestinations = new ConcurrentSkipListSet<>();
        private final ConcurrentSkipListSet<Integer> typicalPorts = new ConcurrentSkipListSet<>();
        private final AtomicLong totalBytesTransferred = new AtomicLong(0);
        private final AtomicInteger totalConnections = new AtomicInteger(0);
        private final CopyOnWriteArrayList<Long> activityTimestamps = new CopyOnWriteArrayList<>();
        private final ConcurrentHashMap<Integer, Integer> portFrequency = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, Integer> destFrequency = new ConcurrentHashMap<>();
        private final long profileCreatedTime;
        private volatile long lastActivityTime;
        private volatile boolean isBaselineEstablished = false;
        private volatile double avgBytesPerConnection = 0.0;

        public ApplicationProfile() {
            this.profileCreatedTime = System.currentTimeMillis();
            this.lastActivityTime = profileCreatedTime;
        }

        /**
         * Registra actividad de un paquete en el perfil.
         */
        public void recordActivity(@NonNull NetworkPacket packet) {
            typicalDestinations.add(packet.getDestinationIp());
            typicalPorts.add(packet.getDestinationPort());
            totalBytesTransferred.addAndGet(packet.getSize());
            totalConnections.incrementAndGet();
            activityTimestamps.add(System.currentTimeMillis());
            lastActivityTime = System.currentTimeMillis();

            // Actualizar frecuencias
            portFrequency.merge(packet.getDestinationPort(), 1, Integer::sum);
            destFrequency.merge(packet.getDestinationIp(), 1, Integer::sum);

            // Limitar tamaño de timestamps
            if (activityTimestamps.size() > 1000) {
                activityTimestamps.remove(0);
            }

            // Recalcular promedio
            if (totalConnections.get() > 0) {
                avgBytesPerConnection = (double) totalBytesTransferred.get() / totalConnections.get();
            }
        }

        /**
         * Verifica si el perfil ha expirado.
         */
        public boolean hasTimedOut(long timeoutMs) {
            return (System.currentTimeMillis() - lastActivityTime) > timeoutMs;
        }

        // Getters
        public ConcurrentSkipListSet<String> getTypicalDestinations() {
            return typicalDestinations;
        }

        public ConcurrentSkipListSet<Integer> getTypicalPorts() {
            return typicalPorts;
        }

        public long getTotalBytesTransferred() {
            return totalBytesTransferred.get();
        }

        public int getTotalConnections() {
            return totalConnections.get();
        }

        public List<Long> getActivityTimestamps() {
            return new ArrayList<>(activityTimestamps);
        }

        public ConcurrentHashMap<Integer, Integer> getPortFrequency() {
            return portFrequency;
        }

        public ConcurrentHashMap<String, Integer> getDestFrequency() {
            return destFrequency;
        }

        public long getProfileCreatedTime() {
            return profileCreatedTime;
        }

        public long getLastActivityTime() {
            return lastActivityTime;
        }

        public boolean isBaselineEstablished() {
            return isBaselineEstablished;
        }

        public double getAvgBytesPerConnection() {
            return avgBytesPerConnection;
        }
    }

    /**
     * Estadísticas del profiler.
     */
    public static final class ProfilerStatistics {
        public int activeProfiles;
        public long totalProfilesCreated;
        public long totalProfilesExpired;
        public int baselineEstablishedCount;

        @Override
        public String toString() {
            return String.format(
                "BehavioralProfiler{active=%d, created=%d, expired=%d, baseline=%d}",
                activeProfiles, totalProfilesCreated, totalProfilesExpired, baselineEstablishedCount
            );
        }
    }
}
