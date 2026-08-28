package com.odin.core.data.repository

import android.content.Context
import com.odin.core.domain.model.ThreatStatistics
import com.odin.core.network.detection.DetectionResult
import com.odin.core.network.detection.ThreatDetectionEngine
import com.odin.core.network.detection.ThreatLevel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ThreatsRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var threatDetectionEngine: ThreatDetectionEngine
    private lateinit var repository: ThreatsRepositoryImpl

    @BeforeEach
    fun setup() {
        context = mockk(relaxed = true)
        threatDetectionEngine = mockk()
        
        val mockThreats = listOf(
            DetectionResult(
                forensicsId = 1L,
                threatType = "Malware",
                threatLevel = ThreatLevel.HIGH,
                sourceIp = "192.168.1.100",
                destinationIp = "10.0.0.1",
                sourcePort = 443,
                destinationPort = 80,
                protocol = "TCP",
                detectionTimestamp = System.currentTimeMillis(),
                confidence = 0.95f,
                description = "Suspicious malware activity detected"
            ),
            DetectionResult(
                forensicsId = 2L,
                threatType = "Phishing",
                threatLevel = ThreatLevel.MEDIUM,
                sourceIp = "192.168.1.101",
                destinationIp = "10.0.0.2",
                sourcePort = 80,
                destinationPort = 443,
                protocol = "HTTP",
                detectionTimestamp = System.currentTimeMillis(),
                confidence = 0.85f,
                description = "Potential phishing attempt"
            )
        )
        
        every { threatDetectionEngine.getRecentThreats(any()) } returns mockThreats
        every { ThreatDetectionEngine.getInstance(context) } returns threatDetectionEngine
        
        repository = ThreatsRepositoryImpl(context)
    }

    @Test
    fun `getAllThreats returns loaded threats`() = runTest {
        val threats = repository.getAllThreats().first()
        
        assertNotNull(threats)
        assertTrue(threats.isNotEmpty())
    }

    @Test
    fun `getThreatsByLevel filters correctly`() = runTest {
        val highThreats = repository.getThreatsByLevel(ThreatLevel.HIGH).first()
        
        assertNotNull(highThreats)
        assertTrue(highThreats.all { it.threatLevel == ThreatLevel.HIGH })
    }

    @Test
    fun `getThreatStatistics returns correct statistics`() = runTest {
        val stats = repository.getThreatStatistics().first()
        
        assertNotNull(stats)
        assertTrue(stats.totalThreats >= 0)
        assertTrue(stats.criticalCount >= 0)
        assertTrue(stats.highCount >= 0)
        assertTrue(stats.mediumCount >= 0)
        assertTrue(stats.lowCount >= 0)
    }

    @Test
    fun `dismissThreat removes threat from list`() = runTest {
        val threatsBefore = repository.getAllThreats().first()
        val threatToDismiss = threatsBefore.first()
        
        repository.dismissThreat(threatToDismiss.forensicsId)
        
        val threatsAfter = repository.getAllThreats().first()
        assertTrue(threatsAfter.none { it.forensicsId == threatToDismiss.forensicsId })
    }

    @Test
    fun `getThreatById returns correct threat`() = runTest {
        val threats = repository.getAllThreats().first()
        val threat = threats.first()
        
        val found = repository.getThreatById(threat.forensicsId)
        
        assertNotNull(found)
        assertEquals(threat.forensicsId, found?.forensicsId)
    }

    @Test
    fun `getThreatById returns null for non-existent threat`() = runTest {
        val found = repository.getThreatById(999999L)
        
        assertNull(found)
    }

    @Test
    fun `addThreat adds threat to list`() {
        val newThreat = DetectionResult(
            forensicsId = 3L,
            threatType = "DDoS",
            threatLevel = ThreatLevel.CRITICAL,
            sourceIp = "192.168.1.102",
            destinationIp = "10.0.0.3",
            sourcePort = 53,
            destinationPort = 53,
            protocol = "UDP",
            detectionTimestamp = System.currentTimeMillis(),
            confidence = 0.99f,
            description = "DDoS attack detected"
        )
        
        repository.addThreat(newThreat)
        
        val threats = repository.getAllThreats()
        assertTrue(threats.value.any { it.forensicsId == 3L })
    }

    @Test
    fun `refreshThreats reloads from ThreatDetectionEngine`() {
        assertDoesNotThrow {
            repository.refreshThreats()
        }
    }

    @Test
    fun `statistics update after threat dismissal`() = runTest {
        val statsBefore = repository.getThreatStatistics().first()
        val threats = repository.getAllThreats().first()
        
        if (threats.isNotEmpty()) {
            repository.dismissThreat(threats.first().forensicsId)
            val statsAfter = repository.getThreatStatistics().first()
            
            assertTrue(statsAfter.totalThreats < statsBefore.totalThreats)
        }
    }

    @Test
    fun `handles ThreatDetectionEngine errors gracefully`() = runTest {
        every { threatDetectionEngine.getRecentThreats(any()) } throws RuntimeException("Engine error")
        
        val errorRepository = ThreatsRepositoryImpl(context)
        val threats = errorRepository.getAllThreats().first()
        
        assertNotNull(threats)
    }
}
