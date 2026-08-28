package com.odin.core.ui.screens.logs

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.odin.core.ui.components.OdinCard
import com.odin.core.ui.components.OdinEmptyState
import com.odin.core.ui.components.OdinSecondaryButton
import com.odin.core.ui.components.OdinSectionHeader
import com.odin.core.ui.components.OdinStatusChip
import com.odin.core.ui.components.OdinStatusTone
import com.odin.core.ui.model.LogEventUi
import com.odin.core.ui.model.LogExportFormat
import com.odin.core.ui.model.LogExportState
import com.odin.core.ui.model.LogModule
import com.odin.core.ui.model.LogSeverity
import com.odin.core.ui.theme.OdinError
import com.odin.core.ui.theme.OdinPrimary
import com.odin.core.ui.theme.OdinTextMuted
import com.odin.core.ui.theme.OdinTextPrimary
import com.odin.core.ui.theme.OdinTextSecondary
import com.odin.core.ui.theme.OdinWarning
import com.odin.core.ui.viewmodel.LogsUiState
import com.odin.core.ui.viewmodel.LogsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    viewModel: LogsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val errorMessage = uiState.errorMessage
    val lifecycleOwner = LocalLifecycleOwner.current
    val jsonExportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(LogExportFormat.JSON.mimeType),
        onResult = { uri -> viewModel.writePendingExport(uri) }
    )
    val csvExportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(LogExportFormat.CSV.mimeType),
        onResult = { uri -> viewModel.writePendingExport(uri) }
    )

    LaunchedEffect(Unit) {
        viewModel.refreshLogs()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshLogs()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Logs / Reportes") },
                actions = {
                    IconButton(onClick = { viewModel.refreshLogs() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar eventos")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                LogsHeader(
                    uiState = uiState,
                    onRefresh = viewModel::refreshLogs
                )
            }
            item {
                LogsSearchAndFilters(
                    uiState = uiState,
                    onQueryChange = viewModel::updateSearchQuery,
                    onSeveritySelected = viewModel::selectSeverity,
                    onModuleSelected = viewModel::selectModule,
                    onClearFilters = viewModel::clearFilters
                )
            }
            item {
                ExportCard(
                    uiState = uiState,
                    onExportJson = {
                        viewModel.prepareExport(LogExportFormat.JSON)?.let(jsonExportLauncher::launch)
                    },
                    onExportCsv = {
                        viewModel.prepareExport(LogExportFormat.CSV)?.let(csvExportLauncher::launch)
                    },
                    onPdfUnavailable = viewModel::markPdfUnavailable
                )
            }

            when {
                uiState.isLoading -> {
                    item { LoadingCard() }
                }
                errorMessage != null -> {
                    item {
                        OdinEmptyState(
                            title = "Eventos no disponibles",
                            message = errorMessage,
                            icon = Icons.Default.ErrorOutline
                        )
                    }
                }
                !uiState.hasEvents -> {
                    item {
                        OdinEmptyState(
                            title = "Sin eventos recientes",
                            message = "No hay eventos locales todavia. La pantalla no usa datos simulados.",
                            icon = Icons.Default.Info
                        )
                    }
                }
                !uiState.hasFilteredEvents -> {
                    item {
                        OdinEmptyState(
                            title = "Sin coincidencias",
                            message = "No hay eventos que coincidan con los filtros.",
                            icon = Icons.Default.FilterList
                        )
                    }
                }
                else -> {
                    items(uiState.filteredEvents, key = { it.id }) { event ->
                        LogEventCard(
                            event = event,
                            onClick = { viewModel.selectEvent(event) }
                        )
                    }
                }
            }
        }
    }

    uiState.selectedEvent?.let { event ->
        LogEventDetailDialog(
            event = event,
            onDismiss = viewModel::clearSelectedEvent
        )
    }

    ExportStateDialog(
        state = uiState.exportState,
        onDismiss = viewModel::clearExportState
    )
}

@Composable
private fun LogsHeader(
    uiState: LogsUiState,
    onRefresh: () -> Unit
) {
    OdinCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Eventos locales registrados por ODIN",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = OdinTextPrimary
                )
                Text(
                    text = "Vista sanitizada. No muestra payloads, tokens ni rutas privadas completas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OdinTextSecondary
                )
                Text(
                    text = "Ultima actualizacion: ${formatRefreshTime(uiState.lastRefreshTime)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = OdinTextMuted
                )
            }
            OdinStatusChip(
                text = "${uiState.events.size} reales",
                tone = if (uiState.events.isEmpty()) OdinStatusTone.Neutral else OdinStatusTone.Safe
            )
        }
        OdinSecondaryButton(
            text = "Actualizar eventos",
            icon = Icons.Default.Refresh,
            onClick = onRefresh,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LogsSearchAndFilters(
    uiState: LogsUiState,
    onQueryChange: (String) -> Unit,
    onSeveritySelected: (LogSeverity?) -> Unit,
    onModuleSelected: (LogModule?) -> Unit,
    onClearFilters: () -> Unit
) {
    OdinCard {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar eventos reales") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            singleLine = true
        )

        FilterRow(
            title = "Severidad",
            icon = Icons.Default.FilterList
        ) {
            LogSeverity.values().forEach { severity ->
                FilterChip(
                    selected = uiState.selectedSeverity == severity,
                    onClick = {
                        onSeveritySelected(
                            if (uiState.selectedSeverity == severity) null else severity
                        )
                    },
                    label = { Text(severity.label) }
                )
            }
        }

        FilterRow(
            title = "Modulo",
            icon = Icons.Default.Security
        ) {
            LogModule.values().forEach { module ->
                FilterChip(
                    selected = uiState.selectedModule == module,
                    onClick = {
                        onModuleSelected(
                            if (uiState.selectedModule == module) null else module
                        )
                    },
                    label = { Text(module.label) }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${uiState.filteredEvents.size} visibles",
                style = MaterialTheme.typography.labelMedium,
                color = OdinTextMuted
            )
            OdinSecondaryButton(
                text = "Limpiar",
                onClick = onClearFilters,
                enabled = uiState.isFiltered,
                modifier = Modifier.height(44.dp)
            )
        }
    }
}

@Composable
private fun FilterRow(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = OdinPrimary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = OdinTextSecondary
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = { content() }
        )
    }
}

@Composable
private fun ExportCard(
    uiState: LogsUiState,
    onExportJson: () -> Unit,
    onExportCsv: () -> Unit,
    onPdfUnavailable: () -> Unit
) {
    val hasVisibleEvents = uiState.filteredEvents.isNotEmpty()
    OdinCard(alert = uiState.exportState is LogExportState.Error || uiState.exportState is LogExportState.PdfUnavailable) {
        OdinSectionHeader(
            title = "Exportacion segura",
            subtitle = "JSON y CSV usan solo datos sanitizados visibles. PDF queda bloqueado hasta existir PDF real."
        )
        Text(
            text = exportStateLabel(uiState.exportState),
            style = MaterialTheme.typography.bodySmall,
            color = OdinTextSecondary
        )
        OdinSecondaryButton(
            text = "Exportar JSON",
            icon = Icons.Default.FileDownload,
            onClick = onExportJson,
            enabled = hasVisibleEvents && uiState.exportState !is LogExportState.Writing,
            modifier = Modifier.fillMaxWidth()
        )
        OdinSecondaryButton(
            text = "Exportar CSV",
            icon = Icons.Default.FileDownload,
            onClick = onExportCsv,
            enabled = hasVisibleEvents && uiState.exportState !is LogExportState.Writing,
            modifier = Modifier.fillMaxWidth()
        )
        OdinSecondaryButton(
            text = "PDF no disponible todavia",
            icon = Icons.Default.Description,
            onClick = onPdfUnavailable,
            modifier = Modifier.fillMaxWidth()
        )
        if (!hasVisibleEvents) {
            Text(
                text = "No hay eventos para exportar.",
                style = MaterialTheme.typography.labelSmall,
                color = OdinTextMuted
            )
        }
    }
}

@Composable
private fun LoadingCard() {
    OdinCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
            Text(
                text = "Cargando eventos locales",
                style = MaterialTheme.typography.bodyMedium,
                color = OdinTextSecondary
            )
        }
    }
}

@Composable
private fun LogEventCard(
    event: LogEventUi,
    onClick: () -> Unit
) {
    OdinCard(alert = event.severity == LogSeverity.ERROR || event.severity == LogSeverity.CRITICAL) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = severityIcon(event.severity),
                    contentDescription = null,
                    tint = severityColor(event.severity),
                    modifier = Modifier.size(28.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = OdinTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = event.summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = OdinTextSecondary,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${event.module.label} - ${event.formattedTime}",
                        style = MaterialTheme.typography.labelSmall,
                        color = OdinTextMuted
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OdinStatusChip(
                    text = event.severity.label,
                    tone = event.severity.tone()
                )
                OdinStatusChip(
                    text = event.module.label,
                    tone = OdinStatusTone.Neutral
                )
            }
        }
        OdinSecondaryButton(
            text = "Ver detalle",
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LogEventDetailDialog(
    event: LogEventUi,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(event.title) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OdinStatusChip(event.severity.label, event.severity.tone())
                    OdinStatusChip(event.module.label, OdinStatusTone.Neutral)
                }
                DetailLine("Fecha", event.formattedTime)
                DetailLine("Fuente", event.source)
                DetailLine("Resumen", event.summary)
                Text(
                    text = "Detalle sanitizado",
                    style = MaterialTheme.typography.labelLarge,
                    color = OdinTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = event.sanitizedDetails,
                    style = MaterialTheme.typography.bodySmall,
                    color = OdinTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Raw no disponible en UI para evitar exposicion de datos sensibles.",
                    style = MaterialTheme.typography.labelSmall,
                    color = OdinTextMuted
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
private fun DetailLine(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = OdinTextMuted
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = OdinTextSecondary
        )
    }
}

@Composable
private fun ExportStateDialog(
    state: LogExportState,
    onDismiss: () -> Unit
) {
    val titleAndMessage = when (state) {
        LogExportState.Idle,
        LogExportState.Preparing,
        is LogExportState.WaitingForDestination,
        LogExportState.Writing -> null
        is LogExportState.Success -> "Exportacion completada" to
            "${state.format.label} sanitizado exportado correctamente (${state.eventCount} eventos)."
        LogExportState.Cancelled -> "Exportacion cancelada" to
            "No se creo ningun archivo."
        LogExportState.NoEvents -> "Sin eventos para exportar" to
            "No hay eventos visibles para exportar."
        LogExportState.PdfUnavailable -> "PDF no disponible todavia" to
            "ODIN no genera PDF real en esta fase. No se creara archivo de texto con extension PDF."
        is LogExportState.Error -> "Error al exportar" to state.message
    }

    titleAndMessage?.let { (title, message) ->
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OdinTextSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Entendido")
                }
            }
        )
    }
}

private fun exportStateLabel(state: LogExportState): String {
    return when (state) {
        LogExportState.Idle -> "Lista para exportar eventos visibles sanitizados."
        LogExportState.Preparing -> "Preparando exportacion."
        is LogExportState.WaitingForDestination -> "Esperando ubicacion para ${state.format.label}."
        LogExportState.Writing -> "Escribiendo archivo seleccionado."
        is LogExportState.Success -> "${state.format.label} exportado correctamente."
        LogExportState.Cancelled -> "Exportacion cancelada."
        LogExportState.NoEvents -> "No hay eventos para exportar."
        LogExportState.PdfUnavailable -> "PDF bloqueado: no existe generador PDF real."
        is LogExportState.Error -> state.message
    }
}

private fun LogSeverity.tone(): OdinStatusTone {
    return when (this) {
        LogSeverity.INFO -> OdinStatusTone.Neutral
        LogSeverity.WARNING -> OdinStatusTone.Warning
        LogSeverity.ERROR -> OdinStatusTone.Error
        LogSeverity.CRITICAL -> OdinStatusTone.Error
    }
}

private fun severityIcon(severity: LogSeverity): ImageVector {
    return when (severity) {
        LogSeverity.INFO -> Icons.Default.Info
        LogSeverity.WARNING -> Icons.Default.Warning
        LogSeverity.ERROR,
        LogSeverity.CRITICAL -> Icons.Default.ErrorOutline
    }
}

private fun severityColor(severity: LogSeverity) = when (severity) {
    LogSeverity.INFO -> OdinPrimary
    LogSeverity.WARNING -> OdinWarning
    LogSeverity.ERROR,
    LogSeverity.CRITICAL -> OdinError
}

private fun formatRefreshTime(timestamp: Long?): String {
    if (timestamp == null || timestamp <= 0L) {
        return "Sin datos"
    }
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
}
