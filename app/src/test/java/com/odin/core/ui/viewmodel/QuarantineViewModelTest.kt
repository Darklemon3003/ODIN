package com.odin.core.ui.viewmodel

import com.odin.core.domain.model.QuarantinedApp
import com.odin.core.domain.repository.QuarantineRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class QuarantineViewModelTest {
    
    @Mock
    private lateinit var quarantineRepository: QuarantineRepository
    
    private lateinit var viewModel: QuarantineViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = QuarantineViewModel(quarantineRepository)
    }
    
    @Test
    fun `release app calls repository`() = runTest {
        val app = QuarantinedApp(
            packageName = "com.example.app",
            appName = "Test App",
            quarantineReason = "Suspicious behavior",
            quarantineDate = System.currentTimeMillis(),
            riskScore = 80,
            behaviorScore = 60,
            permissions = emptyList(),
            isMonitored = true
        )
        
        viewModel.releaseApp(app)
        
        verify(quarantineRepository).releaseApp(app.packageName)
    }
    
    @Test
    fun `delete app calls repository`() = runTest {
        val app = QuarantinedApp(
            packageName = "com.example.app",
            appName = "Test App",
            quarantineReason = "Suspicious behavior",
            quarantineDate = System.currentTimeMillis(),
            riskScore = 80,
            behaviorScore = 60,
            permissions = emptyList(),
            isMonitored = true
        )
        
        viewModel.deleteApp(app)
        
        verify(quarantineRepository).deleteApp(app.packageName)
    }
    
    @Test
    fun `quarantine app calls repository`() = runTest {
        viewModel.quarantineApp("com.example.app", "Malware detected")
        
        verify(quarantineRepository).quarantineApp("com.example.app", "Malware detected")
    }
}
