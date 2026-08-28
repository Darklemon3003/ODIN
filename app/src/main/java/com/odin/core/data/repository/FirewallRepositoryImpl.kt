package com.odin.core.data.repository

import com.odin.core.data.local.SecureLocalStorage
import com.odin.core.domain.model.FirewallRule
import com.odin.core.domain.model.NetworkPacket
import com.odin.core.domain.repository.FirewallRepository
import com.odin.core.network.rules.DynamicRuleEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirewallRepositoryImpl @Inject constructor() : FirewallRepository {

    constructor(storage: SecureLocalStorage) : this()
    
    private val dynamicRuleEngine = DynamicRuleEngine()
    
    private val _rules = MutableStateFlow<List<FirewallRule>>(emptyList())
    
    init {
        loadRealRules()
    }
    
    private fun loadRealRules() {
        _rules.value = emptyList()
    }
    
    override fun getAllRules(): Flow<List<FirewallRule>> {
        return _rules.asStateFlow()
    }
    
    override fun getActiveRules(): Flow<List<FirewallRule>> {
        return flow {
            emit(_rules.value.filter { it.isEnabled() })
        }
    }
    
    override suspend fun createRule(rule: FirewallRule) {
        throw UnsupportedOperationException(
            "FirewallRepositoryImpl cannot create domain FirewallRule entries because DynamicRuleEngine exposes a different rule model."
        )
    }
    
    override suspend fun updateRule(rule: FirewallRule) {
        throw UnsupportedOperationException(
            "FirewallRepositoryImpl cannot update domain FirewallRule entries because DynamicRuleEngine exposes a different rule model."
        )
    }
    
    override suspend fun deleteRule(ruleId: String) {
        try {
            dynamicRuleEngine.removeRule(ruleId)
            _rules.value = _rules.value.filterNot { it.id == ruleId }
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    override suspend fun toggleRule(ruleId: String, enabled: Boolean) {
        throw UnsupportedOperationException(
            "FirewallRepositoryImpl cannot toggle domain FirewallRule entries because DynamicRuleEngine exposes a different rule model."
        )
    }

    override fun isBlocked(packet: NetworkPacket): Boolean {
        return _rules.value.any { rule ->
            rule.isEnabled() && rule.matches(packet)
        }
    }
    
    fun refreshRules() {
        loadRealRules()
    }
}
