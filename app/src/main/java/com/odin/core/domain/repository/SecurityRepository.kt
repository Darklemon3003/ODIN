package com.odin.core.domain.repository

import com.odin.core.domain.model.SecurityMetrics
import com.odin.core.domain.model.SecurityStatus
import kotlinx.coroutines.flow.Flow

/**
 * SecurityRepository proporciona acceso a datos de seguridad.
 */
interface SecurityRepository {
    /**
     * Obtiene el estado de seguridad actual.
     */
    fun getSecurityStatus(): Flow<SecurityStatus>
    
    /**
     * Obtiene las métricas de seguridad.
     */
    fun getMetrics(): Flow<SecurityMetrics>
    
    /**
     * Actualiza el estado de seguridad.
     */
    suspend fun updateSecurityStatus(status: SecurityStatus)
}
