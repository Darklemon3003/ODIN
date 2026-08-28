package com.odin.core.ui.model

data class NetShieldUiState(
    val isConsentGranted: Boolean = false,
    val isVpnAuthorized: Boolean = false,
    val isNotificationPermissionGranted: Boolean = false,
    val isConnectivityRiskAccepted: Boolean = false,
    val isServiceRunning: Boolean = false,
    val isSafeValidationMode: Boolean = true,
    val status: NetShieldStatus = NetShieldStatus.REQUIRES_CONSENT,
    val statusMessage: String = "NetShield requiere consentimiento explicito.",
    val lastHeartbeat: Long? = null,
    val lastStartedAt: Long? = null,
    val lastStoppedAt: Long? = null,
    val errorMessage: String? = null,
    val canRequestVpnAuthorization: Boolean = false,
    val canStart: Boolean = false,
    val canStop: Boolean = false,
    val requiresConsent: Boolean = true,
    val requiresVpnAuthorization: Boolean = false,
    val requiresNotificationPermission: Boolean = false,
    val metrics: NetShieldMetrics = NetShieldMetrics()
)

data class NetShieldMetrics(
    val packetsAudited: Long? = null,
    val packetsAllowed: Long? = null,
    val packetsBlocked: Long? = null,
    val bytesIn: Long? = null,
    val bytesOut: Long? = null,
    val lastPacketTime: Long? = null,
    val message: String = "Sin metricas todavia"
)

enum class NetShieldStatus {
    INACTIVE,
    REQUIRES_CONSENT,
    REQUIRES_VPN_AUTHORIZATION,
    REQUIRES_NOTIFICATION_PERMISSION,
    READY_TO_START,
    STARTING,
    ACTIVE,
    STOPPING,
    ERROR,
    NOT_VALIDATED,
    UNSAFE_TO_START
}
