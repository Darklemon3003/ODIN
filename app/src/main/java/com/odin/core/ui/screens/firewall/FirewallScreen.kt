package com.odin.core.ui.screens.firewall

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.odin.core.domain.model.FirewallRule
import com.odin.core.ui.viewmodel.FirewallViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirewallScreen(
    viewModel: FirewallViewModel = hiltViewModel()
) {
    val rules by viewModel.rules.collectAsState()
    val selectedRule by viewModel.selectedRule.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isCreatingRule by viewModel.isCreatingRule.collectAsState()
    
    var showCreateDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Centro de Firewall") },
                actions = {
                    IconButton(
                        onClick = { },
                        enabled = false
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Rule")
                    }
                }
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Creacion, edicion y activacion de reglas deshabilitadas hasta completar la implementacion del contrato de Firewall.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                items(rules) { rule ->
                    FirewallRuleItem(
                        rule = rule,
                        onToggle = { },
                        onClick = { },
                        onDelete = { viewModel.deleteRule(rule.id) }
                    )
                }
            }
        }
        
        if (showCreateDialog) {
            CreateRuleDialog(
                onDismiss = { showCreateDialog = false },
                onCreate = { rule ->
                    viewModel.createRule(rule)
                    showCreateDialog = false
                },
                isLoading = isCreatingRule
            )
        }
        
        selectedRule?.let { rule ->
            EditRuleDialog(
                rule = rule,
                onDismiss = { viewModel.clearSelectedRule() },
                onUpdate = { updatedRule ->
                    viewModel.updateRule(updatedRule)
                    viewModel.clearSelectedRule()
                }
            )
        }
    }
}

@Composable
fun FirewallRuleItem(
    rule: FirewallRule,
    onToggle: () -> Unit,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = rule.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
                Text(
                    text = "${rule.sourceIp}:${rule.sourcePort} → ${rule.destinationIp}:${rule.destinationPort}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Protocolo: ${rule.protocol}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Switch(
                    checked = rule.isEnabled(),
                    onCheckedChange = { onToggle() },
                    enabled = false
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Composable
fun CreateRuleDialog(
    onDismiss: () -> Unit,
    onCreate: (FirewallRule) -> Unit,
    isLoading: Boolean
) {
    var name by remember { mutableStateOf("") }
    var sourceIp by remember { mutableStateOf("") }
    var sourcePort by remember { mutableStateOf("0") }
    var destinationIp by remember { mutableStateOf("") }
    var destinationPort by remember { mutableStateOf("0") }
    var protocol by remember { mutableStateOf("TCP") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Regla de Firewall") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = sourceIp,
                    onValueChange = { sourceIp = it },
                    label = { Text("IP Origen") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = sourcePort,
                    onValueChange = { sourcePort = it },
                    label = { Text("Puerto Origen") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = destinationIp,
                    onValueChange = { destinationIp = it },
                    label = { Text("IP Destino") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = destinationPort,
                    onValueChange = { destinationPort = it },
                    label = { Text("Puerto Destino") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = protocol,
                    onValueChange = { protocol = it },
                    label = { Text("Protocolo") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val destPort = destinationPort.toIntOrNull() ?: 0
                    val portValue = if (destPort == 0) null else Integer.valueOf(destPort)
                    onCreate(
                        FirewallRule(
                            java.util.UUID.randomUUID().toString(),
                            name,
                            destinationIp,
                            sourceIp,
                            sourcePort.toIntOrNull() ?: 0,
                            destinationIp,
                            destPort,
                            protocol,
                            true,
                            System.currentTimeMillis(),
                            portValue
                        )
                    )
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Crear")
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

@Composable
fun EditRuleDialog(
    rule: FirewallRule,
    onDismiss: () -> Unit,
    onUpdate: (FirewallRule) -> Unit
) {
    var name by remember { mutableStateOf(rule.name) }
    var sourceIp by remember { mutableStateOf(rule.sourceIp) }
    var sourcePort by remember { mutableStateOf(rule.sourcePort.toString()) }
    var destinationIp by remember { mutableStateOf(rule.destinationIp) }
    var destinationPort by remember { mutableStateOf(rule.destinationPort.toString()) }
    var protocol by remember { mutableStateOf(rule.protocol) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Regla de Firewall") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = sourceIp,
                    onValueChange = { sourceIp = it },
                    label = { Text("IP Origen") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = sourcePort,
                    onValueChange = { sourcePort = it },
                    label = { Text("Puerto Origen") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = destinationIp,
                    onValueChange = { destinationIp = it },
                    label = { Text("IP Destino") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = destinationPort,
                    onValueChange = { destinationPort = it },
                    label = { Text("Puerto Destino") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = protocol,
                    onValueChange = { protocol = it },
                    label = { Text("Protocolo") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val destPort = destinationPort.toIntOrNull() ?: 0
                val portValue = if (destPort == 0) null else Integer.valueOf(destPort)
                onUpdate(
                    FirewallRule(
                        rule.id,
                        name,
                        destinationIp,
                        sourceIp,
                        sourcePort.toIntOrNull() ?: 0,
                        destinationIp,
                        destPort,
                        protocol,
                        rule.isEnabled(),
                        rule.createdAt,
                        portValue
                    )
                )
            }) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
