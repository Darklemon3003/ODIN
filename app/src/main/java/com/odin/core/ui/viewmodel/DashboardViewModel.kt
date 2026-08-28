package com.odin.core.ui.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odin.core.domain.model.LogEvent
import com.odin.core.domain.repository.LogsRepository
import com.odin.core.netshield.NetShieldController
import com.odin.core.security.ConsentManager
import com.odin.core.sentry.SentryController
import com.odin.core.ui.model.DashboardAction
import com.odin.core.ui.model.DashboardBlockedFeature
import com.odin.core.ui.model.DashboardModuleState
import com.odin.core.ui.model.DashboardQuickAction
import com.odin.core.ui.model.DashboardUiState
import com.odin.core.ui.model.ModuleStatus
import com.odin.core.ui.model.NetShieldStatus
import com.odin.core.ui.model.OverallStatus
import com.odin.core.ui.model.SentryStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logsRepository: LogsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun refreshData() {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val errors = mutableListOf<String>()
            val consentManager = ConsentManager.getInstance(context)
            val notificationsConsent = consentManager.isNotificationsEnabled()
            val notificationPermissionGranted = hasNotificationPermission()
            val sentryState = SentryController(context).readState()
            val netShieldState = NetShieldController(context).readState()

            val recentEvents = loadRecentEvents(errors)
            val recentAlertsCount = recentEvents.count {
                it.severity == LogEvent.Severity.ERROR || it.severity == LogEvent.Severity.CRITICAL
            }

            val sentryStatus = sentryState.toDashboardModuleStatus()
            val netShieldStatus = netShieldState.toDashboardModuleStatus()
            val vpnStatus = netShieldState.toDashboardVpnStatus()

            val permissionsStatus = when {
                !notificationsConsent -> ModuleStatus.REQUIRES_CONSENT
                !notificationPermissionGranted -> ModuleStatus.REQUIRES_PERMISSION
                else -> ModuleStatus.ACTIVE
            }

            val overallStatus = calculateOverallStatus(
                sentryServiceRunning = sentryState.isServiceRunning,
                netShieldServiceRunning = netShieldState.isServiceRunning,
                notificationPermissionGranted = notificationPermissionGranted,
                recentAlertsCount = recentAlertsCount
            )

            _uiState.value = DashboardUiState(
                isLoading = false,
                overallStatus = overallStatus,
                postureScore = null,
                sentryStatus = sentryStatus,
                netShieldStatus = netShieldStatus,
                vpnStatus = vpnStatus,
                permissionsStatus = permissionsStatus,
                recentAlertsCount = recentAlertsCount,
                recentEvents = recentEvents,
                modules = buildModuleStates(
                    sentryStatus = sentryStatus,
                    sentryState = sentryState,
                    netShieldStatus = netShieldStatus,
                    netShieldState = netShieldState,
                    vpnStatus = vpnStatus,
                    permissionsStatus = permissionsStatus,
                    logsAvailable = recentEvents.isNotEmpty()
                ),
                blockedFeatures = buildBlockedFeatures(),
                availableActions = buildQuickActions(),
                lastUpdated = System.currentTimeMillis(),
                errorMessage = errors.takeIf { it.isNotEmpty() }?.joinToString(separator = " | ")
            )
        }
    }

    private suspend fun loadRecentEvents(errors: MutableList<String>): List<LogEvent> {
        return try {
            logsRepository.getRecentEvents(limit = 5).first()
        } catch (e: IllegalStateException) {
            errors.add("Logs no disponibles: ${e.message ?: "estado invalido"}")
            emptyList()
        } catch (e: RuntimeException) {
            errors.add("Logs no disponibles: ${e.message ?: "error local"}")
            emptyList()
        }
    }

    private fun hasNotificationPermission(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun calculateOverallStatus(
        sentryServiceRunning: Boolean,
        netShieldServiceRunning: Boolean,
        notificationPermissionGranted: Boolean,
        recentAlertsCount: Int
    ): OverallStatus {
        if (recentAlertsCount > 0) {
            return OverallStatus.NEEDS_ATTENTION
        }

        val configuredSignals = listOf(
            sentryServiceRunning,
            netShieldServiceRunning,
            notificationPermissionGranted
        ).count { it }

        return when (configuredSignals) {
            0 -> OverallStatus.NOT_VALIDATED
            1, 2 -> OverallStatus.NEEDS_ATTENTION
            else -> OverallStatus.PARTIAL_PROTECTION
        }
    }

    private fun buildModuleStates(
        sentryStatus: ModuleStatus,
        sentryState: com.odin.core.ui.model.SentryUiState,
        netShieldStatus: ModuleStatus,
        netShieldState: com.odin.core.ui.model.NetShieldUiState,
        vpnStatus: ModuleStatus,
        permissionsStatus: ModuleStatus,
        logsAvailable: Boolean
    ): List<DashboardModuleState> {
        return listOf(
            DashboardModuleState(
                title = "SentryCore",
                status = sentryStatus,
                statusLabel = sentryStatus.label(),
                description = sentryState.statusMessage,
                actionLabel = "Configurar",
                action = DashboardAction.CONFIGURE_PROTECTION
            ),
            DashboardModuleState(
                title = "NetShield",
                status = netShieldStatus,
                statusLabel = netShieldStatus.label(),
                description = netShieldState.statusMessage,
                actionLabel = "Configurar",
                action = DashboardAction.REVIEW_NETWORK
            ),
            DashboardModuleState(
                title = "VPN local",
                status = vpnStatus,
                statusLabel = vpnStatus.label(),
                description = "La autorizacion VPN se solicita solo desde la accion explicita del usuario.",
                actionLabel = "Configurar",
                action = DashboardAction.REVIEW_NETWORK
            ),
            DashboardModuleState(
                title = "Permisos",
                status = permissionsStatus,
                statusLabel = permissionsStatus.label(),
                description = when (permissionsStatus) {
                    ModuleStatus.REQUIRES_PERMISSION -> "El permiso de notificaciones no esta concedido."
                    ModuleStatus.REQUIRES_CONSENT -> "El consentimiento de notificaciones no esta registrado."
                    else -> "Permisos basicos revisados localmente; no se solicitan al abrir."
                },
                actionLabel = "Revisar",
                action = DashboardAction.REVIEW_PERMISSIONS
            ),
            DashboardModuleState(
                title = "Logs",
                status = if (logsAvailable) ModuleStatus.ACTIVE else ModuleStatus.INACTIVE,
                statusLabel = if (logsAvailable) "Con eventos" else "Sin datos todavia",
                description = if (logsAvailable) {
                    "Hay eventos locales recientes disponibles para revision."
                } else {
                    "No hay eventos recientes en el repositorio local disponible."
                },
                actionLabel = "Ver logs",
                action = DashboardAction.VIEW_LOGS
            )
        )
    }

    private fun buildBlockedFeatures(): List<DashboardBlockedFeature> {
        return listOf(
            DashboardBlockedFeature(
                title = "Firewall avanzado",
                reason = "Contrato de creacion, edicion y activacion de reglas pendiente."
            ),
            DashboardBlockedFeature(
                title = "Cuarentena",
                reason = "Acciones destructivas deshabilitadas hasta completar consentimiento y contrato."
            ),
            DashboardBlockedFeature(
                title = "Metricas de red",
                reason = "Sin fuente confiable de trafico real para este Dashboard."
            )
        )
    }

    private fun buildQuickActions(): List<DashboardQuickAction> {
        return listOf(
            DashboardQuickAction("Permisos", DashboardAction.REVIEW_PERMISSIONS),
            DashboardQuickAction("Logs", DashboardAction.VIEW_LOGS),
            DashboardQuickAction("Ajustes", DashboardAction.OPEN_SETTINGS),
            DashboardQuickAction("Red", DashboardAction.REVIEW_NETWORK)
        )
    }

    private fun ModuleStatus.label(): String {
        return when (this) {
            ModuleStatus.ACTIVE -> "Activo"
            ModuleStatus.INACTIVE -> "Sin datos todavia"
            ModuleStatus.REQUIRES_PERMISSION -> "Requiere permiso"
            ModuleStatus.REQUIRES_CONSENT -> "Requiere consentimiento"
            ModuleStatus.REQUIRES_CONFIGURATION -> "Requiere configuracion"
            ModuleStatus.NOT_AVAILABLE -> "No disponible"
            ModuleStatus.PLANNED -> "Planeado"
            ModuleStatus.ERROR -> "Error"
        }
    }

    private fun com.odin.core.ui.model.SentryUiState.toDashboardModuleStatus(): ModuleStatus {
        return when (status) {
            SentryStatus.ACTIVE -> ModuleStatus.ACTIVE
            SentryStatus.REQUIRES_CONSENT -> ModuleStatus.REQUIRES_CONSENT
            SentryStatus.REQUIRES_NOTIFICATION_PERMISSION -> ModuleStatus.REQUIRES_PERMISSION
            SentryStatus.READY_TO_START,
            SentryStatus.INACTIVE,
            SentryStatus.STOPPING,
            SentryStatus.STARTING -> ModuleStatus.REQUIRES_CONFIGURATION
            SentryStatus.NOT_VALIDATED -> ModuleStatus.NOT_AVAILABLE
            SentryStatus.ERROR -> ModuleStatus.ERROR
        }
    }

    private fun com.odin.core.ui.model.NetShieldUiState.toDashboardModuleStatus(): ModuleStatus {
        return when (status) {
            NetShieldStatus.ACTIVE -> ModuleStatus.ACTIVE
            NetShieldStatus.REQUIRES_CONSENT -> ModuleStatus.REQUIRES_CONSENT
            NetShieldStatus.REQUIRES_VPN_AUTHORIZATION,
            NetShieldStatus.REQUIRES_NOTIFICATION_PERMISSION -> ModuleStatus.REQUIRES_PERMISSION
            NetShieldStatus.READY_TO_START,
            NetShieldStatus.UNSAFE_TO_START,
            NetShieldStatus.INACTIVE,
            NetShieldStatus.STARTING,
            NetShieldStatus.STOPPING -> ModuleStatus.REQUIRES_CONFIGURATION
            NetShieldStatus.NOT_VALIDATED -> ModuleStatus.NOT_AVAILABLE
            NetShieldStatus.ERROR -> ModuleStatus.ERROR
        }
    }

    private fun com.odin.core.ui.model.NetShieldUiState.toDashboardVpnStatus(): ModuleStatus {
        return when {
            status == NetShieldStatus.ACTIVE -> ModuleStatus.ACTIVE
            !isConsentGranted -> ModuleStatus.REQUIRES_CONSENT
            !isVpnAuthorized -> ModuleStatus.REQUIRES_PERMISSION
            else -> ModuleStatus.REQUIRES_CONFIGURATION
        }
    }
}
