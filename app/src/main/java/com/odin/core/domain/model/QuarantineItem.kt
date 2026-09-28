package com.odin.core.domain.model

/**
 * Metadata for a quarantined file.
 *
 * Stores only sanitized information. No full filesystem paths.
 *
 * @property quarantineId Unique identifier.
 * @property displayNameSanitized Sanitized display name.
 * @property mimeType MIME type (may be null).
 * @property size Original file size in bytes.
 * @property sha256 SHA-256 of original content.
 * @property timestamp Quarantine timestamp (epoch ms).
 * @property analysisVerdict Verdict at time of quarantine.
 * @property formatVersion Quarantine format version.
 * @property integrityVerified Whether the quarantine copy was integrity-verified.
 */
data class QuarantineItem(
    val quarantineId: String,
    val displayNameSanitized: String,
    val mimeType: String?,
    val size: Long,
    val sha256: String,
    val timestamp: Long,
    val analysisVerdict: AnalysisVerdict,
    val formatVersion: Int,
    val integrityVerified: Boolean
)
