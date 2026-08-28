package com.odin.core.domain.model

/**
 * SecurityStatus representa el estado actual de seguridad del sistema.
 */
data class SecurityStatus(
    val isSecure: Boolean,
    val threatLevel: ThreatLevel,
    val activeThreats: Int,
    val lastUpdateTime: Long,
    val components: ComponentStatus
)

/**
 * ComponentStatus representa el estado de los componentes de seguridad.
 */
data class ComponentStatus(
    val vpnActive: Boolean,
    val firewallActive: Boolean,
    val detectionActive: Boolean,
    val sentryActive: Boolean,
    val netShieldActive: Boolean
)

/**
 * SecurityMetrics representa las métricas de seguridad del sistema.
 */
data class SecurityMetrics(
    val cpuUsage: Float,
    val ramUsage: Float,
    val postureScore: Int,
    val edrHealth: Int,
    val packetsAnalyzed: Long,
    val threatsBlocked: Long,
    val scanDuration: Long
)

/**
 * NetworkActivity representa la actividad de red en un momento dado.
 */
data class NetworkActivity(
    val timestamp: Long,
    val bytesIn: Long,
    val bytesOut: Long,
    val packetCount: Int,
    val connectionCount: Int
)

/**
 * ThreatLevel representa el nivel de amenaza.
 */
enum class ThreatLevel(val displayName: String, val severity: Int) {
    SECURE("Seguro", 0),
    LOW("Bajo", 1),
    MEDIUM("Medio", 2),
    HIGH("Alto", 3),
    CRITICAL("Crítico", 4);

    fun isAtLeast(level: ThreatLevel): Boolean {
        return this.severity >= level.severity
    }
}
