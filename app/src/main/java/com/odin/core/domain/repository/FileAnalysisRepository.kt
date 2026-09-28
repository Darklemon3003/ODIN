package com.odin.core.domain.repository

import android.net.Uri
import com.odin.core.domain.model.FileAnalysisResult
import com.odin.core.domain.model.QuarantineDeleteResult
import com.odin.core.domain.model.QuarantineItem
import com.odin.core.domain.model.QuarantineResult
import com.odin.core.domain.model.RestoreResult
import com.odin.core.domain.model.SelectedFile

/**
 * Canonical repository for file analysis and quarantine.
 *
 * All file reading uses ContentResolver + InputStream.
 * No raw filesystem path conversion.
 */
interface FileAnalysisRepository {

    /** Resolve a SAF content URI into a SelectedFile abstraction. */
    suspend fun resolveSelectedFile(uri: Uri): SelectedFile?

    /** Analyze a user-selected file. Returns ERROR verdict on failure, never NO_KNOWN_INDICATORS. */
    suspend fun analyzeFile(uri: Uri): FileAnalysisResult

    /** Quarantine a file. Requires explicit user confirmation before calling. */
    suspend fun quarantineFile(uri: Uri, analysisResult: FileAnalysisResult): QuarantineResult

    /** List all quarantined items. */
    suspend fun listQuarantinedItems(): List<QuarantineItem>

    /** Restore a quarantined item to a user-selected destination via SAF. */
    suspend fun restoreQuarantinedItem(
        quarantineId: String,
        destinationUri: Uri
    ): RestoreResult

    /** Delete a quarantined item from app-private storage. Requires explicit confirmation. */
    suspend fun deleteQuarantinedItem(quarantineId: String): QuarantineDeleteResult
}
