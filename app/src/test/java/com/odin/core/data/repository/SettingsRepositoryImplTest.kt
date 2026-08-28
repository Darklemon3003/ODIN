package com.odin.core.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.odin.core.domain.model.DNSProvider
import com.odin.core.domain.model.GeneralSettings
import com.odin.core.domain.model.NetworkSettings
import com.odin.core.domain.model.SecuritySettings
import com.odin.core.domain.model.SensitivityLevel
import com.odin.core.domain.model.ThemeMode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SettingsRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var repository: SettingsRepositoryImpl

    @BeforeEach
    fun setup() {
        context = mockk(relaxed = true)
        sharedPreferences = mockk()
        editor = mockk()
        
        every { context.getSharedPreferences("odin_settings", Context.MODE_PRIVATE) } returns sharedPreferences
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.apply() } returns Unit
        every { sharedPreferences.getString("general_settings", null) } returns null
        every { sharedPreferences.getString("security_settings", null) } returns null
        every { sharedPreferences.getString("network_settings", null) } returns null
        
        repository = SettingsRepositoryImpl(context)
    }

    @Test
    fun `getGeneralSettings returns default settings when none saved`() = runTest {
        val settings = repository.getGeneralSettings().first()
        
        assertNotNull(settings)
        assertEquals(ThemeMode.DARK, settings.theme)
        assertEquals("es", settings.language)
        assertEquals(true, settings.notificationsEnabled)
        assertEquals(true, settings.autoUpdateEnabled)
    }

    @Test
    fun `getSecuritySettings returns default settings when none saved`() = runTest {
        val settings = repository.getSecuritySettings().first()
        
        assertNotNull(settings)
        assertEquals(SensitivityLevel.MEDIUM, settings.sensitivityLevel)
        assertEquals(true, settings.autoResponseEnabled)
        assertEquals(true, settings.realTimeScanning)
        assertEquals(true, settings.heuristicAnalysis)
    }

    @Test
    fun `getNetworkSettings returns default settings when none saved`() = runTest {
        val settings = repository.getNetworkSettings().first()
        
        assertNotNull(settings)
        assertEquals(false, settings.vpnEnabled)
        assertEquals(false, settings.torEnabled)
        assertEquals(DNSProvider.GOOGLE, settings.dnsProvider)
        assertEquals(false, settings.proxyEnabled)
        assertEquals("", settings.proxyAddress)
        assertEquals(8080, settings.proxyPort)
    }

    @Test
    fun `saveGeneralSettings persists settings`() = runTest {
        val newSettings = GeneralSettings(
            theme = ThemeMode.LIGHT,
            language = "en",
            notificationsEnabled = false,
            autoUpdateEnabled = false
        )
        
        repository.saveGeneralSettings(newSettings)
        
        verify { editor.putString("general_settings", any()) }
        verify { editor.apply() }
        
        val settings = repository.getGeneralSettings().first()
        assertEquals(newSettings.theme, settings.theme)
        assertEquals(newSettings.language, settings.language)
    }

    @Test
    fun `saveSecuritySettings persists settings`() = runTest {
        val newSettings = SecuritySettings(
            sensitivityLevel = SensitivityLevel.HIGH,
            autoResponseEnabled = false,
            realTimeScanning = false,
            heuristicAnalysis = false
        )
        
        repository.saveSecuritySettings(newSettings)
        
        verify { editor.putString("security_settings", any()) }
        verify { editor.apply() }
        
        val settings = repository.getSecuritySettings().first()
        assertEquals(newSettings.sensitivityLevel, settings.sensitivityLevel)
    }

    @Test
    fun `saveNetworkSettings persists settings`() = runTest {
        val newSettings = NetworkSettings(
            vpnEnabled = true,
            torEnabled = true,
            dnsProvider = DNSProvider.CLOUDFLARE,
            proxyEnabled = true,
            proxyAddress = "127.0.0.1",
            proxyPort = 9050
        )
        
        repository.saveNetworkSettings(newSettings)
        
        verify { editor.putString("network_settings", any()) }
        verify { editor.apply() }
        
        val settings = repository.getNetworkSettings().first()
        assertEquals(newSettings.vpnEnabled, settings.vpnEnabled)
        assertEquals(newSettings.dnsProvider, settings.dnsProvider)
    }

    @Test
    fun `loadGeneralSettings deserializes from JSON`() = runTest {
        val gson = Gson()
        val settings = GeneralSettings(
            theme = ThemeMode.AUTO,
            language = "fr",
            notificationsEnabled = true,
            autoUpdateEnabled = false
        )
        val json = gson.toJson(settings)
        
        every { sharedPreferences.getString("general_settings", null) } returns json
        
        val newRepository = SettingsRepositoryImpl(context)
        val loadedSettings = newRepository.getGeneralSettings().first()
        
        assertEquals(settings.theme, loadedSettings.theme)
        assertEquals(settings.language, loadedSettings.language)
    }

    @Test
    fun `handles SharedPreferences errors gracefully`() = runTest {
        every { sharedPreferences.getString(any(), any()) } throws RuntimeException("SharedPreferences error")
        
        val errorRepository = SettingsRepositoryImpl(context)
        val settings = errorRepository.getGeneralSettings().first()
        
        assertNotNull(settings)
    }

    @Test
    fun `save handles editor errors gracefully`() = runTest {
        every { editor.putString(any(), any()) } throws RuntimeException("Editor error")
        
        val settings = GeneralSettings()
        
        assertDoesNotThrow {
            repository.saveGeneralSettings(settings)
        }
    }

    @Test
    fun `multiple saves persist correctly`() = runTest {
        val generalSettings = GeneralSettings(theme = ThemeMode.LIGHT)
        val securitySettings = SecuritySettings(sensitivityLevel = SensitivityLevel.HIGH)
        val networkSettings = NetworkSettings(vpnEnabled = true)
        
        repository.saveGeneralSettings(generalSettings)
        repository.saveSecuritySettings(securitySettings)
        repository.saveNetworkSettings(networkSettings)
        
        verify(exactly = 1) { editor.putString("general_settings", any()) }
        verify(exactly = 1) { editor.putString("security_settings", any()) }
        verify(exactly = 1) { editor.putString("network_settings", any()) }
    }
}
