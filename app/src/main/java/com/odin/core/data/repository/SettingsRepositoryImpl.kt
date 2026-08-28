package com.odin.core.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
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

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("odin_settings", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    private val _generalSettings = MutableStateFlow(loadGeneralSettings())
    private val _securitySettings = MutableStateFlow(loadSecuritySettings())
    private val _networkSettings = MutableStateFlow(loadNetworkSettings())
    
    private fun loadGeneralSettings(): GeneralSettings {
        val json = sharedPreferences.getString("general_settings", null)
        return if (json != null) {
            gson.fromJson(json, GeneralSettings::class.java)
        } else {
            GeneralSettings()
        }
    }
    
    private fun loadSecuritySettings(): SecuritySettings {
        val json = sharedPreferences.getString("security_settings", null)
        return if (json != null) {
            gson.fromJson(json, SecuritySettings::class.java)
        } else {
            SecuritySettings()
        }
    }
    
    private fun loadNetworkSettings(): NetworkSettings {
        val json = sharedPreferences.getString("network_settings", null)
        return if (json != null) {
            gson.fromJson(json, NetworkSettings::class.java)
        } else {
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
        sharedPreferences.edit().putString("security_settings", json).apply()
        _securitySettings.value = settings
    }
    
    override fun getNetworkSettings(): Flow<NetworkSettings> {
        return _networkSettings.asStateFlow()
    }
    
    override suspend fun saveNetworkSettings(settings: NetworkSettings) {
        val json = gson.toJson(settings)
        sharedPreferences.edit().putString("network_settings", json).apply()
        _networkSettings.value = settings
    }
}
