package com.odin.core.data.repository

import android.content.Context
import android.content.pm.PackageManager
import com.odin.core.domain.model.BehaviorData
import com.odin.core.domain.model.QuarantinedApp
import com.odin.core.domain.repository.QuarantineRepository
import com.odin.core.network.response.QuarantineManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuarantineRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : QuarantineRepository {
    
    private val quarantineManager = QuarantineManager(context)
    
    private val _quarantinedApps = MutableStateFlow<List<QuarantinedApp>>(emptyList())
    private val _behaviorData = MutableStateFlow<Map<String, BehaviorData>>(emptyMap())
    
    init {
        loadRealQuarantinedApps()
    }
    
    private fun loadRealQuarantinedApps() {
        try {
            val realApps = quarantineManager.getQuarantinedApps().map { record ->
                record.toDomainModel()
            }
            _quarantinedApps.value = realApps
        } catch (e: Exception) {
            // Handle error - keep empty list
        }
    }
    
    override fun getAllQuarantinedApps(): Flow<List<QuarantinedApp>> {
        return _quarantinedApps.asStateFlow()
    }
    
    override fun getBehaviorData(packageName: String): Flow<BehaviorData> {
        return flow {
            val data = _behaviorData.value[packageName]
                ?: throw UnsupportedOperationException(
                    "QuarantineManager does not expose BehaviorData for quarantined applications."
                )
            emit(data)
        }
    }
    
    override suspend fun releaseApp(packageName: String) {
        try {
            val released = quarantineManager.releaseQuarantine(packageName)
            if (!released) {
                throw IllegalStateException("Application is not currently quarantined: $packageName")
            }
            _quarantinedApps.value = _quarantinedApps.value.filterNot { it.packageName == packageName }
        } catch (e: Exception) {
            throw e
        }
    }
    
    override suspend fun deleteApp(packageName: String) {
        throw UnsupportedOperationException(
            "Deleting applications is not supported by QuarantineManager."
        )
    }
    
    override suspend fun keepApp(packageName: String) {
        throw UnsupportedOperationException(
            "Keeping or extending quarantine is not supported by QuarantineManager."
        )
    }
    
    override suspend fun quarantineApp(packageName: String, reason: String) {
        try {
            val quarantined = quarantineManager.quarantineApplication(packageName, reason)
            if (!quarantined) {
                throw IllegalStateException("Unable to quarantine application: $packageName")
            }
            loadRealQuarantinedApps()
        } catch (e: Exception) {
            throw e
        }
    }
    
    fun refreshQuarantinedApps() {
        loadRealQuarantinedApps()
    }

    private fun QuarantineManager.QuarantineRecord.toDomainModel(): QuarantinedApp {
        return QuarantinedApp(
            packageName = packageName,
            appName = appName ?: packageName,
            quarantineReason = reason,
            quarantineDate = quarantineTime,
            riskScore = severity.toRiskScore(),
            behaviorScore = violationCount,
            permissions = readRequestedPermissions(packageName),
            isMonitored = isActive
        )
    }

    private fun QuarantineManager.QuarantineSeverity.toRiskScore(): Int {
        return when (this) {
            QuarantineManager.QuarantineSeverity.CRITICAL -> 100
            QuarantineManager.QuarantineSeverity.HIGH -> 75
            QuarantineManager.QuarantineSeverity.MEDIUM -> 50
            QuarantineManager.QuarantineSeverity.LOW -> 25
        }
    }

    @Suppress("DEPRECATION")
    private fun readRequestedPermissions(packageName: String): List<String> {
        return try {
            context.packageManager
                .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                .requestedPermissions
                ?.toList()
                ?: emptyList()
        } catch (e: PackageManager.NameNotFoundException) {
            emptyList()
        }
    }
}
