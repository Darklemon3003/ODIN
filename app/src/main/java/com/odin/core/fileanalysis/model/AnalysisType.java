package com.odin.core.fileanalysis.model;

/**
 * Real analysis types for malware analysis.
 * No simulated analysis types - only actual analysis methods.
 */
public enum AnalysisType {
    STATIC("static"),
    DYNAMIC("dynamic"),
    HEURISTIC("heuristic"),
    BEHAVIORAL("behavioral"),
    MACHINE_LEARNING("machine_learning"),
    HYBRID("hybrid");

    private final String type;

    AnalysisType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static AnalysisType fromString(String type) {
        for (AnalysisType analysisType : values()) {
            if (analysisType.type.equalsIgnoreCase(type)) {
                return analysisType;
            }
        }
        return STATIC;
    }
}
