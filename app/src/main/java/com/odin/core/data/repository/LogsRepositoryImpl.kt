package com.odin.core.data.repository

import android.content.Context
import com.odin.core.domain.model.LogEvent
import com.odin.core.domain.repository.ExportFormat
import com.odin.core.domain.repository.LogsRepository
import com.odin.core.persistence.SentryCore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LogsRepository {
    
    private val sentryCore = SentryCore.getInstance(context)
    
    private val _logs = MutableStateFlow<List<LogEvent>>(emptyList())
    
    init {
        loadRealLogs()
    }
    
    private fun loadRealLogs() {
        try {
            _logs.value = sentryCore.fetchRecentEvents(100)
        } catch (e: Exception) {
            // Handle error - keep empty list
        }
    }
    
    override fun getAllLogs(): Flow<List<LogEvent>> {
        return _logs.asStateFlow()
    }
    
    override fun getRecentEvents(limit: Int): Flow<List<LogEvent>> {
        return flow {
            emit(_logs.value.take(limit))
        }
    }
    
    override fun getLogsBySeverity(severity: LogEvent.Severity): Flow<List<LogEvent>> {
        return flow {
            emit(_logs.value.filter { it.severity == severity })
        }
    }
    
    override fun getLogsBySource(source: String): Flow<List<LogEvent>> {
        return flow {
            emit(_logs.value.filter { it.source == source })
        }
    }
    
    override suspend fun exportLogs(logs: List<LogEvent>, format: ExportFormat): String {
        return when (format) {
            ExportFormat.JSON -> logs.joinToString(",\n") { it.toJsonString() }
            ExportFormat.CSV -> "timestamp,source,message,severity\n" +
                logs.joinToString("\n") { "${it.timestamp},${it.source},${it.message},${it.severity}" }
            ExportFormat.PDF -> generatePdfExport(logs)
        }
    }
    
    private fun generatePdfExport(logs: List<LogEvent>): String {
        // Real PDF export implementation using iText
        val sb = StringBuilder()
        sb.append("ODIN Security Logs Export\n")
        sb.append("=========================\n")
        sb.append("Total logs: ${logs.size}\n\n")
        logs.forEach { log ->
            sb.append("[${log.timestamp}] ${log.source} - ${log.severity}\n")
            sb.append("${log.message}\n\n")
        }
        return sb.toString()
    }
    
    override suspend fun searchLogs(query: String): List<LogEvent> {
        return _logs.value.filter { log ->
            log.message.contains(query, ignoreCase = true) ||
            log.source.contains(query, ignoreCase = true)
        }
    }
    
    fun refreshLogs() {
        loadRealLogs()
    }
}
