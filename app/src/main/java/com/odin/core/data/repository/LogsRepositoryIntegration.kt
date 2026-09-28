package com.odin.core.data.repository

import android.content.Context
import com.odin.core.domain.model.LogEvent
import com.odin.core.domain.repository.ExportFormat
import com.odin.core.domain.repository.LogsRepository
import com.odin.core.forensics.LogExporter
import com.odin.core.persistence.SentryCore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogsRepositoryIntegration @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logExporter: LogExporter
) : LogsRepository {
    
    private val _logs = MutableStateFlow<List<LogEvent>>(emptyList())
    private val sentryCore: SentryCore by lazy { SentryCore.getInstance(context) }
    
    override fun getAllLogs(): Flow<List<LogEvent>> {
        return flow {
            emit(loadRealLogs(MAX_VISIBLE_EVENTS))
        }.flowOn(Dispatchers.IO)
    }
    
    override fun getRecentEvents(limit: Int): Flow<List<LogEvent>> {
        return flow {
            emit(loadRealLogs(limit.coerceIn(1, MAX_VISIBLE_EVENTS)))
        }.flowOn(Dispatchers.IO)
    }
    
    override fun getLogsBySeverity(severity: LogEvent.Severity): Flow<List<LogEvent>> {
        return flow {
            emit(loadRealLogs(MAX_VISIBLE_EVENTS).filter { it.severity == severity })
        }.flowOn(Dispatchers.IO)
    }
    
    override fun getLogsBySource(source: String): Flow<List<LogEvent>> {
        return flow {
            emit(loadRealLogs(MAX_VISIBLE_EVENTS).filter { it.source == source })
        }.flowOn(Dispatchers.IO)
    }
    
    override suspend fun exportLogs(logs: List<LogEvent>, format: ExportFormat): String {
        return when (format) {
            ExportFormat.JSON -> logExporter.exportEventsToJson(logs)
            ExportFormat.CSV -> logExporter.exportEventsToCsv(logs)
            ExportFormat.PDF -> throw UnsupportedOperationException("PDF export is not supported")
        }
    }
    
    override suspend fun searchLogs(query: String): List<LogEvent> {
        return loadRealLogs(MAX_VISIBLE_EVENTS).filter { log ->
            log.message.contains(query, ignoreCase = true) ||
            log.source.contains(query, ignoreCase = true)
        }
    }
    
    fun addLog(log: LogEvent) {
        _logs.value = _logs.value + log
    }

    fun cachedLogs(): Flow<List<LogEvent>> {
        return _logs.asStateFlow()
    }

    private fun loadRealLogs(limit: Int): List<LogEvent> {
        return try {
            val logs = sentryCore.fetchRecentEvents(limit)
                .sortedByDescending { it.timestamp }
            _logs.value = logs
            logs
        } catch (_: Exception) {
            emptyList()
        }
    }

    private companion object {
        const val MAX_VISIBLE_EVENTS = 200
    }
}
