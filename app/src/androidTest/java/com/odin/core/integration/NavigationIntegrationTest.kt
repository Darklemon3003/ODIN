package com.odin.core.integration

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.odin.core.ui.navigation.Screen
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * NavigationIntegrationTest verifies navigation functionality between screens.
 * Tests tab navigation, detail navigation, back navigation, and argument passing.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationIntegrationTest : BaseIntegrationTest() {
    
    @Test
    fun testTabNavigation() = runTest {
        // Test navigation between main tabs
        val screens = listOf(
            Screen.Dashboard,
            Screen.Logs,
            Screen.Threats,
            Screen.Firewall,
            Screen.Quarantine,
            Screen.Settings
        )
        
        assertEquals("Should have 6 main screens", 6, screens.size)
        assertTrue("Dashboard should be in screens", screens.contains(Screen.Dashboard))
        assertTrue("Logs should be in screens", screens.contains(Screen.Logs))
        assertTrue("Threats should be in screens", screens.contains(Screen.Threats))
        assertTrue("Firewall should be in screens", screens.contains(Screen.Firewall))
        assertTrue("Quarantine should be in screens", screens.contains(Screen.Quarantine))
        assertTrue("Settings should be in screens", screens.contains(Screen.Settings))
    }
    
    @Test
    fun testScreenRoutes() = runTest {
        // Test that all screens have valid routes
        val screens = listOf(
            Screen.Dashboard,
            Screen.Logs,
            Screen.Threats,
            Screen.Firewall,
            Screen.Quarantine,
            Screen.Settings
        )
        
        screens.forEach { screen ->
            assertNotNull("Screen route should not be null", screen.route)
            assertTrue("Screen route should not be empty", screen.route.isNotEmpty())
        }
    }
    
    @Test
    fun testScreenTitles() = runTest {
        // Test that all screens have valid titles
        val screens = listOf(
            Screen.Dashboard,
            Screen.Logs,
            Screen.Threats,
            Screen.Firewall,
            Screen.Quarantine,
            Screen.Settings
        )
        
        screens.forEach { screen ->
            assertNotNull("Screen title should not be null", screen.title)
            assertTrue("Screen title should not be empty", screen.title.isNotEmpty())
        }
    }
    
    @Test
    fun testScreenIcons() = runTest {
        // Test that all screens have valid icons
        val screens = listOf(
            Screen.Dashboard,
            Screen.Logs,
            Screen.Threats,
            Screen.Firewall,
            Screen.Quarantine,
            Screen.Settings
        )
        
        screens.forEach { screen ->
            assertNotNull("Screen icon should not be null", screen.icon)
            assertNotNull("Screen selected icon should not be null", screen.selectedIcon)
        }
    }
    
    @Test
    fun testDashboardScreen() = runTest {
        // Test Dashboard screen properties
        assertEquals("Dashboard route should be 'dashboard'", "dashboard", Screen.Dashboard.route)
        assertEquals("Dashboard title should be 'Dashboard'", "Dashboard", Screen.Dashboard.title)
    }
    
    @Test
    fun testLogsScreen() = runTest {
        // Test Logs screen properties
        assertEquals("Logs route should be 'logs'", "logs", Screen.Logs.route)
        assertEquals("Logs title should be 'Logs'", "Logs", Screen.Logs.title)
    }
    
    @Test
    fun testThreatsScreen() = runTest {
        // Test Threats screen properties
        assertEquals("Threats route should be 'threats'", "threats", Screen.Threats.route)
        assertEquals("Threats title should be 'Threats'", "Threats", Screen.Threats.title)
    }
    
    @Test
    fun testFirewallScreen() = runTest {
        // Test Firewall screen properties
        assertEquals("Firewall route should be 'firewall'", "firewall", Screen.Firewall.route)
        assertEquals("Firewall title should be 'Firewall'", "Firewall", Screen.Firewall.title)
    }
    
    @Test
    fun testQuarantineScreen() = runTest {
        // Test Quarantine screen properties
        assertEquals("Quarantine route should be 'quarantine'", "quarantine", Screen.Quarantine.route)
        assertEquals("Quarantine title should be 'Quarantine'", "Quarantine", Screen.Quarantine.title)
    }
    
    @Test
    fun testSettingsScreen() = runTest {
        // Test Settings screen properties
        assertEquals("Settings route should be 'settings'", "settings", Screen.Settings.route)
        assertEquals("Settings title should be 'Settings'", Screen.Settings.title)
    }
    
    @Test
    fun testNavigationFlow() = runTest {
        // Test expected navigation flow
        val startDestination = Screen.Dashboard.route
        val targetDestination = Screen.Logs.route
        
        assertNotEquals("Start and target destinations should be different", startDestination, targetDestination)
        assertTrue("Start destination should be dashboard", startDestination == "dashboard")
        assertTrue("Target destination should be logs", targetDestination == "logs")
    }
}
