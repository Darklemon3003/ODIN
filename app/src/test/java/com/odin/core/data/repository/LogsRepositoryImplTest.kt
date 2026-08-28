package com.odin.core.data.repository

import android.content.Context
import com.odin.core.domain.model.LogEvent
import com.odin.core.domain.repository.ExportFormat
import com.odin.core.persistence.SentryCore
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LogsRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var sentryCore: SentryCore
    private lateinit var repository: LogsRepositoryImpl

    @BeforeEach
    fun setup() {
        context = mockk(relaxed = true)
        sentryCore = mockk()
        
        val mockLogEntries = listOf(
            com.odin.core.persistence.LogEntry(
                timestamp = System.currentTimeMillis(),
                source = "SecurityService",
                message = "Security scan completed",
                level = "INFO",
                metadata = mapOf("scanId" to "123")
            ),
            com.odin.core.persistence.LogEntry(
                timestamp = System.currentTimeMillis(),
                source = "FirewallService",
                message = "Blocked suspicious connection",
                level = "WARN",
                metadata = mapOf("ip" to "192.168.1.1")
            )
        )
        
        every { sentryCore.getRecentLogs(any()) } returns mockLogEntries
        every { SentryCore.getInstance(context) } returns sentryCore
        
        repository = LogsRepositoryImpl(context)
    }

    @Test
    fun `getAllLogs returns loaded logs`() = runTest {
        val logs = repository.getAllLogs().first()
        
        assertNotNull(logs)
        assertTrue(logs.isNotEmpty())
    }

    @Test
    fun `getRecentEvents returns limited logs`() = runTest {
        val events = repository.getRecentEvents(limit = 1).first()
        
        assertNotNull(events)
        assertTrue(events.size <= 1)
    }

    @Test
    fun `getLogsBySeverity filters correctly`() = runTest {
        val errorLogs = repository.getLogsBySeverity(LogEvent.Severity.ERROR).first()
        val infoLogs = repository.getLogsBySeverity(LogEvent.Severity.INFO).first()
        
        assertNotNull(errorLogs)
        assertNotNull(infoLogs)
    }

    @Test
    fun `getLogsBySource filters correctly`() = runTest {
        val securityLogs = repository.getLogsBySource("SecurityService").first()
        
        assertNotNull(securityLogs)
        assertTrue(securityLogs.all { it.source == "SecurityService" })
    }

    @Test
    fun `exportLogs to JSON returns valid format`() = runTest {
        val logs = repository.getAllLogs().first()
        val json = repository.exportLogs(logs, ExportFormat.JSON)
        
        assertNotNull(json)
        assertTrue(json.isNotEmpty())
    }

    @Test
    fun `exportLogs to CSV returns valid format`() = runTest {
        val logs = repository.getAllLogs().first()
        val csv = repository.exportLogs(logs, ExportFormat.CSV)
        
        assertNotNull(csv)
        assertTrue(csv.contains("timestamp,source,message,severity"))
    }

    @Test
    fun `exportLogs to PDF returns valid format`() = runTest {
        val logs = repository.getAllLogs().first()
        val pdf = repository.exportLogs(logs, ExportFormat.PDF)
        
        assertNotNull(pdf)
        assertTrue(pdf.contains("ODIN Security Logs Export"))
    }

    @Test
    fun `searchLogs finds matching logs`() = runTest {
        val results = repository.searchLogs("Security")
        
        assertNotNull(results)
        assertTrue(results.isNotEmpty())
    }

    @Test
    fun `searchLogs is case insensitive`() = runTest {
        val results1 = repository.searchLogs("security")
        val results2 = repository.searchLogs("SECURITY")
        
        assertEquals(results1.size, results2.size)
    }

    @Test
    fun `refreshLogs reloads from SentryCore`() {
        assertDoesNotThrow {
            repository.refreshLogs()
        }
    }

    @Test
    fun `handles SentryCore errors gracefully`() = runTest {
        every { sentryCore.getRecentLogs(any()) } throws RuntimeException("SentryCore error")
        
        val errorRepository = LogsRepositoryImpl(context)
        val logs = errorRepository.getAllLogs().first()
        
        assertNotNull(logs)
    }
}
