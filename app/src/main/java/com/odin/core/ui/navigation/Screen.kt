package com.odin.core.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Dashboard : Screen(
        route = "dashboard",
        title = "Inicio",
        icon = Icons.Default.Dashboard,
        selectedIcon = Icons.Default.Dashboard
    )

    object Tools : Screen(
        route = "tools",
        title = "Herramientas",
        icon = Icons.Default.Build,
        selectedIcon = Icons.Default.Build
    )

    object Network : Screen(
        route = "network",
        title = "Red",
        icon = Icons.Default.VpnKey,
        selectedIcon = Icons.Default.VpnKey
    )

    object Logs : Screen(
        route = "logs",
        title = "Logs",
        icon = Icons.Default.Description,
        selectedIcon = Icons.Default.Description
    )

    object Settings : Screen(
        route = "settings",
        title = "Ajustes",
        icon = Icons.Default.Settings,
        selectedIcon = Icons.Default.Settings
    )

    object Permissions : Screen(
        route = "permissions",
        title = "Permisos",
        icon = Icons.Default.Notifications,
        selectedIcon = Icons.Default.Notifications
    )

    object ServicesStatus : Screen(
        route = "services_status",
        title = "Servicios",
        icon = Icons.Default.Security,
        selectedIcon = Icons.Default.Security
    )

    object SentryControl : Screen(
        route = "sentry_control",
        title = "Sentry",
        icon = Icons.Default.Shield,
        selectedIcon = Icons.Default.Shield
    )

    object NetShieldControl : Screen(
        route = "netshield_control",
        title = "NetShield",
        icon = Icons.Default.VpnKey,
        selectedIcon = Icons.Default.VpnKey
    )

    object About : Screen(
        route = "about",
        title = "Acerca",
        icon = Icons.Default.Info,
        selectedIcon = Icons.Default.Info
    )

    object Threats : Screen(
        route = "threats",
        title = "Amenazas",
        icon = Icons.Default.Shield,
        selectedIcon = Icons.Default.Shield
    )

    object Firewall : Screen(
        route = "firewall",
        title = "Firewall",
        icon = Icons.Default.Security,
        selectedIcon = Icons.Default.Security
    )

    object Quarantine : Screen(
        route = "quarantine",
        title = "Cuarentena",
        icon = Icons.Default.Block,
        selectedIcon = Icons.Default.Block
    )

    object FileAnalysis : Screen(
        route = "file_analysis",
        title = "Analisis de archivos",
        icon = Icons.Default.Description,
        selectedIcon = Icons.Default.Description
    )
}
