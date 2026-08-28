package com.odin.core.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.odin.core.domain.model.LogEvent
import com.odin.core.ui.components.OdinBlockedFeatureCard
import com.odin.core.ui.components.OdinCard
import com.odin.core.ui.components.OdinEmptyState
import com.odin.core.ui.components.OdinSecondaryButton
import com.odin.core.ui.components.OdinSectionHeader
import com.odin.core.ui.components.OdinStatusChip
import com.odin.core.ui.components.OdinStatusTone
import com.odin.core.ui.model.DashboardAction
import com.odin.core.ui.model.DashboardBlockedFeature
import com.odin.core.ui.model.DashboardModuleState
import com.odin.core.ui.model.DashboardQuickAction
import com.odin.core.ui.model.DashboardUiState
import com.odin.core.ui.model.ModuleStatus
import com.odin.core.ui.model.OverallStatus
import com.odin.core.ui.theme.OdinError
import com.odin.core.ui.theme.OdinPrimary
import com.odin.core.ui.theme.OdinTextPrimary
import com.odin.core.ui.theme.OdinTextSecondary
import com.odin.core.ui.theme.OdinWarning
import com.odin.core.ui.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onOpenLogs: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenFirewall: () -> Unit = {},
    onOpenSentry: () -> Unit = {},
    onOpenPermissions: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ODIN") },
                actions = {
                    IconButton(onClick = { viewModel.refreshData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar estado local")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Revisando estado local",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OdinTextSecondary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    DashboardHeader(uiState)
                }

                item {
                    PostureCard(uiState)
                }

                item {
                    ModuleSection(
                        modules = uiState.modules,
                        onAction = { action ->
                            handleDashboardAction(
                                action = action,
                                onOpenLogs = onOpenLogs,
                                onOpenSettings = onOpenSettings,
                                onOpenFirewall = onOpenFirewall,
                                onOpenSentry = onOpenSentry,
                                onOpenPermissions = onOpenPermissions
                            )
                        }
                    )
                }

                item {
                    RecentActivityCard(
                        events = uiState.recentEvents,
                        recentAlertsCount = uiState.recentAlertsCount,
                        onOpenLogs = onOpenLogs
                    )
                }

                item {
                    QuickActionsCard(
                        actions = uiState.availableActions,
                        onAction = { action ->
                            handleDashboardAction(
                                action = action,
                                onOpenLogs = onOpenLogs,
                                onOpenSettings = onOpenSettings,
                                onOpenFirewall = onOpenFirewall,
                                onOpenSentry = onOpenSentry,
                                onOpenPermissions = onOpenPermissions
                            )
                        }
                    )
                }

                item {
                    BlockedFeaturesSection(uiState.blockedFeatures)
                }

                uiState.errorMessage?.let { errorMessage ->
                    item {
                        OdinEmptyState(
                            title = "Estado parcial",
                            message = errorMessage,
                            icon = Icons.Default.ErrorOutline
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader(uiState: DashboardUiState) {
    OdinCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Centro de control",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = OdinTextPrimary
                )
                Text(
                    text = "Estado local y configuracion defensiva",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OdinTextSecondary
                )
            }
            OdinStatusChip(
                text = uiState.overallStatus.label(),
                tone = uiState.overallStatus.tone()
            )
        }

        Text(
            text = "Ultima revision local: ${formatTime(uiState.lastUpdated)}",
            style = MaterialTheme.typography.labelMedium,
            color = OdinTextSecondary
        )
    }
}

@Composable
private fun PostureCard(uiState: DashboardUiState) {
    OdinCard(alert = uiState.overallStatus == OverallStatus.NEEDS_ATTENTION) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Estado general ODIN",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = OdinTextPrimary
                )
                Text(
                    text = uiState.overallStatus.description(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = OdinTextSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = uiState.overallStatus.iconColor(),
                modifier = Modifier.size(48.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HonestMetric(
                label = "Postura",
                value = uiState.postureScore?.toString() ?: "No disponible",
                modifier = Modifier.weight(1f)
            )
            HonestMetric(
                label = "Alertas locales",
                value = uiState.recentAlertsCount.toString(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HonestMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = OdinTextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = if (value == "No disponible") OdinTextSecondary else OdinTextPrimary
        )
    }
}

@Composable
private fun ModuleSection(
    modules: List<DashboardModuleState>,
    onAction: (DashboardAction) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OdinSectionHeader(
            title = "Modulos principales",
            subtitle = "Estados basados en datos locales disponibles"
        )
        modules.forEach { module ->
            ModuleCard(module = module, onAction = onAction)
        }
    }
}

@Composable
private fun ModuleCard(
    module: DashboardModuleState,
    onAction: (DashboardAction) -> Unit
) {
    OdinCard {
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
                    imageVector = module.icon(),
                    contentDescription = null,
                    tint = module.status.iconColor(),
                    modifier = Modifier.size(28.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = module.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = OdinTextPrimary
                    )
                    Text(
                        text = module.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = OdinTextSecondary
                    )
                }
            }
            OdinStatusChip(text = module.statusLabel, tone = module.status.tone())
        }

        if (module.action != null && module.actionLabel != null) {
            OdinSecondaryButton(
                text = module.actionLabel,
                onClick = { onAction(module.action) },
                enabled = module.actionEnabled,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RecentActivityCard(
    events: List<LogEvent>,
    recentAlertsCount: Int,
    onOpenLogs: () -> Unit
) {
    OdinCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OdinSectionHeader(
                title = "Actividad reciente",
                subtitle = "Eventos locales reales disponibles",
                modifier = Modifier.weight(1f)
            )
            OdinStatusChip(
                text = if (recentAlertsCount > 0) "Requiere revision" else "Sin eventos recientes",
                tone = if (recentAlertsCount > 0) OdinStatusTone.Warning else OdinStatusTone.Neutral
            )
        }

        if (events.isEmpty()) {
            Text(
                text = "Sin eventos recientes",
                style = MaterialTheme.typography.bodyMedium,
                color = OdinTextSecondary
            )
        } else {
            events.take(3).forEach { event ->
                RecentEventRow(event)
            }
        }

        OdinSecondaryButton(
            text = "Ver logs",
            icon = Icons.Default.Description,
            onClick = onOpenLogs,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RecentEventRow(event: LogEvent) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = when (event.severity) {
                LogEvent.Severity.INFO -> Icons.Default.Description
                LogEvent.Severity.WARNING -> Icons.Default.Warning
                LogEvent.Severity.ERROR -> Icons.Default.ErrorOutline
                LogEvent.Severity.CRITICAL -> Icons.Default.ErrorOutline
            },
            contentDescription = null,
            tint = when (event.severity) {
                LogEvent.Severity.INFO -> OdinTextSecondary
                LogEvent.Severity.WARNING -> OdinWarning
                LogEvent.Severity.ERROR -> OdinError
                LogEvent.Severity.CRITICAL -> OdinError
            }
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = event.message,
                style = MaterialTheme.typography.bodySmall,
                color = OdinTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${event.source} - ${formatTime(event.timestamp)}",
                style = MaterialTheme.typography.labelSmall,
                color = OdinTextSecondary
            )
        }
    }
}

@Composable
private fun QuickActionsCard(
    actions: List<DashboardQuickAction>,
    onAction: (DashboardAction) -> Unit
) {
    OdinCard {
        OdinSectionHeader(
            title = "Acciones rapidas",
            subtitle = "Atajos seguros; no activan servicios"
        )
        actions.chunked(2).forEach { rowActions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowActions.forEach { action ->
                    OdinSecondaryButton(
                        text = action.label,
                        icon = action.icon(),
                        onClick = { onAction(action.action) },
                        enabled = action.enabled,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowActions.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun BlockedFeaturesSection(blockedFeatures: List<DashboardBlockedFeature>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OdinSectionHeader(
            title = "Funciones bloqueadas",
            subtitle = "No se ejecutan hasta completar contrato y consentimiento"
        )
        blockedFeatures.forEach { feature ->
            OdinBlockedFeatureCard(
                title = feature.title,
                message = feature.reason,
                status = feature.statusLabel
            )
        }
    }
}

private fun handleDashboardAction(
    action: DashboardAction,
    onOpenLogs: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenFirewall: () -> Unit,
    onOpenSentry: () -> Unit,
    onOpenPermissions: () -> Unit
) {
    when (action) {
        DashboardAction.REVIEW_PERMISSIONS -> onOpenPermissions()
        DashboardAction.OPEN_SETTINGS -> onOpenSettings()
        DashboardAction.CONFIGURE_PROTECTION -> onOpenSentry()
        DashboardAction.VIEW_LOGS -> onOpenLogs()
        DashboardAction.REVIEW_NETWORK -> onOpenFirewall()
    }
}

private fun OverallStatus.label(): String {
    return when (this) {
        OverallStatus.INACTIVE -> "Inactivo"
        OverallStatus.NOT_VALIDATED -> "No validado"
        OverallStatus.NEEDS_ATTENTION -> "Requiere atencion"
        OverallStatus.PARTIAL_PROTECTION -> "Proteccion parcial"
        OverallStatus.PROTECTED -> "Validado"
    }
}

private fun OverallStatus.description(): String {
    return when (this) {
        OverallStatus.INACTIVE -> "ODIN no tiene modulos activos validados en este momento."
        OverallStatus.NOT_VALIDATED -> "No hay evidencia suficiente para afirmar proteccion activa."
        OverallStatus.NEEDS_ATTENTION -> "Hay configuraciones o permisos pendientes de revision."
        OverallStatus.PARTIAL_PROTECTION -> "Existen senales configuradas, pero el estado activo aun requiere validacion."
        OverallStatus.PROTECTED -> "Estado validado por fuentes locales suficientes."
    }
}

private fun OverallStatus.tone(): OdinStatusTone {
    return when (this) {
        OverallStatus.INACTIVE -> OdinStatusTone.Unavailable
        OverallStatus.NOT_VALIDATED -> OdinStatusTone.Neutral
        OverallStatus.NEEDS_ATTENTION -> OdinStatusTone.Warning
        OverallStatus.PARTIAL_PROTECTION -> OdinStatusTone.Planned
        OverallStatus.PROTECTED -> OdinStatusTone.Safe
    }
}

private fun OverallStatus.iconColor() = when (this) {
    OverallStatus.INACTIVE -> OdinTextSecondary
    OverallStatus.NOT_VALIDATED -> OdinTextSecondary
    OverallStatus.NEEDS_ATTENTION -> OdinWarning
    OverallStatus.PARTIAL_PROTECTION -> OdinPrimary
    OverallStatus.PROTECTED -> OdinPrimary
}

private fun ModuleStatus.tone(): OdinStatusTone {
    return when (this) {
        ModuleStatus.ACTIVE -> OdinStatusTone.Safe
        ModuleStatus.INACTIVE -> OdinStatusTone.Neutral
        ModuleStatus.REQUIRES_PERMISSION -> OdinStatusTone.Warning
        ModuleStatus.REQUIRES_CONSENT -> OdinStatusTone.Warning
        ModuleStatus.REQUIRES_CONFIGURATION -> OdinStatusTone.Planned
        ModuleStatus.NOT_AVAILABLE -> OdinStatusTone.Unavailable
        ModuleStatus.PLANNED -> OdinStatusTone.Planned
        ModuleStatus.ERROR -> OdinStatusTone.Error
    }
}

private fun ModuleStatus.iconColor() = when (this) {
    ModuleStatus.ACTIVE -> OdinPrimary
    ModuleStatus.INACTIVE -> OdinTextSecondary
    ModuleStatus.REQUIRES_PERMISSION -> OdinWarning
    ModuleStatus.REQUIRES_CONSENT -> OdinWarning
    ModuleStatus.REQUIRES_CONFIGURATION -> OdinPrimary
    ModuleStatus.NOT_AVAILABLE -> OdinTextSecondary
    ModuleStatus.PLANNED -> OdinPrimary
    ModuleStatus.ERROR -> OdinError
}

private fun DashboardModuleState.icon(): ImageVector {
    return when (title) {
        "SentryCore" -> Icons.Default.Security
        "NetShield" -> Icons.Default.Shield
        "VPN local" -> Icons.Default.VpnKey
        "Permisos" -> Icons.Default.Notifications
        "Logs" -> Icons.Default.Description
        else -> Icons.Default.Settings
    }
}

private fun DashboardQuickAction.icon(): ImageVector {
    return when (action) {
        DashboardAction.REVIEW_PERMISSIONS -> Icons.Default.Notifications
        DashboardAction.VIEW_LOGS -> Icons.Default.Description
        DashboardAction.OPEN_SETTINGS -> Icons.Default.Settings
        DashboardAction.REVIEW_NETWORK -> Icons.Default.VpnKey
        DashboardAction.CONFIGURE_PROTECTION -> Icons.Default.Security
    }
}

private fun formatTime(timestamp: Long?): String {
    if (timestamp == null || timestamp <= 0L) {
        return "Sin datos todavia"
    }
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
}
