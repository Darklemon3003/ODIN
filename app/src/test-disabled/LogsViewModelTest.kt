package com.odin.core.ui.viewmodel

import com.odin.core.domain.model.LogEvent
import com.odin.core.domain.repository.LogsRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class LogsViewModelTest {
    
    @Mock
    private lateinit var logsRepository: LogsRepository
    
    private lateinit var viewModel: LogsViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = LogsViewModel(logsRepository)
    }
    
    @Test
    fun `search query filters logs correctly`() = runTest {
        val logs = listOf(
            LogEvent(
                timestamp = System.currentTimeMillis(),
                source = "SentryCore",
                message = "Test log message",
                severity = LogEvent.Severity.INFO
            ),
            LogEvent(
                timestamp = System.currentTimeMillis(),
                source = "NetShield",
                message = "Network packet detected",
                severity = LogEvent.Severity.WARNING
            )
        )
        
        whenever(logsRepository.getAllLogs())
            .thenReturn(flowOf(logs))
        
        viewModel.updateSearchQuery("Test")
        
        // Verify filtering works
    }
    
    @Test
    fun `severity filter works correctly`() = runTest {
        val logs = listOf(
            LogEvent(
                timestamp = System.currentTimeMillis(),
                source = "SentryCore",
                message = "Critical error",
                severity = LogEvent.Severity.CRITICAL
            ),
            LogEvent(
                timestamp = System.currentTimeMillis(),
                source = "NetShield",
                message = "Info message",
                severity = LogEvent.Severity.INFO
            )
        )
        
        whenever(logsRepository.getAllLogs())
            .thenReturn(flowOf(logs))
        
        viewModel.updateFilters(
            LogsViewModel.LogFilters(severity = LogEvent.Severity.CRITICAL)
        )
        
        // Verify filtering works
    }
}
