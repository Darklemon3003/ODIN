package com.odin.core.domain.model

/**
 * Canonical analysis verdict for file analysis.
 *
 * Semantics:
 * - NOT_ANALYZED: No analysis has been performed yet.
 * - ANALYZING: Analysis is in progress.
 * - NO_KNOWN_INDICATORS: ODIN did not detect indicators using the checks currently available.
 *   This does NOT mean the file is safe.
 * - SUSPICIOUS: One or more deterministic indicators were found but malware identity is not proven.
 * - MALICIOUS: Strong deterministic evidence (exact known test signature or trusted database entry).
 * - UNKNOWN: Analysis could not establish a meaningful result.
 * - ERROR: Analysis failed.
 * - UNSUPPORTED: Analysis method does not support this file.
 *
 * No state may ever be converted to NO_KNOWN_INDICATORS as a fallback for errors.
 */
enum class AnalysisVerdict {
    NOT_ANALYZED,
    ANALYZING,
    NO_KNOWN_INDICATORS,
    SUSPICIOUS,
    MALICIOUS,
    UNKNOWN,
    ERROR,
    UNSUPPORTED
}
