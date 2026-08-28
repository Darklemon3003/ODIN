package com.odin.core.sentry

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.odin.core.domain.model.LogEvent
import com.odin.core.persistence.SentryCore
import com.odin.core.security.ConsentManager
import com.odin.core.service.SentryService
import com.odin.core.ui.model.SentryStatus
import com.odin.core.ui.model.SentryUiState

class SentryController(context: Context) {
    private val appContext = context.applicationContext
    private val consentManager = ConsentManager.getInstance(appContext)

    fun readState(): SentryUiState {
        val snapshot = SentryRuntimeState.read(appContext)
        val consentGranted = consentManager.isSentryEnabled
        val bootConsentGranted = consentManager.isBootSentryEnabled
        val notificationGranted = hasNotificationPermission()
        val freshRunning = snapshot.serviceRunning && snapshot.hasFreshHeartbeat()
        val requiresConsent = !consentGranted
        val requiresNotification = consentGranted && !notificationGranted

        val status = when {
            requiresConsent -> SentryStatus.REQUIRES_CONSENT
            requiresNotification -> SentryStatus.REQUIRES_NOTIFICATION_PERMISSION
            snapshot.state == SentryRuntimeState.STATE_ERROR -> SentryStatus.ERROR
            snapshot.state == SentryRuntimeState.STATE_STARTING -> SentryStatus.STARTING
            snapshot.state == SentryRuntimeState.STATE_STOPPING -> SentryStatus.STOPPING
            freshRunning -> SentryStatus.ACTIVE
            snapshot.serviceRunning && !snapshot.hasFreshHeartbeat() -> SentryStatus.NOT_VALIDATED
            consentGranted -> SentryStatus.READY_TO_START
            else -> SentryStatus.INACTIVE
        }

        return SentryUiState(
            isConsentGranted = consentGranted,
            isBootConsentGranted = bootConsentGranted,
            isNotificationPermissionGranted = notificationGranted,
            isServiceRunning = freshRunning,
            isCoreHealthy = freshRunning && snapshot.coreHealthy,
            status = status,
            statusMessage = statusMessage(status, bootConsentGranted),
            lastHeartbeat = snapshot.lastHeartbeat.takeIf { it > 0L },
            errorMessage = snapshot.lastError.takeIf { status == SentryStatus.ERROR || status == SentryStatus.NOT_VALIDATED },
            canStart = consentGranted && notificationGranted && !freshRunning && status != SentryStatus.STARTING,
            canStop = freshRunning || status == SentryStatus.STARTING,
            requiresConsent = requiresConsent,
            requiresNotificationPermission = requiresNotification,
            requiresBootConsent = consentGranted && !bootConsentGranted
        )
    }

    fun grantConsent() {
        consentManager.setSentryEnabled(true)
        logEvent("SENTRY_CONSENT", "Consentimiento principal de Sentry concedido.")
    }

    fun revokeConsent() {
        stopSentry()
        consentManager.setSentryEnabled(false)
        consentManager.setBootSentryEnabled(false)
        SentryRuntimeState.markStopped(appContext, "Consentimiento revocado por el usuario.")
        logEvent("SENTRY_CONSENT", "Consentimiento principal de Sentry revocado.")
    }

    fun setBootConsent(enabled: Boolean) {
        val normalized = enabled && consentManager.isSentryEnabled
        consentManager.setBootSentryEnabled(normalized)
        logEvent(
            "SENTRY_BOOT_CONSENT",
            if (normalized) "Inicio tras reinicio autorizado por el usuario." else "Inicio tras reinicio desactivado."
        )
    }

    fun setNotificationConsent(granted: Boolean) {
        consentManager.setNotificationsEnabled(granted)
        logEvent(
            "SENTRY_NOTIFICATION_PERMISSION",
            if (granted) "Permiso de notificaciones disponible para Sentry." else "Permiso de notificaciones no disponible para Sentry."
        )
    }

    fun startSentry(): Result<Unit> {
        val state = readState()
        if (!state.isConsentGranted) {
            logEvent("SENTRY_START_BLOCKED", "Inicio bloqueado por falta de consentimiento.")
            return Result.failure(IllegalStateException("Sentry requiere consentimiento explicito."))
        }
        if (!state.isNotificationPermissionGranted) {
            SentryRuntimeState.markError(appContext, "Permiso de notificaciones requerido.")
            logEvent("SENTRY_START_BLOCKED", "Inicio bloqueado por falta de permiso de notificaciones.")
            return Result.failure(IllegalStateException("Sentry requiere notificacion visible."))
        }
        if (state.isServiceRunning) {
            return Result.success(Unit)
        }

        return try {
            SentryRuntimeState.markStarting(appContext, null)
            val intent = Intent(appContext, SentryService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(appContext, intent)
            } else {
                appContext.startService(intent)
            }
            logEvent("SENTRY_SERVICE", "Sentry iniciado por accion explicita del usuario.")
            Result.success(Unit)
        } catch (error: RuntimeException) {
            SentryRuntimeState.markError(appContext, error.message ?: "Error iniciando Sentry.")
            Result.failure(error)
        }
    }

    fun stopSentry(): Result<Unit> {
        return try {
            SentryRuntimeState.markStopping(appContext)
            appContext.stopService(Intent(appContext, SentryService::class.java))
            logEvent("SENTRY_SERVICE", "Sentry detenido por accion explicita del usuario.")
            Result.success(Unit)
        } catch (error: RuntimeException) {
            SentryRuntimeState.markError(appContext, error.message ?: "Error deteniendo Sentry.")
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

    private fun statusMessage(status: SentryStatus, bootConsentGranted: Boolean): String {
        return when (status) {
            SentryStatus.INACTIVE -> "Sentry local detenido."
            SentryStatus.REQUIRES_CONSENT -> "Requiere consentimiento antes de activar el servicio visible."
            SentryStatus.REQUIRES_NOTIFICATION_PERMISSION -> "Notificacion requerida para ejecutar el foreground service."
            SentryStatus.READY_TO_START -> if (bootConsentGranted) {
                "Listo para iniciar. Inicio tras reinicio autorizado."
            } else {
                "Listo para iniciar. Inicio tras reinicio desactivado."
            }
            SentryStatus.STARTING -> "Iniciando servicio visible."
            SentryStatus.ACTIVE -> "Proteccion local visible en ejecucion."
            SentryStatus.STOPPING -> "Deteniendo servicio visible."
            SentryStatus.ERROR -> "Sentry requiere revision por un error local."
            SentryStatus.NOT_VALIDATED -> "Estado runtime no validado; heartbeat desactualizado."
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
            // Logging must never block consent or service controls.
        }
    }
}
