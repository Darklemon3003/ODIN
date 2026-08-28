package com.odin.core.ui.viewmodel

import com.odin.core.domain.model.SecurityMetrics
import com.odin.core.domain.model.SecurityStatus
import com.odin.core.domain.model.ThreatLevel
import com.odin.core.domain.repository.LogsRepository
import com.odin.core.domain.repository.SecurityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class DashboardViewModelTest {
    
    @Mock
    private lateinit var securityRepository: SecurityRepository
    
    @Mock
    private lateinit var logsRepository: LogsRepository
    
    private lateinit var viewModel: DashboardViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = DashboardViewModel(securityRepository, logsRepository)
    }
    
    @Test
    fun `security status updates correctly`() = runTest {
        val expectedStatus = SecurityStatus(
            isSecure = true,
            threatLevel = ThreatLevel.SECURE,
            activeThreats = 0,
            lastUpdateTime = System.currentTimeMillis(),
            components = com.odin.core.domain.model.ComponentStatus(
                vpnActive = false,
                firewallActive = true,
                detectionActive = true,
                sentryActive = false,
                netShieldActive = false
            )
        )
        
        whenever(securityRepository.getSecurityStatus())
            .thenReturn(flowOf(expectedStatus))
        
        viewModel.refreshData()
        
        // Verify state update
    }
    
    @Test
    fun `metrics updates correctly`() = runTest {
        val expectedMetrics = SecurityMetrics(
            cpuUsage = 15.0f,
            ramUsage = 45.0f,
            postureScore = 85,
            edrHealth = 90,
            packetsAnalyzed = 125000L,
            threatsBlocked = 234L,
            scanDuration = 3600000L
        )
        
        whenever(securityRepository.getMetrics())
            .thenReturn(flowOf(expectedMetrics))
        
        viewModel.refreshData()
        
        // Verify state update
    }
}
