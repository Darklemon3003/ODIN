package com.odin.core.ui.screens.fileanalysis

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.odin.core.domain.model.AnalysisIndicator
import com.odin.core.domain.model.AnalysisVerdict
import com.odin.core.domain.model.FileAnalysisResult
import com.odin.core.domain.model.QuarantineDeleteResult
import com.odin.core.domain.model.QuarantineItem
import com.odin.core.domain.model.QuarantineResult
import com.odin.core.domain.model.RestoreResult
import com.odin.core.domain.model.SelectedFile
import com.odin.core.ui.components.OdinCard
import com.odin.core.ui.components.OdinDangerButton
import com.odin.core.ui.components.OdinEmptyState
import com.odin.core.ui.components.OdinPrimaryButton
import com.odin.core.ui.components.OdinSecondaryButton
import com.odin.core.ui.components.OdinSectionHeader
import com.odin.core.ui.components.OdinStatusChip
import com.odin.core.ui.components.OdinStatusTone
import com.odin.core.ui.viewmodel.FileAnalysisViewModel

@Composable
fun FileAnalysisScreen(
    viewModel: FileAnalysisViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val quarantineState by viewModel.quarantineState.collectAsState()
    val quarantineItems by viewModel.quarantineItems.collectAsState()
    val restoreState by viewModel.restoreState.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()

    var showQuarantineConfirm by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf<String?>(null) }
    var pendingRestoreId by remember { mutableStateOf<String?>(null) }

    // SAF file picker
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onFileSelected(uri)
        }
    }

    // SAF restore destination picker
    val restorePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*")
    ) { uri: Uri? ->
        val restoreId = pendingRestoreId
        if (uri != null && restoreId != null) {
            viewModel.restoreQuarantinedItem(restoreId, uri)
        }
        pendingRestoreId = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        OdinSectionHeader(
            title = "Analisis de archivos",
            subtitle = "Analisis local de archivos seleccionados. No es un antivirus completo."
        )

        // File selection
        OdinCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Seleccion de archivo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Selecciona un archivo del sistema. ODIN no solicita permisos de almacenamiento.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OdinPrimaryButton(
                    text = "Seleccionar archivo",
                    onClick = { filePickerLauncher.launch(arrayOf("*/*")) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Selected file info
        when (val state = uiState) {
            is FileAnalysisViewModel.UiState.FileSelected -> {
                SelectedFileCard(state.file)
                OdinPrimaryButton(
                    text = "Analizar",
                    onClick = { viewModel.analyzeFile() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is FileAnalysisViewModel.UiState.Analyzing -> {
                OdinCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Analizando...")
                    }
                }
            }
            is FileAnalysisViewModel.UiState.AnalysisComplete -> {
                AnalysisResultCard(state.result)
                if (state.result.verdict != AnalysisVerdict.ERROR) {
                    OdinSecondaryButton(
                        text = "Analizar de nuevo",
                        onClick = { viewModel.analyzeFile() },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OdinSecondaryButton(
                        text = "Poner en cuarentena",
                        onClick = { showQuarantineConfirm = true },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OdinSecondaryButton(
                    text = "Limpiar seleccion",
                    onClick = { viewModel.clearSelectedFile() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is FileAnalysisViewModel.UiState.Error -> {
                OdinCard(alert = true) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(state.message)
                    }
                }
                OdinSecondaryButton(
                    text = "Limpiar",
                    onClick = { viewModel.clearSelectedFile() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            FileAnalysisViewModel.UiState.Idle -> {}
        }

        // Quarantine result
        when (val qState = quarantineState) {
            is FileAnalysisViewModel.QuarantineState.QuarantineResult -> {
                QuarantineResultCard(qState.result)
            }
            FileAnalysisViewModel.QuarantineState.Quarantining -> {
                OdinCard {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Poniendo en cuarentena...")
                    }
                }
            }
            FileAnalysisViewModel.QuarantineState.NotQuarantined -> {}
        }

        // Quarantine list
        OdinSectionHeader(
            title = "Archivos en cuarentena",
            subtitle = "Copias aisladas en almacenamiento privado cifrado"
        )

        if (quarantineItems.isEmpty()) {
            OdinEmptyState(
                title = "Cuarentena vacia",
                message = "No hay archivos en cuarentena",
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            quarantineItems.forEach { item ->
                QuarantineItemCard(
                    item = item,
                    onRestore = {
                        pendingRestoreId = item.quarantineId
                        restorePickerLauncher.launch(item.displayNameSanitized)
                    },
                    onDelete = { showDeleteConfirm = item.quarantineId }
                )
            }
        }

        // Restore result
        when (val rState = restoreState) {
            is FileAnalysisViewModel.RestoreState.RestoreResult -> {
                RestoreResultCard(rState.result)
            }
            FileAnalysisViewModel.RestoreState.Restoring -> {
                OdinCard {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Restaurando...")
                    }
                }
            }
            FileAnalysisViewModel.RestoreState.NotStarted -> {}
        }

        // Delete result
        when (val dState = deleteState) {
            is FileAnalysisViewModel.DeleteState.DeleteResult -> {
                DeleteResultCard(dState.result)
            }
            FileAnalysisViewModel.DeleteState.NotStarted -> {}
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Quarantine confirmation dialog
    if (showQuarantineConfirm) {
        AlertDialog(
            onDismissRequest = { showQuarantineConfirm = false },
            title = { Text("Confirmar cuarentena") },
            text = {
                Text(
                    "Se creara una copia cifrada en almacenamiento privado. " +
                    "Si es posible, se eliminara el archivo original. " +
                    "Esta accion requiere consentimiento de cuarentena."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showQuarantineConfirm = false
                    viewModel.quarantineCurrentFile()
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showQuarantineConfirm = false }) { Text("Cancelar") }
            }
        )
    }

    // Delete confirmation dialog
    showDeleteConfirm?.let { id ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Eliminar de cuarentena") },
            text = {
                Text(
                    "Se eliminara la copia cifrada del almacenamiento privado. " +
                    "Esta accion no afecta archivos originales."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirm = null
                    viewModel.deleteQuarantinedItem(id)
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun SelectedFileCard(file: SelectedFile) {
    OdinCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Archivo seleccionado", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            InfoRow("Nombre", file.displayNameSanitized)
            file.mimeType?.let { InfoRow("Tipo MIME", it) }
            if (file.size >= 0) InfoRow("Tamano", formatFileSize(file.size))
            if (file.extension.isNotBlank()) InfoRow("Extension", ".${file.extension}")
        }
    }
}

@Composable
private fun AnalysisResultCard(result: FileAnalysisResult) {
    val (tone, verdictText) = when (result.verdict) {
        AnalysisVerdict.NO_KNOWN_INDICATORS -> OdinStatusTone.Safe to "Sin indicadores conocidos detectados"
        AnalysisVerdict.SUSPICIOUS -> OdinStatusTone.Warning to "Indicadores sospechosos detectados"
        AnalysisVerdict.MALICIOUS -> OdinStatusTone.Warning to "Indicador conocido detectado"
        AnalysisVerdict.ERROR -> OdinStatusTone.Error to "Error de analisis"
        AnalysisVerdict.UNKNOWN -> OdinStatusTone.Neutral to "Resultado desconocido"
        AnalysisVerdict.UNSUPPORTED -> OdinStatusTone.Neutral to "Tipo no soportado"
        AnalysisVerdict.NOT_ANALYZED -> OdinStatusTone.Neutral to "No analizado"
        AnalysisVerdict.ANALYZING -> OdinStatusTone.Neutral to "Analizando"
    }

    OdinCard(alert = result.verdict == AnalysisVerdict.SUSPICIOUS || result.verdict == AnalysisVerdict.MALICIOUS) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Resultado", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OdinStatusChip(text = verdictText, tone = tone)
            }

            // Limitation notice for NO_KNOWN_INDICATORS
            if (result.verdict == AnalysisVerdict.NO_KNOWN_INDICATORS) {
                Text(
                    text = "Este resultado se basa unicamente en las comprobaciones locales disponibles en ODIN " +
                           "y no garantiza que el archivo este libre de malware.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // SHA-256
            result.sha256?.let {
                Text("SHA-256", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }

            // File type
            InfoRow("Tipo detectado", result.detectedFileType)

            // Indicators
            if (result.indicators.isNotEmpty()) {
                Text("Indicadores", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                result.indicators.forEach { indicator ->
                    IndicatorRow(indicator)
                }
            }

            // Checks performed
            if (result.checksPerformed.isNotEmpty()) {
                Text(
                    "Comprobaciones realizadas: ${result.checksPerformed.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Checks not performed
            if (result.checksNotPerformed.isNotEmpty()) {
                Text(
                    "Comprobaciones no realizadas: ${result.checksNotPerformed.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Error message
            result.errorMessage?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun IndicatorRow(indicator: AnalysisIndicator) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = when (indicator.severity) {
                com.odin.core.domain.model.IndicatorSeverity.HIGH -> Icons.Default.Warning
                com.odin.core.domain.model.IndicatorSeverity.MEDIUM -> Icons.Default.Warning
                com.odin.core.domain.model.IndicatorSeverity.LOW -> Icons.Default.Description
            },
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = when (indicator.severity) {
                com.odin.core.domain.model.IndicatorSeverity.HIGH -> MaterialTheme.colorScheme.error
                com.odin.core.domain.model.IndicatorSeverity.MEDIUM -> MaterialTheme.colorScheme.tertiary
                com.odin.core.domain.model.IndicatorSeverity.LOW -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = indicator.description,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = indicator.evidenceSummary,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QuarantineResultCard(result: QuarantineResult) {
    val (tone, text) = when (result) {
        is QuarantineResult.QuarantinedAndOriginalRemoved ->
            OdinStatusTone.Safe to "Archivo puesto en cuarentena y original eliminado"
        is QuarantineResult.QuarantinedCopyOnly ->
            OdinStatusTone.Warning to "Copia aislada creada; el archivo original no pudo eliminarse"
        is QuarantineResult.Failed ->
            OdinStatusTone.Warning to "Cuarentena fallida: ${result.reason}"
    }

    OdinCard(alert = tone == OdinStatusTone.Warning) {
        Column(modifier = Modifier.padding(16.dp)) {
            OdinStatusChip(text = text, tone = tone)
        }
    }
}

@Composable
private fun QuarantineItemCard(
    item: QuarantineItem,
    onRestore: () -> Unit,
    onDelete: () -> Unit
) {
    OdinCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(item.displayNameSanitized, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            InfoRow("SHA-256", item.sha256.take(16) + "...")
            InfoRow("Veredicto", item.analysisVerdict.name)
            InfoRow("Tamano", formatFileSize(item.size))
            InfoRow("Fecha", java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
                .format(java.util.Date(item.timestamp)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OdinSecondaryButton(
                    text = "Restaurar",
                    onClick = onRestore,
                    modifier = Modifier.weight(1f)
                )
                OdinDangerButton(
                    text = "Eliminar",
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RestoreResultCard(result: RestoreResult) {
    val (tone, text) = when (result) {
        is RestoreResult.Restored -> OdinStatusTone.Safe to "Archivo restaurado y verificado"
        is RestoreResult.Failed -> OdinStatusTone.Warning to "Restauracion fallida: ${result.reason}"
        RestoreResult.Cancelled -> OdinStatusTone.Neutral to "Restauracion cancelada"
    }

    OdinCard(alert = tone == OdinStatusTone.Warning) {
        Column(modifier = Modifier.padding(16.dp)) {
            OdinStatusChip(text = text, tone = tone)
        }
    }
}

@Composable
private fun DeleteResultCard(result: QuarantineDeleteResult) {
    val (tone, text) = when (result) {
        QuarantineDeleteResult.Deleted -> OdinStatusTone.Safe to "Item eliminado de cuarentena"
        is QuarantineDeleteResult.Failed -> OdinStatusTone.Warning to "Eliminacion fallida: ${result.reason}"
        QuarantineDeleteResult.Cancelled -> OdinStatusTone.Neutral to "Eliminacion cancelada"
    }

    OdinCard(alert = tone == OdinStatusTone.Warning) {
        Column(modifier = Modifier.padding(16.dp)) {
            OdinStatusChip(text = text, tone = tone)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
        else -> "${bytes / (1024 * 1024 * 1024)} GB"
    }
}
