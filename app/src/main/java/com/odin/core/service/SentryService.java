package com.odin.core.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.odin.core.R;
import com.odin.core.di.DependencyProvider;
import com.odin.core.domain.model.LogEvent;
import com.odin.core.domain.model.SystemStatus;
import com.odin.core.domain.repository.LogRepository;
import com.odin.core.domain.repository.SystemStatusRepository;
import com.odin.core.exception.OdinException;
import com.odin.core.exception.PersistenceException;
import com.odin.core.persistence.LogRotationManager;
import com.odin.core.persistence.SentryCore;
import com.odin.core.security.ConsentManager;
import com.odin.core.sentry.SentryRuntimeState;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SentryService es el corazón persistente de ODÍN (Foreground Service de máxima
 * prioridad).
 *
 * Características:
 * - Integración con SentryCore para persistencia cifrada AES-256-GCM
 * - START_NOT_STICKY: evita reinicios ocultos sin accion explicita del usuario
 * - WakeLock gestionado (solo para operaciones críticas)
 * - WorkManager para tareas periódicas eficientes
 * - Manejo defensivo con try-catch granulares
 * - Thread-safe: sincronización de operaciones críticas
 * - Consumo energético: <1% CPU en reposo
 */
public class SentryService extends Service {

    private static final String CHANNEL_ID = "odin_sentry_channel";
    private static final String CHANNEL_NAME = "ODIN Persistencia";
    private static final int NOTIFICATION_ID = 8901;
    private static final String TAG = "SentryService";
    private static final String WAKELOCK_TAG = "odin:sentry_wakelock";
    private static final long HEARTBEAT_INTERVAL_MS = 30_000L;
    public static final String ACTION_STOP_SENTRY = "com.odin.core.action.STOP_SENTRY";

    private LogRepository logRepository;
    private SystemStatusRepository statusRepository;
    private SentryCore sentryCore;
    private PowerManager.WakeLock wakeLock;
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    private Handler heartbeatHandler;
    private final Runnable heartbeatRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                SentryRuntimeState.markHeartbeat(
                        SentryService.this,
                        sentryCore != null && sentryCore.isHealthy());
            } catch (Exception e) {
                Log.w(TAG, "Error actualizando heartbeat: " + e.getMessage());
            }
            if (heartbeatHandler != null) {
                heartbeatHandler.postDelayed(this, HEARTBEAT_INTERVAL_MS);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            SentryRuntimeState.markStarting(this, null);
            createNotificationChannel();
            logRepository = DependencyProvider.provideLogRepository(this);
            statusRepository = DependencyProvider.provideSystemStatusRepository(this);
            sentryCore = SentryCore.getInstance(this);
            heartbeatHandler = new Handler(Looper.getMainLooper());
            acquireWakeLock();
            LogRotationManager.scheduleLogRotation(this);
            initializeAgent();
            isInitialized.set(true);
            Log.i(TAG, "SentryService inicializado exitosamente");
        } catch (Exception e) {
            Log.e(TAG, "Error crítico en onCreate: " + e.getMessage());
            SentryRuntimeState.markError(this, e.getMessage());
            handleCriticalError(e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent != null && ACTION_STOP_SENTRY.equals(intent.getAction())) {
                SentryRuntimeState.markStopping(this);
                stopSelf();
                return START_NOT_STICKY;
            }
            if (!ConsentManager.getInstance(this).isSentryEnabled()) {
                SentryRuntimeState.markStopped(this, "Consentimiento no concedido.");
                stopSelf();
                return START_NOT_STICKY;
            }
            if (!hasNotificationPermission()) {
                SentryRuntimeState.markError(this, "Permiso de notificaciones requerido.");
                stopSelf();
                return START_NOT_STICKY;
            }
            if (!isInitialized.get()) {
                Log.w(TAG, "Servicio aún no inicializado, reintentando...");
                SentryRuntimeState.markError(this, "Servicio no inicializado.");
                stopSelf();
                return START_NOT_STICKY;
            }
            startForeground(NOTIFICATION_ID, buildForegroundNotification());
            SentryRuntimeState.markActive(this, sentryCore != null && sentryCore.isHealthy());
            startHeartbeat();
            publishAgentStatus();
            return START_NOT_STICKY;
        } catch (Exception e) {
            Log.e(TAG, "Error en onStartCommand: " + e.getMessage());
            SentryRuntimeState.markError(this, e.getMessage());
            handleCriticalError(e);
            stopSelf();
            return START_NOT_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        try {
            Log.i(TAG, "Destruyendo SentryService...");
            stopHeartbeat();
            LogRotationManager.cancelLogRotation(this);
            saveShutdownStatus();
            releaseWakeLock();
            SentryRuntimeState.markStopped(this, "Servicio detenido.");
        } catch (Exception e) {
            Log.e(TAG, "Error en onDestroy: " + e.getMessage());
            SentryRuntimeState.markError(this, e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initializeAgent() {
        try {
            sentryCore.appendEvent(new LogEvent(
                    new Date().getTime(),
                    "SENTRY_SERVICE",
                    "Servicio de persistencia inicializado exitosamente"));
            logRepository.appendEvent(createLogEvent("SENTRY_SERVICE",
                    "Inicializando servicio de alta persistencia."));
            statusRepository.saveStatus(new SystemStatus(true, System.currentTimeMillis(),
                    "SERVICIO_INICIADO"));
            Log.i(TAG, "Agente inicializado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando agente", e);
        }
    }

    private void publishAgentStatus() {
        try {
            List<LogEvent> recentEvents = sentryCore.fetchRecentEvents(10);
            sentryCore.appendEvent(new LogEvent(
                    new Date().getTime(),
                    "STATUS_PUBLISH",
                    "Estado publicado. Eventos: " + recentEvents.size() +
                            ", Crashes: " + sentryCore.getCrashCount() +
                            ", Health: " + (sentryCore.isHealthy() ? "OK" : "CRITICAL")));
            logRepository.appendEvent(createLogEvent("STATUS_PUBLISH",
                    "Se publicaron " + recentEvents.size() + " eventos recientes."));
            statusRepository.saveStatus(new SystemStatus(true, System.currentTimeMillis(),
                    "SERVICIO_FOREGROUND"));
        } catch (Exception e) {
            Log.w(TAG, "Error publicando estado", e);
        }
    }

    private void saveShutdownStatus() {
        try {
            sentryCore.appendEvent(new LogEvent(
                    new Date().getTime(),
                    "SENTRY_SERVICE",
                    "Servicio detenido de forma controlada"));
            statusRepository.saveStatus(new SystemStatus(false, System.currentTimeMillis(),
                    "SERVICIO_DETENIDO"));
            logRepository.appendEvent(createLogEvent("SENTRY_SERVICE",
                    "Servicio detenido de forma controlada."));
        } catch (Exception e) {
            Log.e(TAG, "Error guardando estado de apagado: " + e.getMessage());
        }
    }

    private void acquireWakeLock() {
        try {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (powerManager != null) {
                wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);
                Log.d(TAG, "WakeLock inicializado (no adquirido aún)");
            }
        } catch (Exception e) {
            Log.w(TAG, "Error inicializando WakeLock: " + e.getMessage());
        }
    }

    private void releaseWakeLock() {
        try {
            if (wakeLock != null && wakeLock.isHeld()) {
                wakeLock.release();
                Log.d(TAG, "WakeLock liberado");
            }
        } catch (Exception e) {
            Log.w(TAG, "Error liberando WakeLock: " + e.getMessage());
        }
    }

    private void handleCriticalError(Exception error) {
        try {
            if (sentryCore != null) {
                sentryCore.appendEvent(new LogEvent(
                        new Date().getTime(),
                        "CRITICAL_ERROR",
                        "Error crítico en SentryService: " + error.getMessage()));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error registrando error crítico: " + e.getMessage());
        }
    }

    private boolean hasNotificationPermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }

    private void startHeartbeat() {
        if (heartbeatHandler != null) {
            heartbeatHandler.removeCallbacks(heartbeatRunnable);
            heartbeatHandler.post(heartbeatRunnable);
        }
    }

    private void stopHeartbeat() {
        if (heartbeatHandler != null) {
            heartbeatHandler.removeCallbacks(heartbeatRunnable);
        }
    }

    private Notification buildForegroundNotification() {
        Intent activityIntent = new Intent();
        activityIntent.setClassName(this, "com.odin.core.ui.MainActivity");
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("ODIN Sentry activo")
                .setContentText("Proteccion local visible en ejecucion")
                .setSmallIcon(R.drawable.ic_odin_notification)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setOngoing(true)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Servicio visible de proteccion local ODIN Sentry.");
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private LogEvent createLogEvent(String source, String message) {
        return new LogEvent(new Date().getTime(), source, message);
    }
}
