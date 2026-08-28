package com.odin.core.ui.screens.sentry

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.odin.core.ui.components.OdinCard
import com.odin.core.ui.components.OdinDangerButton
import com.odin.core.ui.components.OdinPrimaryButton
import com.odin.core.ui.components.OdinSecondaryButton
import com.odin.core.ui.components.OdinSectionHeader
import com.odin.core.ui.components.OdinStatusChip
import com.odin.core.ui.components.OdinStatusTone
import com.odin.core.ui.model.SentryStatus
import com.odin.core.ui.model.SentryUiState
import com.odin.core.ui.theme.OdinTextPrimary
import com.odin.core.ui.theme.OdinTextSecondary
import com.odin.core.ui.viewmodel.SentryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentryControlScreen(
    viewModel: SentryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> viewModel.onNotificationPermissionResult(granted) }
    )

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sentry local") },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar Sentry")
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
            item { SentryHeader(uiState) }
            item { ConsentCard(uiState, viewModel) }
            item {
                NotificationCard(
                    uiState = uiState,
                    onRequestPermission = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            viewModel.onNotificationPermissionResult(true)
                        }
                    }
                )
            }
            item { BootConsentCard(uiState, viewModel) }
            item { ControlsCard(uiState, viewModel) }
            item { RuntimeCard(uiState) }
            item {
                OdinCard(alert = true) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Sentry funciona como servicio visible. Puede consumir bateria y debe permanecer bajo control del usuario.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OdinTextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SentryHeader(uiState: SentryUiState) {
    OdinCard(alert = uiState.status == SentryStatus.ERROR || uiState.status == SentryStatus.REQUIRES_NOTIFICATION_PERMISSION) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "SentryCore y servicio visible",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = OdinTextPrimary
                )
                Text(
                    text = "Persistencia cifrada, auditoria local y heartbeat. No se activa sin consentimiento.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OdinTextSecondary
                )
            }
            OdinStatusChip(text = uiState.status.label(), tone = uiState.status.tone())
        }
        Text(
            text = uiState.statusMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = OdinTextSecondary
        )
    }
}

@Composable
private fun ConsentCard(
    uiState: SentryUiState,
    viewModel: SentryViewModel
) {
    OdinCard(alert = uiState.requiresConsent) {
        StatusRow(
            icon = Icons.Default.Shield,
            title = "Consentimiento principal",
            message = "Permite iniciar Sentry como servicio visible. No activa inicio tras reinicio.",
            status = if (uiState.isConsentGranted) "Concedido" else "Requiere consentimiento",
            tone = if (uiState.isConsentGranted) OdinStatusTone.Safe else OdinStatusTone.Warning
        )
        if (!uiState.isConsentGranted) {
            OdinPrimaryButton(
                text = "Entiendo y acepto activar Sentry",
                icon = Icons.Default.Shield,
                onClick = { viewModel.acceptConsent() },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            OdinDangerButton(
                text = "Revocar consentimiento",
                icon = Icons.Default.Warning,
                onClick = { viewModel.revokeConsent() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun NotificationCard(
    uiState: SentryUiState,
    onRequestPermission: () -> Unit
) {
    OdinCard(alert = uiState.requiresNotificationPermission) {
        StatusRow(
            icon = Icons.Default.Notifications,
            title = "Notificacion foreground",
            message = "Sentry debe mostrar una notificacion permanente mientras este activo.",
            status = if (uiState.isNotificationPermissionGranted) "Disponible" else "Requiere permiso",
            tone = if (uiState.isNotificationPermissionGranted) OdinStatusTone.Safe else OdinStatusTone.Warning
        )
        if (!uiState.isNotificationPermissionGranted) {
            OdinSecondaryButton(
                text = "Solicitar permiso de notificaciones",
                icon = Icons.Default.Notifications,
                onClick = onRequestPermission,
                enabled = uiState.isConsentGranted,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun BootConsentCard(
    uiState: SentryUiState,
    viewModel: SentryViewModel
) {
    OdinCard {
        StatusRow(
            icon = Icons.Default.Update,
            title = "Inicio tras reinicio",
            message = "Separado del consentimiento principal. BootReceiver exige ambos consentimientos.",
            status = if (uiState.isBootConsentGranted) "Autorizado" else "Desactivado",
            tone = if (uiState.isBootConsentGranted) OdinStatusTone.Safe else OdinStatusTone.Neutral
        )
        if (uiState.isBootConsentGranted) {
            OdinSecondaryButton(
                text = "Desactivar inicio tras reinicio",
                icon = Icons.Default.Update,
                onClick = { viewModel.setBootConsent(false) },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            OdinSecondaryButton(
                text = "Permitir inicio tras reinicio",
                icon = Icons.Default.Update,
                onClick = { viewModel.setBootConsent(true) },
                enabled = uiState.isConsentGranted,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ControlsCard(
    uiState: SentryUiState,
    viewModel: SentryViewModel
) {
    OdinCard {
        OdinSectionHeader(
            title = "Control del servicio",
            subtitle = "Solo acciones explicitas del usuario"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OdinPrimaryButton(
                text = "Iniciar Sentry",
                icon = Icons.Default.PowerSettingsNew,
                onClick = { viewModel.startSentry() },
                enabled = uiState.canStart,
                modifier = Modifier.weight(1f)
            )
            OdinDangerButton(
                text = "Detener",
                icon = Icons.Default.StopCircle,
                onClick = { viewModel.stopSentry() },
                enabled = uiState.canStop,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RuntimeCard(uiState: SentryUiState) {
    OdinCard {
        OdinSectionHeader(
            title = "Estado runtime",
            subtitle = "El servicio activo requiere heartbeat fresco"
        )
        StatusRow(
            icon = Icons.Default.Security,
            title = "Servicio",
            message = if (uiState.isServiceRunning) "SentryService reporta heartbeat vigente." else "SentryService no esta activo.",
            status = if (uiState.isServiceRunning) "Activo" else "Detenido",
            tone = if (uiState.isServiceRunning) OdinStatusTone.Safe else OdinStatusTone.Neutral
        )
        StatusRow(
            icon = Icons.Default.Shield,
            title = "SentryCore",
            message = "Salud reportada por el servicio cuando esta activo.",
            status = if (uiState.isCoreHealthy) "Salud OK" else "No validado",
            tone = if (uiState.isCoreHealthy) OdinStatusTone.Safe else OdinStatusTone.Neutral
        )
        Text(
            text = "Ultimo heartbeat: ${formatHeartbeat(uiState.lastHeartbeat)}",
            style = MaterialTheme.typography.bodySmall,
            color = OdinTextSecondary
        )
        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun StatusRow(
    icon: ImageVector,
    title: String,
    message: String,
    status: String,
    tone: OdinStatusTone
) {
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
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = OdinTextPrimary
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = OdinTextSecondary
                )
            }
        }
        OdinStatusChip(text = status, tone = tone)
    }
    Spacer(modifier = Modifier.height(4.dp))
}

private fun SentryStatus.label(): String {
    return when (this) {
        SentryStatus.INACTIVE -> "Detenido"
        SentryStatus.REQUIRES_CONSENT -> "Requiere consentimiento"
        SentryStatus.REQUIRES_NOTIFICATION_PERMISSION -> "Notificacion requerida"
        SentryStatus.READY_TO_START -> "Listo para iniciar"
        SentryStatus.STARTING -> "Iniciando"
        SentryStatus.ACTIVE -> "Activo"
        SentryStatus.STOPPING -> "Deteniendo"
        SentryStatus.ERROR -> "Error"
        SentryStatus.NOT_VALIDATED -> "No validado"
    }
}

private fun SentryStatus.tone(): OdinStatusTone {
    return when (this) {
        SentryStatus.ACTIVE -> OdinStatusTone.Safe
        SentryStatus.READY_TO_START -> OdinStatusTone.Planned
        SentryStatus.REQUIRES_CONSENT,
        SentryStatus.REQUIRES_NOTIFICATION_PERMISSION,
        SentryStatus.NOT_VALIDATED -> OdinStatusTone.Warning
        SentryStatus.ERROR -> OdinStatusTone.Error
        SentryStatus.INACTIVE,
        SentryStatus.STARTING,
        SentryStatus.STOPPING -> OdinStatusTone.Neutral
    }
}

private fun formatHeartbeat(timestamp: Long?): String {
    if (timestamp == null || timestamp <= 0L) {
        return "Sin datos"
    }
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
}
