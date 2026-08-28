package com.odin.core.domain.repository

import com.odin.core.domain.model.GeneralSettings
import com.odin.core.domain.model.NetworkSettings
import com.odin.core.domain.model.SecuritySettings
import kotlinx.coroutines.flow.Flow

/**
 * SettingsRepository proporciona acceso a configuraciones.
 */
interface SettingsRepository {
    /**
     * Obtiene configuraciones generales.
     */
    fun getGeneralSettings(): Flow<GeneralSettings>
    
    /**
     * Guarda configuraciones generales.
     */
    suspend fun saveGeneralSettings(settings: GeneralSettings)
    
    /**
     * Obtiene configuraciones de seguridad.
     */
    fun getSecuritySettings(): Flow<SecuritySettings>
    
    /**
     * Guarda configuraciones de seguridad.
     */
    suspend fun saveSecuritySettings(settings: SecuritySettings)
    
    /**
     * Obtiene configuraciones de red.
     */
    fun getNetworkSettings(): Flow<NetworkSettings>
    
    /**
     * Guarda configuraciones de red.
     */
    suspend fun saveNetworkSettings(settings: NetworkSettings)
}
