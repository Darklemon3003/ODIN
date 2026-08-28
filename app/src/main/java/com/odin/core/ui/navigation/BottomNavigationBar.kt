package com.odin.core.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(
    navController: NavController,
    notificationBadges: Map<String, Int> = emptyMap(),
    modifier: Modifier = Modifier
) {
    val screens = listOf(
        Screen.Dashboard,
        Screen.Tools,
        Screen.Network,
        Screen.Logs,
        Screen.Settings
    )
    
    NavigationBar(modifier = modifier) {
        screens.forEach { screen ->
            val selected = navController.currentDestination?.route == screen.route
            val badgeCount = notificationBadges[screen.route] ?: 0
            
            NavigationBarItem(
                icon = {
                    BadgedBox(
                        badge = {
                            if (badgeCount > 0) {
                                Badge {
                                    Text(
                                        text = if (badgeCount > 99) "99+" else badgeCount.toString()
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (selected) screen.selectedIcon else screen.icon,
                            contentDescription = screen.title
                        )
                    }
                },
                label = { Text(screen.title) },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
