package com.odin.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.odin.core.domain.model.SecurityStatus
import com.odin.core.domain.model.ThreatLevel
import com.odin.core.ui.theme.*

@Composable
fun SecurityStatusCard(
    status: SecurityStatus,
    modifier: Modifier = Modifier
) {
    val statusColor = when (status.threatLevel) {
        ThreatLevel.SECURE -> Primary
        ThreatLevel.LOW -> Warning
        ThreatLevel.MEDIUM -> WarningDark
        ThreatLevel.HIGH -> Error
        ThreatLevel.CRITICAL -> ErrorDark
    }
    
    val statusText = when (status.threatLevel) {
        ThreatLevel.SECURE -> "SIN ALERTAS ACTIVAS"
        ThreatLevel.LOW -> "RIESGO BAJO"
        ThreatLevel.MEDIUM -> "RIESGO MEDIO"
        ThreatLevel.HIGH -> "RIESGO ALTO"
        ThreatLevel.CRITICAL -> "RIESGO CRITICO"
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 232.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            statusColor.copy(alpha = 0.2f),
                            SurfaceVariant
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Security Shield",
                    tint = statusColor,
                    modifier = Modifier.size(56.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = statusColor,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Amenazas Activas: ${status.activeThreats}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurface,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ComponentIndicator("VPN", status.components.vpnActive)
                    ComponentIndicator("Firewall", status.components.firewallActive)
                    ComponentIndicator("Deteccion", status.components.detectionActive)
                }
            }
        }
    }
}

@Composable
fun ComponentIndicator(
    label: String,
    isActive: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    if (isActive) Primary else Neutral600,
                    RoundedCornerShape(4.dp)
                )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isActive) Primary else Neutral600
        )
    }
}
