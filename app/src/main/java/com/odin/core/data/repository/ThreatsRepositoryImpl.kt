package com.odin.core.data.repository

import com.odin.core.domain.model.ThreatStatistics
import com.odin.core.domain.repository.ThreatsRepository
import com.odin.core.network.detection.DetectionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThreatsRepositoryImpl @Inject constructor() : ThreatsRepository {
    
    private val _threats = MutableStateFlow<List<DetectionResult>>(emptyList())
    private val _statistics = MutableStateFlow(
        ThreatStatistics(
            totalThreats = 0,
            criticalCount = 0,
            highCount = 0,
            mediumCount = 0,
            lowCount = 0,
            threatsByType = emptyMap(),
            topSourceIPs = emptyList(),
            topDestinationIPs = emptyList()
        )
    )
    
    init {
        loadRealThreats()
    }
    
    private fun loadRealThreats() {
        _threats.value = emptyList()
        updateStatistics()
    }
    
    override fun getAllThreats(): Flow<List<DetectionResult>> {
        return _threats.asStateFlow()
    }
    
    override fun getThreatsByLevel(level: com.odin.core.network.detection.ThreatLevel): Flow<List<DetectionResult>> {
        return flow {
            emit(_threats.value.filter { it.threatLevel == level })
        }
    }
    
    override fun getThreatStatistics(): Flow<ThreatStatistics> {
        return _statistics.asStateFlow()
    }
    
    override suspend fun dismissThreat(forensicsId: Long) {
        _threats.value = _threats.value.filterNot { it.forensicsId == forensicsId }
        updateStatistics()
    }
    
    override suspend fun getThreatById(forensicsId: Long): DetectionResult? {
        return _threats.value.find { it.forensicsId == forensicsId }
    }
    
    fun addThreat(threat: DetectionResult) {
        _threats.value = _threats.value + threat
        updateStatistics()
    }
    
    fun refreshThreats() {
        loadRealThreats()
    }
    
    private fun updateStatistics() {
        val threats = _threats.value
        _statistics.value = ThreatStatistics(
            totalThreats = threats.size,
            criticalCount = threats.count { it.threatLevel == com.odin.core.network.detection.ThreatLevel.CRITICAL },
            highCount = threats.count { it.threatLevel == com.odin.core.network.detection.ThreatLevel.HIGH },
            mediumCount = threats.count { it.threatLevel == com.odin.core.network.detection.ThreatLevel.MEDIUM },
            lowCount = threats.count { it.threatLevel == com.odin.core.network.detection.ThreatLevel.LOW },
            threatsByType = threats.groupBy { it.threatType }.mapValues { it.value.size },
            topSourceIPs = threats.groupBy { it.sourceIp }
                .map { (ip, list) -> com.odin.core.domain.model.IPThreatCount(ip, list.size, list.maxOfOrNull { it.detectionTimestamp } ?: 0) }
                .sortedByDescending { it.threatCount }
                .take(10),
            topDestinationIPs = threats.groupBy { it.destinationIp }
                .map { (ip, list) -> com.odin.core.domain.model.IPThreatCount(ip, list.size, list.maxOfOrNull { it.detectionTimestamp } ?: 0) }
                .sortedByDescending { it.threatCount }
                .take(10)
        )
    }
}
