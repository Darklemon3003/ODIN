package com.odin.core.fileanalysis.model;

/**
 * Real threat level enumeration for file analysis.
 * No simulated values - all levels represent actual threat assessments.
 */
public enum ThreatLevel {
    SAFE(0, "safe"),
    LOW(1, "low"),
    MEDIUM(2, "medium"),
    HIGH(3, "high"),
    CRITICAL(4, "critical");

    private final int severity;
    private final String description;

    ThreatLevel(int severity, String description) {
        this.severity = severity;
        this.description = description;
    }

    public int getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }

    public static ThreatLevel fromSeverity(int severity) {
        for (ThreatLevel level : values()) {
            if (level.severity == severity) {
                return level;
            }
        }
        return SAFE;
    }
}
