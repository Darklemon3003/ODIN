package com.odin.core.network.detection;

/**
 * ThreatLevel define los niveles de severidad de amenazas detectadas.
 * 
 * Utilizado para priorización y respuesta automática.
 */
public enum ThreatLevel {
    /**
     * Tráfico anómalo pero no necesariamente malicioso.
     * Requiere monitoreo adicional.
     */
    INFO("INFO", 0),

    /**
     * Comportamiento sospechoso que podría indicar ataque.
     * Requiere investigación.
     */
    LOW("LOW", 1),

    /**
     * Actividad potencialmente maliciosa.
     * Debe aplicarse mitigación.
     */
    MEDIUM("MEDIUM", 2),

    /**
     * Ataque activo o exfiltración en progreso.
     * Respuesta inmediata requerida.
     */
    HIGH("HIGH", 3),

    /**
     * Amenaza crítica que compromete la seguridad del sistema.
     * Respuesta máxima requerida (aislamiento posible).
     */
    CRITICAL("CRITICAL", 4);

    private final String label;
    private final int severity;

    ThreatLevel(String label, int severity) {
        this.label = label;
        this.severity = severity;
    }

    public String getLabel() { return label; }
    public int getSeverity() { return severity; }
    
    /**
     * Compara dos niveles de amenaza.
     * @return true si este nivel es más severo o igual que el otro
     */
    public boolean isAtLeast(ThreatLevel other) {
        return this.severity >= other.severity;
    }
}
