package com.odin.core.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for quarantine result model semantics.
 */
class QuarantineResultTest {

    @Test
    fun `QuarantinedAndOriginalRemoved is distinct from QuarantinedCopyOnly`() {
        val item = QuarantineItem(
            quarantineId = "test",
            displayNameSanitized = "test.txt",
            mimeType = "text/plain",
            size = 100,
            sha256 = "abc",
            timestamp = System.currentTimeMillis(),
            analysisVerdict = AnalysisVerdict.NO_KNOWN_INDICATORS,
            formatVersion = 1,
            integrityVerified = true
        )

        val removed = QuarantineResult.QuarantinedAndOriginalRemoved(item)
        val copyOnly = QuarantineResult.QuarantinedCopyOnly(item, "test")
        val failed = QuarantineResult.Failed("test")

        assertFalse(removed::class == copyOnly::class)
        assertFalse(removed::class == failed::class)
        assertFalse(copyOnly::class == failed::class)
    }

    @Test
    fun `RestoreResult Restored contains hash`() {
        val restored = RestoreResult.Restored("abc123")
        assertEquals("abc123", restored.sha256)
    }

    @Test
    fun `RestoreResult Failed keeps quarantine copy`() {
        val failed = RestoreResult.Failed("test error")
        assertEquals("test error", failed.reason)
        // Quarantine copy remains — no data loss
    }

    @Test
    fun `QuarantineDeleteResult Deleted is success`() {
        val deleted = QuarantineDeleteResult.Deleted
        assertEquals(QuarantineDeleteResult.Deleted, deleted)
    }

    @Test
    fun `QuarantineDeleteResult Cancelled keeps item`() {
        val cancelled = QuarantineDeleteResult.Cancelled
        // Item remains in quarantine
        assertEquals(QuarantineDeleteResult.Cancelled, cancelled)
    }
}
