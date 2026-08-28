package com.odin.core.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odin.core.domain.model.BehaviorData
import com.odin.core.domain.model.QuarantinedApp
import com.odin.core.domain.repository.QuarantineRepository
import com.odin.core.security.ConsentManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuarantineViewModel @Inject constructor(
    private val quarantineRepository: QuarantineRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val _quarantinedApps = MutableStateFlow<List<QuarantinedApp>>(emptyList())
    val quarantinedApps: StateFlow<List<QuarantinedApp>> = _quarantinedApps.asStateFlow()
    
    private val _selectedApp = MutableStateFlow<QuarantinedApp?>(null)
    val selectedApp: StateFlow<QuarantinedApp?> = _selectedApp.asStateFlow()
    
    private val _behaviorData = MutableStateFlow<BehaviorData?>(null)
    val behaviorData: StateFlow<BehaviorData?> = _behaviorData.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadQuarantinedApps()
    }
    
    private fun loadQuarantinedApps() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                quarantineRepository.getAllQuarantinedApps().collect { apps ->
                    _quarantinedApps.value = apps
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                android.util.Log.e("QuarantineViewModel", "Error loading quarantined apps: ${e.message}", e)
                _isLoading.value = false
            }
        }
    }
    
    fun selectApp(app: QuarantinedApp) {
        _selectedApp.value = app
        _behaviorData.value = null
    }
    
    fun clearSelectedApp() {
        _selectedApp.value = null
        _behaviorData.value = null
    }
    
    private fun loadBehaviorData(packageName: String) {
        viewModelScope.launch {
            try {
                quarantineRepository.getBehaviorData(packageName).collect { data ->
                    _behaviorData.value = data
                }
            } catch (e: Exception) {
                android.util.Log.e("QuarantineViewModel", "Error loading behavior data: ${e.message}", e)
            }
        }
    }
    
    fun releaseApp(app: QuarantinedApp) {
        viewModelScope.launch {
            try {
                quarantineRepository.releaseApp(app.packageName)
            } catch (e: Exception) {
                android.util.Log.e("QuarantineViewModel", "Error releasing app: ${e.message}", e)
            }
        }
    }
    
    fun deleteApp(app: QuarantinedApp) {
        viewModelScope.launch {
            try {
                if (!ConsentManager.getInstance(context).isQuarantineActionsEnabled) {
                    android.util.Log.w("QuarantineViewModel", "Delete blocked: missing user consent")
                    return@launch
                }
                quarantineRepository.deleteApp(app.packageName)
            } catch (e: Exception) {
                android.util.Log.e("QuarantineViewModel", "Error deleting app: ${e.message}", e)
            }
        }
    }
    
    fun keepApp(app: QuarantinedApp) {
        viewModelScope.launch {
            try {
                if (!ConsentManager.getInstance(context).isQuarantineActionsEnabled) {
                    android.util.Log.w("QuarantineViewModel", "Keep blocked: missing user consent")
                    return@launch
                }
                quarantineRepository.keepApp(app.packageName)
            } catch (e: Exception) {
                android.util.Log.e("QuarantineViewModel", "Error keeping app: ${e.message}", e)
            }
        }
    }
    
    fun quarantineApp(packageName: String, reason: String) {
        viewModelScope.launch {
            try {
                if (!ConsentManager.getInstance(context).isQuarantineActionsEnabled) {
                    android.util.Log.w("QuarantineViewModel", "Quarantine blocked: missing user consent")
                    return@launch
                }
                quarantineRepository.quarantineApp(packageName, reason)
            } catch (e: Exception) {
                android.util.Log.e("QuarantineViewModel", "Error quarantining app: ${e.message}", e)
            }
        }
    }
}
