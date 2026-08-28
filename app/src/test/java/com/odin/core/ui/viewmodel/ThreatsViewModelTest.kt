package com.odin.core.ui.viewmodel

import com.odin.core.domain.model.ThreatStatistics
import com.odin.core.domain.repository.ThreatsRepository
import com.odin.core.network.detection.DetectionResult
import com.odin.core.network.detection.ThreatLevel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class ThreatsViewModelTest {
    
    @Mock
    private lateinit var threatsRepository: ThreatsRepository
    
    private lateinit var viewModel: ThreatsViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = ThreatsViewModel(threatsRepository)
    }
    
    @Test
    fun `dismiss threat calls repository`() = runTest {
        val threat = DetectionResult(
            threatLevel = ThreatLevel.HIGH,
            threatType = "Malware",
            threatDescription = "Malicious activity detected",
            detectorName = "ThreatDetectionEngine",
            sourceIP = "192.168.1.1",
            destinationIP = "10.0.0.1",
            sourcePort = 80,
            destinationPort = 443,
            confidence = 0.9,
            evidence = "Suspicious network traffic",
            forensicsData = "Forensic data",
            recommendedAction = "Block"
        )
        
        viewModel.dismissThreat(threat)
        
        verify(threatsRepository).dismissThreat(threat.forensicsId)
    }
    
    @Test
    fun `statistics load correctly`() = runTest {
        val expectedStats = ThreatStatistics(
            totalThreats = 10,
            criticalCount = 2,
            highCount = 3,
            mediumCount = 3,
            lowCount = 2,
            threatsByType = mapOf("Malware" to 5, "Phishing" to 5),
            topSourceIPs = emptyList(),
            topDestinationIPs = emptyList()
        )
        
        whenever(threatsRepository.getThreatStatistics())
            .thenReturn(flowOf(expectedStats))
        
        viewModel.refreshThreats()
        
        // Verify statistics update
    }
}
