package com.odin.core.ui.model

enum class LogExportFormat(
    val label: String,
    val extension: String,
    val mimeType: String
) {
    JSON("JSON", "json", "application/json"),
    CSV("CSV", "csv", "text/csv")
}

sealed class LogExportState {
    object Idle : LogExportState()
    object Preparing : LogExportState()
    data class WaitingForDestination(
        val format: LogExportFormat,
        val fileName: String
    ) : LogExportState()
    object Writing : LogExportState()
    data class Success(
        val format: LogExportFormat,
        val eventCount: Int
    ) : LogExportState()
    object Cancelled : LogExportState()
    object NoEvents : LogExportState()
    object PdfUnavailable : LogExportState()
    data class Error(val message: String) : LogExportState()
}
