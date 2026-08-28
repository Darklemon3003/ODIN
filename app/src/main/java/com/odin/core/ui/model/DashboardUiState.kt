package com.odin.core.ui.model

import com.odin.core.domain.model.LogEvent

data class DashboardUiState(
    val isLoading: Boolean = true,
    val overallStatus: OverallStatus = OverallStatus.NOT_VALIDATED,
    val postureScore: Int? = null,
    val sentryStatus: ModuleStatus = ModuleStatus.NOT_AVAILABLE,
    val netShieldStatus: ModuleStatus = ModuleStatus.NOT_AVAILABLE,
    val vpnStatus: ModuleStatus = ModuleStatus.NOT_AVAILABLE,
    val permissionsStatus: ModuleStatus = ModuleStatus.NOT_AVAILABLE,
    val recentAlertsCount: Int = 0,
    val recentEvents: List<LogEvent> = emptyList(),
    val modules: List<DashboardModuleState> = emptyList(),
    val blockedFeatures: List<DashboardBlockedFeature> = emptyList(),
    val availableActions: List<DashboardQuickAction> = emptyList(),
    val lastUpdated: Long? = null,
    val errorMessage: String? = null
)

enum class OverallStatus {
    INACTIVE,
    NOT_VALIDATED,
    NEEDS_ATTENTION,
    PARTIAL_PROTECTION,
    PROTECTED
}

enum class ModuleStatus {
    ACTIVE,
    INACTIVE,
    REQUIRES_PERMISSION,
    REQUIRES_CONSENT,
    REQUIRES_CONFIGURATION,
    NOT_AVAILABLE,
    PLANNED,
    ERROR
}

enum class DashboardAction {
    REVIEW_PERMISSIONS,
    VIEW_LOGS,
    OPEN_SETTINGS,
    REVIEW_NETWORK,
    CONFIGURE_PROTECTION
}

data class DashboardModuleState(
    val title: String,
    val status: ModuleStatus,
    val statusLabel: String,
    val description: String,
    val actionLabel: String? = null,
    val action: DashboardAction? = null,
    val actionEnabled: Boolean = true
)

data class DashboardBlockedFeature(
    val title: String,
    val reason: String,
    val statusLabel: String = "No disponible"
)

data class DashboardQuickAction(
    val label: String,
    val action: DashboardAction,
    val enabled: Boolean = true
)
