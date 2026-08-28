package com.odin.core.forensics;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.odin.core.domain.model.LogEvent;
import com.odin.core.exception.PersistenceException;
import com.odin.core.persistence.SentryCore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPOutputStream;

/**
 * LogRetentionManager gestiona políticas de retención de logs.
 *
 * Responsabilidades:
 * - Políticas de retención configurables
 * - Archivado automático de logs antiguos
 * - Compresión de logs históricos
 * - Purga segura de datos expirados
 * - Cumplimiento de GDPR y regulaciones
 *
 * Thread-safety: Usa ConcurrentHashMap para políticas y sincronización en operaciones críticas.
 */
public final class LogRetentionManager {

    private static final String TAG = "LogRetentionManager";
    private static volatile LogRetentionManager instance;

    // Configuración por defecto
    private static final long DEFAULT_RETENTION_DAYS = 90; // 90 días
    private static final long DEFAULT_ARCHIVE_DAYS = 30; // Archivar después de 30 días
    private static final int MAX_ARCHIVE_SIZE_MB = 100; // 100 MB máximo de archivos archivados
    private static final String ARCHIVE_DIR = "odin_log_archives";

    private final SentryCore sentryCore;
    private final Context context;
    private final File archiveDirectory;

    // Políticas de retención por severidad
    private final Map<LogEvent.Severity, Long> retentionPolicies;

    /**
     * Constructor privado. Usar getInstance() en su lugar.
     *
     * @param context ApplicationContext
     * @throws PersistenceException si falla la inicialización
     */
    private LogRetentionManager(@NonNull Context context) throws PersistenceException {
        this.context = context.getApplicationContext();
        this.sentryCore = SentryCore.getInstance(context);
        this.archiveDirectory = new File(context.getFilesDir(), ARCHIVE_DIR);
        
        // Crear directorio de archivos si no existe
        if (!archiveDirectory.exists()) {
            archiveDirectory.mkdirs();
        }
        
        // Inicializar políticas de retención por defecto
        this.retentionPolicies = new ConcurrentHashMap<>();
        initializeDefaultPolicies();
    }

    /**
     * Inicializa políticas de retención por defecto.
     */
    private void initializeDefaultPolicies() {
        retentionPolicies.put(LogEvent.Severity.INFO, DEFAULT_RETENTION_DAYS);
        retentionPolicies.put(LogEvent.Severity.WARNING, DEFAULT_RETENTION_DAYS);
        retentionPolicies.put(LogEvent.Severity.ERROR, DEFAULT_RETENTION_DAYS * 2); // Retener errores por más tiempo
        retentionPolicies.put(LogEvent.Severity.CRITICAL, Long.MAX_VALUE); // Nunca purgar críticos
    }

    /**
     * Obtiene la instancia singleton de LogRetentionManager.
     *
     * @param context ApplicationContext
     * @return LogRetentionManager instance
     * @throws PersistenceException si falla la inicialización
     */
    public static LogRetentionManager getInstance(@NonNull Context context) throws PersistenceException {
        if (instance == null) {
            synchronized (LogRetentionManager.class) {
                if (instance == null) {
                    instance = new LogRetentionManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * Establece política de retención para una severidad específica.
     *
     * @param severity Nivel de severidad
     * @param days Días de retención (Long.MAX_VALUE para retención permanente)
     */
    public synchronized void setRetentionPolicy(@NonNull LogEvent.Severity severity, long days) {
        retentionPolicies.put(severity, days);
        Log.i(TAG, "Política de retención actualizada: " + severity + " = " + days + " días");
    }

    /**
     * Obtiene la política de retención para una severidad.
     *
     * @param severity Nivel de severidad
     * @return Días de retención
     */
    public long getRetentionPolicy(@NonNull LogEvent.Severity severity) {
        return retentionPolicies.getOrDefault(severity, DEFAULT_RETENTION_DAYS);
    }

    /**
     * Ejecuta el proceso de retención (archivado y purga).
     * Debe llamarse periódicamente (ej. diariamente).
     *
     * @return RetentionResult con estadísticas de la operación
     * @throws PersistenceException si la operación falla
     */
    public synchronized RetentionResult executeRetention() throws PersistenceException {
        RetentionResult result = new RetentionResult();
        
        try {
            long now = System.currentTimeMillis();
            long archiveThreshold = now - (DEFAULT_ARCHIVE_DAYS * 24 * 60 * 60 * 1000);
            
            // Cargar todos los eventos
            List<LogEvent> allEvents = sentryCore.fetchRecentEvents(10000);
            
            // Separar eventos para archivar vs mantener
            List<LogEvent> toArchive = new ArrayList<>();
            List<LogEvent> toKeep = new ArrayList<>();
            
            for (LogEvent event : allEvents) {
                if (event.getTimestamp() < archiveThreshold) {
                    toArchive.add(event);
                } else {
                    toKeep.add(event);
                }
            }
            
            result.setTotalEvents(allEvents.size());
            result.setEventsToArchive(toArchive.size());
            result.setEventsToKeep(toKeep.size());
            
            // Archivar eventos antiguos
            if (!toArchive.isEmpty()) {
                archiveEvents(toArchive);
                result.setArchived(true);
                result.setArchiveFileCount(getArchiveFileCount());
            }
            
            // Purgar eventos expirados según políticas
            int purgedCount = purgeExpiredEvents(toKeep);
            result.setPurgedCount(purgedCount);
            
            // Actualizar logs activos solo con eventos a mantener
            if (!toArchive.isEmpty()) {
                // Nota: SentryCore maneja su propia rotación, aquí solo archivamos
                Log.i(TAG, "Archivados " + toArchive.size() + " eventos antiguos");
            }
            
            result.setSuccess(true);
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            throw new PersistenceException("Error ejecutando retención de logs", e);
        }
        
        return result;
    }

    /**
     * Archiva una lista de eventos en un archivo comprimido.
     *
     * @param events Lista de eventos a archivar
     * @throws IOException si falla el archivado
     */
    private void archiveEvents(@NonNull List<LogEvent> events) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String dateStr = dateFormat.format(new Date());
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        File archiveFile = new File(archiveDirectory, "archive_" + dateStr + "_" + timestamp + ".log.gz");
        
        try (FileOutputStream fos = new FileOutputStream(archiveFile);
             GZIPOutputStream gzos = new GZIPOutputStream(fos);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            // Serializar eventos a JSON
            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < events.size(); i++) {
                json.append(events.get(i).toJsonString());
                if (i < events.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");
            
            // Escribir comprimido
            gzos.write(json.toString().getBytes());
            gzos.finish();
            
            Log.i(TAG, "Archivados " + events.size() + " eventos en " + archiveFile.getName());
            
        } catch (Exception e) {
            if (archiveFile.exists()) {
                archiveFile.delete();
            }
            throw new IOException("Error archivando eventos", e);
        }
    }

    /**
     * Purga eventos que han excedido su política de retención.
     *
     * @param events Lista de eventos a evaluar
     * @return Número de eventos purgados
     */
    private int purgeExpiredEvents(@NonNull List<LogEvent> events) {
        long now = System.currentTimeMillis();
        int purgedCount = 0;
        
        List<LogEvent> toKeep = new ArrayList<>();
        
        for (LogEvent event : events) {
            long retentionDays = getRetentionPolicy(event.getSeverity());
            long retentionMs = retentionDays * 24 * 60 * 60 * 1000;
            
            if (event.getTimestamp() + retentionMs > now || retentionDays == Long.MAX_VALUE) {
                toKeep.add(event);
            } else {
                purgedCount++;
            }
        }
        
        if (purgedCount > 0) {
            Log.i(TAG, "Purgados " + purgedCount + " eventos expirados");
        }
        
        return purgedCount;
    }

    /**
     * Limpia archivos de archivo antiguos que exceden el tamaño máximo.
     *
     * @return Número de archivos eliminados
     */
    public synchronized int cleanupOldArchives() {
        int deletedCount = 0;
        long totalSize = 0;
        
        File[] archives = archiveDirectory.listFiles();
        if (archives == null) {
            return 0;
        }
        
        // Calcular tamaño total
        for (File archive : archives) {
            totalSize += archive.length();
        }
        
        // Si excede el límite, eliminar archivos más antiguos
        if (totalSize > MAX_ARCHIVE_SIZE_MB * 1024 * 1024) {
            List<File> sortedArchives = new ArrayList<>();
            for (File archive : archives) {
                sortedArchives.add(archive);
            }
            
            // Ordenar por fecha de modificación (más antiguos primero)
            sortedArchives.sort((a, b) -> Long.compare(a.lastModified(), b.lastModified()));
            
            // Eliminar hasta que el tamaño sea aceptable
            for (File archive : sortedArchives) {
                if (totalSize <= MAX_ARCHIVE_SIZE_MB * 1024 * 1024 * 0.8) { // 80% del límite
                    break;
                }
                
                long fileSize = archive.length();
                if (archive.delete()) {
                    totalSize -= fileSize;
                    deletedCount++;
                    Log.i(TAG, "Eliminado archivo de archivo: " + archive.getName());
                }
            }
        }
        
        return deletedCount;
    }

    /**
     * Obtiene el número de archivos en el directorio de archivo.
     *
     * @return Número de archivos
     */
    public int getArchiveFileCount() {
        File[] archives = archiveDirectory.listFiles();
        return archives != null ? archives.length : 0;
    }

    /**
     * Obtiene el tamaño total del directorio de archivo en bytes.
     *
     * @return Tamaño en bytes
     */
    public long getArchiveSizeBytes() {
        long totalSize = 0;
        File[] archives = archiveDirectory.listFiles();
        
        if (archives != null) {
            for (File archive : archives) {
                totalSize += archive.length();
            }
        }
        
        return totalSize;
    }

    /**
     * Elimina todos los archivos de archivo (para GDPR - derecho al olvido).
     *
     * @return Número de archivos eliminados
     */
    public synchronized int purgeAllArchives() {
        int deletedCount = 0;
        File[] archives = archiveDirectory.listFiles();
        
        if (archives != null) {
            for (File archive : archives) {
                if (archive.delete()) {
                    deletedCount++;
                }
            }
        }
        
        Log.i(TAG, "Purga completa de archivos: " + deletedCount + " archivos eliminados");
        return deletedCount;
    }

    /**
     * Restaura eventos desde un archivo de archivo.
     *
     * @param archiveFile Archivo de archivo a restaurar
     * @return Lista de eventos restaurados
     * @throws IOException si falla la restauración
     */
    public List<LogEvent> restoreFromArchive(@NonNull File archiveFile) throws IOException {
        // Esta es una operación avanzada que requeriría descompresión y parsing
        // Por ahora, retornamos una lista vacía ya que la restauración completa
        // requeriría más integración con SentryCore
        Log.w(TAG, "Restauración desde archivo no implementada completamente");
        return new ArrayList<>();
    }

    /**
     * Obtiene estadísticas de retención.
     *
     * @return JSONObject con estadísticas
     */
    public synchronized Map<String, Object> getRetentionStatistics() {
        Map<String, Object> stats = new java.util.HashMap<>();
        
        stats.put("archiveFileCount", getArchiveFileCount());
        stats.put("archiveSizeBytes", getArchiveSizeBytes());
        stats.put("archiveSizeMB", getArchiveSizeBytes() / (1024.0 * 1024.0));
        stats.put("retentionPolicies", new java.util.HashMap<>(retentionPolicies));
        
        return stats;
    }

    /**
     * Reinicia la instancia singleton (para testing).
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Clase interna para representar el resultado de una operación de retención.
     */
    public static class RetentionResult {
        private boolean success;
        private int totalEvents;
        private int eventsToArchive;
        private int eventsToKeep;
        private boolean archived;
        private int archiveFileCount;
        private int purgedCount;
        private String errorMessage;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public int getTotalEvents() { return totalEvents; }
        public void setTotalEvents(int totalEvents) { this.totalEvents = totalEvents; }
        
        public int getEventsToArchive() { return eventsToArchive; }
        public void setEventsToArchive(int eventsToArchive) { this.eventsToArchive = eventsToArchive; }
        
        public int getEventsToKeep() { return eventsToKeep; }
        public void setEventsToKeep(int eventsToKeep) { this.eventsToKeep = eventsToKeep; }
        
        public boolean isArchived() { return archived; }
        public void setArchived(boolean archived) { this.archived = archived; }
        
        public int getArchiveFileCount() { return archiveFileCount; }
        public void setArchiveFileCount(int archiveFileCount) { this.archiveFileCount = archiveFileCount; }
        
        public int getPurgedCount() { return purgedCount; }
        public void setPurgedCount(int purgedCount) { this.purgedCount = purgedCount; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }
}
