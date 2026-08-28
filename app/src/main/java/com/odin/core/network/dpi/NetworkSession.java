package com.odin.core.network.dpi;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * NetworkSession representa una sesión TCP stateful.
 * 
 * Utilizado para reconstrucción de flujos, detección de anomalías y análisis forense.
 * Thread-safe para actualizaciones concurrentes.
 */
public final class NetworkSession implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum SessionState {
        NEW,           // SYN recibido
        ESTABLISHED,   // 3-way handshake completado
        CLOSE_WAIT,    // FIN recibido, esperando cierre
        CLOSED,        // FIN-ACK completado
        TIMEOUT,       // Timeout sin actividad
        RESET          // RST recibido
    }

    private final String sessionId; // Hash único de la sesión
    private final String sourceIP;
    private final String destinationIP;
    private final int sourcePort;
    private final int destinationPort;
    private final long createdTime;
    
    private volatile SessionState state;
    private volatile long lastActivityTime;
    private volatile long uploadedBytes;
    private volatile long downloadedBytes;
    private volatile int packetCount;
    private volatile long maxInactivityTime; // En ms

    public NetworkSession(
            String sourceIP,
            String destinationIP,
            int sourcePort,
            int destinationPort
    ) {
        this.sourceIP = sourceIP;
        this.destinationIP = destinationIP;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.sessionId = generateSessionId(sourceIP, destinationIP, sourcePort, destinationPort);
        this.createdTime = System.currentTimeMillis();
        this.lastActivityTime = createdTime;
        this.state = SessionState.NEW;
        this.uploadedBytes = 0;
        this.downloadedBytes = 0;
        this.packetCount = 0;
        this.maxInactivityTime = 60_000; // 60 segundos por defecto
    }

    // Getters
    public String getSessionId() { return sessionId; }
    public String getSourceIp() { return sourceIP; }
    public String getDestinationIp() { return destinationIP; }
    public int getSourcePort() { return sourcePort; }
    public int getDestinationPort() { return destinationPort; }
    public long getCreatedTime() { return createdTime; }
    public SessionState getState() { return state; }
    public long getLastActivityTime() { return lastActivityTime; }
    public long getUploadedBytes() { return uploadedBytes; }
    public long getDownloadedBytes() { return downloadedBytes; }
    public int getPacketCount() { return packetCount; }
    public long getDurationMs() { return System.currentTimeMillis() - createdTime; }
    public long getTotalBytes() { return uploadedBytes + downloadedBytes; }

    /**
     * Registra actividad en la sesión (paquete recibido).
     * 
     * @param isUpload Si es true, se cuenta como bytes subidos; false = descargados
     * @param bytes Cantidad de bytes
     */
    public synchronized void recordActivity(boolean isUpload, int bytes) {
        lastActivityTime = System.currentTimeMillis();
        if (isUpload) {
            uploadedBytes += bytes;
        } else {
            downloadedBytes += bytes;
        }
        packetCount++;
    }

    /**
     * Actualiza el estado de la sesión.
     */
    public synchronized void setState(SessionState newState) {
        this.state = newState;
        this.lastActivityTime = System.currentTimeMillis();
    }

    /**
     * Verifica si la sesión ha expirado por inactividad.
     */
    public boolean hasTimedOut() {
        return (System.currentTimeMillis() - lastActivityTime) > maxInactivityTime;
    }

    /**
     * Obtiene descripción de la sesión.
     */
    @Override
    public String toString() {
        return String.format(
                "Session{%s:%d -> %s:%d, state=%s, up=%d, down=%d, duration=%dms}",
                formatIP(sourceIP), sourcePort,
                formatIP(destinationIP), destinationPort,
                state.name(),
                uploadedBytes, downloadedBytes,
                getDurationMs()
        );
    }

    /**
     * Genera ID único para la sesión.
     */
    private static String generateSessionId(String srcIP, String dstIP, int srcPort, int dstPort) {
        return String.format("%s_%d_%s_%d", srcIP, srcPort, dstIP, dstPort);
    }

    private static String formatIP(String ip) {
        return ip;
    }
}
