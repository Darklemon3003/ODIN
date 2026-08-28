package com.odin.core.network.response;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * RateLimiter: Limitador de velocidad para prevenir ataques DoS/DDoS.
 * 
 * Responsabilidades:
 * - Rate limiting por IP, puerto, aplicación
 * - Algoritmos: Token Bucket, Leaky Bucket
 * - Configuración de límites dinámicos
 * - Detección de ataques de DoS/DDoS
 * - Bloqueo temporal de violadores
 * - Estadísticas de rate limiting
 * - Integración con firewall
 * 
 * Performance: <10ms por decisión
 * Thread-safe: Concurrent collections + locks
 */
public final class RateLimiter {

    private static final String TAG = "RateLimiter";
    private static final int CLEANUP_INTERVAL_MS = 60_000; // 1 minuto
    private static final int MAX_TRACKED_ENTITIES = 10_000;
    private static final long DEFAULT_BLOCK_DURATION_MS = 300_000; // 5 minutos

    // Algoritmos de rate limiting
    private final ConcurrentHashMap<String, TokenBucket> tokenBuckets;
    private final ConcurrentHashMap<String, LeakyBucket> leakyBuckets;
    private final ConcurrentHashMap<String, BlockedEntity> blockedEntities;
    
    // Estadísticas
    private final AtomicLong requestsAllowed;
    private final AtomicLong requestsDenied;
    private final AtomicLong blocksIssued;
    private final AtomicLong dosAttacksDetected;
    
    // Cleanup
    private final ScheduledExecutorService cleanupExecutor;
    private final AtomicBoolean isRunning;

    /**
     * Algoritmo Token Bucket.
     */
    private static final class TokenBucket {
        private final long capacity;
        private final long refillRate; // tokens por segundo
        private volatile long tokens;
        private volatile long lastRefillTime;
        private final ReentrantLock lock;

        public TokenBucket(long capacity, long refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.tokens = capacity;
            this.lastRefillTime = System.currentTimeMillis();
            this.lock = new ReentrantLock();
        }

        public boolean tryConsume(int tokensRequested) {
            lock.lock();
            try {
                refill();
                if (tokens >= tokensRequested) {
                    tokens -= tokensRequested;
                    return true;
                }
                return false;
            } finally {
                lock.unlock();
            }
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRefillTime;
            
            if (elapsed > 0) {
                long tokensToAdd = (elapsed * refillRate) / 1000;
                tokens = Math.min(capacity, tokens + tokensToAdd);
                lastRefillTime = now;
            }
        }

        public long getAvailableTokens() {
            lock.lock();
            try {
                refill();
                return tokens;
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Algoritmo Leaky Bucket.
     */
    private static final class LeakyBucket {
        private final long capacity;
        private final long leakRate; // bytes por segundo
        private volatile long currentSize;
        private volatile long lastLeakTime;
        private final ReentrantLock lock;

        public LeakyBucket(long capacity, long leakRate) {
            this.capacity = capacity;
            this.leakRate = leakRate;
            this.currentSize = 0;
            this.lastLeakTime = System.currentTimeMillis();
            this.lock = new ReentrantLock();
        }

        public boolean tryAdd(long size) {
            lock.lock();
            try {
                leak();
                if (currentSize + size <= capacity) {
                    currentSize += size;
                    return true;
                }
                return false;
            } finally {
                lock.unlock();
            }
        }

        private void leak() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastLeakTime;
            
            if (elapsed > 0) {
                long leaked = (elapsed * leakRate) / 1000;
                currentSize = Math.max(0, currentSize - leaked);
                lastLeakTime = now;
            }
        }

        public long getCurrentSize() {
            lock.lock();
            try {
                leak();
                return currentSize;
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Entidad bloqueada temporalmente.
     */
    private static final class BlockedEntity {
        private final String entityId;
        private final long blockTime;
        private final long expirationTime;
        private final String reason;
        private volatile int violationCount;

        public BlockedEntity(String entityId, long durationMs, String reason) {
            this.entityId = entityId;
            this.blockTime = System.currentTimeMillis();
            this.expirationTime = blockTime + durationMs;
            this.reason = reason;
            this.violationCount = 1;
        }

        public boolean isBlocked() {
            return System.currentTimeMillis() < expirationTime;
        }

        public void incrementViolations() {
            violationCount++;
        }

        public String getEntityId() { return entityId; }
        public long getBlockTime() { return blockTime; }
        public long getExpirationTime() { return expirationTime; }
        public String getReason() { return reason; }
        public int getViolationCount() { return violationCount; }
    }

    /**
     * Constructor.
     */
    public RateLimiter() {
        this.tokenBuckets = new ConcurrentHashMap<>();
        this.leakyBuckets = new ConcurrentHashMap<>();
        this.blockedEntities = new ConcurrentHashMap<>();
        this.requestsAllowed = new AtomicLong(0);
        this.requestsDenied = new AtomicLong(0);
        this.blocksIssued = new AtomicLong(0);
        this.dosAttacksDetected = new AtomicLong(0);
        this.isRunning = new AtomicBoolean(false);

        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "RateLimiterCleanup");
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Inicia el rate limiter.
     */
    public void start() {
        if (!isRunning.compareAndSet(false, true)) {
            return; // Ya está corriendo
        }

        cleanupExecutor.scheduleAtFixedRate(
                this::cleanup,
                CLEANUP_INTERVAL_MS,
                CLEANUP_INTERVAL_MS,
                TimeUnit.MILLISECONDS
        );

        Log.i(TAG, "RateLimiter iniciado");
    }

    /**
     * Detiene el rate limiter.
     */
    public void stop() {
        if (!isRunning.compareAndSet(true, false)) {
            return; // Ya está detenido
        }

        cleanupExecutor.shutdown();
        try {
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        Log.i(TAG, "RateLimiter detenido");
    }

    /**
     * Verifica si una IP puede hacer una solicitud (Token Bucket).
     * 
     * @param ip Dirección IP
     * @param requestsPerSecond Límite de solicitudes por segundo
     * @param burstSize Tamaño del burst
     * @return true si se permite la solicitud
     */
    public boolean allowRequest(@NonNull String ip, long requestsPerSecond, long burstSize) {
        long startTime = System.nanoTime();

        try {
            // Verificar si está bloqueado
            if (isBlocked(ip)) {
                requestsDenied.incrementAndGet();
                return false;
            }

            // Obtener o crear bucket
            TokenBucket bucket = tokenBuckets.computeIfAbsent(
                    ip,
                    k -> new TokenBucket(burstSize, requestsPerSecond)
            );

            boolean allowed = bucket.tryConsume(1);

            if (allowed) {
                requestsAllowed.incrementAndGet();
            } else {
                requestsDenied.incrementAndGet();
                handleViolation(ip, "Rate limit exceeded");
            }

            long duration = System.nanoTime() - startTime;
            if (duration > 10_000_000) { // 10ms
                Log.w(TAG, "allowRequest tomó más de 10ms: " + (duration / 1_000_000) + "ms");
            }

            return allowed;

        } catch (Exception e) {
            Log.e(TAG, "Error en allowRequest", e);
            return false;
        }
    }

    /**
     * Verifica si una IP puede hacer una solicitud (límites por defecto).
     */
    public boolean allowRequest(@NonNull String ip) {
        return allowRequest(ip, 100, 10); // 100 req/s, burst de 10
    }

    /**
     * Limita una IP específica.
     * 
     * @param ip Dirección IP
     * @param requestsPerSecond Límite de solicitudes por segundo
     * @param durationSeconds Duración del límite
     */
    public void limitIP(@NonNull String ip, long requestsPerSecond, long durationSeconds) {
        TokenBucket bucket = new TokenBucket(requestsPerSecond * 2, requestsPerSecond);
        tokenBuckets.put(ip, bucket);
        
        // Programar remoción después de la duración
        cleanupExecutor.schedule(() -> {
            tokenBuckets.remove(ip);
        }, durationSeconds, TimeUnit.SECONDS);
    }

    /**
     * Remueve el límite de una IP.
     */
    public void unlimitIP(@NonNull String ip) {
        tokenBuckets.remove(ip);
        blockedEntities.remove(ip);
    }

    /**
     * Verifica si una IP puede enviar datos (Leaky Bucket).
     * 
     * @param ip Dirección IP
     * @param bytes Tamaño de datos
     * @param bytesPerSecond Límite de bytes por segundo
     * @param capacity Capacidad del bucket
     * @return true si se permite el envío
     */
    public boolean allowDataTransfer(@NonNull String ip, long bytes, long bytesPerSecond, long capacity) {
        long startTime = System.nanoTime();

        try {
            // Verificar si está bloqueado
            if (isBlocked(ip)) {
                requestsDenied.incrementAndGet();
                return false;
            }

            // Obtener o crear bucket
            LeakyBucket bucket = leakyBuckets.computeIfAbsent(
                    ip,
                    k -> new LeakyBucket(capacity, bytesPerSecond)
            );

            boolean allowed = bucket.tryAdd(bytes);

            if (allowed) {
                requestsAllowed.incrementAndGet();
            } else {
                requestsDenied.incrementAndGet();
                handleViolation(ip, "Data rate limit exceeded");
            }

            long duration = System.nanoTime() - startTime;
            if (duration > 10_000_000) { // 10ms
                Log.w(TAG, "allowDataTransfer tomó más de 10ms: " + (duration / 1_000_000) + "ms");
            }

            return allowed;

        } catch (Exception e) {
            Log.e(TAG, "Error en allowDataTransfer", e);
            return false;
        }
    }

    /**
     * Verifica si una aplicación puede hacer solicitudes.
     * 
     * @param packageName Nombre del paquete
     * @param requestsPerSecond Límite de solicitudes por segundo
     * @return true si se permite
     */
    public boolean allowAppRequest(@NonNull String packageName, long requestsPerSecond) {
        return allowRequest("app:" + packageName, requestsPerSecond, requestsPerSecond);
    }

    /**
     * Verifica si un puerto puede recibir conexiones.
     * 
     * @param port Puerto
     * @param connectionsPerSecond Límite de conexiones por segundo
     * @return true si se permite
     */
    public boolean allowPortConnection(int port, long connectionsPerSecond) {
        return allowRequest("port:" + port, connectionsPerSecond, connectionsPerSecond);
    }

    /**
     * Verifica si una entidad está bloqueada.
     */
    private boolean isBlocked(@NonNull String entityId) {
        BlockedEntity blocked = blockedEntities.get(entityId);
        if (blocked != null) {
            if (blocked.isBlocked()) {
                return true;
            } else {
                // Expiró, remover
                blockedEntities.remove(entityId);
            }
        }
        return false;
    }

    /**
     * Maneja una violación de rate limit.
     */
    private void handleViolation(@NonNull String entityId, @NonNull String reason) {
        BlockedEntity existing = blockedEntities.get(entityId);
        
        if (existing != null && existing.isBlocked()) {
            existing.incrementViolations();
            
            // Si hay muchas violaciones, extender bloqueo
            if (existing.getViolationCount() > 10) {
                Log.w(TAG, "Muchas violaciones para: " + entityId + ", posible DoS");
                dosAttacksDetected.incrementAndGet();
            }
        } else {
            // Bloquear temporalmente
            BlockedEntity blocked = new BlockedEntity(entityId, DEFAULT_BLOCK_DURATION_MS, reason);
            blockedEntities.put(entityId, blocked);
            blocksIssued.incrementAndGet();
            
            Log.w(TAG, "Entidad bloqueada temporalmente: " + entityId);
        }
    }

    /**
     * Bloquea una entidad explícitamente.
     */
    public void blockEntity(@NonNull String entityId, long durationMs, @NonNull String reason) {
        BlockedEntity blocked = new BlockedEntity(entityId, durationMs, reason);
        blockedEntities.put(entityId, blocked);
        blocksIssued.incrementAndGet();
        
        Log.i(TAG, "Entidad bloqueada: " + entityId + " por " + reason);
    }

    /**
     * Desbloquea una entidad.
     */
    public void unblockEntity(@NonNull String entityId) {
        blockedEntities.remove(entityId);
        Log.i(TAG, "Entidad desbloqueada: " + entityId);
    }

    /**
     * Limpia buckets y bloqueos expirados.
     */
    private void cleanup() {
        long now = System.currentTimeMillis();

        // Limpiar buckets inactivos
        tokenBuckets.entrySet().removeIf(entry -> {
            TokenBucket bucket = entry.getValue();
            return bucket.getAvailableTokens() == bucket.capacity; // Bucket lleno = inactivo
        });

        leakyBuckets.entrySet().removeIf(entry -> {
            LeakyBucket bucket = entry.getValue();
            return bucket.getCurrentSize() == 0; // Bucket vacío = inactivo
        });

        // Limpiar bloqueos expirados
        blockedEntities.entrySet().removeIf(entry -> {
            BlockedEntity blocked = entry.getValue();
            return !blocked.isBlocked();
        });

        // Limitar tamaño de mapas
        if (tokenBuckets.size() > MAX_TRACKED_ENTITIES) {
            tokenBuckets.clear();
        }
        if (leakyBuckets.size() > MAX_TRACKED_ENTITIES) {
            leakyBuckets.clear();
        }
        if (blockedEntities.size() > MAX_TRACKED_ENTITIES) {
            blockedEntities.clear();
        }

        Log.d(TAG, "Cleanup completado. Buckets: " + tokenBuckets.size() + 
                ", Leaky: " + leakyBuckets.size() + 
                ", Blocked: " + blockedEntities.size());
    }

    /**
     * Detecta posible ataque DoS basado en patrones.
     * 
     * @param ip Dirección IP
     * @return true si se detecta posible DoS
     */
    public boolean detectDoSAttack(@NonNull String ip) {
        BlockedEntity blocked = blockedEntities.get(ip);
        if (blocked != null && blocked.getViolationCount() > 20) {
            return true;
        }
        return false;
    }

    /**
     * Obtiene estadísticas del rate limiter.
     */
    public RateLimiterStatistics getStatistics() {
        RateLimiterStatistics stats = new RateLimiterStatistics();
        stats.requestsAllowed = requestsAllowed.get();
        stats.requestsDenied = requestsDenied.get();
        stats.blocksIssued = blocksIssued.get();
        stats.dosAttacksDetected = dosAttacksDetected.get();
        stats.activeTokenBuckets = tokenBuckets.size();
        stats.activeLeakyBuckets = leakyBuckets.size();
        stats.blockedEntities = blockedEntities.size();
        return stats;
    }

    /**
     * Estadísticas del rate limiter.
     */
    public static final class RateLimiterStatistics {
        public long requestsAllowed;
        public long requestsDenied;
        public long blocksIssued;
        public long dosAttacksDetected;
        public int activeTokenBuckets;
        public int activeLeakyBuckets;
        public int blockedEntities;

        @Override
        public String toString() {
            return String.format(
                    "RateLimiter{allowed=%d, denied=%d, blocks=%d, dos=%d, tokenBuckets=%d, leakyBuckets=%d, blocked=%d}",
                    requestsAllowed, requestsDenied, blocksIssued, dosAttacksDetected,
                    activeTokenBuckets, activeLeakyBuckets, blockedEntities
            );
        }
    }

    /**
     * Método especial para ResponseOrchestrator - verifica si se permite una respuesta.
     */
    public boolean allowResponse(@NonNull String sourceIp) {
        return allowRequest(sourceIp, 50, 5); // Límite más estricto para respuestas
    }
}
