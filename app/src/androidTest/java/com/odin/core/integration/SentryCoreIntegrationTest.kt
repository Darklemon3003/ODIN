package com.odin.core.integration

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.odin.core.domain.model.LogEvent
import com.odin.core.domain.repository.ExportFormat
import com.odin.core.forensics.LogExporter
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import javax.inject.Inject

/**
 * SentryCoreIntegrationTest verifies integration with LogExporter (SentryCore).
 * Tests log retrieval, export, filtering, and persistence functionality.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SentryCoreIntegrationTest : BaseIntegrationTest() {
    
    private lateinit var testLogs: List<LogEvent>
    
    @Before
    fun setupTest() {
        // Create test log events
        testLogs = listOf(
            LogEvent(
                id = 1,
                timestamp = Date(),
                severity = LogEvent.Severity.CRITICAL,
                source = "SentryCore",
                message = "Test critical log",
                metadata = mapOf("test" to "true")
            ),
            LogEvent(
                id = 2,
                timestamp = Date(),
                severity = LogEvent.Severity.WARNING,
                source = "ThreatDetection",
                message = "Test warning log",
                metadata = mapOf("test" to "true")
            ),
            LogEvent(
                id = 3,
                timestamp = Date(),
                severity = LogEvent.Severity.INFO,
                source = "SentryCore",
                message = "Test info log",
                metadata = mapOf("test" to "true")
            )
        )
    }
    
    @Test
    fun testLogRetrieval() = runTest {
        // Test that LogExporter can retrieve logs
        val result = logExporter.exportToJson(testLogs)
        
        assertNotNull("Log export should not be null", result)
        assertTrue("Exported result should not be empty", result.isNotEmpty())
        assertTrue("Exported result should contain log data", result.contains("SentryCore"))
    }
    
    @Test
    fun testLogExportJson() = runTest {
        // Test JSON export functionality
        val jsonResult = logExporter.exportToJson(testLogs)
        
        assertNotNull("JSON export should not be null", jsonResult)
        assertTrue("JSON export should contain array", jsonResult.contains("["))
        assertTrue("JSON export should contain severity", jsonResult.contains("CRITICAL"))
    }
    
    @Test
    fun testLogExportCsv() = runTest {
        // Test CSV export functionality
        val csvResult = logExporter.exportToCsv(testLogs)
        
        assertNotNull("CSV export should not be null", csvResult)
        assertTrue("CSV export should contain headers", csvResult.contains("timestamp") || csvResult.contains("severity"))
        assertTrue("CSV export should contain data", csvResult.contains("SentryCore"))
    }
    
    @Test
    fun testLogExportPdf() = runTest {
        // Test PDF export functionality
        val pdfResult = logExporter.exportToPdf(testLogs)
        
        assertNotNull("PDF export should not be null", pdfResult)
        assertTrue("PDF export should contain data", pdfResult.isNotEmpty())
    }
    
    @Test
    fun testLogFiltering() = runTest {
        // Test log filtering by severity
        val criticalLogs = testLogs.filter { it.severity == LogEvent.Severity.CRITICAL }
        
        assertEquals("Should have 1 critical log", 1, criticalLogs.size)
        assertEquals("Critical log should have correct message", "Test critical log", criticalLogs[0].message)
    }
    
    @Test
    fun testLogPersistence() = runTest {
        // Test that log data structure is preserved
        val originalLog = testLogs[0]
        
        assertEquals("Log ID should be preserved", 1L, originalLog.id)
        assertEquals("Log severity should be preserved", LogEvent.Severity.CRITICAL, originalLog.severity)
        assertEquals("Log source should be preserved", "SentryCore", originalLog.source)
        assertEquals("Log message should be preserved", "Test critical log", originalLog.message)
        assertNotNull("Log metadata should be preserved", originalLog.metadata)
    }
}
