package com.odin.core.domain.repository

import com.odin.core.domain.model.BehaviorData
import com.odin.core.domain.model.QuarantinedApp
import kotlinx.coroutines.flow.Flow

/**
 * QuarantineRepository proporciona acceso a aplicaciones en cuarentena.
 */
interface QuarantineRepository {
    /**
     * Obtiene todas las aplicaciones en cuarentena.
     */
    fun getAllQuarantinedApps(): Flow<List<QuarantinedApp>>
    
    /**
     * Obtiene datos de comportamiento de una aplicación.
     */
    fun getBehaviorData(packageName: String): Flow<BehaviorData>
    
    /**
     * Libera una aplicación de cuarentena.
     */
    suspend fun releaseApp(packageName: String)
    
    /**
     * Elimina una aplicación.
     */
    suspend fun deleteApp(packageName: String)
    
    /**
     * Mantiene una aplicación en cuarentena.
     */
    suspend fun keepApp(packageName: String)
    
    /**
     * Pone una aplicación en cuarentena.
     */
    suspend fun quarantineApp(packageName: String, reason: String)
}
