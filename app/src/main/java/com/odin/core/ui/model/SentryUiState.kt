package com.odin.core.ui.model

data class SentryUiState(
    val isConsentGranted: Boolean = false,
    val isBootConsentGranted: Boolean = false,
    val isNotificationPermissionGranted: Boolean = false,
    val isServiceRunning: Boolean = false,
    val isCoreHealthy: Boolean = false,
    val status: SentryStatus = SentryStatus.REQUIRES_CONSENT,
    val statusMessage: String = "Sentry local requiere consentimiento explicito.",
    val lastHeartbeat: Long? = null,
    val errorMessage: String? = null,
    val canStart: Boolean = false,
    val canStop: Boolean = false,
    val requiresConsent: Boolean = true,
    val requiresNotificationPermission: Boolean = false,
    val requiresBootConsent: Boolean = false
)

enum class SentryStatus {
    INACTIVE,
    REQUIRES_CONSENT,
    REQUIRES_NOTIFICATION_PERMISSION,
    READY_TO_START,
    STARTING,
    ACTIVE,
    STOPPING,
    ERROR,
    NOT_VALIDATED
}
