package com.odin.core.domain.model;

/**
 * SystemStatus representa el estado actual del agente y del sistema.
 * Está diseñado como un modelo ligero para persistencia de estado de ejecución.
 */
public final class SystemStatus {

    private final boolean serviceActive;
    private final long lastHeartbeat;
    private final String currentState;

    public SystemStatus(boolean serviceActive, long lastHeartbeat, String currentState) {
        this.serviceActive = serviceActive;
        this.lastHeartbeat = lastHeartbeat;
        this.currentState = currentState;
    }

    public boolean isServiceActive() {
        return serviceActive;
    }

    public long getLastHeartbeat() {
        return lastHeartbeat;
    }

    public String getCurrentState() {
        return currentState;
    }
}
