package com.odin.core.ui.screens.netshield

import android.Manifest
import android.app.Activity
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.VpnKey
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
import com.odin.core.ui.components.OdinEmptyState
import com.odin.core.ui.components.OdinPrimaryButton
import com.odin.core.ui.components.OdinSecondaryButton
import com.odin.core.ui.components.OdinSectionHeader
import com.odin.core.ui.components.OdinStatusChip
import com.odin.core.ui.components.OdinStatusTone
import com.odin.core.ui.model.NetShieldStatus
import com.odin.core.ui.model.NetShieldUiState
import com.odin.core.ui.theme.OdinTextPrimary
import com.odin.core.ui.theme.OdinTextSecondary
import com.odin.core.ui.viewmodel.NetShieldViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetShieldControlScreen(
    viewModel: NetShieldViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val vpnAuthorizationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            viewModel.onVpnAuthorizationResult(result.resultCode == Activity.RESULT_OK)
        }
    )
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
                title = { Text("NetShield / VPN local") },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar NetShield")
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
            item { HeaderCard(uiState) }
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
            item {
                VpnAuthorizationCard(
                    uiState = uiState,
                    onAuthorize = {
                        val authorizationIntent = viewModel.createVpnAuthorizationIntent()
                        if (authorizationIntent != null) {
                            vpnAuthorizationLauncher.launch(authorizationIntent)
                        }
                    }
                )
            }
            item { ConnectivityRiskCard(uiState, viewModel) }
            item { ControlsCard(uiState, viewModel) }
            item { RuntimeCard(uiState) }
            item { MetricsCard(uiState) }
            item {
                OdinEmptyState(
                    title = "Limites de privacidad",
                    message = "NetShield utiliza la interfaz VPN local de Android para el modo de red de ODIN. Esta version no garantiza reenvio completo del trafico como un servicio VPN comercial. No descifra TLS, no inspecciona contenido privado, no garantiza anonimato y no envia trafico a servidores externos.",
                    icon = Icons.Default.Info
                )
            }
        }
    }
}

@Composable
private fun HeaderCard(uiState: NetShieldUiState) {
    OdinCard(alert = uiState.status == NetShieldStatus.ERROR || uiState.status == NetShieldStatus.UNSAFE_TO_START) {
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
                    text = "Control transparente de VPN local",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = OdinTextPrimary
                )
                Text(
                    text = "Flujo separado de consentimiento, autorizacion Android VPN, inicio y detencion.",
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
    uiState: NetShieldUiState,
    viewModel: NetShieldViewModel
) {
    OdinCard(alert = uiState.requiresConsent) {
        StatusRow(
            icon = Icons.Default.Security,
            title = "Consentimiento NetShield",
            message = "Permite configurar una VPN local visible. No inicia el servicio ni autoriza VPN por si solo.",
            status = if (uiState.isConsentGranted) "Concedido" else "Requiere consentimiento",
            tone = if (uiState.isConsentGranted) OdinStatusTone.Safe else OdinStatusTone.Warning
        )
        if (!uiState.isConsentGranted) {
            OdinPrimaryButton(
                text = "Entiendo y acepto configurar NetShield",
                icon = Icons.Default.CheckCircle,
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
    uiState: NetShieldUiState,
    onRequestPermission: () -> Unit
) {
    OdinCard(alert = uiState.requiresNotificationPermission) {
        StatusRow(
            icon = Icons.Default.Notifications,
            title = "Notificacion foreground",
            message = "NetShield debe mostrar una notificacion permanente mientras la VPN local este activa.",
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
private fun VpnAuthorizationCard(
    uiState: NetShieldUiState,
    onAuthorize: () -> Unit
) {
    OdinCard(alert = uiState.requiresVpnAuthorization) {
        StatusRow(
            icon = Icons.Default.VpnKey,
            title = "Autorizacion Android VPN",
            message = "Android mostrara el dialogo oficial. Si se cancela, NetShield no inicia.",
            status = if (uiState.isVpnAuthorized) "Autorizada" else "Requiere autorizacion",
            tone = if (uiState.isVpnAuthorized) OdinStatusTone.Safe else OdinStatusTone.Warning
        )
        OdinSecondaryButton(
            text = "Autorizar VPN local",
            icon = Icons.Default.VpnKey,
            onClick = onAuthorize,
            enabled = uiState.canRequestVpnAuthorization,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ConnectivityRiskCard(
    uiState: NetShieldUiState,
    viewModel: NetShieldViewModel
) {
    OdinCard(alert = !uiState.isConnectivityRiskAccepted) {
        StatusRow(
            icon = Icons.Default.Warning,
            title = "Validacion segura",
            message = "El motor de forwarding sigue pendiente. Esta fase inicia la VPN en modo visible sin ruta full-tunnel ni DNS forzado.",
            status = if (uiState.isConnectivityRiskAccepted) "Aceptado" else "Pendiente",
            tone = if (uiState.isConnectivityRiskAccepted) OdinStatusTone.Safe else OdinStatusTone.Warning
        )
        if (!uiState.isConnectivityRiskAccepted) {
            OdinSecondaryButton(
                text = "Entiendo el riesgo de conectividad",
                icon = Icons.Default.Warning,
                onClick = { viewModel.acceptConnectivityRisk() },
                enabled = uiState.isConsentGranted && uiState.isVpnAuthorized,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ControlsCard(
    uiState: NetShieldUiState,
    viewModel: NetShieldViewModel
) {
    OdinCard {
        OdinSectionHeader(
            title = "Control del servicio",
            subtitle = "No se inicia desde Dashboard ni al abrir ODIN"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OdinPrimaryButton(
                text = "Iniciar",
                icon = Icons.Default.PowerSettingsNew,
                onClick = { viewModel.startNetShield() },
                enabled = uiState.canStart,
                modifier = Modifier.weight(1f)
            )
            OdinDangerButton(
                text = "Detener",
                icon = Icons.Default.StopCircle,
                onClick = { viewModel.stopNetShield() },
                enabled = uiState.canStop,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RuntimeCard(uiState: NetShieldUiState) {
    OdinCard {
        OdinSectionHeader(
            title = "Estado runtime",
            subtitle = "Activo solo si hay heartbeat fresco del servicio"
        )
        StatusRow(
            icon = Icons.Default.VpnKey,
            title = "NetShieldVpnService",
            message = if (uiState.isServiceRunning) {
                "Servicio foreground con heartbeat vigente."
            } else {
                "Servicio detenido o no validado."
            },
            status = if (uiState.isServiceRunning) "Activo" else "Detenido",
            tone = if (uiState.isServiceRunning) OdinStatusTone.Safe else OdinStatusTone.Neutral
        )
        StatusRow(
            icon = Icons.Default.Security,
            title = "Motor VPN",
            message = "Modo validacion segura: sin full-tunnel, sin DNS forzado y sin metricas inventadas.",
            status = if (uiState.isSafeValidationMode) "Validacion" else "Operativo",
            tone = if (uiState.isSafeValidationMode) OdinStatusTone.Planned else OdinStatusTone.Safe
        )
        Text(
            text = "Ultimo heartbeat: ${formatTime(uiState.lastHeartbeat)}",
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
private fun MetricsCard(uiState: NetShieldUiState) {
    OdinCard {
        OdinSectionHeader(
            title = "Metricas",
            subtitle = "Solo datos reales cuando el servicio los exponga de forma confiable"
        )
        StatusRow(
            icon = Icons.Default.Info,
            title = "Trafico",
            message = uiState.metrics.message,
            status = "No disponible",
            tone = OdinStatusTone.Neutral
        )
        StatusRow(
            icon = Icons.Default.Security,
            title = "Paquetes auditados",
            message = "No se muestran amenazas bloqueadas ni trafico protegido sin evidencia.",
            status = uiState.metrics.packetsAudited?.toString() ?: "Sin datos",
            tone = if (uiState.metrics.packetsAudited != null) OdinStatusTone.Planned else OdinStatusTone.Neutral
        )
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

private fun NetShieldStatus.label(): String {
    return when (this) {
        NetShieldStatus.INACTIVE -> "Detenido"
        NetShieldStatus.REQUIRES_CONSENT -> "Requiere consentimiento"
        NetShieldStatus.REQUIRES_VPN_AUTHORIZATION -> "Autorizar VPN"
        NetShieldStatus.REQUIRES_NOTIFICATION_PERMISSION -> "Notificacion requerida"
        NetShieldStatus.READY_TO_START -> "Listo"
        NetShieldStatus.STARTING -> "Iniciando"
        NetShieldStatus.ACTIVE -> "Activo"
        NetShieldStatus.STOPPING -> "Deteniendo"
        NetShieldStatus.ERROR -> "Error"
        NetShieldStatus.NOT_VALIDATED -> "No validado"
        NetShieldStatus.UNSAFE_TO_START -> "Advertencia pendiente"
    }
}

private fun NetShieldStatus.tone(): OdinStatusTone {
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

private fun formatTime(timestamp: Long?): String {
    if (timestamp == null || timestamp <= 0L) {
        return "Sin datos"
    }
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
}
