package com.odin.core.domain.repository

import com.odin.core.domain.model.LogEvent
import kotlinx.coroutines.flow.Flow

/**
 * LogsRepository proporciona acceso a logs de seguridad.
 */
interface LogsRepository {
    /**
     * Obtiene todos los logs.
     */
    fun getAllLogs(): Flow<List<LogEvent>>
    
    /**
     * Obtiene logs recientes.
     */
    fun getRecentEvents(limit: Int): Flow<List<LogEvent>>
    
    /**
     * Obtiene logs filtrados por severidad.
     */
    fun getLogsBySeverity(severity: LogEvent.Severity): Flow<List<LogEvent>>
    
    /**
     * Obtiene logs filtrados por fuente.
     */
    fun getLogsBySource(source: String): Flow<List<LogEvent>>
    
    /**
     * Exporta logs a un formato específico.
     */
    suspend fun exportLogs(logs: List<LogEvent>, format: ExportFormat): String
    
    /**
     * Busca logs por query.
     */
    suspend fun searchLogs(query: String): List<LogEvent>
}

/**
 * ExportFormat representa el formato de exportación.
 */
enum class ExportFormat {
    PDF,
    CSV,
    JSON
}
