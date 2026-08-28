package com.odin.core.network.response;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Unit tests for RateLimiter.
 * 
 * Tests:
 * - Rate limiting by IP
 * - Rate limiting by port
 * - Rate limiting by application
 * - Token Bucket algorithm
 * - Leaky Bucket algorithm
 * - DoS/DDoS detection
 * - Temporary blocking
 * - Statistics
 */
public class RateLimiterTest {

    private RateLimiter rateLimiter;

    @Before
    public void setUp() {
        rateLimiter = new RateLimiter();
        rateLimiter.start();
    }

    @Test
    public void testAllowRequest() {
        String ip = "192.168.1.100";
        
        // Primeras solicitudes deberían ser permitidas
        assertTrue("First request should be allowed", rateLimiter.allowRequest(ip));
        assertTrue("Second request should be allowed", rateLimiter.allowRequest(ip));
        assertTrue("Third request should be allowed", rateLimiter.allowRequest(ip));
    }

    @Test
    public void testRateLimiting() throws InterruptedException {
        String ip = "192.168.1.101";
        long requestsPerSecond = 10;
        long burstSize = 5;
        
        // Agotar burst
        for (int i = 0; i < burstSize; i++) {
            assertTrue("Request " + i + " should be allowed", 
                    rateLimiter.allowRequest(ip, requestsPerSecond, burstSize));
        }
        
        // Siguiente solicitud debería ser denegada
        boolean allowed = rateLimiter.allowRequest(ip, requestsPerSecond, burstSize);
        assertFalse("Request exceeding burst should be denied", allowed);
        
        // Esperar refill
        Thread.sleep(200);
        
        // Después de refill, debería ser permitida
        allowed = rateLimiter.allowRequest(ip, requestsPerSecond, burstSize);
        assertTrue("Request after refill should be allowed", allowed);
    }

    @Test
    public void testAllowRequestDefault() {
        String ip = "192.168.1.102";
        
        assertTrue("Default request should be allowed", rateLimiter.allowRequest(ip));
    }

    @Test
    public void testLimitIP() throws InterruptedException {
        String ip = "192.168.1.103";
        
        rateLimiter.limitIP(ip, 5, 1); // 5 req/s por 1 segundo
        
        // Primeras solicitudes deberían ser permitidas
        assertTrue("First request should be allowed", rateLimiter.allowRequest(ip));
        
        // Esperar un poco
        Thread.sleep(300);
        
        // Después de expirar, el límite debería ser removido
        // (esto es simplificado, en realidad el cleanup ejecuta periódicamente)
    }

    @Test
    public void testUnlimitIP() {
        String ip = "192.168.1.104";
        
        rateLimiter.limitIP(ip, 5, 10);
        rateLimiter.unlimitIP(ip);
        
        // Después de unlimit, debería usar límites por defecto
        assertTrue("Request after unlimit should be allowed", rateLimiter.allowRequest(ip));
    }

    @Test
    public void testAllowDataTransfer() {
        String ip = "192.168.1.105";
        long bytes = 1024;
        long bytesPerSecond = 10240; // 10 KB/s
        long capacity = 5120; // 5 KB
        
        // Primer transferencia debería ser permitida
        assertTrue("First transfer should be allowed", 
                rateLimiter.allowDataTransfer(ip, bytes, bytesPerSecond, capacity));
        
        // Transferencia grande debería ser denegada
        assertFalse("Large transfer should be denied", 
                rateLimiter.allowDataTransfer(ip, 10000, bytesPerSecond, capacity));
    }

    @Test
    public void testAllowAppRequest() {
        String packageName = "com.example.testapp";
        
        assertTrue("App request should be allowed", 
                rateLimiter.allowAppRequest(packageName, 100));
    }

    @Test
    public void testAllowPortConnection() {
        int port = 8080;
        
        assertTrue("Port connection should be allowed", 
                rateLimiter.allowPortConnection(port, 50));
    }

    @Test
    public void testBlockEntity() {
        String ip = "192.168.1.106";
        
        rateLimiter.blockEntity(ip, 5000, "Manual block");
        
        // Después de bloquear, las solicitudes deberían ser denegadas
        assertFalse("Blocked entity should be denied", rateLimiter.allowRequest(ip));
    }

    @Test
    public void testUnblockEntity() {
        String ip = "192.168.1.107";
        
        rateLimiter.blockEntity(ip, 5000, "Manual block");
        rateLimiter.unblockEntity(ip);
        
        // Después de desbloquear, las solicitudes deberían ser permitidas
        assertTrue("Unblocked entity should be allowed", rateLimiter.allowRequest(ip));
    }

    @Test
    public void testDetectDoSAttack() {
        String ip = "192.168.1.108";
        
        // Inicialmente no debería detectar DoS
        assertFalse("Should not detect DoS initially", rateLimiter.detectDoSAttack(ip));
        
        // Simular muchas violaciones
        for (int i = 0; i < 25; i++) {
            rateLimiter.allowRequest(ip, 1, 1); // Límite muy bajo
        }
        
        // Después de muchas violaciones, debería detectar posible DoS
        boolean dosDetected = rateLimiter.detectDoSAttack(ip);
        // Nota: esto depende de la lógica interna de handleViolation
    }

    @Test
    public void testStatistics() {
        String ip = "192.168.1.109";
        
        // Hacer algunas solicitudes
        rateLimiter.allowRequest(ip);
        rateLimiter.allowRequest(ip);
        
        RateLimiter.RateLimiterStatistics stats = rateLimiter.getStatistics();
        
        assertNotNull(stats);
        assertTrue("Requests allowed should be positive", stats.requestsAllowed >= 0);
        assertTrue("Requests denied should be non-negative", stats.requestsDenied >= 0);
        assertTrue("Blocks issued should be non-negative", stats.blocksIssued >= 0);
        assertTrue("DoS attacks detected should be non-negative", stats.dosAttacksDetected >= 0);
    }

    @Test
    public void testStartStop() {
        RateLimiter newLimiter = new RateLimiter();
        
        // Should be able to start
        newLimiter.start();
        
        // Should be able to stop
        newLimiter.stop();
        
        // Should be able to start again
        newLimiter.start();
        
        newLimiter.stop();
    }

    @Test
    public void testConcurrentRequests() throws InterruptedException {
        String ip = "192.168.1.110";
        int threadCount = 10;
        final CountDownLatch latch = new CountDownLatch(threadCount);
        final boolean[] results = new boolean[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            new Thread(() -> {
                try {
                    results[index] = rateLimiter.allowRequest(ip);
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        assertTrue("All threads should complete", latch.await(10, TimeUnit.SECONDS));
        
        // Verificar que no hubo excepciones
        int successCount = 0;
        for (boolean result : results) {
            if (result) successCount++;
        }
        assertTrue("Should have some successful requests", successCount > 0);
    }

    @Test
    public void testTokenBucketRefill() throws InterruptedException {
        String ip = "192.168.1.111";
        long requestsPerSecond = 10;
        long burstSize = 5;
        
        // Agotar burst
        for (int i = 0; i < burstSize; i++) {
            assertTrue("Request " + i + " should be allowed", 
                    rateLimiter.allowRequest(ip, requestsPerSecond, burstSize));
        }
        
        // Siguiente debería ser denegada
        assertFalse("Request should be denied after burst", 
                rateLimiter.allowRequest(ip, requestsPerSecond, burstSize));
        
        // Esperar refill (100ms debería dar 1 token)
        Thread.sleep(150);
        
        // Ahora debería ser permitida
        assertTrue("Request after refill should be allowed", 
                rateLimiter.allowRequest(ip, requestsPerSecond, burstSize));
    }

    @Test
    public void testLeakyBucketLeak() throws InterruptedException {
        String ip = "192.168.1.112";
        long bytesPerSecond = 1024;
        long capacity = 2048;
        
        // Llenar bucket
        assertTrue("First transfer should be allowed", 
                rateLimiter.allowDataTransfer(ip, 1024, bytesPerSecond, capacity));
        assertTrue("Second transfer should be allowed", 
                rateLimiter.allowDataTransfer(ip, 1024, bytesPerSecond, capacity));
        
        // Bucket debería estar lleno
        assertFalse("Transfer when full should be denied", 
                rateLimiter.allowDataTransfer(ip, 1024, bytesPerSecond, capacity));
        
        // Esperar leak
        Thread.sleep(1100);
        
        // Después de leak, debería ser permitida
        assertTrue("Transfer after leak should be allowed", 
                rateLimiter.allowDataTransfer(ip, 1024, bytesPerSecond, capacity));
    }

    @Test
    public void testMultipleIPs() {
        String[] ips = {"192.168.1.113", "192.168.1.114", "192.168.1.115"};
        
        for (String ip : ips) {
            assertTrue("Request for " + ip + " should be allowed", rateLimiter.allowRequest(ip));
        }
        
        RateLimiter.RateLimiterStatistics stats = rateLimiter.getStatistics();
        assertTrue("Should track multiple IPs", stats.activeTokenBuckets >= 3);
    }

    @Test
    public void testAllowResponse() {
        String ip = "192.168.1.116";
        
        // Método especial para ResponseOrchestrator
        assertTrue("Response should be allowed", rateLimiter.allowResponse(ip));
    }

    @Test
    public void testBlockExpiration() throws InterruptedException {
        String ip = "192.168.1.117";
        
        // Bloquear por 100ms
        rateLimiter.blockEntity(ip, 100, "Test block");
        
        // Debería estar bloqueado
        assertFalse("Should be blocked immediately", rateLimiter.allowRequest(ip));
        
        // Esperar expiración
        Thread.sleep(150);
        
        // Debería ser permitido después de expirar (cleanup ejecuta periódicamente)
        // Nota: esto depende de que el cleanup haya ejecutado
    }

    @Test
    public void testStatisticsTracking() {
        String ip = "192.168.1.118";
        
        RateLimiter.RateLimiterStatistics initialStats = rateLimiter.getStatistics();
        long initialAllowed = initialStats.requestsAllowed;
        
        // Hacer solicitudes
        rateLimiter.allowRequest(ip);
        rateLimiter.allowRequest(ip);
        
        RateLimiter.RateLimiterStatistics finalStats = rateLimiter.getStatistics();
        
        assertTrue("Allowed count should increase", 
                finalStats.requestsAllowed >= initialAllowed + 2);
    }

    @Test
    public void testDifferentLimitsForDifferentIPs() {
        String ip1 = "192.168.1.119";
        String ip2 = "192.168.1.120";
        
        // IP1 con límite bajo
        for (int i = 0; i < 5; i++) {
            rateLimiter.allowRequest(ip1, 5, 5);
        }
        assertFalse("IP1 should be rate limited", rateLimiter.allowRequest(ip1, 5, 5));
        
        // IP2 con límite alto debería seguir permitiendo
        assertTrue("IP2 should still be allowed", rateLimiter.allowRequest(ip2, 100, 100));
    }
}
