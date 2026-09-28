package com.odin.core.domain.model

/**
 * Complete result of a file analysis.
 *
 * @property verdict Canonical analysis verdict.
 * @property sha256 Primary identification hash (may be null if hashing failed).
 * @property sha1 Compatibility reference hash (may be null).
 * @property md5 Compatibility reference hash (may be null).
 * @property detectedFileType Detected file type from header/extension.
 * @property indicators List of deterministic indicators found.
 * @property checksPerformed List of check names that were executed.
 * @property checksNotPerformed List of check names not executed (with reason).
 * @property analysisDurationMs Analysis duration in milliseconds.
 * @property errorMessage Error message if verdict is ERROR (sanitized, no paths).
 */
data class FileAnalysisResult(
    val verdict: AnalysisVerdict,
    val sha256: String?,
    val sha1: String?,
    val md5: String?,
    val detectedFileType: String,
    val indicators: List<AnalysisIndicator>,
    val checksPerformed: List<String>,
    val checksNotPerformed: List<String>,
    val analysisDurationMs: Long,
    val errorMessage: String?
) {
    companion object {
        fun notAnalyzed(): FileAnalysisResult = FileAnalysisResult(
            verdict = AnalysisVerdict.NOT_ANALYZED,
            sha256 = null,
            sha1 = null,
            md5 = null,
            detectedFileType = "UNKNOWN",
            indicators = emptyList(),
            checksPerformed = emptyList(),
            checksNotPerformed = emptyList(),
            analysisDurationMs = 0,
            errorMessage = null
        )

        fun error(message: String, durationMs: Long): FileAnalysisResult = FileAnalysisResult(
            verdict = AnalysisVerdict.ERROR,
            sha256 = null,
            sha1 = null,
            md5 = null,
            detectedFileType = "UNKNOWN",
            indicators = emptyList(),
            checksPerformed = emptyList(),
            checksNotPerformed = emptyList(),
            analysisDurationMs = durationMs,
            errorMessage = message
        )
    }
}
