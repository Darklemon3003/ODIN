package com.odin.core.ui.screens.quarantine

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
import com.odin.core.domain.model.QuarantinedApp
import com.odin.core.ui.theme.*
import com.odin.core.ui.viewmodel.QuarantineViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuarantineScreen(
    viewModel: QuarantineViewModel = hiltViewModel()
) {
    val quarantinedApps by viewModel.quarantinedApps.collectAsState()
    val selectedApp by viewModel.selectedApp.collectAsState()
    val behaviorData by viewModel.behaviorData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Centro de Cuarentena") }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (quarantinedApps.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay aplicaciones en cuarentena",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(quarantinedApps) { app ->
                        QuarantineAppItem(
                            app = app,
                            onClick = { viewModel.selectApp(app) }
                        )
                    }
                }
            }
        }
        
        selectedApp?.let { app ->
            QuarantineAppDetailDialog(
                app = app,
                behaviorData = behaviorData,
                onDismiss = { viewModel.clearSelectedApp() },
                onRelease = { viewModel.releaseApp(app) },
                onDelete = { },
                onKeep = { }
            )
        }
    }
}

@Composable
fun QuarantineAppItem(
    app: QuarantinedApp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val riskColor = when {
        app.riskScore >= 80 -> ErrorDark
        app.riskScore >= 60 -> Error
        app.riskScore >= 40 -> Warning
        else -> WarningLight
    }
    
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = dateFormat.format(Date(app.quarantineDate))
    
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
                    .size(48.dp)
                    .background(
                        riskColor.copy(alpha = 0.2f),
                        androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = "Quarantined",
                    tint = riskColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = app.appName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Riesgo: ${app.riskScore}/100",
                    style = MaterialTheme.typography.labelSmall,
                    color = riskColor
                )
                Text(
                    text = "Cuarentenado: $date",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun QuarantineAppDetailDialog(
    app: QuarantinedApp,
    behaviorData: com.odin.core.domain.model.BehaviorData?,
    onDismiss: () -> Unit,
    onRelease: () -> Unit,
    onDelete: () -> Unit,
    onKeep: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(app.appName) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Paquete: ${app.packageName}")
                Text("Motivo: ${app.quarantineReason}")
                Text("Puntuación Riesgo: ${app.riskScore}/100")
                Text("Puntuación Comportamiento: ${app.behaviorScore}/100")
                
                if (app.permissions.isNotEmpty()) {
                    Text("Permisos:")
                    app.permissions.forEach { permission ->
                        Text("• $permission")
                    }
                }
                
                Text(
                    text = "Eliminar y mantener estan deshabilitados hasta completar la implementacion segura de cuarentena.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                behaviorData?.let { data ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Datos de Comportamiento:")
                    Text("Solicitudes de Red: ${data.networkRequests}")
                    Text("Uso CPU: ${data.resourceUsage.cpuUsage}%")
                    Text("Uso Memoria: ${data.resourceUsage.memoryUsage}%")
                    Text("Uso Batería: ${data.resourceUsage.batteryUsage}%")
                }
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onRelease) {
                    Text("Liberar")
                }
                Button(
                    onClick = onDelete,
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(containerColor = Error)
                ) {
                    Text("Eliminar")
                }
                OutlinedButton(
                    onClick = onKeep,
                    enabled = false
                ) {
                    Text("Mantener")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
