package com.odin.core.network.rules;

import androidx.annotation.NonNull;

import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatLevel;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Unit tests for DynamicRuleEngine.
 * 
 * Tests:
 * - Rule evaluation <1ms
 * - Auto-generation of rules
 * - Expiration of rules
 * - Conflict resolution
 * - Thread-safety
 * - Max rules limit (10,000)
 * - Integration with DetectionResult
 */
public class DynamicRuleEngineTest {

    private DynamicRuleEngine engine;

    @Before
    public void setUp() {
        engine = new DynamicRuleEngine();
    }

    @Test
    public void testRuleEvaluationPerformance() {
        // Create a rule
        DynamicRuleEngine.FirewallRule rule = new DynamicRuleEngine.FirewallRule(
                "Test Rule",
                DynamicRuleEngine.RulePriority.HIGH,
                DynamicRuleEngine.RuleAction.BLOCK,
                "192.168.1.*",
                "*",
                80,
                null,
                null,
                "MALICIOUS.*"
        );
        engine.addRule(rule);

        // Measure evaluation time
        long startTime = System.nanoTime();
        DynamicRuleEngine.FirewallRule result = engine.evaluatePacket(
                "192.168.1.100",
                "10.0.0.1",
                80,
                443,
                6,
                "MALICIOUS_PAYLOAD"
        );
        long duration = System.nanoTime() - startTime;

        assertNotNull(result);
        assertTrue("Evaluation should be <1ms", duration < 1_000_000); // 1ms in nanoseconds
    }

    @Test
    public void testAutoGenerateRule() {
        DetectionResult detection = new DetectionResult(
                ThreatLevel.HIGH,
                "PORT_SCAN",
                "Port scanning detected",
                "PortScanDetector",
                "192.168.1.100",
                "10.0.0.1",
                1234,
                80,
                0.85,
                "Multiple ports scanned",
                "forensics_data",
                "Block IP"
        );

        DynamicRuleEngine.FirewallRule rule = engine.autoGenerateRule(detection);

        assertNotNull(rule);
        assertEquals("Auto-PORT_SCAN", rule.getName());
        assertEquals(DynamicRuleEngine.RuleAction.BLOCK, rule.getAction());
        assertTrue(rule.hasExpiration());
    }

    @Test
    public void testRuleExpiration() throws InterruptedException {
        DynamicRuleEngine.FirewallRule rule = new DynamicRuleEngine.FirewallRule(
                "Expiring Rule",
                DynamicRuleEngine.RulePriority.MEDIUM,
                DynamicRuleEngine.RuleAction.BLOCK,
                "*",
                "*",
                null,
                null,
                null,
                "TEST.*"
        );
        
        // Set expiration to 100ms
        rule.setExpiration(100);
        engine.addRule(rule);

        // Rule should be active initially
        List<DynamicRuleEngine.FirewallRule> rules = engine.getActiveRules();
        assertEquals(1, rules.size());

        // Wait for expiration + cleanup interval
        Thread.sleep(200);

        // Trigger cleanup
        engine.getActiveRules();

        // Rule should be expired after cleanup
        rules = engine.getActiveRules();
        assertTrue("Rule should be expired and removed", rules.isEmpty());
    }

    @Test
    public void testConflictResolution() {
        // Create two conflicting rules with same priority
        DynamicRuleEngine.FirewallRule rule1 = new DynamicRuleEngine.FirewallRule(
                "Rule 1 - BLOCK",
                DynamicRuleEngine.RulePriority.HIGH,
                DynamicRuleEngine.RuleAction.BLOCK,
                "192.168.1.*",
                "*",
                80,
                null,
                null,
                "TEST.*"
        );

        DynamicRuleEngine.FirewallRule rule2 = new DynamicRuleEngine.FirewallRule(
                "Rule 2 - ALLOW",
                DynamicRuleEngine.RulePriority.HIGH,
                DynamicRuleEngine.RuleAction.ALLOW,
                "192.168.1.*",
                "*",
                80,
                null,
                null,
                "TEST.*"
        );

        engine.addRule(rule1);
        engine.addRule(rule2);

        // Check conflicts
        List<DynamicRuleEngine.FirewallRule> conflicts = engine.checkConflicts(rule2);
        assertFalse("Should detect conflicts", conflicts.isEmpty());

        // Evaluate - should return BLOCK (preferred over ALLOW)
        DynamicRuleEngine.FirewallRule result = engine.evaluatePacket(
                "192.168.1.100",
                "10.0.0.1",
                80,
                443,
                6,
                "TEST_PAYLOAD"
        );

        assertNotNull(result);
        assertEquals("BLOCK should be preferred", DynamicRuleEngine.RuleAction.BLOCK, result.getAction());
    }

    @Test
    public void testThreadSafety() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 100; j++) {
                        DynamicRuleEngine.FirewallRule rule = new DynamicRuleEngine.FirewallRule(
                                "Thread-" + threadId + "-Rule-" + j,
                                DynamicRuleEngine.RulePriority.MEDIUM,
                                DynamicRuleEngine.RuleAction.BLOCK,
                                "192.168." + threadId + ".*",
                                "*",
                                null,
                                null,
                                null,
                                "TEST.*"
                        );
                        engine.addRule(rule);
                        
                        engine.evaluatePacket(
                                "192.168." + threadId + ".1",
                                "10.0.0.1",
                                80,
                                443,
                                6,
                                "TEST_PAYLOAD"
                        );
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue("Threads should complete", latch.await(30, TimeUnit.SECONDS));
        executor.shutdown();

        // Verify no exceptions and rules were added
        assertTrue("Should have rules added", engine.getActiveRules().size() > 0);
    }

    @Test
    public void testMaxRulesLimit() {
        // Add rules up to limit
        for (int i = 0; i < DynamicRuleEngine.MAX_RULES + 10; i++) {
            DynamicRuleEngine.FirewallRule rule = new DynamicRuleEngine.FirewallRule(
                    "Rule-" + i,
                    DynamicRuleEngine.RulePriority.LOW,
                    DynamicRuleEngine.RuleAction.ALLOW,
                    "10.0." + (i / 255) + "." + (i % 255),
                    "*",
                    null,
                    null,
                    null,
                    "TEST.*"
            );
            engine.addRule(rule);
        }

        // Should not exceed max
        assertTrue("Should not exceed max rules", engine.getActiveRules().size() <= DynamicRuleEngine.MAX_RULES);
    }

    @Test
    public void testEvaluateDetection() {
        DetectionResult detection = new DetectionResult(
                ThreatLevel.CRITICAL,
                "C2_COMMUNICATION",
                "Command and control detected",
                "C2Detector",
                "192.168.1.100",
                "10.0.0.1",
                443,
                8080,
                0.95,
                "Suspicious C2 beacon",
                "forensics_data",
                "Block immediately"
        );

        DynamicRuleEngine.RuleAction action = engine.evaluateDetection(detection);

        // Default action for CRITICAL should be BLOCK
        assertEquals(DynamicRuleEngine.RuleAction.BLOCK, action);
    }

    @Test
    public void testRulePriorityOrdering() {
        DynamicRuleEngine.FirewallRule lowRule = new DynamicRuleEngine.FirewallRule(
                "Low Priority",
                DynamicRuleEngine.RulePriority.LOW,
                DynamicRuleEngine.RuleAction.ALLOW,
                "*",
                "*",
                null,
                null,
                null,
                "TEST.*"
        );

        DynamicRuleEngine.FirewallRule highRule = new DynamicRuleEngine.FirewallRule(
                "High Priority",
                DynamicRuleEngine.RulePriority.HIGH,
                DynamicRuleEngine.RuleAction.BLOCK,
                "*",
                "*",
                null,
                null,
                null,
                "TEST.*"
        );

        engine.addRule(lowRule);
        engine.addRule(highRule);

        // High priority should be evaluated first
        DynamicRuleEngine.FirewallRule result = engine.evaluatePacket(
                "192.168.1.100",
                "10.0.0.1",
                80,
                443,
                6,
                "TEST_PAYLOAD"
        );

        assertNotNull(result);
        assertEquals("High priority rule should match", "High Priority", result.getName());
    }

    @Test
    public void testStatistics() {
        // Add some rules and evaluate
        DynamicRuleEngine.FirewallRule rule = new DynamicRuleEngine.FirewallRule(
                "Stats Rule",
                DynamicRuleEngine.RulePriority.MEDIUM,
                DynamicRuleEngine.RuleAction.BLOCK,
                "192.168.1.*",
                "*",
                80,
                null,
                null,
                "TEST.*"
        );
        engine.addRule(rule);

        engine.evaluatePacket("192.168.1.100", "10.0.0.1", 80, 443, 6, "TEST_PAYLOAD");

        DetectionResult detection = new DetectionResult(
                ThreatLevel.HIGH,
                "TEST_THREAT",
                "Test threat",
                "TestDetector",
                "192.168.1.100",
                "10.0.0.1",
                80,
                443,
                0.8,
                "Test evidence",
                "forensics",
                "Block"
        );
        engine.autoGenerateRule(detection);

        DynamicRuleEngine.RuleEngineStatistics stats = engine.getStatistics();

        assertTrue("Should have active rules", stats.totalRulesActive > 0);
        assertTrue("Should have evaluated rules", stats.rulesEvaluated > 0);
        assertTrue("Should have auto-generated rules", stats.rulesAutoGenerated > 0);
    }

    @Test
    public void testRuleListener() {
        final boolean[] ruleCreated = {false};
        final boolean[] ruleMatched = {false};

        DynamicRuleEngine.RuleEvaluationListener listener = new DynamicRuleEngine.RuleEvaluationListener() {
            @Override
            public void onRuleMatched(DynamicRuleEngine.FirewallRule rule, String srcIP, String dstIP) {
                ruleMatched[0] = true;
            }

            @Override
            public void onRuleCreated(DynamicRuleEngine.FirewallRule rule) {
                ruleCreated[0] = true;
            }

            @Override
            public void onRuleExpired(DynamicRuleEngine.FirewallRule rule) {
                // Not tested here
            }
        };

        engine.addListener(listener);

        DynamicRuleEngine.FirewallRule rule = new DynamicRuleEngine.FirewallRule(
                "Listener Test",
                DynamicRuleEngine.RulePriority.MEDIUM,
                DynamicRuleEngine.RuleAction.BLOCK,
                "192.168.1.*",
                "*",
                80,
                null,
                null,
                "TEST.*"
        );
        engine.addRule(rule);

        assertTrue("Rule created listener should be called", ruleCreated[0]);

        engine.evaluatePacket("192.168.1.100", "10.0.0.1", 80, 443, 6, "TEST_PAYLOAD");

        assertTrue("Rule matched listener should be called", ruleMatched[0]);
    }

    @Test
    public void testRemoveRule() {
        DynamicRuleEngine.FirewallRule rule = new DynamicRuleEngine.FirewallRule(
                "Removable Rule",
                DynamicRuleEngine.RulePriority.MEDIUM,
                DynamicRuleEngine.RuleAction.BLOCK,
                "192.168.1.*",
                "*",
                80,
                null,
                null,
                "TEST.*"
        );
        engine.addRule(rule);

        assertEquals(1, engine.getActiveRules().size());

        engine.removeRule(rule.getRuleId());

        assertEquals(0, engine.getActiveRules().size());
    }

    @Test
    public void testRuleEnableDisable() {
        DynamicRuleEngine.FirewallRule rule = new DynamicRuleEngine.FirewallRule(
                "Toggle Rule",
                DynamicRuleEngine.RulePriority.MEDIUM,
                DynamicRuleEngine.RuleAction.BLOCK,
                "192.168.1.*",
                "*",
                80,
                null,
                null,
                "TEST.*"
        );
        engine.addRule(rule);

        // Should match when enabled
        DynamicRuleEngine.FirewallRule result = engine.evaluatePacket(
                "192.168.1.100",
                "10.0.0.1",
                80,
                443,
                6,
                "TEST_PAYLOAD"
        );
        assertNotNull(result);

        // Disable rule
        rule.setEnabled(false);

        // Should not match when disabled
        result = engine.evaluatePacket(
                "192.168.1.100",
                "10.0.0.1",
                80,
                443,
                6,
                "TEST_PAYLOAD"
        );
        assertNull(result);
    }
}
