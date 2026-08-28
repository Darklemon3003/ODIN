package com.odin.core.ui.screens.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.odin.core.ui.components.OdinSecondaryButton
import com.odin.core.ui.components.OdinSectionHeader
import com.odin.core.ui.components.OdinStatusChip
import com.odin.core.ui.components.OdinStatusTone
import com.odin.core.ui.model.NetShieldStatus
import com.odin.core.ui.model.SentryStatus
import com.odin.core.ui.theme.OdinTextPrimary
import com.odin.core.ui.theme.OdinTextSecondary
import com.odin.core.ui.viewmodel.NetShieldViewModel
import com.odin.core.ui.viewmodel.SentryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesStatusScreen(
    onOpenSentry: () -> Unit = {},
    onOpenNetShield: () -> Unit = {},
    sentryViewModel: SentryViewModel = hiltViewModel(),
    netShieldViewModel: NetShieldViewModel = hiltViewModel()
) {
    val sentryState by sentryViewModel.uiState.collectAsState()
    val netShieldState by netShieldViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        sentryViewModel.refresh()
        netShieldViewModel.refresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Estado de servicios") })
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
                OdinCard {
                    Text(
                        text = "Servicios declarados",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = OdinTextPrimary
                    )
                    Text(
                        text = "Vista informativa de Fase 6. No inicia, detiene ni vincula servicios.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OdinTextSecondary
                    )
                }
            }

            item {
                OdinSectionHeader(
                    title = "Proteccion local",
                    subtitle = "Estados conservadores hasta validar una fuente runtime confiable"
                )
            }
            item {
                ServiceInfoCard(
                    title = "SentryService",
                    description = sentryState.statusMessage,
                    status = sentryState.status.serviceLabel(),
                    tone = sentryState.status.serviceTone(),
                    icon = Icons.Default.Shield,
                    actionLabel = "Control Sentry",
                    onAction = onOpenSentry
                )
            }
            item {
                ServiceInfoCard(
                    title = "NetShieldVpnService",
                    description = netShieldState.statusMessage,
                    status = netShieldState.status.serviceLabel(),
                    tone = netShieldState.status.serviceTone(),
                    icon = Icons.Default.VpnKey,
                    actionLabel = "Control NetShield",
                    onAction = onOpenNetShield
                )
            }
            item {
                ServiceInfoCard(
                    title = "Foreground services",
                    description = "No deben quedar activos al abrir ODIN sin accion explicita del usuario.",
                    status = "Control pendiente",
                    tone = OdinStatusTone.Warning,
                    icon = Icons.Default.Notifications
                )
            }

            item {
                OdinSectionHeader(title = "Arranque y legacy")
            }
            item {
                ServiceInfoCard(
                    title = "BootReceiver",
                    description = "Debe exigir consentimiento de Sentry y consentimiento de arranque antes de actuar.",
                    status = "Condicionado",
                    tone = OdinStatusTone.Warning,
                    icon = Icons.Default.Update
                )
            }
            item {
                ServiceInfoCard(
                    title = "TorService / VpnAnonymityService",
                    description = "Servicios legacy de anonimato detectados; no forman parte del flujo principal de Fase 6.",
                    status = "Legacy parcial",
                    tone = OdinStatusTone.Planned,
                    icon = Icons.Default.Security
                )
            }
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
                            text = "Cualquier control para iniciar o detener servicios queda reservado para fases posteriores con consentimiento, permisos y validacion runtime.",
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
private fun ServiceInfoCard(
    title: String,
    description: String,
    status: String,
    tone: OdinStatusTone,
    icon: ImageVector,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    OdinCard(alert = tone == OdinStatusTone.Warning) {
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
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = OdinTextSecondary
                    )
                }
            }
            OdinStatusChip(text = status, tone = tone)
        }
        if (actionLabel != null && onAction != null) {
            OdinSecondaryButton(
                text = actionLabel,
                onClick = onAction,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun SentryStatus.serviceLabel(): String {
    return when (this) {
        SentryStatus.ACTIVE -> "Activo"
        SentryStatus.READY_TO_START -> "Listo"
        SentryStatus.REQUIRES_CONSENT -> "Requiere consentimiento"
        SentryStatus.REQUIRES_NOTIFICATION_PERMISSION -> "Notificacion requerida"
        SentryStatus.STARTING -> "Iniciando"
        SentryStatus.STOPPING -> "Deteniendo"
        SentryStatus.ERROR -> "Error"
        SentryStatus.NOT_VALIDATED -> "No validado"
        SentryStatus.INACTIVE -> "Detenido"
    }
}

private fun SentryStatus.serviceTone(): OdinStatusTone {
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

private fun NetShieldStatus.serviceLabel(): String {
    return when (this) {
        NetShieldStatus.ACTIVE -> "Activo"
        NetShieldStatus.READY_TO_START -> "Listo"
        NetShieldStatus.REQUIRES_CONSENT -> "Requiere consentimiento"
        NetShieldStatus.REQUIRES_VPN_AUTHORIZATION -> "Autorizar VPN"
        NetShieldStatus.REQUIRES_NOTIFICATION_PERMISSION -> "Notificacion requerida"
        NetShieldStatus.UNSAFE_TO_START -> "Advertencia pendiente"
        NetShieldStatus.STARTING -> "Iniciando"
        NetShieldStatus.STOPPING -> "Deteniendo"
        NetShieldStatus.ERROR -> "Error"
        NetShieldStatus.NOT_VALIDATED -> "No validado"
        NetShieldStatus.INACTIVE -> "Detenido"
    }
}

private fun NetShieldStatus.serviceTone(): OdinStatusTone {
    return when (this) {
        NetShieldStatus.ACTIVE -> OdinStatusTone.Safe
        NetShieldStatus.READY_TO_START -> OdinStatusTone.Planned
        NetShieldStatus.REQUIRES_CONSENT,
        NetShieldStatus.REQUIRES_VPN_AUTHORIZATION,
        NetShieldStatus.REQUIRES_NOTIFICATION_PERMISSION,
        NetShieldStatus.UNSAFE_TO_START,
        NetShieldStatus.NOT_VALIDATED -> OdinStatusTone.Warning
        NetShieldStatus.ERROR -> OdinStatusTone.Error
        NetShieldStatus.INACTIVE,
        NetShieldStatus.STARTING,
        NetShieldStatus.STOPPING -> OdinStatusTone.Neutral
    }
}
