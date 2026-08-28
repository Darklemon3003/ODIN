package com.odin.core.exception;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * GlobalExceptionHandler implementa Thread.UncaughtExceptionHandler para
 * capturar
 * todas las excepciones no controladas de la aplicación.
 *
 * Responsabilidades:
 * - Capturar el stack trace completo
 * - Encriptarlo inmediatamente (mediante SentryCore)
 * - Almacenar en logs encriptados antes de un cierre
 * - Notificar al usuario de forma segura
 *
 * Thread-safe: Usa synchronized para operaciones críticas.
 */
public final class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "GlobalExceptionHandler";
    private static volatile GlobalExceptionHandler instance;

    private final Thread.UncaughtExceptionHandler defaultHandler;
    private final ExceptionCallback callback;

    /**
     * Callback interface para notificar a SentryCore cuando una excepción es
     * capturada.
     */
    public interface ExceptionCallback {
        void onUncaughtException(@NonNull OdinException exception, @NonNull String stackTrace);
    }

    private GlobalExceptionHandler(
            Thread.UncaughtExceptionHandler defaultHandler,
            @NonNull ExceptionCallback callback) {
        this.defaultHandler = defaultHandler;
        this.callback = callback;
    }

    /**
     * Inicializa el manejador global de excepciones. Se recomienda llamar en
     * Application.onCreate()
     *
     * @param callback Implementación que recibirá las excepciones capturadas
     */
    public static void initialize(@NonNull ExceptionCallback callback) {
        if (instance == null) {
            synchronized (GlobalExceptionHandler.class) {
                if (instance == null) {
                    instance = new GlobalExceptionHandler(
                            Thread.getDefaultUncaughtExceptionHandler(),
                            callback);
                    Thread.setDefaultUncaughtExceptionHandler(instance);
                }
            }
        }
    }

    /**
     * Se invoca cuando un thread lanza una excepción no capturada.
     * Esta es la entrada principal del manejador.
     *
     * @param thread    Thread que generó la excepción
     * @param throwable La excepción lanzada
     */
    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        try {
            // Convertir a OdinException si es necesario
            OdinException odinException = convertToOdinException(throwable);

            // Obtener stack trace legible
            String stackTrace = getStackTraceString(throwable);

            // Log de emergencia (no confiar en otros sistemas)
            Log.e(TAG, "UNCAUGHT EXCEPTION: " + odinException.getFormattedMessage());
            Log.e(TAG, "Stack Trace:\n" + stackTrace);

            // Notificar a SentryCore para cifrar y almacenar
            try {
                callback.onUncaughtException(odinException, stackTrace);
            } catch (Exception e) {
                // Si SentryCore falla, al menos loggeamos el error
                Log.e(TAG, "Error notificando a SentryCore: " + e.getMessage());
            }

        } catch (Exception e) {
            // Si todo falla, fallback al handler por defecto
            Log.e(TAG, "Error crítico en GlobalExceptionHandler: " + e.getMessage());
        }

        // Invocar al handler por defecto (probablemente reinicie la app)
        if (defaultHandler != null) {
            defaultHandler.uncaughtException(thread, throwable);
        } else {
            // Fallback: mostrar error y salir
            System.exit(1);
        }
    }

    /**
     * Convierte cualquier Throwable a OdinException preservando la causa original.
     *
     * @param throwable La excepción a convertir
     * @return OdinException equivalente
     */
    private OdinException convertToOdinException(Throwable throwable) {
        if (throwable instanceof OdinException) {
            return (OdinException) throwable;
        }

        String message = throwable.getMessage() != null
                ? throwable.getMessage()
                : throwable.getClass().getSimpleName();

        // Clasificar severidad basada en el tipo de excepción
        OdinException.ErrorSeverity severity = classifySeverity(throwable);

        return new OdinException(message, throwable, severity);
    }

    /**
     * Clasifica la severidad de una excepción basada en su tipo.
     *
     * @param throwable La excepción a clasificar
     * @return Nivel de severidad estimado
     */
    private OdinException.ErrorSeverity classifySeverity(Throwable throwable) {
        if (throwable instanceof OutOfMemoryError ||
                throwable instanceof StackOverflowError ||
                throwable instanceof VirtualMachineError) {
            return OdinException.ErrorSeverity.CRITICAL;
        } else if (throwable instanceof SecurityException ||
                throwable instanceof SecurityException) {
            return OdinException.ErrorSeverity.CRITICAL;
        } else if (throwable instanceof NullPointerException ||
                throwable instanceof ClassCastException ||
                throwable instanceof IllegalArgumentException) {
            return OdinException.ErrorSeverity.MEDIUM;
        } else {
            return OdinException.ErrorSeverity.LOW;
        }
    }

    /**
     * Obtiene una representación legible del stack trace.
     *
     * @param throwable La excepción de la cual obtener el stack trace
     * @return String con stack trace formateado
     */
    private String getStackTraceString(@NonNull Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.getClass().getName())
                .append(": ")
                .append(throwable.getMessage())
                .append("\n");

        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ")
                    .append(element.getClassName())
                    .append(".")
                    .append(element.getMethodName())
                    .append("(")
                    .append(element.getFileName())
                    .append(":")
                    .append(element.getLineNumber())
                    .append(")\n");
        }

        // Incluir causa si existe
        if (throwable.getCause() != null) {
            sb.append("Caused by: ")
                    .append(getStackTraceString(throwable.getCause()));
        }

        return sb.toString();
    }

    /**
     * Retorna la instancia del handler (para debugging).
     *
     * @return GlobalExceptionHandler instance o null si no fue inicializado
     */
    public static GlobalExceptionHandler getInstance() {
        return instance;
    }
}
