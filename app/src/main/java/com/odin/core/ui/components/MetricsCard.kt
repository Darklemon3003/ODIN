package com.odin.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.odin.core.ui.theme.SurfaceVariant
import com.odin.core.ui.theme.Warning
import com.odin.core.ui.theme.Primary
import com.odin.core.ui.theme.Error

@Composable
fun MetricsCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun MetricsGrid(
    cpuUsage: Float,
    ramUsage: Float,
    postureScore: Int,
    activeThreats: Int,
    edrHealth: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricsCard(
                title = "CPU ODIN",
                value = "${cpuUsage.toInt()}%",
                icon = Icons.Default.Memory,
                modifier = Modifier.weight(1f),
                color = if (cpuUsage < 50) Primary else Warning
            )
            MetricsCard(
                title = "RAM ODIN",
                value = "${ramUsage.toInt()}%",
                icon = Icons.Default.Storage,
                modifier = Modifier.weight(1f),
                color = if (ramUsage < 70) Primary else Warning
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricsCard(
                title = "Postura",
                value = postureScore.toString(),
                icon = Icons.Default.Star,
                modifier = Modifier.weight(1f),
                color = if (postureScore >= 80) Primary else if (postureScore >= 60) Warning else Error
            )
            MetricsCard(
                title = "Amenazas Activas",
                value = activeThreats.toString(),
                icon = Icons.Default.Warning,
                modifier = Modifier.weight(1f),
                color = if (activeThreats == 0) Primary else Error
            )
        }
        
        MetricsCard(
            title = "Salud EDR",
            value = "$edrHealth%",
            icon = Icons.Default.HealthAndSafety,
            modifier = Modifier.fillMaxWidth(),
            color = if (edrHealth >= 80) Primary else Warning
        )
    }
}
