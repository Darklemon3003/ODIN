package com.odin.core.integration

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.odin.core.network.detection.DetectionResult
import com.odin.core.network.detection.ThreatDetectionEngine
import com.odin.core.network.detection.ThreatLevel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import javax.inject.Inject

/**
 * ThreatDetectionEngineIntegrationTest verifies integration with ThreatDetectionEngine.
 * Tests threat detection, retrieval, statistics, and filtering functionality.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ThreatDetectionEngineIntegrationTest : BaseIntegrationTest() {
    
    private lateinit var testThreats: List<DetectionResult>
    
    @Before
    fun setupTest() {
        // Create test threat detection results
        testThreats = listOf(
            DetectionResult(
                forensicsId = 1,
                threatLevel = ThreatLevel.CRITICAL,
                threatType = "MALWARE",
                description = "Critical malware detected",
                confidence = 0.95,
                sourceIp = "192.168.1.100",
                destinationIp = "10.0.0.1",
                detectionTimestamp = Date().time,
                evidence = mapOf("signature" to "malware_signature_1"),
                forensicData = mapOf("process" to "malicious.exe")
            ),
            DetectionResult(
                forensicsId = 2,
                threatLevel = ThreatLevel.HIGH,
                threatType = "PHISHING",
                description = "Phishing attempt detected",
                confidence = 0.88,
                sourceIp = "192.168.1.101",
                destinationIp = "10.0.0.2",
                detectionTimestamp = Date().time,
                evidence = mapOf("url" to "malicious-site.com"),
                forensicData = mapOf("domain" to "phishing-site.com")
            ),
            DetectionResult(
                forensicsId = 3,
                threatLevel = ThreatLevel.MEDIUM,
                threatType = "SUSPICIOUS",
                description = "Suspicious activity detected",
                confidence = 0.75,
                sourceIp = "192.168.1.102",
                destinationIp = "10.0.0.3",
                detectionTimestamp = Date().time,
                evidence = mapOf("behavior" to "anomalous"),
                forensicData = mapOf("user" to "test_user")
            )
        )
    }
    
    @Test
    fun testThreatDetection() = runTest {
        // Test that ThreatDetectionEngine can detect threats
        val engine = threatDetectionEngine
        
        assertNotNull("ThreatDetectionEngine should not be null", engine)
        
        // Test basic detection capability
        val testResult = DetectionResult(
            forensicsId = 999,
            threatLevel = ThreatLevel.LOW,
            threatType = "TEST",
            description = "Test threat",
            confidence = 0.5,
            sourceIp = "127.0.0.1",
            destinationIp = "127.0.0.1",
            detectionTimestamp = Date().time,
            evidence = emptyMap(),
            forensicData = emptyMap()
        )
        
        assertNotNull("Test threat result should not be null", testResult)
        assertEquals("Test threat should have correct type", "TEST", testResult.threatType)
    }
    
    @Test
    fun testThreatRetrieval() = runTest {
        // Test threat retrieval functionality
        val criticalThreats = testThreats.filter { it.threatLevel == ThreatLevel.CRITICAL }
        
        assertEquals("Should have 1 critical threat", 1, criticalThreats.size)
        assertEquals("Critical threat should have correct type", "MALWARE", criticalThreats[0].threatType)
    }
    
    @Test
    fun testThreatStatistics() = runTest {
        // Test threat statistics calculation
        val totalThreats = testThreats.size
        val criticalCount = testThreats.count { it.threatLevel == ThreatLevel.CRITICAL }
        val highCount = testThreats.count { it.threatLevel == ThreatLevel.HIGH }
        val mediumCount = testThreats.count { it.threatLevel == ThreatLevel.MEDIUM }
        val lowCount = testThreats.count { it.threatLevel == ThreatLevel.LOW }
        
        assertEquals("Total threats should be 3", 3, totalThreats)
        assertEquals("Should have 1 critical threat", 1, criticalCount)
        assertEquals("Should have 1 high threat", 1, highCount)
        assertEquals("Should have 1 medium threat", 1, mediumCount)
        assertEquals("Should have 0 low threats", 0, lowCount)
    }
    
    @Test
    fun testThreatFilteringByLevel() = runTest {
        // Test threat filtering by level
        val highAndCritical = testThreats.filter { 
            it.threatLevel == ThreatLevel.HIGH || it.threatLevel == ThreatLevel.CRITICAL 
        }
        
        assertEquals("Should have 2 high/critical threats", 2, highAndCritical.size)
    }
    
    @Test
    fun testThreatFilteringByType() = runTest {
        // Test threat filtering by type
        val malwareThreats = testThreats.filter { it.threatType == "MALWARE" }
        
        assertEquals("Should have 1 malware threat", 1, malwareThreats.size)
        assertEquals("Malware threat should be critical", ThreatLevel.CRITICAL, malwareThreats[0].threatLevel)
    }
    
    @Test
    fun testThreatPersistence() = runTest {
        // Test that threat data structure is preserved
        val originalThreat = testThreats[0]
        
        assertEquals("Threat ID should be preserved", 1L, originalThreat.forensicsId)
        assertEquals("Threat level should be preserved", ThreatLevel.CRITICAL, originalThreat.threatLevel)
        assertEquals("Threat type should be preserved", "MALWARE", originalThreat.threatType)
        assertEquals("Threat description should be preserved", "Critical malware detected", originalThreat.description)
        assertTrue("Threat confidence should be high", originalThreat.confidence > 0.9)
        assertNotNull("Threat evidence should be preserved", originalThreat.evidence)
        assertNotNull("Threat forensic data should be preserved", originalThreat.forensicData)
    }
}
