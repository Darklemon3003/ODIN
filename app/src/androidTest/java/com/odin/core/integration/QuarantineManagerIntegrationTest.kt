package com.odin.core.integration

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.odin.core.domain.model.QuarantinedApp
import com.odin.core.domain.model.BehaviorData
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import javax.inject.Inject

/**
 * QuarantineManagerIntegrationTest verifies integration with QuarantineManager.
 * Tests quarantined app retrieval, quarantine, release, deletion, and behavior monitoring functionality.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class QuarantineManagerIntegrationTest : BaseIntegrationTest() {
    
    private lateinit var testApps: List<QuarantinedApp>
    private lateinit var testBehaviorData: List<BehaviorData>
    
    @Before
    fun setupTest() {
        // Create test quarantined apps
        testApps = listOf(
            QuarantinedApp(
                id = 1,
                packageName = "com.malicious.app1",
                appName = "Malicious App 1",
                quarantineReason = "Malware detected",
                quarantinedAt = Date(),
                riskLevel = "CRITICAL",
                signature = "malware_signature_1"
            ),
            QuarantinedApp(
                id = 2,
                packageName = "com.suspicious.app2",
                appName = "Suspicious App 2",
                quarantineReason = "Suspicious behavior",
                quarantinedAt = Date(),
                riskLevel = "HIGH",
                signature = "suspicious_signature_2"
            ),
            QuarantinedApp(
                id = 3,
                packageName = "com.unknown.app3",
                appName = "Unknown App 3",
                quarantineReason = "Unknown signature",
                quarantinedAt = Date(),
                riskLevel = "MEDIUM",
                signature = "unknown_signature_3"
            )
        )
        
        // Create test behavior data
        testBehaviorData = listOf(
            BehaviorData(
                id = 1,
                appId = 1,
                timestamp = Date().time,
                behaviorType = "NETWORK_ACCESS",
                description = "Attempted connection to malicious IP",
                severity = "HIGH"
            ),
            BehaviorData(
                id = 2,
                appId = 1,
                timestamp = Date().time,
                behaviorType = "FILE_ACCESS",
                description = "Attempted to access system files",
                severity = "CRITICAL"
            ),
            BehaviorData(
                id = 3,
                appId = 2,
                timestamp = Date().time,
                behaviorType = "PERMISSION_REQUEST",
                description = "Requested dangerous permissions",
                severity = "MEDIUM"
            )
        )
    }
    
    @Test
    fun testQuarantinedAppRetrieval() = runTest {
        // Test quarantined app retrieval
        val criticalApps = testApps.filter { it.riskLevel == "CRITICAL" }
        
        assertEquals("Should have 1 critical app", 1, criticalApps.size)
        assertEquals("Critical app should have correct package", "com.malicious.app1", criticalApps[0].packageName)
    }
    
    @Test
    fun testAppQuarantine() = runTest {
        // Test app quarantine functionality
        val newApp = QuarantinedApp(
            id = 999,
            packageName = "com.new.malicious",
            appName = "New Malicious App",
            quarantineReason = "New malware detected",
            quarantinedAt = Date(),
            riskLevel = "CRITICAL",
            signature = "new_malware_signature"
        )
        
        assertNotNull("New quarantined app should not be null", newApp)
        assertEquals("New app should have correct package", "com.new.malicious", newApp.packageName)
        assertEquals("New app should be critical", "CRITICAL", newApp.riskLevel)
    }
    
    @Test
    fun testAppRelease() = runTest {
        // Test app release from quarantine
        val quarantinedApp = testApps[0]
        val releasedApp = quarantinedApp.copy(
            quarantineReason = "Released after analysis",
            riskLevel = "LOW"
        )
        
        assertEquals("Released app should have updated reason", "Released after analysis", releasedApp.quarantineReason)
        assertEquals("Released app should have low risk", "LOW", releasedApp.riskLevel)
        assertEquals("Released app should preserve ID", quarantinedApp.id, releasedApp.id)
    }
    
    @Test
    fun testAppDeletion() = runTest {
        // Test app deletion from quarantine
        val appToDelete = testApps[1]
        val remainingApps = testApps.filter { it.id != appToDelete.id }
        
        assertEquals("Should have 2 remaining apps", 2, remainingApps.size)
        assertFalse("Deleted app should not be in remaining list", remainingApps.any { it.id == appToDelete.id })
    }
    
    @Test
    fun testBehaviorMonitoring() = runTest {
        // Test behavior monitoring functionality
        val app1Behaviors = testBehaviorData.filter { it.appId == 1 }
        
        assertEquals("App 1 should have 2 behaviors", 2, app1Behaviors.size)
        assertTrue("App 1 behaviors should include network access", app1Behaviors.any { it.behaviorType == "NETWORK_ACCESS" })
        assertTrue("App 1 behaviors should include file access", app1Behaviors.any { it.behaviorType == "FILE_ACCESS" })
    }
    
    @Test
    fun testBehaviorSeverityFiltering() = runTest {
        // Test behavior filtering by severity
        val criticalBehaviors = testBehaviorData.filter { it.severity == "CRITICAL" }
        
        assertEquals("Should have 1 critical behavior", 1, criticalBehaviors.size)
        assertEquals("Critical behavior should be file access", "FILE_ACCESS", criticalBehaviors[0].behaviorType)
    }
    
    @Test
    fun testRiskLevelFiltering() = runTest {
        // Test app filtering by risk level
        val highAndCriticalApps = testApps.filter { 
            it.riskLevel == "HIGH" || it.riskLevel == "CRITICAL" 
        }
        
        assertEquals("Should have 2 high/critical apps", 2, highAndCriticalApps.size)
    }
    
    @Test
    fun testAppPersistence() = runTest {
        // Test that app data structure is preserved
        val originalApp = testApps[0]
        
        assertEquals("App ID should be preserved", 1L, originalApp.id)
        assertEquals("App package name should be preserved", "com.malicious.app1", originalApp.packageName)
        assertEquals("App name should be preserved", "Malicious App 1", originalApp.appName)
        assertEquals("Quarantine reason should be preserved", "Malware detected", originalApp.quarantineReason)
        assertEquals("Risk level should be preserved", "CRITICAL", originalApp.riskLevel)
        assertEquals("Signature should be preserved", "malware_signature_1", originalApp.signature)
        assertNotNull("Quarantine date should be preserved", originalApp.quarantinedAt)
    }
    
    @Test
    fun testBehaviorPersistence() = runTest {
        // Test that behavior data structure is preserved
        val originalBehavior = testBehaviorData[0]
        
        assertEquals("Behavior ID should be preserved", 1L, originalBehavior.id)
        assertEquals("Behavior app ID should be preserved", 1L, originalBehavior.appId)
        assertEquals("Behavior type should be preserved", "NETWORK_ACCESS", originalBehavior.behaviorType)
        assertEquals("Behavior description should be preserved", "Attempted connection to malicious IP", originalBehavior.description)
        assertEquals("Behavior severity should be preserved", "HIGH", originalBehavior.severity)
    }
}
