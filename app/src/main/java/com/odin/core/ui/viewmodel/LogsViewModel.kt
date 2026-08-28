package com.odin.core.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odin.core.domain.repository.LogsRepository
import com.odin.core.ui.model.LogEventUi
import com.odin.core.ui.model.LogExportFormat
import com.odin.core.ui.model.LogExportState
import com.odin.core.ui.model.LogModule
import com.odin.core.ui.model.LogSanitizer
import com.odin.core.ui.model.LogSeverity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class LogsUiState(
    val isLoading: Boolean = true,
    val events: List<LogEventUi> = emptyList(),
    val filteredEvents: List<LogEventUi> = emptyList(),
    val selectedSeverity: LogSeverity? = null,
    val selectedModule: LogModule? = null,
    val searchQuery: String = "",
    val selectedEvent: LogEventUi? = null,
    val lastRefreshTime: Long? = null,
    val errorMessage: String? = null,
    val exportState: LogExportState = LogExportState.Idle
) {
    val hasEvents: Boolean = events.isNotEmpty()
    val hasFilteredEvents: Boolean = filteredEvents.isNotEmpty()
    val isFiltered: Boolean = selectedSeverity != null || selectedModule != null || searchQuery.isNotBlank()
}

@HiltViewModel
class LogsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logsRepository: LogsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogsUiState())
    val uiState: StateFlow<LogsUiState> = _uiState.asStateFlow()

    private var allEvents: List<LogEventUi> = emptyList()
    private var pendingExport: PendingExport? = null

    init {
        refreshLogs()
    }

    fun refreshLogs() {
        viewModelScope.launch {
            val previous = _uiState.value
            _uiState.value = previous.copy(isLoading = true, errorMessage = null)
            try {
                allEvents = withContext(Dispatchers.IO) {
                    logsRepository
                        .getRecentEvents(MAX_VISIBLE_EVENTS)
                        .first()
                        .map(LogSanitizer::toUi)
                        .distinctBy { it.id }
                        .sortedByDescending { it.timestamp }
                }

                val current = _uiState.value
                _uiState.value = current.copy(
                    isLoading = false,
                    events = allEvents,
                    filteredEvents = filterEvents(
                        events = allEvents,
                        query = current.searchQuery,
                        severity = current.selectedSeverity,
                        module = current.selectedModule
                    ),
                    lastRefreshTime = System.currentTimeMillis(),
                    errorMessage = null
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    events = emptyList(),
                    filteredEvents = emptyList(),
                    errorMessage = "No fue posible cargar eventos locales de ODIN."
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        val current = _uiState.value.copy(searchQuery = query)
        updateFilteredState(current)
    }

    fun selectSeverity(severity: LogSeverity?) {
        val current = _uiState.value.copy(selectedSeverity = severity)
        updateFilteredState(current)
    }

    fun selectModule(module: LogModule?) {
        val current = _uiState.value.copy(selectedModule = module)
        updateFilteredState(current)
    }

    fun clearFilters() {
        val current = _uiState.value.copy(
            selectedSeverity = null,
            selectedModule = null,
            searchQuery = ""
        )
        updateFilteredState(current)
    }

    fun selectEvent(event: LogEventUi) {
        _uiState.value = _uiState.value.copy(selectedEvent = event)
    }

    fun clearSelectedEvent() {
        _uiState.value = _uiState.value.copy(selectedEvent = null)
    }

    fun prepareExport(format: LogExportFormat): String? {
        val eventsToExport = _uiState.value.filteredEvents
        if (eventsToExport.isEmpty()) {
            pendingExport = null
            _uiState.value = _uiState.value.copy(exportState = LogExportState.NoEvents)
            return null
        }

        _uiState.value = _uiState.value.copy(exportState = LogExportState.Preparing)
        val content = when (format) {
            LogExportFormat.JSON -> buildJsonExport(eventsToExport)
            LogExportFormat.CSV -> buildCsvExport(eventsToExport)
        }
        val fileName = buildExportFileName(format)
        pendingExport = PendingExport(format, content, eventsToExport.size)
        _uiState.value = _uiState.value.copy(
            exportState = LogExportState.WaitingForDestination(format, fileName)
        )
        return fileName
    }

    fun markPdfUnavailable() {
        pendingExport = null
        _uiState.value = _uiState.value.copy(exportState = LogExportState.PdfUnavailable)
    }

    fun writePendingExport(destination: Uri?) {
        val export = pendingExport
        if (destination == null) {
            pendingExport = null
            _uiState.value = _uiState.value.copy(exportState = LogExportState.Cancelled)
            return
        }
        if (export == null) {
            _uiState.value = _uiState.value.copy(
                exportState = LogExportState.Error("No hay exportacion preparada.")
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(exportState = LogExportState.Writing)
            try {
                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(destination, "w")?.use { output ->
                        output.write(export.content.toByteArray(StandardCharsets.UTF_8))
                        output.flush()
                    } ?: error("Android no entrego un destino escribible.")
                }
                pendingExport = null
                _uiState.value = _uiState.value.copy(
                    exportState = LogExportState.Success(export.format, export.eventCount)
                )
            } catch (error: Exception) {
                pendingExport = null
                _uiState.value = _uiState.value.copy(
                    exportState = LogExportState.Error("No fue posible guardar el archivo exportado.")
                )
            }
        }
    }

    fun clearExportState() {
        if (_uiState.value.exportState !is LogExportState.Writing) {
            _uiState.value = _uiState.value.copy(exportState = LogExportState.Idle)
        }
    }

    private fun updateFilteredState(state: LogsUiState) {
        _uiState.value = state.copy(
            filteredEvents = filterEvents(
                events = allEvents,
                query = state.searchQuery,
                severity = state.selectedSeverity,
                module = state.selectedModule
            )
        )
    }

    private fun filterEvents(
        events: List<LogEventUi>,
        query: String,
        severity: LogSeverity?,
        module: LogModule?
    ): List<LogEventUi> {
        val normalizedQuery = query.trim()
        return events.filter { event ->
            val matchesSeverity = severity == null || event.severity == severity
            val matchesModule = module == null || event.module == module
            val matchesQuery = normalizedQuery.isBlank() ||
                event.title.contains(normalizedQuery, ignoreCase = true) ||
                event.summary.contains(normalizedQuery, ignoreCase = true) ||
                event.sanitizedDetails.contains(normalizedQuery, ignoreCase = true) ||
                event.source.contains(normalizedQuery, ignoreCase = true)

            matchesSeverity && matchesModule && matchesQuery
        }
    }

    private fun buildJsonExport(events: List<LogEventUi>): String {
        val root = JSONObject()
            .put("exportedAt", System.currentTimeMillis())
            .put("format", "JSON")
            .put("sanitized", true)
            .put("eventCount", events.size)

        val eventArray = JSONArray()
        events.forEach { event ->
            eventArray.put(
                JSONObject()
                    .put("timestamp", event.timestamp)
                    .put("formattedTime", event.formattedTime)
                    .put("module", event.module.label)
                    .put("severity", event.severity.label)
                    .put("title", event.title)
                    .put("summary", event.summary)
                    .put("sanitizedDetails", event.sanitizedDetails)
                    .put("source", event.source)
            )
        }
        root.put("events", eventArray)
        return root.toString(2)
    }

    private fun buildCsvExport(events: List<LogEventUi>): String {
        val builder = StringBuilder()
        builder.append("timestamp,module,severity,title,summary,details,source\n")
        events.forEach { event ->
            builder
                .append(event.timestamp).append(',')
                .append(csv(event.module.label)).append(',')
                .append(csv(event.severity.label)).append(',')
                .append(csv(event.title)).append(',')
                .append(csv(event.summary)).append(',')
                .append(csv(event.sanitizedDetails)).append(',')
                .append(csv(event.source))
                .append('\n')
        }
        return builder.toString()
    }

    private fun csv(value: String): String {
        val normalized = value.replace("\r\n", "\n").replace('\r', '\n')
        val escaped = normalized.replace("\"", "\"\"")
        return "\"$escaped\""
    }

    private fun buildExportFileName(format: LogExportFormat): String {
        val stamp = exportDateFormat.format(Date())
        return "odin_logs_$stamp.${format.extension}"
    }

    private data class PendingExport(
        val format: LogExportFormat,
        val content: String,
        val eventCount: Int
    )

    private companion object {
        const val MAX_VISIBLE_EVENTS = 200
        val exportDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
    }
}
