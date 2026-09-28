package com.odin.core.persistence;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.odin.core.data.local.EnhancedSecureLocalStorage;
import com.odin.core.domain.model.LogEvent;
import com.odin.core.domain.model.SystemStatus;
import com.odin.core.domain.model.FirewallRule;
import com.odin.core.exception.GlobalExceptionHandler;
import com.odin.core.exception.OdinException;
import com.odin.core.exception.PersistenceException;
import com.odin.core.security.CryptographicManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SentryCore es el orquestador central de persistencia del proyecto ODÍN.
 *
 * Responsabilidades:
 * - Gestión del ciclo de vida de datos (LogEvents, SystemStatus, FirewallRules)
 * - Cifrado/descifrado transparente mediante CryptographicManager
 * - Rotación automática de logs con límites de tamaño
 * - Monitoreo de salud del sistema (heartbeat)
 * - Manejo centralizado de excepciones críticas
 * - Auto-sanación ante fallos de persistencia
 *
 * Thread-safety:
 * - Usa synchronized en operaciones de I/O críticas
 * - Atomic variables para contadores y flags
 * - No bloquea operaciones no relacionadas
 *
 * Patrón: Singleton thread-safe con inicialización lazy.
 */
public final class SentryCore implements GlobalExceptionHandler.ExceptionCallback {

    private static final String TAG = "SentryCore";
    private static volatile SentryCore instance;

    private static final String KEY_LOG_EVENTS = "key_log_events_encrypted";
    private static final String KEY_SYSTEM_STATUS = "key_system_status_encrypted";
    private static final String KEY_HEARTBEAT = "key_heartbeat_timestamp";
    private static final String KEY_CRASH_COUNT = "key_crash_count";
    private static final String KEY_LAST_SYNC = "key_last_sync_timestamp";
    private static final String KEY_CORRUPTED_DATA_MARKER = "key_corrupted_marker";
    private static final String KEY_QUARANTINED_LOG_EVENTS = "key_quarantined_log_events";
    private static final String KEY_CRASH_TRACE_INDEX = "key_crash_trace_index";

    // Límites de seguridad
    private static final int MAX_LOG_ENTRIES = 5000;
    private static final int MAX_CRASH_COUNT_BEFORE_CLEANUP = 10;
    private static final int MAX_CRASH_TRACES = 10; // Limite de crash traces retenidos
    private static final long HEARTBEAT_INTERVAL_MS = 60_000; // 1 minuto
    private static final long DATA_SYNC_INTERVAL_MS = 300_000; // 5 minutos

    private final Context context;
    private final EnhancedSecureLocalStorage storage;
    private final CryptographicManager cryptoManager;

    // Estado thread-safe
    private final AtomicLong lastHeartbeat = new AtomicLong(System.currentTimeMillis());
    private final AtomicBoolean isHealthy = new AtomicBoolean(true);
    private final AtomicLong crashCount = new AtomicLong(0);
    private final AtomicBoolean hasCorruption = new AtomicBoolean(false);

    /**
     * Constructor privado. Usar getInstance() en su lugar.
     *
     * @param context ApplicationContext
     * @throws PersistenceException si falla la inicialización
     */
    private SentryCore(@NonNull Context context) throws PersistenceException {
        this.context = context.getApplicationContext();
        try {
            this.storage = EnhancedSecureLocalStorage.getInstance(context);
            this.cryptoManager = new CryptographicManager(context);

            // Registrar este instance como manejador de excepciones global
            GlobalExceptionHandler.initialize(this);

            // Inicializar estado
            initializeState();

        } catch (Exception e) {
            throw new PersistenceException(
                    "No se pudo inicializar SentryCore",
                    e,
                    OdinException.ErrorSeverity.CRITICAL);
        }
    }

    /**
     * Inicializa el estado de SentryCore al primer arranque.
     * Verifica integridad de datos y limpia si es necesario.
     */
    private void initializeState() {
        try {
            // Verificar integridad de datos
            verifyDataIntegrity();

            // Cargar contadores
            long crashes = loadCrashCount();
            crashCount.set(crashes);

            // Actualizar heartbeat de inicialización
            recordHeartbeat();

            // Log de inicialización
            appendEvent(new LogEvent(
                    new Date().getTime(),
                    "SENTRY_CORE",
                    "Inicialización completada. Crash count: " + crashes));

        } catch (Exception e) {
            Log.e(TAG, "Error en inicialización de estado: " + e.getMessage());
            isHealthy.set(false);
        }
    }

    /**
     * Obtiene la instancia singleton de SentryCore.
     * Double-check locking para eficiencia.
     *
     * @param context ApplicationContext
     * @return SentryCore instance
     * @throws PersistenceException si falla la inicialización
     */
    public static SentryCore getInstance(@NonNull Context context) throws PersistenceException {
        if (instance == null) {
            synchronized (SentryCore.class) {
                if (instance == null) {
                    instance = new SentryCore(context);
                }
            }
        }
        return instance;
    }

    /**
     * Añade un evento de auditoría con cifrado automático.
     *
     * @param event LogEvent a almacenar
     * @throws PersistenceException si el almacenamiento falla
     */
    public synchronized void appendEvent(@NonNull LogEvent event) throws PersistenceException {
        try {
            // Cargar eventos actuales
            List<LogEvent> events = loadAllEvents();

            // Añadir nuevo evento
            events.add(event);

            // Aplicar rotación si se excede el límite
            if (events.size() > MAX_LOG_ENTRIES) {
                performLogRotation(events);
            }

            // Serializar y cifrar
            String eventsJson = serializeEvents(events);
            String encryptedJson = cryptoManager.encrypt(eventsJson);

            // Guardar encriptado
            storage.saveString(KEY_LOG_EVENTS, encryptedJson);

            // Actualizar heartbeat
            recordHeartbeat();

        } catch (Exception e) {
            Log.e(TAG, "Error persistiendo evento de auditoria", e);
            isHealthy.set(false);
            throw new PersistenceException(
                    "Error al añadir evento de auditoría",
                    e,
                    OdinException.ErrorSeverity.MEDIUM);
        }
    }

    /**
     * Recupera los eventos recientes (últimos N).
     *
     * @param limit Número máximo de eventos a recuperar
     * @return Lista de LogEvent descifrados
     * @throws PersistenceException si la operación falla
     */
    public synchronized List<LogEvent> fetchRecentEvents(int limit) throws PersistenceException {
        try {
            List<LogEvent> allEvents = loadAllEvents();

            // Retornar últimos 'limit' eventos
            int startIndex = Math.max(0, allEvents.size() - limit);
            return new ArrayList<>(allEvents.subList(startIndex, allEvents.size()));

        } catch (Exception e) {
            throw new PersistenceException(
                    "Error al recuperar eventos recientes",
                    e,
                    OdinException.ErrorSeverity.LOW);
        }
    }

    /**
     * Carga todos los eventos disponibles (descifrados).
     *
     * Si los datos estan corruptos, NO los elimina. En su lugar:
     * - Marca el sistema con corrupcion (hasCorruption = true)
     * - Cuarentena el blob corrupto bajo KEY_QUARANTINED_LOG_EVENTS
     * - Retorna lista vacia (sin implicar que no hubo eventos)
     *
     * @return Lista completa de LogEvent
     * @throws PersistenceException si la carga falla
     */
    private synchronized List<LogEvent> loadAllEvents() throws PersistenceException {
        try {
            String encryptedJson = storage.loadString(KEY_LOG_EVENTS, "");

            if (encryptedJson.isEmpty()) {
                return new ArrayList<>();
            }

            // Descifrar
            String plainJson = cryptoManager.decrypt(encryptedJson);
            return deserializeEvents(plainJson);

        } catch (Exception e) {
            Log.w(TAG, "Error descargando eventos. Datos corruptos preservados, no eliminados.");
            quarantineCorruptData(e);
            return new ArrayList<>();
        }
    }

    /**
     * Implementa rotación de logs para evitar consumo ilimitado de almacenamiento.
     * Mantiene los últimos MAX_LOG_ENTRIES eventos y archiva los antiguos.
     *
     * @param events Lista actual de eventos
     */
    private void performLogRotation(@NonNull List<LogEvent> events) {
        try {
            // Mantener solo los últimos MAX_LOG_ENTRIES
            int startIndex = events.size() - MAX_LOG_ENTRIES;
            List<LogEvent> retainedEvents = new ArrayList<>(
                    events.subList(startIndex, events.size()));

            // Registrar rotación
            appendEvent(new LogEvent(
                    new Date().getTime(),
                    "LOG_ROTATION",
                    "Rotación realizada. Eventos antes: " + events.size() +
                            ", después: " + retainedEvents.size()));

        } catch (Exception e) {
            Log.e(TAG, "Error en rotación de logs: " + e.getMessage());
        }
    }

    /**
     * Registra un latido de vida del sistema (heartbeat).
     * Utilizado para monitoreo de salud y recuperación automática.
     */
    private void recordHeartbeat() {
        try {
            long now = System.currentTimeMillis();
            lastHeartbeat.set(now);
            storage.saveLong(KEY_HEARTBEAT, now);
            isHealthy.set(true);
        } catch (Exception e) {
            Log.w(TAG, "Error registrando heartbeat: " + e.getMessage());
        }
    }

    /**
     * Verifica si el sistema está dentro de rangos normales.
     *
     * @return true si está sano, false si requiere intervención
     */
    public boolean isHealthy() {
        return isHealthy.get() && crashCount.get() < MAX_CRASH_COUNT_BEFORE_CLEANUP;
    }

    /**
     * Obtiene el timestamp del último heartbeat.
     *
     * @return Milisegundos desde epoch
     */
    public long getLastHeartbeat() {
        return lastHeartbeat.get();
    }

    /**
     * Obtiene el número de crashes registrados en esta sesión.
     *
     * @return Contador de crashes
     */
    public long getCrashCount() {
        return crashCount.get();
    }

    /**
     * Implementa Callback de GlobalExceptionHandler.
     * Cifra y almacena stack traces de excepciones no controladas.
     *
     * @param exception  OdinException capturada
     * @param stackTrace Stack trace formateado
     */
    @Override
    public void onUncaughtException(@NonNull OdinException exception, @NonNull String stackTrace) {
        try {
            // Incrementar contador de crashes
            crashCount.incrementAndGet();

            // Crear evento especial para crash
            String crashMessage = "UNCAUGHT EXCEPTION: " + exception.getFormattedMessage() +
                    "\nStack:\n" + stackTrace;

            LogEvent crashEvent = new LogEvent(
                    new Date().getTime(),
                    "CRASH_HANDLER",
                    crashMessage);

            // Cifrar stack trace adicional por seguridad
            String encryptedStackTrace = cryptoManager.encrypt(stackTrace);
            String crashLog = exception.getFormattedMessage() + "\n[ENCRYPTED_STACKTRACE:" +
                    encryptedStackTrace + "]";

            // Almacenar en storage seguro con clave de timestamp
            String crashKey = "crash_" + System.currentTimeMillis();
            storage.saveString(crashKey, cryptoManager.encrypt(crashLog));

            // Pruning: mantener solo los ultimos MAX_CRASH_TRACES crash traces
            pruneCrashTraces(crashKey);

            // Registrar contador
            storage.saveLong(KEY_CRASH_COUNT, crashCount.get());

            // Marcar como unhealthy si los crashes son excesivos
            if (crashCount.get() >= MAX_CRASH_COUNT_BEFORE_CLEANUP) {
                isHealthy.set(false);
                Log.e(TAG, "SISTEMA CRÍTICO: Demasiados crashes. Activando modo de recuperación.");
            }

        } catch (Exception e) {
            Log.e(TAG, "Error crítico en onUncaughtException: " + e.getMessage());
        }
    }

    /**
     * Pruning de crash traces para evitar crecimiento ilimitado.
     * Mantiene un registro de claves de crash traces en KEY_CRASH_TRACE_INDEX
     * y elimina los mas antiguos cuando se excede MAX_CRASH_TRACES.
     *
     * @param newCrashKey Clave del crash trace recien agregado
     */
    private void pruneCrashTraces(@NonNull String newCrashKey) {
        try {
            // Cargar indice de crash traces
            String indexJson = storage.loadString(KEY_CRASH_TRACE_INDEX, "[]");
            JSONArray indexArray = new JSONArray(indexJson);

            // Agregar nueva clave
            indexArray.put(newCrashKey);

            // Pruning: eliminar crash traces antiguos si se excede el limite
            while (indexArray.length() > MAX_CRASH_TRACES) {
                String oldKey = indexArray.getString(0);
                storage.remove(oldKey);
                // Remover del indice
                JSONArray newArray = new JSONArray();
                for (int i = 1; i < indexArray.length(); i++) {
                    newArray.put(indexArray.getString(i));
                }
                indexArray = newArray;
            }

            // Guardar indice actualizado
            storage.saveString(KEY_CRASH_TRACE_INDEX, indexArray.toString());

        } catch (Exception e) {
            Log.w(TAG, "Error en pruning de crash traces: " + e.getMessage());
        }
    }

    /**
     * Cuarentena datos corruptos sin eliminarlos.
     *
     * Preserva el blob corrupto bajo KEY_QUARANTINED_LOG_EVENTS para posible
     * recuperacion futura. NO elimina KEY_LOG_EVENTS inmediatamente.
     *
     * Marca el sistema con hasCorruption = true para que la UI pueda
     * distinguir "no hay eventos" de "corrupcion detectada".
     *
     * @param originalError Excepcion original que causo la corrupcion
     */
    private void quarantineCorruptData(@NonNull Exception originalError) {
        try {
            Log.w(TAG, "Cuarentena de datos corruptos. Datos preservados, no eliminados.");

            // Marcar corrupcion
            hasCorruption.set(true);

            // Preservar blob corrupto bajo clave de cuarentena
            String corruptBlob = storage.loadString(KEY_LOG_EVENTS, "");
            if (!corruptBlob.isEmpty()) {
                storage.saveString(KEY_QUARANTINED_LOG_EVENTS, corruptBlob);
            }

            // Marcar con timestamp de corrupcion (no elimina datos originales)
            storage.saveString(KEY_CORRUPTED_DATA_MARKER,
                    cryptoManager.encrypt("Data corruption detected at " + new Date() +
                            ". Original data quarantined. Cause: " +
                            originalError.getMessage()));

            // No eliminar KEY_LOG_EVENTS aqui. El dato corrupto se preserva.
            // Solo se eliminara mediante reset explicito del usuario.

        } catch (Exception e) {
            Log.e(TAG, "Cuarentena de datos falló: " + e.getMessage());
            isHealthy.set(false);
        }
    }

    /**
     * Indica si se ha detectado corrupcion en los datos persistidos.
     *
     * @return true si hay corrupcion detectada, false en caso contrario
     */
    public boolean hasCorruption() {
        return hasCorruption.get();
    }

    /**
     * Elimina explicitamente los datos corruptos en cuarentena.
     * Solo debe llamarse por accion explicita del usuario (reset).
     */
    public synchronized void clearCorruptData() {
        try {
            storage.remove(KEY_QUARANTINED_LOG_EVENTS);
            storage.remove(KEY_CORRUPTED_DATA_MARKER);
            storage.remove(KEY_LOG_EVENTS);
            hasCorruption.set(false);
        } catch (PersistenceException e) {
            Log.e(TAG, "Error limpiando datos corruptos: " + e.getMessage());
        }
    }

    /**
     * Verifica integridad de datos al iniciar.
     * Detecta corrupcion y la cuarentena sin eliminar datos.
     */
    private void verifyDataIntegrity() {
        try {
            // Intentar cargar y descifrar eventos
            String encryptedJson = storage.loadString(KEY_LOG_EVENTS, "");
            if (!encryptedJson.isEmpty()) {
                cryptoManager.decrypt(encryptedJson);
            }
        } catch (Exception e) {
            Log.w(TAG, "Corrupcion detectada durante verificacion: " + e.getMessage());
            quarantineCorruptData(e);
        }
    }

    /**
     * Carga el contador de crashes desde almacenamiento.
     *
     * @return Número de crashes registrados
     */
    private long loadCrashCount() {
        try {
            return storage.loadLong(KEY_CRASH_COUNT, 0);
        } catch (Exception e) {
            Log.w(TAG, "Error cargando crash count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Serializa una lista de LogEvent a JSON encriptable.
     *
     * @param events Lista de eventos
     * @return JSON string
     */
    private String serializeEvents(@NonNull List<LogEvent> events) throws JSONException {
        JSONArray array = new JSONArray();
        for (LogEvent event : events) {
            array.put(event.toJson());
        }
        return array.toString();
    }

    /**
     * Deserializa JSON a lista de LogEvent.
     *
     * @param jsonString JSON a deserializar
     * @return Lista de LogEvent
     */
    private List<LogEvent> deserializeEvents(@NonNull String jsonString) throws JSONException {
        JSONArray array = new JSONArray(jsonString);
        List<LogEvent> events = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            events.add(new LogEvent(obj));
        }

        return events;
    }

    /**
     * Guarda el estado del sistema con cifrado.
     *
     * @param status SystemStatus a almacenar
     * @throws PersistenceException si el almacenamiento falla
     */
    public synchronized void saveSystemStatus(@NonNull SystemStatus status) throws PersistenceException {
        try {
            JSONObject statusJson = new JSONObject();
            statusJson.put("timestamp", status.getLastHeartbeat());
            statusJson.put("serviceActive", status.isServiceActive());
            statusJson.put("currentState", status.getCurrentState());

            String encryptedJson = cryptoManager.encrypt(statusJson.toString());
            storage.saveString(KEY_SYSTEM_STATUS, encryptedJson);
            recordHeartbeat();

        } catch (Exception e) {
            isHealthy.set(false);
            throw new PersistenceException(
                    "Error al guardar estado del sistema",
                    e,
                    OdinException.ErrorSeverity.MEDIUM);
        }
    }

    /**
     * Recupera el estado del sistema (descifrado).
     *
     * @return SystemStatus o null si no existe
     * @throws PersistenceException si la operación falla
     */
    @Nullable
    public synchronized SystemStatus loadSystemStatus() throws PersistenceException {
        try {
            String encryptedJson = storage.loadString(KEY_SYSTEM_STATUS, "");
            if (encryptedJson.isEmpty()) {
                return null;
            }

            String plainJson = cryptoManager.decrypt(encryptedJson);
            JSONObject obj = new JSONObject(plainJson);

            return new SystemStatus(
                    obj.getBoolean("serviceActive"),
                    obj.getLong("timestamp"),
                    obj.getString("currentState"));

        } catch (Exception e) {
            throw new PersistenceException(
                    "Error al cargar estado del sistema",
                    e,
                    OdinException.ErrorSeverity.LOW);
        }
    }

    /**
     * Guarda reglas de firewall con cifrado.
     *
     * @param rules Lista de FirewallRule a almacenar
     * @throws PersistenceException si el almacenamiento falla
     */
    public synchronized void saveFirewallRules(@NonNull List<FirewallRule> rules) throws PersistenceException {
        try {
            JSONArray array = new JSONArray();
            for (FirewallRule rule : rules) {
                JSONObject ruleJson = new JSONObject();
                ruleJson.put("ipPattern", rule.getIpPattern());
                ruleJson.put("port", rule.getPort());
                array.put(ruleJson);
            }

            String encryptedJson = cryptoManager.encrypt(array.toString());
            storage.saveString("key_firewall_rules_encrypted", encryptedJson);
            recordHeartbeat();

        } catch (Exception e) {
            isHealthy.set(false);
            throw new PersistenceException(
                    "Error al guardar reglas de firewall",
                    e,
                    OdinException.ErrorSeverity.MEDIUM);
        }
    }

    /**
     * Recupera reglas de firewall (descifradas).
     *
     * @return Lista de FirewallRule
     * @throws PersistenceException si la operación falla
     */
    public synchronized List<FirewallRule> loadFirewallRules() throws PersistenceException {
        try {
            String encryptedJson = storage.loadString("key_firewall_rules_encrypted", "");
            if (encryptedJson.isEmpty()) {
                return new ArrayList<>();
            }

            String plainJson = cryptoManager.decrypt(encryptedJson);
            JSONArray array = new JSONArray(plainJson);
            List<FirewallRule> rules = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String ipPattern = obj.optString("ipPattern", "");
                Integer port = obj.has("port") ? obj.getInt("port") : null;
                rules.add(new FirewallRule(ipPattern, port));
            }

            return rules;

        } catch (Exception e) {
            throw new PersistenceException(
                    "Error al cargar reglas de firewall",
                    e,
                    OdinException.ErrorSeverity.LOW);
        }
    }

    /**
     * Obtiene estadísticas del sistema para monitoreo.
     *
     * @return JSONObject con estadísticas
     */
    public synchronized JSONObject getSystemStatistics() {
        try {
            JSONObject stats = new JSONObject();
            stats.put("isHealthy", isHealthy());
            stats.put("lastHeartbeat", getLastHeartbeat());
            stats.put("crashCount", getCrashCount());
            stats.put("logEventCount", loadAllEvents().size());
            stats.put("keyAvailable", cryptoManager.isKeyAvailable());
            return stats;
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo estadísticas: " + e.getMessage());
            return new JSONObject();
        }
    }

    /**
     * Reinicia la instancia singleton (para testing).
     */
    public static void resetInstance() {
        instance = null;
    }
}
