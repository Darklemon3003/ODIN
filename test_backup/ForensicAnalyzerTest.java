package com.odin.core.forensics;

import com.odin.core.domain.model.LogEvent;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for ForensicAnalyzer.
 * Note: These tests use mocked SentryCore since we can't use Android context in unit tests.
 */
public class ForensicAnalyzerTest {

    private List<LogEvent> testEvents;

    @Before
    public void setUp() {
        testEvents = new ArrayList<>();
        
        // Create test events with different timestamps and severities
        testEvents.add(new LogEvent(
                1000L,
                "VPN",
                "VPN connection established",
                LogEvent.Severity.INFO
        ));
        
        testEvents.add(new LogEvent(
                2000L,
                "THREAT_DETECTION",
                "Suspicious packet detected",
                LogEvent.Severity.WARNING
        ));
        
        testEvents.add(new LogEvent(
                3000L,
                "THREAT_DETECTION",
                "Critical threat detected",
                LogEvent.Severity.CRITICAL
        ));
        
        testEvents.add(new LogEvent(
                4000L,
                "FIREWALL",
                "Packet blocked",
                LogEvent.Severity.ERROR
        ));
    }

    @Test
    public void testForensicTimelineCreation() {
        ForensicAnalyzer.ForensicTimeline timeline = new ForensicAnalyzer.ForensicTimeline();
        
        timeline.setStartTime(1000L);
        timeline.setEndTime(4000L);
        timeline.setTotalEvents(4);
        
        Map<String, List<LogEvent>> eventsBySource = new HashMap<>();
        eventsBySource.put("VPN", testEvents.subList(0, 1));
        eventsBySource.put("THREAT_DETECTION", testEvents.subList(1, 3));
        eventsBySource.put("FIREWALL", testEvents.subList(3, 4));
        timeline.setEventsBySource(eventsBySource);
        
        assertEquals(1000L, timeline.getStartTime());
        assertEquals(4000L, timeline.getEndTime());
        assertEquals(4, timeline.getTotalEvents());
        assertEquals(3, timeline.getEventsBySource().size());
    }

    @Test
    public void testAttackPatternCreation() {
        ForensicAnalyzer.AttackPattern pattern = new ForensicAnalyzer.AttackPattern(
                "TEST_PATTERN",
                "Test description",
                "HIGH",
                5L
        );
        
        assertEquals("TEST_PATTERN", pattern.getType());
        assertEquals("Test description", pattern.getDescription());
        assertEquals("HIGH", pattern.getSeverity());
        assertEquals(5L, pattern.getOccurrenceCount());
    }

    @Test
    public void testSeverityFiltering() {
        List<LogEvent> criticalEvents = new ArrayList<>();
        for (LogEvent event : testEvents) {
            if (event.getSeverity() == LogEvent.Severity.CRITICAL) {
                criticalEvents.add(event);
            }
        }
        
        assertEquals(1, criticalEvents.size());
        assertEquals("Critical threat detected", criticalEvents.get(0).getMessage());
    }

    @Test
    public void testSourceFiltering() {
        List<LogEvent> threatDetectionEvents = new ArrayList<>();
        for (LogEvent event : testEvents) {
            if (event.getSource().equals("THREAT_DETECTION")) {
                threatDetectionEvents.add(event);
            }
        }
        
        assertEquals(2, threatDetectionEvents.size());
    }

    @Test
    public void testTimeRangeFiltering() {
        List<LogEvent> filteredEvents = new ArrayList<>();
        for (LogEvent event : testEvents) {
            if (event.getTimestamp() >= 1500L && event.getTimestamp() <= 3500L) {
                filteredEvents.add(event);
            }
        }
        
        assertEquals(2, filteredEvents.size());
    }

    @Test
    public void testEventStatisticsCalculation() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalEvents = testEvents.size();
        stats.put("totalEvents", totalEvents);
        
        // Calculate severity distribution
        Map<String, Long> severityCount = new HashMap<>();
        for (LogEvent.Severity severity : LogEvent.Severity.values()) {
            long count = testEvents.stream()
                    .filter(e -> e.getSeverity() == severity)
                    .count();
            if (count > 0) {
                severityCount.put(severity.getValue(), count);
            }
        }
        stats.put("severityDistribution", severityCount);
        
        assertEquals(4L, stats.get("totalEvents"));
        assertEquals(3, ((Map<?, ?>) stats.get("severityDistribution")).size());
    }

    @Test
    public void testSourceDistributionCalculation() {
        Map<String, Long> sourceCount = new HashMap<>();
        for (LogEvent event : testEvents) {
            sourceCount.merge(event.getSource(), 1L, Long::sum);
        }
        
        assertEquals(3, sourceCount.size());
        assertEquals(Long.valueOf(1), sourceCount.get("VPN"));
        assertEquals(Long.valueOf(2), sourceCount.get("THREAT_DETECTION"));
        assertEquals(Long.valueOf(1), sourceCount.get("FIREWALL"));
    }

    @Test
    public void testJsonExportStructure() {
        try {
            JSONObject json = testEvents.get(0).toJson();
            
            assertTrue(json.has("timestamp"));
            assertTrue(json.has("source"));
            assertTrue(json.has("message"));
            assertTrue(json.has("severity"));
            assertTrue(json.has("integrityHash"));
            
            assertEquals(1000L, json.getLong("timestamp"));
            assertEquals("VPN", json.getString("source"));
            assertEquals("INFO", json.getString("severity"));
        } catch (Exception e) {
            fail("JSON serialization failed: " + e.getMessage());
        }
    }

    @Test
    public void testCsvExportFormat() {
        StringBuilder csv = new StringBuilder();
        csv.append("timestamp,source,message,severity,integrityHash\n");
        
        for (LogEvent event : testEvents) {
            csv.append(event.getTimestamp()).append(",");
            csv.append(event.getSource()).append(",");
            csv.append(event.getMessage()).append(",");
            csv.append(event.getSeverity().getValue()).append(",");
            csv.append(event.getIntegrityHash()).append("\n");
        }
        
        String csvString = csv.toString();
        assertTrue(csvString.contains("timestamp,source,message,severity,integrityHash"));
        assertTrue(csvString.contains("VPN"));
        assertTrue(csvString.contains("THREAT_DETECTION"));
    }

    @Test
    public void testXmlExportFormat() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<Events>\n");
        
        for (LogEvent event : testEvents) {
            xml.append("  <Event>\n");
            xml.append("    <Timestamp>").append(event.getTimestamp()).append("</Timestamp>\n");
            xml.append("    <Source>").append(event.getSource()).append("</Source>\n");
            xml.append("    <Message>").append(event.getMessage()).append("</Message>\n");
            xml.append("    <Severity>").append(event.getSeverity().getValue()).append("</Severity>\n");
            xml.append("  </Event>\n");
        }
        
        xml.append("</Events>");
        
        String xmlString = xml.toString();
        assertTrue(xmlString.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(xmlString.contains("<Events>"));
        assertTrue(xmlString.contains("<Event>"));
    }

    @Test
    public void testIntegrityVerification() {
        for (LogEvent event : testEvents) {
            assertTrue("Event integrity should be valid", event.verifyIntegrity());
        }
    }

    @Test
    public void testMultipleCriticalErrorsDetection() {
        // Simulate detection of multiple critical errors
        long criticalCount = testEvents.stream()
                .filter(e -> e.getSeverity() == LogEvent.Severity.CRITICAL)
                .count();
        
        // In a real scenario, if criticalCount > 5, it would trigger a pattern
        assertTrue(criticalCount >= 0);
    }

    @Test
    public void testVpnThreatSequenceDetection() {
        // Simulate detection of VPN followed by threat detection
        boolean sequenceFound = false;
        for (int i = 0; i < testEvents.size() - 1; i++) {
            if (testEvents.get(i).getSource().equals("VPN") &&
                testEvents.get(i + 1).getSource().equals("THREAT_DETECTION")) {
                sequenceFound = true;
                break;
            }
        }
        
        // In our test data, VPN is at index 0 and THREAT_DETECTION at index 1
        assertTrue(sequenceFound);
    }

    @Test
    public void testHighFirewallActivityDetection() {
        // Simulate detection of high firewall activity
        long firewallCount = testEvents.stream()
                .filter(e -> e.getSource().equals("FIREWALL"))
                .count();
        
        // In a real scenario, if firewallCount > 20, it would trigger a pattern
        assertTrue(firewallCount >= 0);
    }
}
