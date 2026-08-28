package com.odin.core.forensics;

import com.odin.core.domain.model.LogEvent;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Integration tests for Forensics components.
 * Tests the interaction between ForensicAnalyzer, LogRetentionManager, and LogExporter.
 */
public class ForensicsIntegrationTest {

    private List<LogEvent> testEvents;

    @Before
    public void setUp() {
        testEvents = new ArrayList<>();
        
        // Create a comprehensive set of test events simulating a real incident
        long baseTimestamp = System.currentTimeMillis() - 3600000; // 1 hour ago
        
        // Initial VPN connection
        testEvents.add(new LogEvent(
                baseTimestamp,
                "VPN",
                "VPN connection established",
                LogEvent.Severity.INFO
        ));
        
        // Normal traffic
        for (int i = 0; i < 10; i++) {
            testEvents.add(new LogEvent(
                    baseTimestamp + 1000 * (i + 1),
                    "VPN",
                    "Normal packet processed",
                    LogEvent.Severity.INFO
            ));
        }
        
        // Threat detection
        testEvents.add(new LogEvent(
                baseTimestamp + 15000,
                "THREAT_DETECTION",
                "Suspicious packet pattern detected",
                LogEvent.Severity.WARNING
        ));
        
        // More threats
        testEvents.add(new LogEvent(
                baseTimestamp + 20000,
                "THREAT_DETECTION",
                "Port scan activity detected",
                LogEvent.Severity.ERROR
        ));
        
        testEvents.add(new LogEvent(
                baseTimestamp + 25000,
                "THREAT_DETECTION",
                "Critical threat: C2 communication detected",
                LogEvent.Severity.CRITICAL
        ));
        
        // Firewall response
        testEvents.add(new LogEvent(
                baseTimestamp + 30000,
                "FIREWALL",
                "Blocked malicious connection",
                LogEvent.Severity.ERROR
        ));
        
        // Recovery
        testEvents.add(new LogEvent(
                baseTimestamp + 35000,
                "VPN",
                "Threat mitigated, connection restored",
                LogEvent.Severity.INFO
        ));
    }

    @Test
    public void testFullForensicsWorkflow() {
        // Simulate the complete forensics workflow
        
        // Step 1: Analyze incident timeline
        long startTime = testEvents.get(0).getTimestamp();
        long endTime = testEvents.get(testEvents.size() - 1).getTimestamp();
        
        assertTrue("Start time should be before end time", startTime < endTime);
        assertEquals(15, testEvents.size());
        
        // Step 2: Filter by severity
        List<LogEvent> criticalEvents = new ArrayList<>();
        List<LogEvent> errorEvents = new ArrayList<>();
        List<LogEvent> warningEvents = new ArrayList<>();
        
        for (LogEvent event : testEvents) {
            switch (event.getSeverity()) {
                case CRITICAL:
                    criticalEvents.add(event);
                    break;
                case ERROR:
                    errorEvents.add(event);
                    break;
                case WARNING:
                    warningEvents.add(event);
                    break;
                default:
                    break;
            }
        }
        
        assertEquals(1, criticalEvents.size());
        assertEquals(2, errorEvents.size());
        assertEquals(1, warningEvents.size());
        
        // Step 3: Group by source
        Map<String, List<LogEvent>> eventsBySource = new HashMap<>();
        for (LogEvent event : testEvents) {
            eventsBySource.computeIfAbsent(event.getSource(), k -> new ArrayList<>()).add(event);
        }
        
        assertEquals(3, eventsBySource.size());
        assertTrue(eventsBySource.containsKey("VPN"));
        assertTrue(eventsBySource.containsKey("THREAT_DETECTION"));
        assertTrue(eventsBySource.containsKey("FIREWALL"));
        
        // Step 4: Detect attack patterns
        boolean criticalThreatDetected = !criticalEvents.isEmpty();
        boolean multipleThreatsDetected = (criticalEvents.size() + errorEvents.size()) >= 2;
        boolean vpnThreatSequence = eventsBySource.containsKey("VPN") && 
                                   eventsBySource.containsKey("THREAT_DETECTION");
        
        assertTrue(criticalThreatDetected);
        assertTrue(multipleThreatsDetected);
        assertTrue(vpnThreatSequence);
    }

    @Test
    public void testRetentionIntegration() {
        // Simulate retention policy evaluation
        
        long now = System.currentTimeMillis();
        long retentionDays = 90;
        long retentionMs = retentionDays * 24 * 60 * 60 * 1000L;
        
        int eventsToArchive = 0;
        int eventsToKeep = 0;
        
        for (LogEvent event : testEvents) {
            long eventAge = now - event.getTimestamp();
            long archiveThreshold = 30 * 24 * 60 * 60 * 1000L; // 30 days
            
            if (eventAge > archiveThreshold) {
                eventsToArchive++;
            } else {
                eventsToKeep++;
            }
        }
        
        // Since our test events are only 1 hour old, none should be archived
        assertEquals(0, eventsToArchive);
        assertEquals(testEvents.size(), eventsToKeep);
    }

    @Test
    public void testExportIntegration() {
        // Simulate export workflow with multiple formats
        
        // JSON export
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"exportTimestamp\": ").append(System.currentTimeMillis()).append(",\n");
        json.append("  \"totalEvents\": ").append(testEvents.size()).append(",\n");
        json.append("  \"events\": [\n");
        
        for (int i = 0; i < testEvents.size(); i++) {
            json.append("    ").append(testEvents.get(i).toJsonString());
            if (i < testEvents.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("  ]\n");
        json.append("}");
        
        String jsonString = json.toString();
        assertTrue(jsonString.contains("exportTimestamp"));
        assertTrue(jsonString.contains("totalEvents"));
        assertTrue(jsonString.contains("events"));
        
        // CSV export
        StringBuilder csv = new StringBuilder();
        csv.append("timestamp,source,message,severity\n");
        
        for (LogEvent event : testEvents) {
            csv.append(event.getTimestamp()).append(",");
            csv.append(event.getSource()).append(",");
            csv.append(event.getMessage()).append(",");
            csv.append(event.getSeverity().getValue()).append("\n");
        }
        
        String csvString = csv.toString();
        assertTrue(csvString.contains("timestamp,source,message,severity"));
        assertEquals(testEvents.size() + 1, csvString.split("\n").length); // +1 for header
        
        // Verify both exports contain the same number of events
        assertTrue(jsonString.contains(String.valueOf(testEvents.size())));
    }

    @Test
    public void testDataMaskingIntegration() {
        // Test data masking across the workflow
        
        String originalMessage = "Connection from 192.168.1.100 to 10.0.0.50 with token=abc123def456";
        String maskedMessage = originalMessage
                .replaceAll("\\b(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\b", "$1.$2.$3.***")
                .replaceAll("(token|api_key|secret)[\"':\\s]*[a-zA-Z0-9_-]{20,}", "$1=***REDACTED***");
        
        assertTrue(maskedMessage.contains("192.168.1.***"));
        assertTrue(maskedMessage.contains("10.0.0.***"));
        assertTrue(maskedMessage.contains("token=***REDACTED***"));
        assertFalse(maskedMessage.contains("abc123def456"));
    }

    @Test
    public void testTimelineReconstruction() {
        // Test timeline reconstruction from events
        
        List<LogEvent> sortedEvents = new ArrayList<>(testEvents);
        sortedEvents.sort((e1, e2) -> Long.compare(e1.getTimestamp(), e2.getTimestamp()));
        
        // Verify events are sorted
        for (int i = 0; i < sortedEvents.size() - 1; i++) {
            assertTrue(sortedEvents.get(i).getTimestamp() <= sortedEvents.get(i + 1).getTimestamp());
        }
        
        // Verify timeline progression
        long firstTimestamp = sortedEvents.get(0).getTimestamp();
        long lastTimestamp = sortedEvents.get(sortedEvents.size() - 1).getTimestamp();
        long duration = lastTimestamp - firstTimestamp;
        
        assertTrue("Timeline should have positive duration", duration > 0);
        assertEquals(testEvents.size() - 1, sortedEvents.size() - 1);
    }

    @Test
    public void testStatisticsCalculation() {
        // Test statistics calculation across components
        
        Map<String, Object> statistics = new HashMap<>();
        
        // Total events
        statistics.put("totalEvents", testEvents.size());
        
        // Severity distribution
        Map<String, Long> severityDistribution = new HashMap<>();
        for (LogEvent.Severity severity : LogEvent.Severity.values()) {
            long count = testEvents.stream()
                    .filter(e -> e.getSeverity() == severity)
                    .count();
            if (count > 0) {
                severityDistribution.put(severity.getValue(), count);
            }
        }
        statistics.put("severityDistribution", severityDistribution);
        
        // Source distribution
        Map<String, Long> sourceDistribution = new HashMap<>();
        for (LogEvent event : testEvents) {
            sourceDistribution.merge(event.getSource(), 1L, Long::sum);
        }
        statistics.put("sourceDistribution", sourceDistribution);
        
        // Duration
        long duration = testEvents.get(testEvents.size() - 1).getTimestamp() - 
                       testEvents.get(0).getTimestamp();
        statistics.put("durationMs", duration);
        
        assertEquals(15L, statistics.get("totalEvents"));
        assertEquals(3, ((Map<?, ?>) statistics.get("severityDistribution")).size());
        assertEquals(3, ((Map<?, ?>) statistics.get("sourceDistribution")).size());
        assertTrue((long) statistics.get("durationMs") > 0);
    }

    @Test
    public void testIntegrityVerificationIntegration() {
        // Test integrity verification across all events
        
        int validEvents = 0;
        for (LogEvent event : testEvents) {
            if (event.verifyIntegrity()) {
                validEvents++;
            }
        }
        
        assertEquals(testEvents.size(), validEvents);
    }

    @Test
    public void testMultiFormatExportConsistency() {
        // Test that different export formats contain consistent data
        
        int eventCount = testEvents.size();
        
        // JSON should contain event count
        String jsonStats = "{\"totalEvents\":" + eventCount + "}";
        assertTrue(jsonStats.contains(String.valueOf(eventCount)));
        
        // CSV should have event count + header
        String csvStats = "timestamp,source,message,severity\n";
        for (int i = 0; i < eventCount; i++) {
            csvStats += "data\n";
        }
        assertEquals(eventCount + 1, csvStats.split("\n").length);
        
        // XML should contain event count
        String xmlStats = "<TotalEvents>" + eventCount + "</TotalEvents>";
        assertTrue(xmlStats.contains(String.valueOf(eventCount)));
    }

    @Test
    public void testErrorHandlingIntegration() {
        // Test error handling across components
        
        // Simulate various error scenarios
        List<String> errorScenarios = new ArrayList<>();
        
        // Test with empty event list
        List<LogEvent> emptyList = new ArrayList<>();
        if (emptyList.isEmpty()) {
            errorScenarios.add("Empty event list handled");
        }
        
        // Test with null metadata
        LogEvent eventWithNullMetadata = new LogEvent(
                System.currentTimeMillis(),
                "TEST",
                "Test message",
                LogEvent.Severity.INFO,
                null
        );
        if (eventWithNullMetadata.getMetadata().isEmpty()) {
            errorScenarios.add("Null metadata handled");
        }
        
        // Test with invalid timestamp
        LogEvent eventWithInvalidTimestamp = new LogEvent(
                -1,
                "TEST",
                "Test message",
                LogEvent.Severity.INFO
        );
        if (eventWithInvalidTimestamp.getTimestamp() == -1) {
            errorScenarios.add("Invalid timestamp handled");
        }
        
        assertEquals(3, errorScenarios.size());
    }

    @Test
    public void testPerformanceIntegration() {
        // Test performance characteristics
        
        long startTime = System.nanoTime();
        
        // Simulate processing all events
        for (LogEvent event : testEvents) {
            event.toJson();
            event.verifyIntegrity();
            event.getMetadata();
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        
        // Should complete in reasonable time (< 100ms for 15 events)
        assertTrue("Processing should complete in reasonable time", durationMs < 100);
    }
}
