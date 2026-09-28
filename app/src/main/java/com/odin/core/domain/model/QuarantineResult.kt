package com.odin.core.domain.model

/**
 * Result of a quarantine operation.
 *
 * No binary success if partial operations occurred.
 */
sealed class QuarantineResult {
    /** Quarantine copy created and verified, original also removed. */
    data class QuarantinedAndOriginalRemoved(val item: QuarantineItem) : QuarantineResult()

    /** Quarantine copy created and verified, but original could not be deleted. */
    data class QuarantinedCopyOnly(val item: QuarantineItem, val reason: String) : QuarantineResult()

    /** Quarantine failed entirely. No copy exists. Original is untouched. */
    data class Failed(val reason: String) : QuarantineResult()
}

/**
 * Result of a restore operation.
 */
sealed class RestoreResult {
    /** Restore completed and integrity verified. */
    data class Restored(val sha256: String) : RestoreResult()

    /** Restore failed. Quarantine copy remains intact. */
    data class Failed(val reason: String) : RestoreResult()

    /** Restore cancelled by user. Quarantine copy remains intact. */
    object Cancelled : RestoreResult()
}

/**
 * Result of deleting a quarantined item.
 */
sealed class QuarantineDeleteResult {
    /** Quarantine item deleted from app-private storage. */
    object Deleted : QuarantineDeleteResult()

    /** Delete failed. Item remains in quarantine. */
    data class Failed(val reason: String) : QuarantineDeleteResult()

    /** Delete cancelled by user. Item remains in quarantine. */
    object Cancelled : QuarantineDeleteResult()
}
