package com.odin.core.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

/**
 * OdinNavigation configura la navegación de la aplicación.
 */
@Composable
fun OdinNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
    screens: Map<String, @Composable (PaddingValues) -> Unit>
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        screens.forEach { (route, screen) ->
            composable(route) {
                screen(paddingValues)
            }
        }
    }
}
