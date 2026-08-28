package com.odin.core.domain.repository

import com.odin.core.domain.model.ThreatStatistics
import com.odin.core.network.detection.DetectionResult
import kotlinx.coroutines.flow.Flow

/**
 * ThreatsRepository proporciona acceso a datos de amenazas.
 */
interface ThreatsRepository {
    /**
     * Obtiene todas las amenazas.
     */
    fun getAllThreats(): Flow<List<DetectionResult>>
    
    /**
     * Obtiene amenazas por nivel.
     */
    fun getThreatsByLevel(level: com.odin.core.network.detection.ThreatLevel): Flow<List<DetectionResult>>
    
    /**
     * Obtiene estadísticas de amenazas.
     */
    fun getThreatStatistics(): Flow<ThreatStatistics>
    
    /**
     * Descarta una amenaza.
     */
    suspend fun dismissThreat(forensicsId: Long)
    
    /**
     * Obtiene una amenaza por ID.
     */
    suspend fun getThreatById(forensicsId: Long): DetectionResult?
}
