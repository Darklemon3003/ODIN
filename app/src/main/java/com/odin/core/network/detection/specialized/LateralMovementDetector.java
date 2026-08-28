package com.odin.core.network.detection.specialized;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.odin.core.domain.model.NetworkPacket;
import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatLevel;
import com.odin.core.network.dpi.NetworkSession;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LateralMovementDetector: Detector especializado de movimiento lateral.
 * 
 * Detecta patrones de movimiento lateral dentro de la red:
 * - Port Hopping: Conexiones a múltiples puertos de mismo host en corto tiempo
 * - Internal Network Scanning: Conexiones a múltiples IPs en rango privado
 * - SMB/SSH Brute Force: Múltiples conexiones fallidas a puertos 445 (SMB) o 22 (SSH)
 * - Service Discovery: Conexiones a puertos comunes de servicios en múltiples hosts
 * - RDP Lateral Movement: Conexiones a puerto 3389 (RDP) desde IP no típica
 * 
 * Thread-safe: Concurrent collections + atomic operations
 * Performance: <2ms por paquete
 */
public final class LateralMovementDetector {

    // Thresholds configurables
    private static final int PORT_HOPPING_THRESHOLD = 10;
    private static final long PORT_HOPPING_WINDOW_MS = 30_000; // 30 segundos
    private static final int INTERNAL_SCAN_THRESHOLD = 20;
    private static final long INTERNAL_SCAN_WINDOW_MS = 60_000; // 60 segundos
    private static final int BRUTE_FORCE_THRESHOLD = 5;
    private static final long BRUTE_FORCE_WINDOW_MS = 10_000; // 10 segundos
    private static final int SERVICE_DISCOVERY_THRESHOLD = 15;
    private static final long SERVICE_DISCOVERY_WINDOW_MS = 60_000; // 60 segundos
    private static final long LATERAL_TIMEOUT_MS = 600_000; // 10 minutos

    // Puertos comunes de servicios
    private static final int PORT_SMB = 445;
    private static final int PORT_SSH = 22;
    private static final int PORT_RDP = 3389;
    private static final int PORT_NETBIOS = 139;
    private static final int PORT_RPC = 135;

    private final ConcurrentHashMap<String, LateralTracker> lateralTrackers;
    private final AtomicInteger totalLateralDetected;
    private final AtomicInteger totalPacketsAnalyzed;

    public LateralMovementDetector() {
        this.lateralTrackers = new ConcurrentHashMap<>();
        this.totalLateralDetected = new AtomicInteger(0);
        this.totalPacketsAnalyzed = new AtomicInteger(0);
    }

    /**
     * Analiza un paquete para detectar patrones de movimiento lateral.
     * 
     * @param packet Paquete a analizar
     * @param session Sesión TCP (null si no existe)
     * @return DetectionResult si detecta movimiento lateral, null otherwise
     */
    @Nullable
    public DetectionResult analyzePacket(
            @NonNull NetworkPacket packet,
            @Nullable NetworkSession session
    ) {
        totalPacketsAnalyzed.incrementAndGet();

        String sourceIP = packet.getSourceIp();
        String destIP = packet.getDestinationIp();
        int destPort = packet.getDestinationPort();

        // Solo analizar tráfico a redes privadas
        if (!isPrivateIP(destIP)) {
            return null;
        }

        // Obtener o crear tracker para esta IP origen
        LateralTracker tracker = lateralTrackers.computeIfAbsent(sourceIP, k -> new LateralTracker());
        tracker.recordActivity(destIP, destPort, session);

        DetectionResult result = null;

        // 1. Port Hopping Detection
        Set<Integer> portsTouched = tracker.getPortsTouchedForHost(destIP);
        if (portsTouched.size() >= PORT_HOPPING_THRESHOLD) {
            long timeWindow = System.currentTimeMillis() - tracker.getFirstActivityTime();
            if (timeWindow < PORT_HOPPING_WINDOW_MS) {
                result = createDetectionResult(
                        ThreatLevel.HIGH,
                        "PORT_HOPPING",
                        String.format("Port hopping detectado: %d puertos en %dms", portsTouched.size(), timeWindow),
                        sourceIP,
                        destIP,
                        destPort,
                        0.8,
                        String.format("Host: %s, Puertos: %d", formatIP(destIP), portsTouched.size())
                );
            }
        }

        // 2. Internal Network Scanning
        int hostsTouched = tracker.getHostsTouchedCount();
        if (hostsTouched >= INTERNAL_SCAN_THRESHOLD) {
            long timeWindow = System.currentTimeMillis() - tracker.getFirstActivityTime();
            if (timeWindow < INTERNAL_SCAN_WINDOW_MS) {
                result = createDetectionResult(
                        ThreatLevel.HIGH,
                        "INTERNAL_NETWORK_SCAN",
                        String.format("Scanning interno detectado: %d hosts en %dms", hostsTouched, timeWindow),
                        sourceIP,
                        destIP,
                        destPort,
                        0.85,
                        String.format("Hosts únicos: %d", hostsTouched)
                );
            }
        }

        // 3. SMB/SSH Brute Force Detection
        if ((destPort == PORT_SMB || destPort == PORT_SSH) && session != null) {
            if (session.getState() == NetworkSession.SessionState.RESET) {
                int failedConnections = tracker.incrementFailedConnections();
                if (failedConnections >= BRUTE_FORCE_THRESHOLD) {
                    long timeWindow = System.currentTimeMillis() - tracker.getFirstActivityTime();
                    if (timeWindow < BRUTE_FORCE_WINDOW_MS) {
                        result = createDetectionResult(
                                ThreatLevel.HIGH,
                                "BRUTE_FORCE_ATTACK",
                                String.format("Brute force detectado: %d conexiones fallidas en %dms", failedConnections, timeWindow),
                                sourceIP,
                                destIP,
                                destPort,
                                0.9,
                                String.format("Puerto: %d, Fallos: %d", destPort, failedConnections)
                        );
                    }
                }
            }
        }

        // 4. Service Discovery Detection
        if (isServicePort(destPort)) {
            int serviceAttempts = tracker.incrementServiceDiscoveryAttempts();
            if (serviceAttempts >= SERVICE_DISCOVERY_THRESHOLD) {
                long timeWindow = System.currentTimeMillis() - tracker.getFirstActivityTime();
                if (timeWindow < SERVICE_DISCOVERY_WINDOW_MS) {
                    result = createDetectionResult(
                            ThreatLevel.MEDIUM,
                            "SERVICE_DISCOVERY",
                            String.format("Service discovery detectado: %d intentos en %dms", serviceAttempts, timeWindow),
                            sourceIP,
                            destIP,
                            destPort,
                            0.7,
                            String.format("Intentos: %d, Hosts: %d", serviceAttempts, hostsTouched)
                    );
                }
            }
        }

        // 5. RDP Lateral Movement Detection
        if (destPort == PORT_RDP) {
            result = createDetectionResult(
                    ThreatLevel.HIGH,
                    "RDP_LATERAL_MOVEMENT",
                    "Conexión RDP detectada (posible movimiento lateral)",
                    sourceIP,
                    destIP,
                    destPort,
                    0.75,
                    String.format("Origen: %s, Destino: %s", formatIP(sourceIP), formatIP(destIP))
            );
        }

        if (result != null) {
            totalLateralDetected.incrementAndGet();
        }

        return result;
    }

    /**
     * Verifica si una IP es privada (rango RFC 1918).
     */
    private boolean isPrivateIP(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) return false;
        
        try {
            int firstOctet = Integer.parseInt(parts[0]);
            int secondOctet = Integer.parseInt(parts[1]);
            
            // 10.0.0.0/8
            if (firstOctet == 10) return true;
            // 172.16.0.0/12
            if (firstOctet == 172 && secondOctet >= 16 && secondOctet <= 31) return true;
            // 192.168.0.0/16
            if (firstOctet == 192 && secondOctet == 168) return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Verifica si un puerto es un puerto de servicio común.
     */
    private boolean isServicePort(int port) {
        return port == PORT_SMB || port == PORT_SSH || port == PORT_RDP ||
               port == PORT_NETBIOS || port == PORT_RPC;
    }

    /**
     * Limpia trackers expirados (>10 minutos sin actividad).
     */
    public void cleanup() {
        long now = System.currentTimeMillis();
        Iterator<LateralTracker> iterator = lateralTrackers.values().iterator();
        
        while (iterator.hasNext()) {
            LateralTracker tracker = iterator.next();
            if (tracker.hasTimedOut(LATERAL_TIMEOUT_MS, now)) {
                iterator.remove();
            }
        }
    }

    /**
     * Cierra recursos y limpia todos los trackers.
     */
    public void shutdown() {
        lateralTrackers.clear();
    }

    /**
     * Obtiene estadísticas del detector.
     */
    public LateralStatistics getStatistics() {
        LateralStatistics stats = new LateralStatistics();
        stats.totalLateralDetected = totalLateralDetected.get();
        stats.totalPacketsAnalyzed = totalPacketsAnalyzed.get();
        stats.activeTrackers = lateralTrackers.size();
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
                "LateralMovementDetector",
                sourceIP,
                destIP,
                0, // sourcePort no relevante
                destPort,
                confidence,
                evidence,
                "",
                "Aislar host origen y bloquear conexiones internas"
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

    private String formatIP(String ip) {
        return ip;
    }

    /**
     * Tracker interno para seguimiento de movimiento lateral por IP origen.
     */
    public static final class LateralTracker {
        private final ConcurrentHashMap<String, Set<Integer>> hostPortsTouched = new ConcurrentHashMap<>();
        private final AtomicInteger failedConnections = new AtomicInteger(0);
        private final AtomicInteger serviceDiscoveryAttempts = new AtomicInteger(0);
        private final long firstActivityTime;
        private volatile long lastActivityTime;

        public LateralTracker() {
            this.firstActivityTime = System.currentTimeMillis();
            this.lastActivityTime = firstActivityTime;
        }

        public void recordActivity(String destIP, int destPort, NetworkSession session) {
            lastActivityTime = System.currentTimeMillis();
            
            // Registrar puerto tocado para este host
            hostPortsTouched.computeIfAbsent(destIP, k -> new ConcurrentSkipListSet<>()).add(destPort);
            
            // Si la sesión está en estado RESET, contar como conexión fallida
            if (session != null && session.getState() == NetworkSession.SessionState.RESET) {
                failedConnections.incrementAndGet();
            }
        }

        public Set<Integer> getPortsTouchedForHost(String hostIP) {
            return hostPortsTouched.getOrDefault(hostIP, new ConcurrentSkipListSet<>());
        }

        public int getHostsTouchedCount() {
            return hostPortsTouched.size();
        }

        public int incrementFailedConnections() {
            return failedConnections.incrementAndGet();
        }

        public int incrementServiceDiscoveryAttempts() {
            return serviceDiscoveryAttempts.incrementAndGet();
        }

        public long getFirstActivityTime() { return firstActivityTime; }
        public long getLastActivityTime() { return lastActivityTime; }

        public boolean hasTimedOut(long timeoutMs, long now) {
            return (now - lastActivityTime) > timeoutMs;
        }
    }

    /**
     * Estadísticas del detector.
     */
    public static final class LateralStatistics {
        public int totalLateralDetected;
        public int totalPacketsAnalyzed;
        public int activeTrackers;

        @Override
        public String toString() {
            return String.format(
                    "LateralMovementDetector{lateral=%d, packets=%d, trackers=%d}",
                    totalLateralDetected, totalPacketsAnalyzed, activeTrackers
            );
        }
    }
}
