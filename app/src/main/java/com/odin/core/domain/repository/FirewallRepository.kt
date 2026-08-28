package com.odin.core.domain.repository

import com.odin.core.domain.model.FirewallRule
import com.odin.core.domain.model.NetworkPacket
import kotlinx.coroutines.flow.Flow

/**
 * FirewallRepository proporciona acceso a reglas de firewall.
 */
interface FirewallRepository {
    /**
     * Obtiene todas las reglas.
     */
    fun getAllRules(): Flow<List<FirewallRule>>
    
    /**
     * Obtiene reglas activas.
     */
    fun getActiveRules(): Flow<List<FirewallRule>>
    
    /**
     * Crea una nueva regla.
     */
    suspend fun createRule(rule: FirewallRule)
    
    /**
     * Actualiza una regla.
     */
    suspend fun updateRule(rule: FirewallRule)
    
    /**
     * Elimina una regla.
     */
    suspend fun deleteRule(ruleId: String)
    
    /**
     * Activa/desactiva una regla.
     */
    suspend fun toggleRule(ruleId: String, enabled: Boolean)

    /**
     * Evalúa si un paquete debe bloquearse según las reglas actuales.
     */
    fun isBlocked(packet: NetworkPacket): Boolean
}
