package com.odin.core.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.odin.core.data.local.EnhancedSecureLocalStorage
import com.odin.core.domain.model.GeneralSettings
import com.odin.core.domain.model.NetworkSettings
import com.odin.core.domain.model.SecuritySettings
import com.odin.core.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SettingsRepositoryImpl persiste configuraciones de ODIN.
 *
 * Phase 10: security_settings y network_settings se almacenan en
 * EnhancedSecureLocalStorage (cifrado AES-256-GCM via AndroidKeyStore).
 * general_settings permanece en SharedPreferences plain (no es sensible).
 *
 * Migracion: al primer arranque, si existen valores legacy en plaintext,
 * se migran al almacenamiento cifrado y se eliminan los valores plaintext.
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("odin_settings", Context.MODE_PRIVATE)
    private val secureStorage: EnhancedSecureLocalStorage = EnhancedSecureLocalStorage.getInstance(context)
    private val gson = Gson()

    private val _generalSettings = MutableStateFlow(loadGeneralSettings())
    private val _securitySettings = MutableStateFlow(loadSecuritySettings())
    private val _networkSettings = MutableStateFlow(loadNetworkSettings())

    init {
        migrateLegacySettingsIfNeeded()
    }

    /**
     * Migra configuraciones legacy desde plaintext a almacenamiento cifrado.
     * Idempotente: seguro de ejecutar en cada arranque.
     */
    private fun migrateLegacySettingsIfNeeded() {
        try {
            val migrationKey = "key_settings_migration_completed"
            if (secureStorage.loadBoolean(migrationKey, false)) {
                return
            }

            // Migrar security_settings
            val legacySecurity = sharedPreferences.getString("security_settings", null)
            if (legacySecurity != null) {
                secureStorage.saveString("security_settings_encrypted", legacySecurity)
                // Verificar que el dato cifrado se guardo correctamente antes de eliminar plaintext
                val verifySecurity = secureStorage.loadString("security_settings_encrypted", "")
                if (verifySecurity == legacySecurity) {
                    sharedPreferences.edit().remove("security_settings").apply()
                } else {
                    Log.e("SettingsMigration", "Verificacion de migracion security_settings fallo")
                    return  // No marcar como completa, reintentar
                }
            }

            // Migrar network_settings (contiene proxyAddress que puede ser sensible)
            val legacyNetwork = sharedPreferences.getString("network_settings", null)
            if (legacyNetwork != null) {
                secureStorage.saveString("network_settings_encrypted", legacyNetwork)
                // Verificar que el dato cifrado se guardo correctamente antes de eliminar plaintext
                val verifyNetwork = secureStorage.loadString("network_settings_encrypted", "")
                if (verifyNetwork == legacyNetwork) {
                    sharedPreferences.edit().remove("network_settings").apply()
                } else {
                    Log.e("SettingsMigration", "Verificacion de migracion network_settings fallo")
                    return  // No marcar como completa, reintentar
                }
            }

            secureStorage.saveBoolean(migrationKey, true)
        } catch (e: Exception) {
            // La migracion fallida no debe bloquear la app. Se reintentara.
        }
    }

    private fun loadGeneralSettings(): GeneralSettings {
        // General settings no son sensibles - permanecen en plain SharedPreferences
        val json = sharedPreferences.getString("general_settings", null)
        return if (json != null) {
            gson.fromJson(json, GeneralSettings::class.java)
        } else {
            GeneralSettings()
        }
    }

    private fun loadSecuritySettings(): SecuritySettings {
        return try {
            val json = secureStorage.loadString("security_settings_encrypted", "")
            if (json.isNotEmpty()) {
                gson.fromJson(json, SecuritySettings::class.java)
            } else {
                SecuritySettings()
            }
        } catch (e: Exception) {
            // Si el almacenamiento cifrado falla, usar defaults seguros.
            // No recurrir a plaintext como fallback.
            SecuritySettings()
        }
    }

    private fun loadNetworkSettings(): NetworkSettings {
        return try {
            val json = secureStorage.loadString("network_settings_encrypted", "")
            if (json.isNotEmpty()) {
                gson.fromJson(json, NetworkSettings::class.java)
            } else {
                NetworkSettings()
            }
        } catch (e: Exception) {
            NetworkSettings()
        }
    }

    override fun getGeneralSettings(): Flow<GeneralSettings> {
        return _generalSettings.asStateFlow()
    }

    override suspend fun saveGeneralSettings(settings: GeneralSettings) {
        val json = gson.toJson(settings)
        sharedPreferences.edit().putString("general_settings", json).apply()
        _generalSettings.value = settings
    }

    override fun getSecuritySettings(): Flow<SecuritySettings> {
        return _securitySettings.asStateFlow()
    }

    override suspend fun saveSecuritySettings(settings: SecuritySettings) {
        val json = gson.toJson(settings)
        secureStorage.saveString("security_settings_encrypted", json)
        _securitySettings.value = settings
    }

    override fun getNetworkSettings(): Flow<NetworkSettings> {
        return _networkSettings.asStateFlow()
    }

    override suspend fun saveNetworkSettings(settings: NetworkSettings) {
        val json = gson.toJson(settings)
        secureStorage.saveString("network_settings_encrypted", json)
        _networkSettings.value = settings
    }
}
