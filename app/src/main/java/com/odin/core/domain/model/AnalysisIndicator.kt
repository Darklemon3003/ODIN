package com.odin.core.domain.model

/**
 * A single deterministic indicator detected during file analysis.
 *
 * Each indicator is explainable and evidence-backed.
 * No indicator alone automatically means MALICIOUS unless it is an exact
 * known test signature or trusted database entry.
 *
 * @property id Unique identifier for the indicator type.
 * @property category Classification: header, string, heuristic, mismatch, test_signature.
 * @property severity LOW, MEDIUM, or HIGH — qualitative, not a probability.
 * @property description Human-readable explanation of what was found.
 * @property evidenceSummary Sanitized summary of the evidence (no full paths or private content).
 */
data class AnalysisIndicator(
    val id: String,
    val category: IndicatorCategory,
    val severity: IndicatorSeverity,
    val description: String,
    val evidenceSummary: String
)

enum class IndicatorCategory {
    HEADER,
    STRING,
    HEURISTIC,
    MISMATCH,
    TEST_SIGNATURE,
    METADATA
}

enum class IndicatorSeverity {
    LOW,
    MEDIUM,
    HIGH
}
