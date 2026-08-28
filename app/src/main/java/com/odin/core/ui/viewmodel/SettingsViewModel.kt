package com.odin.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odin.core.domain.model.GeneralSettings
import com.odin.core.domain.model.NetworkSettings
import com.odin.core.domain.model.SecuritySettings
import com.odin.core.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _generalSettings = MutableStateFlow(GeneralSettings())
    val generalSettings: StateFlow<GeneralSettings> = _generalSettings.asStateFlow()
    
    private val _securitySettings = MutableStateFlow(SecuritySettings())
    val securitySettings: StateFlow<SecuritySettings> = _securitySettings.asStateFlow()
    
    private val _networkSettings = MutableStateFlow(NetworkSettings())
    val networkSettings: StateFlow<NetworkSettings> = _networkSettings.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        _isLoading.value = true

        combine(
            settingsRepository.getGeneralSettings(),
            settingsRepository.getSecuritySettings(),
            settingsRepository.getNetworkSettings()
        ) { general, security, network ->
            Triple(general, security, network)
        }
            .onEach { (general, security, network) ->
                _generalSettings.value = general
                _securitySettings.value = security
                _networkSettings.value = network
                _isLoading.value = false
            }
            .catch { e ->
                android.util.Log.e("SettingsViewModel", "Error loading settings: ${e.message}", e)
                _isLoading.value = false
            }
            .launchIn(viewModelScope)
    }
    
    fun updateGeneralSettings(settings: GeneralSettings) {
        viewModelScope.launch {
            try {
                settingsRepository.saveGeneralSettings(settings)
            } catch (e: Exception) {
                android.util.Log.e("SettingsViewModel", "Error saving general settings: ${e.message}", e)
            }
        }
    }
    
    fun updateSecuritySettings(settings: SecuritySettings) {
        viewModelScope.launch {
            try {
                settingsRepository.saveSecuritySettings(settings)
            } catch (e: Exception) {
                android.util.Log.e("SettingsViewModel", "Error saving security settings: ${e.message}", e)
            }
        }
    }
    
    fun updateNetworkSettings(settings: NetworkSettings) {
        viewModelScope.launch {
            try {
                settingsRepository.saveNetworkSettings(settings)
            } catch (e: Exception) {
                android.util.Log.e("SettingsViewModel", "Error saving network settings: ${e.message}", e)
            }
        }
    }
}
