package com.odin.core.ui.screens.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.odin.core.ui.components.OdinCard
import com.odin.core.ui.components.OdinSecondaryButton
import com.odin.core.ui.components.OdinSectionHeader
import com.odin.core.ui.components.OdinStatusChip
import com.odin.core.ui.components.OdinStatusTone
import com.odin.core.ui.theme.OdinTextPrimary
import com.odin.core.ui.theme.OdinTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    onOpenSentry: () -> Unit = {},
    onOpenPermissions: () -> Unit = {},
    onOpenServices: () -> Unit = {},
    onOpenNetwork: () -> Unit = {},
    onOpenLogs: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenAbout: () -> Unit = {},
    onOpenFirewall: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Herramientas") })
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
                ToolsHeader()
            }
            item {
                QuickAccessCard(
                    onOpenPermissions = onOpenPermissions,
                    onOpenServices = onOpenServices,
                    onOpenAbout = onOpenAbout,
                    onOpenSettings = onOpenSettings
                )
            }
            item {
                ToolCategory(title = "Proteccion") {
                    ToolCard(
                        title = "SentryCore",
                        description = "Servicio visible con consentimiento, notificacion permanente y control de inicio/detencion.",
                        status = "Requiere consentimiento",
                        tone = OdinStatusTone.Warning,
                        icon = Icons.Default.Shield,
                        actionLabel = "Configurar Sentry",
                        onAction = onOpenSentry
                    )
                    ToolCard(
                        title = "Escaner de archivos",
                        description = "Activity legacy existente; no se abre desde Fase 6 hasta auditar permisos y UX.",
                        status = "Legacy parcial",
                        tone = OdinStatusTone.Planned,
                        icon = Icons.Default.Scanner
                    )
                    ToolCard(
                        title = "Firewall avanzado",
                        description = "Crear, editar y alternar reglas sigue bloqueado hasta cerrar el contrato seguro.",
                        status = "Bloqueado",
                        tone = OdinStatusTone.Unavailable,
                        icon = Icons.Default.Security,
                        actionLabel = "Ver estado",
                        onAction = onOpenFirewall
                    )
                    ToolCard(
                        title = "Cuarentena",
                        description = "Acciones destructivas dependen de consentimiento y confirmacion en una fase posterior.",
                        status = "Parcial",
                        tone = OdinStatusTone.Planned,
                        icon = Icons.Default.Block
                    )
                }
            }
            item {
                ToolCategory(title = "Privacidad") {
                    ToolCard(
                        title = "Revision de permisos",
                        description = "Resumen informativo; no solicita permisos automaticamente.",
                        status = "Disponible",
                        tone = OdinStatusTone.Safe,
                        icon = Icons.Default.Notifications,
                        actionLabel = "Revisar",
                        onAction = onOpenPermissions
                    )
                    ToolCard(
                        title = "Consentimientos",
                        description = "Modelo central existe, pero el flujo de configuracion detallado queda pendiente.",
                        status = "Requiere configuracion",
                        tone = OdinStatusTone.Warning,
                        icon = Icons.Default.Tune,
                        actionLabel = "Abrir ajustes",
                        onAction = onOpenSettings
                    )
                    ToolCard(
                        title = "Exportacion de logs",
                        description = "Bloqueada si no existe consentimiento explicito de exportacion.",
                        status = "Bloqueado",
                        tone = OdinStatusTone.Unavailable,
                        icon = Icons.Default.Description,
                        actionLabel = "Ver logs",
                        onAction = onOpenLogs
                    )
                }
            }
            item {
                ToolCategory(title = "Sistema") {
                    ToolCard(
                        title = "Estado de servicios",
                        description = "Vista informativa de servicios declarados, sin iniciar foreground services.",
                        status = "Disponible",
                        tone = OdinStatusTone.Safe,
                        icon = Icons.Default.Security,
                        actionLabel = "Ver estado",
                        onAction = onOpenServices
                    )
                    ToolCard(
                        title = "Diagnostico local",
                        description = "Pendiente de pruebas manuales y telemetria local confiable.",
                        status = "Planeado",
                        tone = OdinStatusTone.Planned,
                        icon = Icons.Default.Info
                    )
                }
            }
            item {
                ToolCategory(title = "Red") {
                    ToolCard(
                        title = "NetShield / VPN local",
                        description = "Requiere consentimiento, autorizacion Android VPN y control visible.",
                        status = "Requiere configuracion",
                        tone = OdinStatusTone.Warning,
                        icon = Icons.Default.VpnKey,
                        actionLabel = "Configurar",
                        onAction = onOpenNetwork
                    )
                    ToolCard(
                        title = "Privacidad DNS y fugas IP",
                        description = "Componentes legacy detectados; no se muestran como proteccion activa.",
                        status = "Planeado",
                        tone = OdinStatusTone.Planned,
                        icon = Icons.Default.Security
                    )
                }
            }
        }
    }
}

@Composable
private fun ToolsHeader() {
    OdinCard {
        Text(
            text = "Centro de herramientas ODIN",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = OdinTextPrimary
        )
        Text(
            text = "Modulos organizados por disponibilidad real. Las funciones sensibles no se activan desde esta fase.",
            style = MaterialTheme.typography.bodyMedium,
            color = OdinTextSecondary
        )
    }
}

@Composable
private fun QuickAccessCard(
    onOpenPermissions: () -> Unit,
    onOpenServices: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenSettings: () -> Unit
) {
    OdinCard {
        OdinSectionHeader(
            title = "Accesos seguros",
            subtitle = "Solo navegacion interna; no cambia consentimientos ni permisos"
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OdinSecondaryButton(
                text = "Permisos",
                icon = Icons.Default.Notifications,
                onClick = onOpenPermissions,
                modifier = Modifier.weight(1f)
            )
            OdinSecondaryButton(
                text = "Servicios",
                icon = Icons.Default.Security,
                onClick = onOpenServices,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OdinSecondaryButton(
                text = "Acerca",
                icon = Icons.Default.Info,
                onClick = onOpenAbout,
                modifier = Modifier.weight(1f)
            )
            OdinSecondaryButton(
                text = "Ajustes",
                icon = Icons.Default.Settings,
                onClick = onOpenSettings,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ToolCategory(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OdinSectionHeader(title = title)
        content()
    }
}

@Composable
private fun ToolCard(
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
        } else {
            Spacer(modifier = Modifier.size(0.dp))
        }
    }
}
