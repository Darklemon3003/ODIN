package com.odin.core.ui.screens.network

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
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
import com.odin.core.ui.components.OdinPrimaryButton
import com.odin.core.ui.components.OdinSecondaryButton
import com.odin.core.ui.components.OdinSectionHeader
import com.odin.core.ui.components.OdinStatusChip
import com.odin.core.ui.components.OdinStatusTone
import com.odin.core.ui.theme.OdinTextPrimary
import com.odin.core.ui.theme.OdinTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkScreen(
    onOpenNetShield: () -> Unit = {},
    onOpenPermissions: () -> Unit = {},
    onOpenServices: () -> Unit = {},
    onOpenLogs: () -> Unit = {},
    onOpenFirewall: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Red / NetShield") })
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
                OdinCard(alert = true) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "NetShield / VPN local",
                                style = MaterialTheme.typography.titleLarge,
                                color = OdinTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Estado informativo. ODIN no inicia VPN ni solicita autorizacion desde esta pantalla.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OdinTextSecondary
                            )
                        }
                        OdinStatusChip(
                            text = "Requiere configuracion",
                            tone = OdinStatusTone.Warning
                        )
                    }
                    OdinSecondaryButton(
                        text = "Revisar permisos",
                        icon = Icons.Default.Tune,
                        onClick = onOpenPermissions,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OdinPrimaryButton(
                        text = "Configurar NetShield",
                        icon = Icons.Default.VpnKey,
                        onClick = onOpenNetShield,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                OdinSectionHeader(
                    title = "Estado de red",
                    subtitle = "Sin metricas simuladas ni trafico inventado"
                )
            }

            item {
                NetworkInfoCard(
                    title = "VPN local",
                    description = "Requiere consentimiento y autorizacion explicita del usuario antes de operar.",
                    status = "No activa",
                    tone = OdinStatusTone.Neutral,
                    icon = Icons.Default.VpnKey
                )
            }
            item {
                NetworkInfoCard(
                    title = "Auditoria de trafico",
                    description = "No hay fuente validada para bytes, paquetes o dominios en esta pantalla.",
                    status = "Sin datos",
                    tone = OdinStatusTone.Neutral,
                    icon = Icons.Default.Description,
                    actionLabel = "Ver logs",
                    onAction = onOpenLogs
                )
            }
            item {
                NetworkInfoCard(
                    title = "Firewall avanzado",
                    description = "La creacion, edicion y activacion de reglas sigue bloqueada.",
                    status = "Bloqueado",
                    tone = OdinStatusTone.Unavailable,
                    icon = Icons.Default.Security,
                    actionLabel = "Ver firewall",
                    onAction = onOpenFirewall
                )
            }
            item {
                NetworkInfoCard(
                    title = "Privacidad DNS",
                    description = "DoH, DoT, DNSSEC e IP leak no se muestran como activos hasta tener validacion real.",
                    status = "Planeado",
                    tone = OdinStatusTone.Planned,
                    icon = Icons.Default.Dns
                )
            }
            item {
                OdinCard {
                    OdinSectionHeader(
                        title = "Limites de privacidad",
                        subtitle = "ODIN debe ser defensivo y transparente"
                    )
                    PrivacyPoint(
                        icon = Icons.Default.Shield,
                        text = "No descifra trafico TLS privado."
                    )
                    PrivacyPoint(
                        icon = Icons.Default.Warning,
                        text = "No promete proteccion de red completa sin autorizacion VPN y evidencia runtime."
                    )
                    PrivacyPoint(
                        icon = Icons.Default.Security,
                        text = "Los servicios de red no se inician automaticamente al abrir la app."
                    )
                    OdinSecondaryButton(
                        text = "Ver servicios",
                        icon = Icons.Default.Security,
                        onClick = onOpenServices,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun NetworkInfoCard(
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

@Composable
private fun PrivacyPoint(
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = OdinTextSecondary
        )
    }
}
