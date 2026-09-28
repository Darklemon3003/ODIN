package com.odin.core.domain.model

/**
 * Abstraction of a user-selected file via SAF.
 *
 * Never exposes a raw filesystem path as required business logic.
 * The analyzer consumes the contentUri via ContentResolver.
 *
 * @property contentUri SAF content:// URI.
 * @property displayNameSanitized Sanitized display name (no full path).
 * @property mimeType MIME type from provider (may be null).
 * @property size File size in bytes (-1 if unknown).
 * @property extension Lowercase extension without dot, or empty.
 */
data class SelectedFile(
    val contentUri: String,
    val displayNameSanitized: String,
    val mimeType: String?,
    val size: Long,
    val extension: String
)
