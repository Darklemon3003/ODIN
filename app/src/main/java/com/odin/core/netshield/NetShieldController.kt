package com.odin.core.netshield

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Build
import androidx.core.content.ContextCompat
import com.odin.core.domain.model.LogEvent
import com.odin.core.persistence.SentryCore
import com.odin.core.security.ConsentManager
import com.odin.core.service.NetShieldVpnService
import com.odin.core.ui.model.NetShieldMetrics
import com.odin.core.ui.model.NetShieldStatus
import com.odin.core.ui.model.NetShieldUiState

class NetShieldController(context: Context) {
    private val appContext = context.applicationContext
    private val consentManager = ConsentManager.getInstance(appContext)

    fun readState(): NetShieldUiState {
        val snapshot = NetShieldRuntimeState.read(appContext)
        val consentGranted = consentManager.isNetShieldVpnEnabled()
        val notificationGranted = hasNotificationPermission()
        val freshRunning = snapshot.serviceRunning && snapshot.hasFreshHeartbeat()
        val requiresConsent = !consentGranted
        val requiresVpnAuthorization = consentGranted && !snapshot.vpnAuthorized
        val requiresNotification = consentGranted && !notificationGranted

        val status = when {
            requiresConsent -> NetShieldStatus.REQUIRES_CONSENT
            requiresNotification -> NetShieldStatus.REQUIRES_NOTIFICATION_PERMISSION
            requiresVpnAuthorization -> NetShieldStatus.REQUIRES_VPN_AUTHORIZATION
            snapshot.state == NetShieldRuntimeState.STATE_ERROR -> NetShieldStatus.ERROR
            snapshot.state == NetShieldRuntimeState.STATE_STARTING -> NetShieldStatus.STARTING
            snapshot.state == NetShieldRuntimeState.STATE_STOPPING -> NetShieldStatus.STOPPING
            freshRunning -> NetShieldStatus.ACTIVE
            snapshot.serviceRunning && !snapshot.hasFreshHeartbeat() -> NetShieldStatus.NOT_VALIDATED
            consentGranted && snapshot.vpnAuthorized && !snapshot.connectivityRiskAccepted -> NetShieldStatus.UNSAFE_TO_START
            consentGranted && snapshot.vpnAuthorized -> NetShieldStatus.READY_TO_START
            else -> NetShieldStatus.INACTIVE
        }

        return NetShieldUiState(
            isConsentGranted = consentGranted,
            isVpnAuthorized = snapshot.vpnAuthorized,
            isNotificationPermissionGranted = notificationGranted,
            isConnectivityRiskAccepted = snapshot.connectivityRiskAccepted,
            isServiceRunning = freshRunning,
            isSafeValidationMode = true,
            status = status,
            statusMessage = statusMessage(status),
            lastHeartbeat = snapshot.lastHeartbeat.takeIf { it > 0L },
            lastStartedAt = snapshot.lastStarted.takeIf { it > 0L },
            lastStoppedAt = snapshot.lastStopped.takeIf { it > 0L },
            errorMessage = snapshot.lastError.takeIf {
                status == NetShieldStatus.ERROR || status == NetShieldStatus.NOT_VALIDATED
            },
            canRequestVpnAuthorization = consentGranted,
            canStart = consentGranted &&
                snapshot.vpnAuthorized &&
                notificationGranted &&
                snapshot.connectivityRiskAccepted &&
                !freshRunning &&
                status != NetShieldStatus.STARTING,
            canStop = freshRunning || status == NetShieldStatus.STARTING,
            requiresConsent = requiresConsent,
            requiresVpnAuthorization = requiresVpnAuthorization,
            requiresNotificationPermission = requiresNotification,
            metrics = metricsFrom(snapshot)
        )
    }

    fun grantConsent() {
        consentManager.setNetShieldVpnEnabled(true)
        logEvent("NETSHIELD_CONSENT", "Consentimiento NetShield concedido por el usuario.")
    }

    fun revokeConsent() {
        stopNetShield()
        consentManager.setNetShieldVpnEnabled(false)
        NetShieldRuntimeState.markVpnAuthorized(appContext, false)
        NetShieldRuntimeState.markConnectivityRiskAccepted(appContext, false)
        NetShieldRuntimeState.markStopped(appContext, "Consentimiento NetShield revocado.")
        logEvent("NETSHIELD_CONSENT", "Consentimiento NetShield revocado por el usuario.")
    }

    fun createVpnAuthorizationIntent(): Intent? {
        val state = readState()
        if (!state.isConsentGranted) {
            NetShieldRuntimeState.markError(appContext, "NetShield requiere consentimiento antes de autorizar VPN.")
            logEvent("NETSHIELD_VPN_AUTH", "Autorizacion VPN bloqueada por falta de consentimiento.")
            return null
        }

        val intent = VpnService.prepare(appContext)
        if (intent == null) {
            NetShieldRuntimeState.markVpnAuthorized(appContext, true)
            logEvent("NETSHIELD_VPN_AUTH", "Android ya tenia autorizacion VPN local disponible.")
        }
        return intent
    }

    fun onVpnAuthorizationResult(granted: Boolean) {
        NetShieldRuntimeState.markVpnAuthorized(appContext, granted)
        logEvent(
            "NETSHIELD_VPN_AUTH",
            if (granted) "Autorizacion VPN concedida por el usuario." else "Autorizacion VPN cancelada o denegada."
        )
    }

    fun setNotificationConsent(granted: Boolean) {
        consentManager.setNotificationsEnabled(granted)
        logEvent(
            "NETSHIELD_NOTIFICATION_PERMISSION",
            if (granted) "Permiso de notificaciones disponible para NetShield." else "Permiso de notificaciones no disponible para NetShield."
        )
    }

    fun acceptConnectivityRisk() {
        NetShieldRuntimeState.markConnectivityRiskAccepted(appContext, true)
        logEvent("NETSHIELD_CONNECTIVITY_RISK", "Advertencia de conectividad aceptada por el usuario.")
    }

    fun startNetShield(): Result<Unit> {
        val state = readState()
        if (!state.isConsentGranted) {
            logEvent("NETSHIELD_START_BLOCKED", "Inicio bloqueado por falta de consentimiento.")
            return Result.failure(IllegalStateException("NetShield requiere consentimiento explicito."))
        }
        if (!state.isNotificationPermissionGranted) {
            NetShieldRuntimeState.markError(appContext, "Permiso de notificaciones requerido.")
            logEvent("NETSHIELD_START_BLOCKED", "Inicio bloqueado por falta de notificacion visible.")
            return Result.failure(IllegalStateException("NetShield requiere notificacion visible."))
        }
        if (!state.isVpnAuthorized) {
            logEvent("NETSHIELD_START_BLOCKED", "Inicio bloqueado por falta de autorizacion VPN.")
            return Result.failure(IllegalStateException("NetShield requiere autorizacion VPN oficial."))
        }
        if (!state.isConnectivityRiskAccepted) {
            logEvent("NETSHIELD_START_BLOCKED", "Inicio bloqueado por advertencia de conectividad pendiente.")
            return Result.failure(IllegalStateException("NetShield requiere aceptar la advertencia de conectividad."))
        }
        if (state.isServiceRunning) {
            return Result.success(Unit)
        }

        return try {
            NetShieldRuntimeState.markStarting(appContext, null)
            val intent = Intent(appContext, NetShieldVpnService::class.java)
                .setAction(NetShieldVpnService.ACTION_START_VPN)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(appContext, intent)
            } else {
                appContext.startService(intent)
            }
            logEvent("NETSHIELD_SERVICE", "NetShield iniciado por accion explicita del usuario.")
            Result.success(Unit)
        } catch (error: RuntimeException) {
            NetShieldRuntimeState.markError(appContext, error.message ?: "Error iniciando NetShield.")
            Result.failure(error)
        }
    }

    fun stopNetShield(): Result<Unit> {
        return try {
            NetShieldRuntimeState.markStopping(appContext)
            val stopIntent = Intent(appContext, NetShieldVpnService::class.java)
                .setAction(NetShieldVpnService.ACTION_STOP_VPN)
            appContext.startService(stopIntent)
            appContext.stopService(Intent(appContext, NetShieldVpnService::class.java))
            logEvent("NETSHIELD_SERVICE", "NetShield detenido por accion explicita del usuario.")
            Result.success(Unit)
        } catch (error: RuntimeException) {
            NetShieldRuntimeState.markError(appContext, error.message ?: "Error deteniendo NetShield.")
            Result.failure(error)
        }
    }

    fun hasNotificationPermission(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun metricsFrom(snapshot: NetShieldRuntimeState.Snapshot): NetShieldMetrics {
        if (snapshot.packetsAudited <= 0L) {
            return NetShieldMetrics(message = "Sin metricas todavia")
        }
        return NetShieldMetrics(
            packetsAudited = snapshot.packetsAudited,
            packetsAllowed = snapshot.packetsAllowed,
            packetsBlocked = snapshot.packetsBlocked,
            bytesIn = snapshot.bytesIn,
            bytesOut = snapshot.bytesOut.takeIf { it > 0L },
            lastPacketTime = snapshot.lastPacketTime.takeIf { it > 0L },
            message = "Metricas locales observadas por el servicio"
        )
    }

    private fun statusMessage(status: NetShieldStatus): String {
        return when (status) {
            NetShieldStatus.INACTIVE -> "NetShield detenido."
            NetShieldStatus.REQUIRES_CONSENT -> "Requiere consentimiento antes de configurar VPN local."
            NetShieldStatus.REQUIRES_VPN_AUTHORIZATION -> "Requiere autorizacion oficial de Android VPN."
            NetShieldStatus.REQUIRES_NOTIFICATION_PERMISSION -> "Requiere notificacion visible para ejecutar foreground service."
            NetShieldStatus.READY_TO_START -> "Listo para iniciar en modo de validacion segura."
            NetShieldStatus.STARTING -> "Iniciando VPN local visible."
            NetShieldStatus.ACTIVE -> "VPN local visible en ejecucion. Motor de trafico en validacion segura."
            NetShieldStatus.STOPPING -> "Deteniendo VPN local."
            NetShieldStatus.ERROR -> "NetShield requiere revision por un error local."
            NetShieldStatus.NOT_VALIDATED -> "Estado runtime no validado; heartbeat desactualizado."
            NetShieldStatus.UNSAFE_TO_START -> "Requiere aceptar advertencia de conectividad antes de iniciar."
        }
    }

    private fun logEvent(source: String, message: String) {
        try {
            SentryCore.getInstance(appContext).appendEvent(
                LogEvent(
                    System.currentTimeMillis(),
                    source,
                    message,
                    LogEvent.Severity.INFO
                )
            )
        } catch (_: Exception) {
            // Logging must never block NetShield controls.
        }
    }
}
