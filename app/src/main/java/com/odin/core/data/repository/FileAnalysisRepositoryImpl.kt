package com.odin.core.data.repository

import android.content.Context
import android.net.Uri
import com.odin.core.data.local.CanonicalFileAnalyzer
import com.odin.core.data.local.CanonicalQuarantineManager
import com.odin.core.domain.model.FileAnalysisResult
import com.odin.core.domain.model.QuarantineDeleteResult
import com.odin.core.domain.model.QuarantineItem
import com.odin.core.domain.model.QuarantineResult
import com.odin.core.domain.model.RestoreResult
import com.odin.core.domain.model.SelectedFile
import com.odin.core.domain.repository.FileAnalysisRepository
import com.odin.core.persistence.SentryCore
import com.odin.core.domain.model.LogEvent
import com.odin.core.security.ConsentManager
import com.odin.core.security.CryptographicManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Canonical implementation of FileAnalysisRepository.
 *
 * Uses CanonicalFileAnalyzer for analysis and CanonicalQuarantineManager for quarantine.
 * All file I/O uses ContentResolver + InputStream.
 * All logging is sanitized (no full paths, no URIs, no content).
 */
@Singleton
class FileAnalysisRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptographicManager: CryptographicManager
) : FileAnalysisRepository {

    private val analyzer = CanonicalFileAnalyzer()
    private val quarantineManager = CanonicalQuarantineManager(context, cryptographicManager)

    override suspend fun resolveSelectedFile(uri: Uri): SelectedFile? =
        withContext(Dispatchers.IO) {
            analyzer.resolveSelectedFile(uri, context.contentResolver)
        }

    override suspend fun analyzeFile(uri: Uri): FileAnalysisResult =
        withContext(Dispatchers.IO) {
            logEvent("FILE_ANALYSIS_STARTED", LogEvent.Severity.INFO)

            val result = analyzer.analyze(uri, context.contentResolver)

            when (result.verdict) {
                com.odin.core.domain.model.AnalysisVerdict.ERROR ->
                    logEvent("FILE_ANALYSIS_ERROR", LogEvent.Severity.ERROR)
                com.odin.core.domain.model.AnalysisVerdict.MALICIOUS ->
                    logEvent("FILE_ANALYSIS_COMPLETED_MALICIOUS", LogEvent.Severity.WARNING)
                com.odin.core.domain.model.AnalysisVerdict.SUSPICIOUS ->
                    logEvent("FILE_ANALYSIS_COMPLETED_SUSPICIOUS", LogEvent.Severity.WARNING)
                else ->
                    logEvent("FILE_ANALYSIS_COMPLETED", LogEvent.Severity.INFO)
            }

            result
        }

    override suspend fun quarantineFile(
        uri: Uri,
        analysisResult: FileAnalysisResult
    ): QuarantineResult = withContext(Dispatchers.IO) {
        // Consent check — fail-closed
        if (!ConsentManager.getInstance(context).isQuarantineActionsEnabled) {
            logEvent("QUARANTINE_BLOCKED_NO_CONSENT", LogEvent.Severity.WARNING)
            return@withContext QuarantineResult.Failed("Consentimiento de cuarentena no otorgado")
        }

        val selectedFile = analyzer.resolveSelectedFile(uri, context.contentResolver)
        val displayName = selectedFile?.displayNameSanitized ?: "unknown"

        val result = quarantineManager.quarantineFile(
            uri = uri,
            contentResolver = context.contentResolver,
            displayName = displayName,
            mimeType = selectedFile?.mimeType,
            analysisVerdict = analysisResult.verdict
        )

        when (result) {
            is QuarantineResult.QuarantinedAndOriginalRemoved -> {
                logEvent("QUARANTINE_COPY_CREATED_AND_ORIGINAL_REMOVED", LogEvent.Severity.INFO)
            }
            is QuarantineResult.QuarantinedCopyOnly -> {
                logEvent("QUARANTINE_COPY_CREATED_ORIGINAL_DELETE_FAILED", LogEvent.Severity.WARNING)
            }
            is QuarantineResult.Failed -> {
                logEvent("QUARANTINE_FAILED", LogEvent.Severity.ERROR)
            }
        }

        result
    }

    override suspend fun listQuarantinedItems(): List<QuarantineItem> =
        withContext(Dispatchers.IO) {
            quarantineManager.listQuarantinedItems()
        }

    override suspend fun restoreQuarantinedItem(
        quarantineId: String,
        destinationUri: Uri
    ): RestoreResult = withContext(Dispatchers.IO) {
        if (!ConsentManager.getInstance(context).isQuarantineActionsEnabled) {
            return@withContext RestoreResult.Failed("Consentimiento de cuarentena no otorgado")
        }

        val result = quarantineManager.restoreQuarantinedItem(
            quarantineId = quarantineId,
            destinationUri = destinationUri,
            contentResolver = context.contentResolver
        )

        when (result) {
            is RestoreResult.Restored ->
                logEvent("QUARANTINE_RESTORE_SUCCEEDED", LogEvent.Severity.INFO)
            is RestoreResult.Failed ->
                logEvent("QUARANTINE_RESTORE_FAILED", LogEvent.Severity.ERROR)
            RestoreResult.Cancelled ->
                logEvent("QUARANTINE_RESTORE_CANCELLED", LogEvent.Severity.INFO)
        }

        result
    }

    override suspend fun deleteQuarantinedItem(
        quarantineId: String
    ): QuarantineDeleteResult = withContext(Dispatchers.IO) {
        if (!ConsentManager.getInstance(context).isQuarantineActionsEnabled) {
            return@withContext QuarantineDeleteResult.Failed("Consentimiento de cuarentena no otorgado")
        }

        val result = quarantineManager.deleteQuarantinedItem(quarantineId)

        when (result) {
            QuarantineDeleteResult.Deleted ->
                logEvent("QUARANTINE_DELETE_SUCCEEDED", LogEvent.Severity.INFO)
            is QuarantineDeleteResult.Failed ->
                logEvent("QUARANTINE_DELETE_FAILED", LogEvent.Severity.ERROR)
            QuarantineDeleteResult.Cancelled ->
                logEvent("QUARANTINE_DELETE_CANCELLED", LogEvent.Severity.INFO)
        }

        result
    }

    /**
     * Log a sanitized event to SentryCore.
     * No full paths, URIs, or file content.
     */
    private fun logEvent(message: String, severity: LogEvent.Severity) {
        try {
            SentryCore.getInstance(context).appendEvent(
                LogEvent(
                    System.currentTimeMillis(),
                    "FILE_ANALYSIS",
                    message,
                    severity
                )
            )
        } catch (e: Exception) {
            // Logging failure must not crash the app
        }
    }
}
