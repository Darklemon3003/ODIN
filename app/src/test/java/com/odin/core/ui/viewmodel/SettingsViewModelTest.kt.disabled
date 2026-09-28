package com.odin.core.ui.viewmodel

import com.odin.core.domain.model.GeneralSettings
import com.odin.core.domain.model.SecuritySettings
import com.odin.core.domain.model.NetworkSettings
import com.odin.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class SettingsViewModelTest {
    
    @Mock
    private lateinit var settingsRepository: SettingsRepository
    
    private lateinit var viewModel: SettingsViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = SettingsViewModel(settingsRepository)
    }
    
    @Test
    fun `update general settings calls repository`() = runTest {
        val settings = GeneralSettings(
            theme = com.odin.core.domain.model.ThemeMode.DARK,
            language = "es",
            notificationsEnabled = true,
            autoUpdateEnabled = true
        )
        
        viewModel.updateGeneralSettings(settings)
        
        verify(settingsRepository).saveGeneralSettings(settings)
    }
    
    @Test
    fun `update security settings calls repository`() = runTest {
        val settings = SecuritySettings(
            sensitivityLevel = com.odin.core.domain.model.SensitivityLevel.MEDIUM,
            autoResponseEnabled = true,
            realTimeScanning = true,
            heuristicAnalysis = true
        )
        
        viewModel.updateSecuritySettings(settings)
        
        verify(settingsRepository).saveSecuritySettings(settings)
    }
    
    @Test
    fun `update network settings calls repository`() = runTest {
        val settings = NetworkSettings(
            vpnEnabled = false,
            torEnabled = false,
            dnsProvider = com.odin.core.domain.model.DNSProvider.GOOGLE,
            proxyEnabled = false,
            proxyAddress = "",
            proxyPort = 8080
        )
        
        viewModel.updateNetworkSettings(settings)
        
        verify(settingsRepository).saveNetworkSettings(settings)
    }
}
