package com.odin.core.persistence;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

/**
 * LogRotationManager coordina la limpieza automática y rotación de logs.
 *
 * Responsabilidades:
 * - Ejecutar rotación periódica de logs vía WorkManager
 * - Limpiar logs antiguos según políticas de retención
 * - Optimizar almacenamiento sin afectar batería
 * - Garantizar que operaciones pesadas ocurran en background
 *
 * Usa WorkManager en lugar de AlarmManager/Handler para máxima eficiencia
 * energética.
 */
public final class LogRotationManager {

    private static final String TAG = "LogRotationManager";
    private static final String WORK_TAG = "odin_log_rotation_work";
    private static final long ROTATION_INTERVAL_MINUTES = 30; // Cada 30 minutos

    /**
     * Inicia el trabajo periódico de rotación de logs.
     * Se recomienda llamar desde SentryService.onCreate()
     *
     * @param context ApplicationContext
     */
    public static void scheduleLogRotation(@NonNull Context context) {
        try {
            // Definir constraints: solo ejecutar en background con batería y conexión
            // estable
            Constraints constraints = new Constraints.Builder()
                    .setRequiresBatteryNotLow(false) // Ejecutar incluso con batería baja
                    .setRequiresCharging(false) // No requiere estar cargando
                    .setRequiresDeviceIdle(false) // No requiere device idle
                    .setRequiresStorageNotLow(true) // Solo si hay espacio suficiente
                    .build();

            // Crear trabajo periódico
            PeriodicWorkRequest logRotationWork = new PeriodicWorkRequest.Builder(
                    LogRotationWorker.class,
                    ROTATION_INTERVAL_MINUTES,
                    TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .addTag(WORK_TAG)
                    .build();

            // Enqueuer con política: reemplazar si ya existe
            WorkManager.getInstance(context)
                    .enqueueUniquePeriodicWork(
                            WORK_TAG,
                            ExistingPeriodicWorkPolicy.KEEP, // Mantener trabajo existente
                            logRotationWork);

            Log.i(TAG, "Log rotation trabajo programado cada " + ROTATION_INTERVAL_MINUTES + " minutos");

        } catch (Exception e) {
            Log.e(TAG, "Error programando rotación de logs: " + e.getMessage());
        }
    }

    /**
     * Cancela el trabajo periódico de rotación.
     *
     * @param context ApplicationContext
     */
    public static void cancelLogRotation(@NonNull Context context) {
        try {
            WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG);
            Log.i(TAG, "Log rotation trabajo cancelado");
        } catch (Exception e) {
            Log.e(TAG, "Error cancelando rotación: " + e.getMessage());
        }
    }
}
