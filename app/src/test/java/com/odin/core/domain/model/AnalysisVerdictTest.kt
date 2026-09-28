package com.odin.core.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for AnalysisVerdict semantics.
 *
 * Critical: No verdict may ever be used as a fallback for ERROR.
 */
class AnalysisVerdictTest {

    @Test
    fun `ERROR is distinct from NO_KNOWN_INDICATORS`() {
        assertFalse(AnalysisVerdict.ERROR == AnalysisVerdict.NO_KNOWN_INDICATORS)
    }

    @Test
    fun `UNKNOWN is distinct from NO_KNOWN_INDICATORS`() {
        assertFalse(AnalysisVerdict.UNKNOWN == AnalysisVerdict.NO_KNOWN_INDICATORS)
    }

    @Test
    fun `UNSUPPORTED is distinct from NO_KNOWN_INDICATORS`() {
        assertFalse(AnalysisVerdict.UNSUPPORTED == AnalysisVerdict.NO_KNOWN_INDICATORS)
    }

    @Test
    fun `NOT_ANALYZED is distinct from NO_KNOWN_INDICATORS`() {
        assertFalse(AnalysisVerdict.NOT_ANALYZED == AnalysisVerdict.NO_KNOWN_INDICATORS)
    }

    @Test
    fun `all verdicts are distinct`() {
        val verdicts = AnalysisVerdict.values().toList()
        assertEquals(8, verdicts.size)
        assertEquals(verdicts.size, verdicts.toSet().size)
    }

    @Test
    fun `error result has ERROR verdict`() {
        val result = FileAnalysisResult.error("test error", 100)
        assertEquals(AnalysisVerdict.ERROR, result.verdict)
        assertNull(result.sha256)
        assertNotNull(result.errorMessage)
    }

    @Test
    fun `notAnalyzed result has NOT_ANALYZED verdict`() {
        val result = FileAnalysisResult.notAnalyzed()
        assertEquals(AnalysisVerdict.NOT_ANALYZED, result.verdict)
        assertNull(result.sha256)
        assertNull(result.errorMessage)
    }

    @Test
    fun `NO_KNOWN_INDICATORS result has no indicators`() {
        val result = FileAnalysisResult(
            verdict = AnalysisVerdict.NO_KNOWN_INDICATORS,
            sha256 = "abc123",
            sha1 = null,
            md5 = null,
            detectedFileType = "TXT",
            indicators = emptyList(),
            checksPerformed = listOf("hash_sha256"),
            checksNotPerformed = emptyList(),
            analysisDurationMs = 50,
            errorMessage = null
        )
        assertTrue(result.indicators.isEmpty())
        assertNull(result.errorMessage)
    }
}
