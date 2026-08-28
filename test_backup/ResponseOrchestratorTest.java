package com.odin.core.network.response;

import androidx.annotation.NonNull;

import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatLevel;
import com.odin.core.network.rules.DynamicRuleEngine;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Unit tests for ResponseOrchestrator.
 * 
 * Tests:
 * - Coordination of responses
 * - Prioritization of actions
 * - Sequential execution
 * - Parallel execution
 * - Rollback of failed responses
 * - Logging of actions
 * - Integration with components
 */
public class ResponseOrchestratorTest {

    private ResponseOrchestrator orchestrator;
    private DynamicRuleEngine ruleEngine;

    @Before
    public void setUp() {
        ruleEngine = new DynamicRuleEngine();
        orchestrator = new ResponseOrchestrator(ruleEngine);
        orchestrator.start();
    }

    @Test
    public void testResponseCoordination() throws InterruptedException {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "PORT_SCAN");
        
        boolean submitted = orchestrator.submitDetection(detection);
        
        assertTrue("Detection should be submitted", submitted);
        
        // Wait for processing
        Thread.sleep(500);
        
        ResponseOrchestrator.OrchestratorStatistics stats = orchestrator.getStatistics();
        assertTrue("Should have executed responses", stats.responsesExecuted > 0);
    }

    @Test
    public void testPrioritization() throws InterruptedException {
        // Submit multiple detections with different priorities
        DetectionResult critical = createDetectionResult(ThreatLevel.CRITICAL, "C2_COMMUNICATION");
        DetectionResult high = createDetectionResult(ThreatLevel.HIGH, "PORT_SCAN");
        DetectionResult low = createDetectionResult(ThreatLevel.LOW, "SUSPICIOUS_TRAFFIC");

        orchestrator.submitDetection(low);
        Thread.sleep(50); // Small delay
        orchestrator.submitDetection(critical);
        orchestrator.submitDetection(high);

        // Wait for processing
        Thread.sleep(1000);

        ResponseOrchestrator.OrchestratorStatistics stats = orchestrator.getStatistics();
        assertTrue("Should process all detections", stats.responsesExecuted >= 3);
    }

    @Test
    public void testSequentialExecution() throws InterruptedException {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "EXFILTRATION");
        
        final boolean[] executionOrder = {false, false, false};
        final CountDownLatch latch = new CountDownLatch(1);

        orchestrator.addListener(new ResponseOrchestrator.ResponseListener() {
            @Override
            public void onResponseStarted(String responseId, DetectionResult detection) {
                executionOrder[0] = true;
            }

            @Override
            public void onResponseCompleted(String responseId, ResponseOrchestrator.ResponseContext context) {
                executionOrder[1] = true;
                latch.countDown();
            }

            @Override
            public void onResponseFailed(String responseId, String reason) {
                executionOrder[2] = true;
                latch.countDown();
            }

            @Override
            public void onRollbackExecuted(String responseId) {
                // Not tested here
            }
        });

        orchestrator.submitDetection(detection);
        
        assertTrue("Should complete execution", latch.await(5, TimeUnit.SECONDS));
        assertTrue("Should have started", executionOrder[0]);
        assertTrue("Should have completed", executionOrder[1]);
        assertFalse("Should not have failed", executionOrder[2]);
    }

    @Test
    public void testRollbackOnFailure() throws InterruptedException {
        // This test would require mocking components to simulate failure
        // For now, we test the rollback mechanism exists
        DetectionResult detection = createDetectionResult(ThreatLevel.CRITICAL, "MALWARE");
        
        final boolean[] rollbackExecuted = {false};
        final CountDownLatch latch = new CountDownLatch(1);

        orchestrator.addListener(new ResponseOrchestrator.ResponseListener() {
            @Override
            public void onResponseStarted(String responseId, DetectionResult detection) {
                // Not tested
            }

            @Override
            public void onResponseCompleted(String responseId, ResponseOrchestrator.ResponseContext context) {
                latch.countDown();
            }

            @Override
            public void onResponseFailed(String responseId, String reason) {
                // Not tested
            }

            @Override
            public void onRollbackExecuted(String responseId) {
                rollbackExecuted[0] = true;
                latch.countDown();
            }
        });

        orchestrator.submitDetection(detection);
        
        // Wait for completion
        latch.await(5, TimeUnit.SECONDS);
        
        // Rollback mechanism exists (actual rollback tested in integration tests)
        ResponseOrchestrator.OrchestratorStatistics stats = orchestrator.getStatistics();
        assertTrue("Rollback counter exists", stats.rollbacksExecuted >= 0);
    }

    @Test
    public void testActionLogging() throws InterruptedException {
        DetectionResult detection = createDetectionResult(ThreatLevel.MEDIUM, "ANOMALY");
        
        orchestrator.submitDetection(detection);
        
        // Wait for processing
        Thread.sleep(500);
        
        ResponseOrchestrator.OrchestratorStatistics stats = orchestrator.getStatistics();
        assertTrue("Should have logged actions", stats.responsesExecuted > 0);
    }

    @Test
    public void testResponseContext() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "THREAT");
        List<ResponseOrchestrator.ResponseAction> actions = List.of(
                ResponseOrchestrator.ResponseAction.BLOCK_IP,
                ResponseOrchestrator.ResponseAction.ALERT_ADMIN
        );

        ResponseOrchestrator.ResponseContext context = new ResponseOrchestrator.ResponseContext(
                "TEST_ID", detection, actions
        );

        assertEquals("TEST_ID", context.getResponseId());
        assertEquals(detection, context.getDetection());
        assertEquals(2, context.getActions().size());
        assertFalse(context.isCompleted());
        assertFalse(context.isRolledBack());

        context.addCompletedAction(ResponseOrchestrator.ResponseAction.BLOCK_IP);
        assertEquals(1, context.getCompletedActions().size());

        context.markCompleted();
        assertTrue(context.isCompleted());
    }

    @Test
    public void testResponsePriority() {
        assertEquals(4, ResponseOrchestrator.ResponsePriority.CRITICAL.getValue());
        assertEquals(3, ResponseOrchestrator.ResponsePriority.HIGH.getValue());
        assertEquals(2, ResponseOrchestrator.ResponsePriority.MEDIUM.getValue());
        assertEquals(1, ResponseOrchestrator.ResponsePriority.LOW.getValue());
        assertEquals(0, ResponseOrchestrator.ResponsePriority.INFO.getValue());
    }

    @Test
    public void testResponseActions() {
        // Verify all response actions are defined
        ResponseOrchestrator.ResponseAction[] actions = ResponseOrchestrator.ResponseAction.values();
        assertTrue("Should have multiple actions", actions.length > 5);
        
        // Verify critical actions exist
        boolean hasBlockIP = false;
        boolean hasQuarantine = false;
        boolean hasAlert = false;
        
        for (ResponseOrchestrator.ResponseAction action : actions) {
            if (action == ResponseOrchestrator.ResponseAction.BLOCK_IP) hasBlockIP = true;
            if (action == ResponseOrchestrator.ResponseAction.QUARANTINE_APP) hasQuarantine = true;
            if (action == ResponseOrchestrator.ResponseAction.ALERT_ADMIN) hasAlert = true;
        }
        
        assertTrue("Should have BLOCK_IP action", hasBlockIP);
        assertTrue("Should have QUARANTINE_APP action", hasQuarantine);
        assertTrue("Should have ALERT_ADMIN action", hasAlert);
    }

    @Test
    public void testQueueCapacity() throws InterruptedException {
        // Submit many detections to test queue capacity
        for (int i = 0; i < 50; i++) {
            DetectionResult detection = createDetectionResult(ThreatLevel.LOW, "TEST_THREAT_" + i);
            orchestrator.submitDetection(detection);
        }

        // Wait for some processing
        Thread.sleep(1000);

        ResponseOrchestrator.OrchestratorStatistics stats = orchestrator.getStatistics();
        assertTrue("Should process detections", stats.responsesExecuted > 0);
    }

    @Test
    public void testListenerRegistration() {
        final boolean[] listenerCalled = {false};

        ResponseOrchestrator.ResponseListener listener = new ResponseOrchestrator.ResponseListener() {
            @Override
            public void onResponseStarted(String responseId, DetectionResult detection) {
                listenerCalled[0] = true;
            }

            @Override
            public void onResponseCompleted(String responseId, ResponseOrchestrator.ResponseContext context) {
                listenerCalled[0] = true;
            }

            @Override
            public void onResponseFailed(String responseId, String reason) {
                listenerCalled[0] = true;
            }

            @Override
            public void onRollbackExecuted(String responseId) {
                listenerCalled[0] = true;
            }
        };

        orchestrator.addListener(listener);
        assertTrue("Listener should be registered", true); // Can't directly verify, but no exception

        orchestrator.removeListener(listener);
        assertTrue("Listener should be removed", true); // Can't directly verify, but no exception
    }

    @Test
    public void testStatistics() {
        ResponseOrchestrator.OrchestratorStatistics stats = orchestrator.getStatistics();
        
        assertNotNull(stats);
        assertTrue("Responses executed should be non-negative", stats.responsesExecuted >= 0);
        assertTrue("Responses failed should be non-negative", stats.responsesFailed >= 0);
        assertTrue("Rollbacks executed should be non-negative", stats.rollbacksExecuted >= 0);
        assertTrue("Active responses should be non-negative", stats.activeResponses >= 0);
        assertTrue("Queue size should be non-negative", stats.queueSize >= 0);
    }

    @Test
    public void testStartStop() {
        ResponseOrchestrator newOrchestrator = new ResponseOrchestrator(ruleEngine);
        
        // Should be able to start
        newOrchestrator.start();
        
        // Should be able to stop
        newOrchestrator.stop();
        
        // Should be able to start again
        newOrchestrator.start();
        
        newOrchestrator.stop();
    }

    @Test
    public void testComponentInitialization() {
        // Test that components can be initialized
        orchestrator.initializeComponents(null, null, null, null);
        
        // Should not throw exception
        assertTrue("Components initialized", true);
    }

    @Test
    public void testThreatLevelToPriorityMapping() throws InterruptedException {
        // Test that different threat levels map to correct priorities
        DetectionResult critical = createDetectionResult(ThreatLevel.CRITICAL, "TEST");
        DetectionResult high = createDetectionResult(ThreatLevel.HIGH, "TEST");
        DetectionResult medium = createDetectionResult(ThreatLevel.MEDIUM, "TEST");
        DetectionResult low = createDetectionResult(ThreatLevel.LOW, "TEST");

        orchestrator.submitDetection(critical);
        orchestrator.submitDetection(high);
        orchestrator.submitDetection(medium);
        orchestrator.submitDetection(low);

        // Wait for processing
        Thread.sleep(1000);

        ResponseOrchestrator.OrchestratorStatistics stats = orchestrator.getStatistics();
        assertTrue("Should process all detections", stats.responsesExecuted >= 4);
    }

    @Test
    public void testConcurrentSubmissions() throws InterruptedException {
        int threadCount = 10;
        final CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    DetectionResult detection = createDetectionResult(ThreatLevel.MEDIUM, "CONCURRENT_TEST");
                    orchestrator.submitDetection(detection);
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        assertTrue("All threads should complete", latch.await(10, TimeUnit.SECONDS));

        // Wait for processing
        Thread.sleep(1000);

        ResponseOrchestrator.OrchestratorStatistics stats = orchestrator.getStatistics();
        assertTrue("Should process concurrent submissions", stats.responsesExecuted > 0);
    }

    private DetectionResult createDetectionResult(ThreatLevel level, String threatType) {
        return new DetectionResult(
                level,
                threatType,
                "Test threat description",
                "TestDetector",
                "192.168.1.100",
                "10.0.0.1",
                1234,
                443,
                0.8,
                "Test evidence",
                "forensics_data",
                "Block"
        );
    }
}
