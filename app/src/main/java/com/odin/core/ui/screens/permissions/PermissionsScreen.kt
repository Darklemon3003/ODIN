package com.odin.core.ui.screens.permissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VpnKey
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
import com.odin.core.ui.theme.OdinTextPrimary
import com.odin.core.ui.theme.OdinTextSecondary
import com.odin.core.ui.viewmodel.SentryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen(
    onOpenNetShield: () -> Unit = {},
    onOpenSentry: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenServices: () -> Unit = {},
    sentryViewModel: SentryViewModel = hiltViewModel()
) {
    val sentryState by sentryViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        sentryViewModel.refresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Permisos") })
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
                        text = "Permisos y consentimientos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = OdinTextPrimary
                    )
                    Text(
                        text = "Esta pantalla solo informa. Ningun permiso se solicita automaticamente al abrir ODIN.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OdinTextSecondary
                    )
                }
            }

            item {
                OdinSectionHeader(
                    title = "Permisos criticos",
                    subtitle = "Basado en el Manifest y el modelo de consentimiento vigente"
                )
            }
            item {
                PermissionInfoCard(
                    title = "Notificaciones",
                    description = "Necesarias para foreground services cuando el usuario active una proteccion.",
                    status = if (sentryState.isNotificationPermissionGranted) "Disponible" else "Requiere permiso",
                    tone = if (sentryState.isNotificationPermissionGranted) OdinStatusTone.Safe else OdinStatusTone.Warning,
                    icon = Icons.Default.Notifications,
                    actionLabel = "Configurar Sentry",
                    onAction = onOpenSentry
                )
            }
            item {
                PermissionInfoCard(
                    title = "VPN local",
                    description = "Requiere autorizacion explicita del sistema antes de crear un tunel local.",
                    status = "Requiere autorizacion",
                    tone = OdinStatusTone.Warning,
                    icon = Icons.Default.VpnKey,
                    actionLabel = "Configurar NetShield",
                    onAction = onOpenNetShield
                )
            }
            item {
                PermissionInfoCard(
                    title = "Acceso a archivos",
                    description = "Debe pedirse desde una accion de analisis de archivo, no desde el inicio.",
                    status = "Consentimiento pendiente",
                    tone = OdinStatusTone.Neutral,
                    icon = Icons.Default.Description
                )
            }
            item {
                PermissionInfoCard(
                    title = "Inicio en arranque",
                    description = "BootReceiver solo debe operar con consentimiento de Sentry y arranque habilitados.",
                    status = "Doble consentimiento",
                    tone = OdinStatusTone.Warning,
                    icon = Icons.Default.Security,
                    actionLabel = "Ver servicios",
                    onAction = onOpenServices
                )
            }
            item {
                PermissionInfoCard(
                    title = "Superposicion",
                    description = "No esta declarada como requisito activo de Fase 6.",
                    status = "No requerido",
                    tone = OdinStatusTone.Unavailable,
                    icon = Icons.Default.Block
                )
            }

            item {
                OdinCard {
                    OdinSectionHeader(
                        title = "Regla de producto",
                        subtitle = "Solicitud progresiva y transparente"
                    )
                    Text(
                        text = "ODIN debe pedir cada permiso desde la funcion que lo necesita y explicar el motivo antes de redirigir al sistema.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OdinTextSecondary
                    )
                    OdinSecondaryButton(
                        text = "Abrir ajustes",
                        icon = Icons.Default.Settings,
                        onClick = onOpenSettings,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionInfoCard(
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
                        color = OdinTextPrimary,
                        fontWeight = FontWeight.SemiBold
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
