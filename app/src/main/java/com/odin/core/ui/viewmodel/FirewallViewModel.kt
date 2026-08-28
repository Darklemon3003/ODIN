package com.odin.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odin.core.domain.model.FirewallRule
import com.odin.core.domain.repository.FirewallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirewallViewModel @Inject constructor(
    private val firewallRepository: FirewallRepository
) : ViewModel() {
    
    private val _rules = MutableStateFlow<List<FirewallRule>>(emptyList())
    val rules: StateFlow<List<FirewallRule>> = _rules.asStateFlow()
    
    private val _selectedRule = MutableStateFlow<FirewallRule?>(null)
    val selectedRule: StateFlow<FirewallRule?> = _selectedRule.asStateFlow()
    
    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val validationErrors: StateFlow<Map<String, String>> = _validationErrors.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isCreatingRule = MutableStateFlow(false)
    val isCreatingRule: StateFlow<Boolean> = _isCreatingRule.asStateFlow()
    
    init {
        loadRules()
    }
    
    private fun loadRules() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firewallRepository.getAllRules().collect { rules ->
                    _rules.value = rules
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                android.util.Log.e("FirewallViewModel", "Error loading rules: ${e.message}", e)
                _isLoading.value = false
            }
        }
    }
    
    fun selectRule(rule: FirewallRule) {
        _selectedRule.value = rule
    }
    
    fun clearSelectedRule() {
        _selectedRule.value = null
    }
    
    fun toggleRule(rule: FirewallRule) {
        viewModelScope.launch {
            try {
                firewallRepository.toggleRule(rule.id, !rule.isEnabled())
            } catch (e: Exception) {
                android.util.Log.e("FirewallViewModel", "Error toggling rule: ${e.message}", e)
            }
        }
    }
    
    fun createRule(rule: FirewallRule) {
        viewModelScope.launch {
            _isCreatingRule.value = true
            val errors = validateRule(rule)
            if (errors.isEmpty()) {
                try {
                    firewallRepository.createRule(rule)
                    _validationErrors.value = emptyMap()
                } catch (e: Exception) {
                    android.util.Log.e("FirewallViewModel", "Error creating rule: ${e.message}", e)
                }
            } else {
                _validationErrors.value = errors
            }
            _isCreatingRule.value = false
        }
    }
    
    fun updateRule(rule: FirewallRule) {
        viewModelScope.launch {
            try {
                firewallRepository.updateRule(rule)
            } catch (e: Exception) {
                android.util.Log.e("FirewallViewModel", "Error updating rule: ${e.message}", e)
            }
        }
    }
    
    fun deleteRule(ruleId: String) {
        viewModelScope.launch {
            try {
                firewallRepository.deleteRule(ruleId)
            } catch (e: Exception) {
                android.util.Log.e("FirewallViewModel", "Error deleting rule: ${e.message}", e)
            }
        }
    }
    
    private fun validateRule(rule: FirewallRule): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        if (rule.name.isBlank()) errors["name"] = "Nombre requerido"
        if (rule.sourceIp.isBlank()) errors["sourceIp"] = "IP de origen requerida"
        if (rule.sourcePort < 0 || rule.sourcePort > 65535) errors["sourcePort"] = "Puerto inválido"
        if (rule.destinationIp.isBlank()) errors["destinationIp"] = "IP de destino requerida"
        if (rule.destinationPort < 0 || rule.destinationPort > 65535) errors["destinationPort"] = "Puerto inválido"
        if (rule.protocol.isBlank()) errors["protocol"] = "Protocolo requerido"
        return errors
    }
    
    fun clearValidationErrors() {
        _validationErrors.value = emptyMap()
    }
}
