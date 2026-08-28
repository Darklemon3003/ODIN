package com.odin.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.odin.core.ui.navigation.BottomNavigationBar
import com.odin.core.ui.navigation.Screen
import com.odin.core.ui.screens.about.AboutScreen
import com.odin.core.ui.screens.dashboard.DashboardScreen
import com.odin.core.ui.screens.firewall.FirewallScreen
import com.odin.core.ui.screens.logs.LogsScreen
import com.odin.core.ui.screens.network.NetworkScreen
import com.odin.core.ui.screens.netshield.NetShieldControlScreen
import com.odin.core.ui.screens.permissions.PermissionsScreen
import com.odin.core.ui.screens.quarantine.QuarantineScreen
import com.odin.core.ui.screens.sentry.SentryControlScreen
import com.odin.core.ui.screens.services.ServicesStatusScreen
import com.odin.core.ui.screens.settings.SettingsScreen
import com.odin.core.ui.screens.threats.ThreatsScreen
import com.odin.core.ui.screens.tools.ToolsScreen
import com.odin.core.ui.theme.OdinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OdinTheme {
                OdinApp()
            }
        }
    }
}

@Composable
fun OdinApp() {
    val navController = rememberNavController()
    val notificationBadges = remember { mutableStateOf(mapOf<String, Int>()) }
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                notificationBadges = notificationBadges.value
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onOpenLogs = {
                        navController.navigateSingleTop(Screen.Logs.route)
                    },
                    onOpenSettings = {
                        navController.navigateSingleTop(Screen.Settings.route)
                    },
                    onOpenFirewall = {
                        navController.navigateSingleTop(Screen.NetShieldControl.route)
                    },
                    onOpenSentry = {
                        navController.navigateSingleTop(Screen.SentryControl.route)
                    },
                    onOpenPermissions = {
                        navController.navigateSingleTop(Screen.Permissions.route)
                    }
                )
            }
            composable(Screen.Tools.route) {
                ToolsScreen(
                    onOpenSentry = {
                        navController.navigateSingleTop(Screen.SentryControl.route)
                    },
                    onOpenPermissions = {
                        navController.navigateSingleTop(Screen.Permissions.route)
                    },
                    onOpenServices = {
                        navController.navigateSingleTop(Screen.ServicesStatus.route)
                    },
                    onOpenNetwork = {
                        navController.navigateSingleTop(Screen.NetShieldControl.route)
                    },
                    onOpenLogs = {
                        navController.navigateSingleTop(Screen.Logs.route)
                    },
                    onOpenSettings = {
                        navController.navigateSingleTop(Screen.Settings.route)
                    },
                    onOpenAbout = {
                        navController.navigateSingleTop(Screen.About.route)
                    },
                    onOpenFirewall = {
                        navController.navigateSingleTop(Screen.Firewall.route)
                    }
                )
            }
            composable(Screen.Network.route) {
                NetworkScreen(
                    onOpenNetShield = {
                        navController.navigateSingleTop(Screen.NetShieldControl.route)
                    },
                    onOpenPermissions = {
                        navController.navigateSingleTop(Screen.Permissions.route)
                    },
                    onOpenServices = {
                        navController.navigateSingleTop(Screen.ServicesStatus.route)
                    },
                    onOpenLogs = {
                        navController.navigateSingleTop(Screen.Logs.route)
                    },
                    onOpenFirewall = {
                        navController.navigateSingleTop(Screen.Firewall.route)
                    }
                )
            }
            composable(Screen.Logs.route) {
                LogsScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(Screen.Permissions.route) {
                PermissionsScreen(
                    onOpenNetShield = {
                        navController.navigateSingleTop(Screen.NetShieldControl.route)
                    },
                    onOpenSentry = {
                        navController.navigateSingleTop(Screen.SentryControl.route)
                    },
                    onOpenSettings = {
                        navController.navigateSingleTop(Screen.Settings.route)
                    },
                    onOpenServices = {
                        navController.navigateSingleTop(Screen.ServicesStatus.route)
                    }
                )
            }
            composable(Screen.ServicesStatus.route) {
                ServicesStatusScreen(
                    onOpenNetShield = {
                        navController.navigateSingleTop(Screen.NetShieldControl.route)
                    },
                    onOpenSentry = {
                        navController.navigateSingleTop(Screen.SentryControl.route)
                    }
                )
            }
            composable(Screen.SentryControl.route) {
                SentryControlScreen()
            }
            composable(Screen.NetShieldControl.route) {
                NetShieldControlScreen()
            }
            composable(Screen.About.route) {
                AboutScreen()
            }
            composable(Screen.Threats.route) {
                ThreatsScreen()
            }
            composable(Screen.Firewall.route) {
                FirewallScreen()
            }
            composable(Screen.Quarantine.route) {
                QuarantineScreen()
            }
        }
    }
}

private fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        launchSingleTop = true
    }
}
