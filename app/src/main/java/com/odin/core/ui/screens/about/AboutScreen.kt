package com.odin.core.ui.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
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
import com.odin.core.ui.components.OdinSectionHeader
import com.odin.core.ui.components.OdinStatusChip
import com.odin.core.ui.components.OdinStatusTone
import com.odin.core.ui.theme.OdinTextPrimary
import com.odin.core.ui.theme.OdinTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Acerca de ODIN") })
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
                                text = "ODIN",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = OdinTextPrimary
                            )
                            Text(
                                text = "Aplicacion defensiva para monitoreo local, permisos, eventos y proteccion transparente del usuario.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OdinTextSecondary
                            )
                        }
                        OdinStatusChip(
                            text = "Fase 6",
                            tone = OdinStatusTone.Planned
                        )
                    }
                }
            }

            item {
                OdinSectionHeader(
                    title = "Principios",
                    subtitle = "Producto defensivo, no invasivo"
                )
            }
            item {
                PrincipleCard(
                    title = "Consentimiento primero",
                    description = "Las funciones sensibles deben explicar su alcance antes de pedir permisos o iniciar servicios.",
                    icon = Icons.Default.PrivacyTip
                )
            }
            item {
                PrincipleCard(
                    title = "Transparencia",
                    description = "ODIN no debe ocultar servicios activos ni presentar funciones parciales como completas.",
                    icon = Icons.Default.Info
                )
            }
            item {
                PrincipleCard(
                    title = "Privacidad local",
                    description = "No debe interceptar contenido privado ni descifrar trafico TLS del usuario.",
                    icon = Icons.Default.Shield
                )
            }
            item {
                PrincipleCard(
                    title = "Cumplimiento Android",
                    description = "Los permisos, foreground services y VPN deben respetar politicas de Android y Google Play.",
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
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Esta version organiza navegacion y pantallas principales. La activacion segura de SentryCore y NetShield queda para fases posteriores.",
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
private fun PrincipleCard(
    title: String,
    description: String,
    icon: ImageVector
) {
    OdinCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
    }
}
