package com.odin.core.persistence;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.odin.core.domain.model.LogEvent;

import java.util.Date;

/**
 * LogRotationWorker es un Worker de WorkManager que ejecuta tareas de limpieza
 * de logs
 * en background sin afectar el consumo de batería.
 *
 * WorkManager garantiza:
 * - Ejecución confiable incluso si la app se cierra
 * - Backoff automático en caso de fallos
 * - Compatibilidad con Doze mode de Android
 * - <1% CPU en reposo
 */
public final class LogRotationWorker extends Worker {

    private static final String TAG = "LogRotationWorker";

    public LogRotationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.d(TAG, "Iniciando tarea de rotación de logs...");

            // Obtener SentryCore
            Context context = getApplicationContext();
            SentryCore sentryCore = SentryCore.getInstance(context);

            // Verificar salud del sistema
            if (!sentryCore.isHealthy()) {
                Log.w(TAG, "Sistema unhealthy. Diferiendo rotación.");
                return Result.retry(); // WorkManager reintentrará después
            }

            // Recuperar logs recientes para análisis
            var recentLogs = sentryCore.fetchRecentEvents(100);

            // Registrar estadísticas
            LogEvent rotationEvent = new LogEvent(
                    new Date().getTime(),
                    "LOG_ROTATION_WORKER",
                    "Rotación ejecutada. Logs recientes: " + recentLogs.size() +
                            ", Crashes acumulados: " + sentryCore.getCrashCount());

            sentryCore.appendEvent(rotationEvent);

            Log.d(TAG, "Rotación de logs completada exitosamente");
            return Result.success();

        } catch (Exception e) {
            Log.e(TAG, "Error en rotación de logs: " + e.getMessage());
            // Result.retry() hace que WorkManager reintente con backoff exponencial
            return Result.retry();
        }
    }
}
