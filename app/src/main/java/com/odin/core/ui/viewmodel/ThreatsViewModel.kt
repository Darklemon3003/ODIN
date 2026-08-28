package com.odin.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odin.core.domain.model.ThreatStatistics
import com.odin.core.domain.repository.ThreatsRepository
import com.odin.core.network.detection.DetectionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThreatsViewModel @Inject constructor(
    private val threatsRepository: ThreatsRepository
) : ViewModel() {
    
    private val _threats = MutableStateFlow<List<DetectionResult>>(emptyList())
    val threats: StateFlow<List<DetectionResult>> = _threats.asStateFlow()
    
    private val _statistics = MutableStateFlow<ThreatStatistics?>(null)
    val statistics: StateFlow<ThreatStatistics?> = _statistics.asStateFlow()
    
    private val _selectedThreat = MutableStateFlow<DetectionResult?>(null)
    val selectedThreat: StateFlow<DetectionResult?> = _selectedThreat.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadThreats()
    }
    
    private fun loadThreats() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                threatsRepository.getAllThreats().collect { threats ->
                    _threats.value = threats
                }
            } catch (e: Exception) {
                android.util.Log.e("ThreatsViewModel", "Error loading threats: ${e.message}", e)
            }
            
            try {
                threatsRepository.getThreatStatistics().collect { stats ->
                    _statistics.value = stats
                }
            } catch (e: Exception) {
                android.util.Log.e("ThreatsViewModel", "Error loading statistics: ${e.message}", e)
            }
            
            _isLoading.value = false
        }
    }
    
    fun selectThreat(threat: DetectionResult) {
        _selectedThreat.value = threat
    }
    
    fun clearSelectedThreat() {
        _selectedThreat.value = null
    }
    
    fun dismissThreat(threat: DetectionResult) {
        viewModelScope.launch {
            try {
                threatsRepository.dismissThreat(threat.forensicsId)
            } catch (e: Exception) {
                android.util.Log.e("ThreatsViewModel", "Error dismissing threat: ${e.message}", e)
            }
        }
    }
    
    fun refreshThreats() {
        loadThreats()
    }
}
