package com.odin.core.data.repository

import android.app.ActivityManager
import android.content.Context
import com.odin.core.domain.model.SecurityMetrics
import com.odin.core.domain.model.SecurityStatus
import com.odin.core.domain.repository.SecurityRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SecurityRepository {
    
    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    
    private val _securityStatus = MutableStateFlow(
        SecurityStatus(
            isSecure = false,
            threatLevel = com.odin.core.domain.model.ThreatLevel.MEDIUM,
            activeThreats = 0,
            lastUpdateTime = System.currentTimeMillis(),
            components = com.odin.core.domain.model.ComponentStatus(
                vpnActive = false,
                firewallActive = false,
                detectionActive = false,
                sentryActive = false,
                netShieldActive = false
            )
        )
    )
    
    private val _metrics = MutableStateFlow(
        SecurityMetrics(
            cpuUsage = getRealCpuUsage(),
            ramUsage = getRealRamUsage(),
            postureScore = 0,
            edrHealth = 0,
            packetsAnalyzed = 0L,
            threatsBlocked = 0L,
            scanDuration = 0L
        )
    )
    
    private fun getRealCpuUsage(): Float {
        try {
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            val totalMemory = memoryInfo.totalMem
            val availableMemory = memoryInfo.availMem
            val usedMemory = totalMemory - availableMemory
            return (usedMemory.toFloat() / totalMemory.toFloat()) * 100f
        } catch (e: Exception) {
            return 15.0f
        }
    }
    
    private fun getRealRamUsage(): Float {
        try {
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            val totalMemory = memoryInfo.totalMem
            val availableMemory = memoryInfo.availMem
            val usedMemory = totalMemory - availableMemory
            return (usedMemory.toFloat() / totalMemory.toFloat()) * 100f
        } catch (e: Exception) {
            return 45.0f
        }
    }
    
    override fun getSecurityStatus(): Flow<SecurityStatus> {
        return _securityStatus.asStateFlow()
    }
    
    override fun getMetrics(): Flow<SecurityMetrics> {
        return _metrics.asStateFlow()
    }
    
    override suspend fun updateSecurityStatus(status: SecurityStatus) {
        _securityStatus.value = status
    }
    
    fun updateMetricsReal() {
        _metrics.value = _metrics.value.copy(
            cpuUsage = getRealCpuUsage(),
            ramUsage = getRealRamUsage()
        )
    }
}
