package com.odin.core.domain.model

/**
 * ThreatStatistics representa estadísticas de amenazas.
 */
data class ThreatStatistics(
    val totalThreats: Int,
    val criticalCount: Int,
    val highCount: Int,
    val mediumCount: Int,
    val lowCount: Int,
    val threatsByType: Map<String, Int>,
    val topSourceIPs: List<IPThreatCount>,
    val topDestinationIPs: List<IPThreatCount>
)

/**
 * IPThreatCount representa el conteo de amenazas por IP.
 */
data class IPThreatCount(
    val ipAddress: String,
    val threatCount: Int,
    val lastSeen: Long
)

/**
 * QuarantinedApp representa una aplicación en cuarentena.
 */
data class QuarantinedApp(
    val packageName: String,
    val appName: String,
    val quarantineReason: String,
    val quarantineDate: Long,
    val riskScore: Int,
    val behaviorScore: Int,
    val permissions: List<String>,
    val isMonitored: Boolean
)

/**
 * BehaviorData representa datos de comportamiento de una aplicación.
 */
data class BehaviorData(
    val packageName: String,
    val networkRequests: Int,
    val suspiciousActivities: List<SuspiciousActivity>,
    val resourceUsage: ResourceUsage,
    val lastUpdated: Long
)

/**
 * SuspiciousActivity representa una actividad sospechosa.
 */
data class SuspiciousActivity(
    val type: String,
    val description: String,
    val timestamp: Long,
    val severity: ThreatLevel
)

/**
 * ResourceUsage representa el uso de recursos.
 */
data class ResourceUsage(
    val cpuUsage: Float,
    val memoryUsage: Float,
    val networkUsage: Long,
    val batteryUsage: Float
)

/**
 * GeneralSettings representa configuraciones generales.
 */
data class GeneralSettings(
    val theme: ThemeMode = ThemeMode.DARK,
    val language: String = "es",
    val notificationsEnabled: Boolean = true,
    val autoUpdateEnabled: Boolean = true
)

/**
 * SecuritySettings representa configuraciones de seguridad.
 */
data class SecuritySettings(
    val sensitivityLevel: SensitivityLevel = SensitivityLevel.MEDIUM,
    val autoResponseEnabled: Boolean = true,
    val realTimeScanning: Boolean = true,
    val heuristicAnalysis: Boolean = true
)

/**
 * NetworkSettings representa configuraciones de red.
 */
data class NetworkSettings(
    val vpnEnabled: Boolean = false,
    val torEnabled: Boolean = false,
    val dnsProvider: DNSProvider = DNSProvider.GOOGLE,
    val proxyEnabled: Boolean = false,
    val proxyAddress: String = "",
    val proxyPort: Int = 8080
)

/**
 * ThemeMode representa el modo de tema.
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    AUTO
}

/**
 * SensitivityLevel representa el nivel de sensibilidad.
 */
enum class SensitivityLevel {
    LOW,
    MEDIUM,
    HIGH,
    PARANOID
}

/**
 * DNSProvider representa el proveedor de DNS.
 */
enum class DNSProvider {
    GOOGLE,
    CLOUDFLARE,
    OPEN_DNS,
    CUSTOM
}
