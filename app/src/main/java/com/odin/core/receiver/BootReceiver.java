package com.odin.core.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.odin.core.domain.model.LogEvent;
import com.odin.core.persistence.SentryCore;
import com.odin.core.security.ConsentManager;
import com.odin.core.sentry.SentryRuntimeState;
import com.odin.core.service.SentryService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null || !Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            return;
        }

        ConsentManager consentManager = ConsentManager.getInstance(context);
        if (!consentManager.isSentryEnabled()) {
            recordBootDecision(context, "BootReceiver no inicio Sentry: falta consentimiento principal.");
            SentryRuntimeState.markStopped(context, "Boot sin consentimiento principal.");
            return;
        }

        if (!consentManager.isBootSentryEnabled()) {
            recordBootDecision(context, "BootReceiver no inicio Sentry: inicio tras reinicio desactivado.");
            SentryRuntimeState.markStopped(context, "Boot consent desactivado.");
            return;
        }

        if (!hasNotificationPermission(context)) {
            recordBootDecision(context, "BootReceiver no inicio Sentry: falta permiso de notificaciones.");
            SentryRuntimeState.markError(context, "Boot bloqueado por permiso de notificaciones.");
            return;
        }

        startSentryService(context);
    }

    private void startSentryService(Context context) {
        Intent serviceIntent = new Intent(context, SentryService.class);
        SentryRuntimeState.markStarting(context, "Inicio tras reinicio autorizado.");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
        recordBootDecision(context, "BootReceiver inicio Sentry con doble consentimiento.");
    }

    private boolean hasNotificationPermission(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }

    private void recordBootDecision(Context context, String message) {
        try {
            SentryCore.getInstance(context).appendEvent(
                    new LogEvent(
                            System.currentTimeMillis(),
                            "SENTRY_BOOT",
                            message,
                            LogEvent.Severity.INFO));
        } catch (Exception ignored) {
            // BootReceiver must not crash if audit logging is unavailable.
        }
    }
}
