package com.odin.core.ui.screens.threats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.odin.core.network.detection.DetectionResult
import com.odin.core.network.detection.ThreatLevel
import com.odin.core.ui.theme.*
import com.odin.core.ui.viewmodel.ThreatsViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreatsScreen(
    viewModel: ThreatsViewModel = hiltViewModel()
) {
    val threats by viewModel.threats.collectAsState()
    val statistics by viewModel.statistics.collectAsState()
    val selectedThreat by viewModel.selectedThreat.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Centro de Amenazas") },
                actions = {
                    IconButton(onClick = { viewModel.refreshThreats() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            statistics?.let { stats ->
                ThreatStatisticsCard(stats)
            }
            
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(threats) { threat ->
                        ThreatItem(
                            threat = threat,
                            onClick = { viewModel.selectThreat(threat) },
                            onDismiss = { viewModel.dismissThreat(threat) }
                        )
                    }
                }
            }
        }
        
        selectedThreat?.let { threat ->
            ThreatDetailDialog(
                threat = threat,
                onDismiss = { viewModel.clearSelectedThreat() }
            )
        }
    }
}

@Composable
fun ThreatStatisticsCard(
    statistics: com.odin.core.domain.model.ThreatStatistics,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Estadísticas de Amenazas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem("Total", statistics.totalThreats.toString(), Neutral600)
                StatItem("Críticas", statistics.criticalCount.toString(), ErrorDark)
                StatItem("Altas", statistics.highCount.toString(), Error)
                StatItem("Medias", statistics.mediumCount.toString(), Warning)
                StatItem("Bajas", statistics.lowCount.toString(), WarningLight)
            }
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ThreatItem(
    threat: DetectionResult,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val threatColor = when (threat.threatLevel) {
        ThreatLevel.CRITICAL -> ErrorDark
        ThreatLevel.HIGH -> Error
        ThreatLevel.MEDIUM -> Warning
        ThreatLevel.LOW -> WarningLight
        ThreatLevel.INFO -> Info
    }
    
    val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val time = dateFormat.format(Date(threat.detectionTimestamp))
    
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(threatColor, androidx.compose.foundation.shape.CircleShape)
            )
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = threat.threatType,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${threat.sourceIp}:${threat.sourcePort} → ${threat.destinationIp}:${threat.destinationPort}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$time • ${(threat.confidence * 100).toInt()}% confianza",
                    style = MaterialTheme.typography.labelSmall,
                    color = threatColor
                )
            }
            
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Dismiss")
            }
        }
    }
}

@Composable
fun ThreatDetailDialog(
    threat: DetectionResult,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalle de Amenaza") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Tipo: ${threat.threatType}")
                Text("Descripción: ${threat.threatDescription}")
                Text("Nivel: ${threat.threatLevel.name}")
                Text("Detector: ${threat.detectorName}")
                Text("IP Origen: ${threat.sourceIp}:${threat.sourcePort}")
                Text("IP Destino: ${threat.destinationIp}:${threat.destinationPort}")
                Text("Confianza: ${(threat.confidence * 100).toInt()}%")
                Text("Evidencia: ${threat.evidence}")
                Text("Acción Recomendada: ${threat.recommendedAction}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
