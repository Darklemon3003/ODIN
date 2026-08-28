package com.odin.core.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.odin.core.domain.model.*
import com.odin.core.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val generalSettings by viewModel.generalSettings.collectAsState()
    val securitySettings by viewModel.securitySettings.collectAsState()
    val networkSettings by viewModel.networkSettings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Text(
                        text = "General",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                item {
                    SettingItem(
                        title = "Tema",
                        description = "Seleccionar tema de la aplicación",
                        icon = Icons.Default.Palette,
                        onClick = { }
                    ) {
                        Text(generalSettings.theme.name)
                    }
                }
                
                item {
                    SettingSwitch(
                        title = "Notificaciones",
                        description = "Habilitar notificaciones",
                        icon = Icons.Default.Notifications,
                        checked = generalSettings.notificationsEnabled,
                        onCheckedChange = {
                            viewModel.updateGeneralSettings(
                                generalSettings.copy(notificationsEnabled = it)
                            )
                        }
                    )
                }
                
                item {
                    SettingSwitch(
                        title = "Actualización Automática",
                        description = "Actualizar automáticamente",
                        icon = Icons.Default.Update,
                        checked = generalSettings.autoUpdateEnabled,
                        onCheckedChange = {
                            viewModel.updateGeneralSettings(
                                generalSettings.copy(autoUpdateEnabled = it)
                            )
                        }
                    )
                }
                
                item {
                    Text(
                        text = "Seguridad",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                item {
                    SettingItem(
                        title = "Nivel de Sensibilidad",
                        description = "Nivel de detección de amenazas",
                        icon = Icons.Default.Tune,
                        onClick = { }
                    ) {
                        Text(securitySettings.sensitivityLevel.name)
                    }
                }
                
                item {
                    SettingSwitch(
                        title = "Respuesta Automática",
                        description = "Responder automáticamente a amenazas",
                        icon = Icons.Default.Autorenew,
                        checked = securitySettings.autoResponseEnabled,
                        onCheckedChange = {
                            viewModel.updateSecuritySettings(
                                securitySettings.copy(autoResponseEnabled = it)
                            )
                        }
                    )
                }
                
                item {
                    SettingSwitch(
                        title = "Escaneo en Tiempo Real",
                        description = "Escanear en tiempo real",
                        icon = Icons.Default.Scanner,
                        checked = securitySettings.realTimeScanning,
                        onCheckedChange = {
                            viewModel.updateSecuritySettings(
                                securitySettings.copy(realTimeScanning = it)
                            )
                        }
                    )
                }
                
                item {
                    SettingSwitch(
                        title = "Análisis Heurístico",
                        description = "Usar análisis heurístico",
                        icon = Icons.Default.Psychology,
                        checked = securitySettings.heuristicAnalysis,
                        onCheckedChange = {
                            viewModel.updateSecuritySettings(
                                securitySettings.copy(heuristicAnalysis = it)
                            )
                        }
                    )
                }
                
                item {
                    Text(
                        text = "Red",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                item {
                    SettingSwitch(
                        title = "VPN",
                        description = "Habilitar VPN",
                        icon = Icons.Default.VpnKey,
                        checked = networkSettings.vpnEnabled,
                        onCheckedChange = {
                            viewModel.updateNetworkSettings(
                                networkSettings.copy(vpnEnabled = it)
                            )
                        }
                    )
                }
                
                item {
                    SettingSwitch(
                        title = "Tor",
                        description = "Habilitar Tor",
                        icon = Icons.Default.Security,
                        checked = networkSettings.torEnabled,
                        onCheckedChange = {
                            viewModel.updateNetworkSettings(
                                networkSettings.copy(torEnabled = it)
                            )
                        }
                    )
                }
                
                item {
                    SettingItem(
                        title = "Proveedor DNS",
                        description = "Seleccionar proveedor de DNS",
                        icon = Icons.Default.Dns,
                        onClick = { }
                    ) {
                        Text(networkSettings.dnsProvider.name)
                    }
                }
                
                item {
                    SettingSwitch(
                        title = "Proxy",
                        description = "Habilitar proxy",
                        icon = Icons.Default.Http,
                        checked = networkSettings.proxyEnabled,
                        onCheckedChange = {
                            viewModel.updateNetworkSettings(
                                networkSettings.copy(proxyEnabled = it)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    trailing: @Composable () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            trailing()
        }
    }
}

@Composable
fun SettingSwitch(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
