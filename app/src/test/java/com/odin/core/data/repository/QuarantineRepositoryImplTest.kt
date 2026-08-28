package com.odin.core.data.repository

import android.content.Context
import com.odin.core.domain.model.BehaviorData
import com.odin.core.domain.model.QuarantinedApp
import com.odin.core.domain.model.ResourceUsage
import com.odin.core.domain.model.SuspiciousActivity
import com.odin.core.domain.model.ThreatLevel
import com.odin.core.fileanalysis.quarantine.QuarantineManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class QuarantineRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var quarantineManager: QuarantineManager
    private lateinit var repository: QuarantineRepositoryImpl

    @BeforeEach
    fun setup() {
        context = mockk(relaxed = true)
        quarantineManager = mockk()
        
        val mockApps = listOf(
            QuarantinedApp(
                packageName = "com.suspicious.app1",
                appName = "Suspicious App 1",
                quarantineReason = "Malware detected",
                quarantineDate = System.currentTimeMillis(),
                riskScore = 85,
                behaviorScore = 70,
                permissions = listOf("INTERNET", "READ_CONTACTS"),
                isMonitored = true
            ),
            QuarantinedApp(
                packageName = "com.suspicious.app2",
                appName = "Suspicious App 2",
                quarantineReason = "Phishing attempt",
                quarantineDate = System.currentTimeMillis(),
                riskScore = 90,
                behaviorScore = 65,
                permissions = listOf("INTERNET", "SEND_SMS"),
                isMonitored = true
            )
        )
        
        val mockBehaviorData = BehaviorData(
            packageName = "com.suspicious.app1",
            networkRequests = 150,
            suspiciousActivities = listOf(
                SuspiciousActivity(
                    type = "Data Exfiltration",
                    description = "Sending data to unknown server",
                    timestamp = System.currentTimeMillis(),
                    severity = ThreatLevel.HIGH
                )
            ),
            resourceUsage = ResourceUsage(
                cpuUsage = 25.5f,
                memoryUsage = 45.2f,
                networkUsage = 1024000L,
                batteryUsage = 15.3f
            ),
            lastUpdated = System.currentTimeMillis()
        )
        
        every { quarantineManager.getQuarantinedApps() } returns mockApps
        every { quarantineManager.getBehaviorData(any()) } returns mockBehaviorData
        every { quarantineManager.getRiskScore(any()) } returns 85
        every { quarantineManager.getBehaviorScore(any()) } returns 70
        every { quarantineManager.getAppPermissions(any()) } returns listOf("INTERNET", "READ_CONTACTS")
        every { QuarantineManager.getInstance(context) } returns quarantineManager
        every { quarantineManager.releaseApp(any()) } returns Unit
        every { quarantineManager.deleteApp(any()) } returns Unit
        every { quarantineManager.keepApp(any()) } returns Unit
        every { quarantineManager.quarantineApp(any(), any()) } returns Unit
        
        repository = QuarantineRepositoryImpl(context)
    }

    @Test
    fun `getAllQuarantinedApps returns loaded apps`() = runTest {
        val apps = repository.getAllQuarantinedApps().first()
        
        assertNotNull(apps)
        assertTrue(apps.isNotEmpty())
    }

    @Test
    fun `getBehaviorData returns behavior data for package`() = runTest {
        val behaviorData = repository.getBehaviorData("com.suspicious.app1").first()
        
        assertNotNull(behaviorData)
        assertEquals("com.suspicious.app1", behaviorData.packageName)
    }

    @Test
    fun `releaseApp removes app from quarantine`() = runTest {
        val appsBefore = repository.getAllQuarantinedApps().first()
        val appToRelease = appsBefore.first()
        
        repository.releaseApp(appToRelease.packageName)
        
        val appsAfter = repository.getAllQuarantinedApps().first()
        assertTrue(appsAfter.none { it.packageName == appToRelease.packageName })
    }

    @Test
    fun `deleteApp removes app from quarantine`() = runTest {
        val appsBefore = repository.getAllQuarantinedApps().first()
        val appToDelete = appsBefore.first()
        
        repository.deleteApp(appToDelete.packageName)
        
        val appsAfter = repository.getAllQuarantinedApps().first()
        assertTrue(appsAfter.none { it.packageName == appToDelete.packageName })
    }

    @Test
    fun `keepApp does not remove app from quarantine`() = runTest {
        val appsBefore = repository.getAllQuarantinedApps().first()
        val appToKeep = appsBefore.first()
        
        repository.keepApp(appToKeep.packageName)
        
        val appsAfter = repository.getAllQuarantinedApps().first()
        assertTrue(appsAfter.any { it.packageName == appToKeep.packageName })
    }

    @Test
    fun `quarantineApp adds app to quarantine`() = runTest {
        val packageName = "com.new.suspicious.app"
        val reason = "Suspicious behavior detected"
        
        repository.quarantineApp(packageName, reason)
        
        val apps = repository.getAllQuarantinedApps().first()
        assertTrue(apps.any { it.packageName == packageName })
    }

    @Test
    fun `refreshQuarantinedApps reloads from QuarantineManager`() {
        assertDoesNotThrow {
            repository.refreshQuarantinedApps()
        }
    }

    @Test
    fun `handles QuarantineManager errors gracefully`() = runTest {
        every { quarantineManager.getQuarantinedApps() } throws RuntimeException("Manager error")
        
        val errorRepository = QuarantineRepositoryImpl(context)
        val apps = errorRepository.getAllQuarantinedApps().first()
        
        assertNotNull(apps)
    }

    @Test
    fun `releaseApp handles errors gracefully`() = runTest {
        every { quarantineManager.releaseApp(any()) } throws RuntimeException("Release failed")
        
        val apps = repository.getAllQuarantinedApps().first()
        if (apps.isNotEmpty()) {
            assertDoesNotThrow {
                repository.releaseApp(apps.first().packageName)
            }
        }
    }

    @Test
    fun `quarantineApp creates app with correct data`() = runTest {
        val packageName = "com.test.app"
        val reason = "Test quarantine"
        
        repository.quarantineApp(packageName, reason)
        
        val apps = repository.getAllQuarantinedApps().first()
        val quarantinedApp = apps.find { it.packageName == packageName }
        
        assertNotNull(quarantinedApp)
        assertEquals(reason, quarantinedApp?.quarantineReason)
        assertTrue(quarantinedApp?.isMonitored == true)
    }
}
