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
 * Performance tests for Forensics components.
 * Tests performance characteristics and benchmarks.
 */
public class ForensicsPerformanceTest {

    private List<LogEvent> largeEventList;

    @Before
    public void setUp() {
        largeEventList = new ArrayList<>();
        
        // Create a large dataset for performance testing
        long baseTimestamp = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            LogEvent.Severity severity;
            if (i % 100 == 0) {
                severity = LogEvent.Severity.CRITICAL;
            } else if (i % 50 == 0) {
                severity = LogEvent.Severity.ERROR;
            } else if (i % 10 == 0) {
                severity = LogEvent.Severity.WARNING;
            } else {
                severity = LogEvent.Severity.INFO;
            }
            
            Map<String, String> metadata = new HashMap<>();
            metadata.put("index", String.valueOf(i));
            metadata.put("source", "PERF_TEST");
            
            largeEventList.add(new LogEvent(
                    baseTimestamp + i,
                    "PERF_TEST",
                    "Performance test event " + i,
                    severity,
                    metadata
            ));
        }
    }

    @Test
    public void testLogEventCreationPerformance() {
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 1000; i++) {
            LogEvent event = new LogEvent(
                    System.currentTimeMillis(),
                    "TEST",
                    "Test message",
                    LogEvent.Severity.INFO
            );
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        
        // Should create 1000 events in less than 100ms
        assertTrue("Event creation should be fast", durationMs < 100);
    }

    @Test
    public void testJsonSerializationPerformance() {
        long startTime = System.nanoTime();
        
        for (LogEvent event : largeEventList) {
            try {
                event.toJson();
            } catch (Exception e) {
                fail("JSON serialization failed");
            }
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        double eventsPerSecond = (largeEventList.size() * 1000.0) / durationMs;
        
        // Should serialize at least 10,000 events per second
        assertTrue("JSON serialization should be fast", eventsPerSecond > 10000);
    }

    @Test
    public void testIntegrityVerificationPerformance() {
        long startTime = System.nanoTime();
        
        int validCount = 0;
        for (LogEvent event : largeEventList) {
            if (event.verifyIntegrity()) {
                validCount++;
            }
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        double eventsPerSecond = (largeEventList.size() * 1000.0) / durationMs;
        
        assertEquals(largeEventList.size(), validCount);
        assertTrue("Integrity verification should be fast", eventsPerSecond > 10000);
    }

    @Test
    public void testFilteringPerformance() {
        long startTime = System.nanoTime();
        
        List<LogEvent> criticalEvents = new ArrayList<>();
        for (LogEvent event : largeEventList) {
            if (event.getSeverity() == LogEvent.Severity.CRITICAL) {
                criticalEvents.add(event);
            }
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        
        assertEquals(100, criticalEvents.size());
        assertTrue("Filtering should be fast", durationMs < 100);
    }

    @Test
    public void testSortingPerformance() {
        long startTime = System.nanoTime();
        
        List<LogEvent> sortedList = new ArrayList<>(largeEventList);
        sortedList.sort((e1, e2) -> Long.compare(e1.getTimestamp(), e2.getTimestamp()));
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        
        assertEquals(largeEventList.size(), sortedList.size());
        assertTrue("Sorting should be fast", durationMs < 500);
    }

    @Test
    public void testCsvExportPerformance() {
        long startTime = System.nanoTime();
        
        StringBuilder csv = new StringBuilder();
        csv.append("timestamp,source,message,severity,integrityHash\n");
        
        for (LogEvent event : largeEventList) {
            csv.append(event.getTimestamp()).append(",");
            csv.append(event.getSource()).append(",");
            csv.append(event.getMessage()).append(",");
            csv.append(event.getSeverity().getValue()).append(",");
            csv.append(event.getIntegrityHash()).append("\n");
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        double eventsPerSecond = (largeEventList.size() * 1000.0) / durationMs;
        
        assertTrue("CSV export should be fast", eventsPerSecond > 10000);
    }

    @Test
    public void testJsonExportPerformance() {
        long startTime = System.nanoTime();
        
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"events\": [\n");
        
        for (int i = 0; i < largeEventList.size(); i++) {
            json.append("    ").append(largeEventList.get(i).toJsonString());
            if (i < largeEventList.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("  ]\n");
        json.append("}");
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        double eventsPerSecond = (largeEventList.size() * 1000.0) / durationMs;
        
        assertTrue("JSON export should be fast", eventsPerSecond > 5000);
    }

    @Test
    public void testXmlExportPerformance() {
        long startTime = System.nanoTime();
        
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<Events>\n");
        
        for (LogEvent event : largeEventList) {
            xml.append("  <Event>\n");
            xml.append("    <Timestamp>").append(event.getTimestamp()).append("</Timestamp>\n");
            xml.append("    <Source>").append(event.getSource()).append("</Source>\n");
            xml.append("    <Message>").append(event.getMessage()).append("</Message>\n");
            xml.append("    <Severity>").append(event.getSeverity().getValue()).append("</Severity>\n");
            xml.append("  </Event>\n");
        }
        
        xml.append("</Events>");
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        double eventsPerSecond = (largeEventList.size() * 1000.0) / durationMs;
        
        assertTrue("XML export should be fast", eventsPerSecond > 5000);
    }

    @Test
    public void testDataMaskingPerformance() {
        long startTime = System.nanoTime();
        
        for (LogEvent event : largeEventList) {
            String message = event.getMessage();
            String masked = message.replaceAll(
                    "\\b(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\b",
                    "$1.$2.$3.***"
            );
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        double eventsPerSecond = (largeEventList.size() * 1000.0) / durationMs;
        
        assertTrue("Data masking should be fast", eventsPerSecond > 10000);
    }

    @Test
    public void testStatisticsCalculationPerformance() {
        long startTime = System.nanoTime();
        
        Map<String, Long> severityDistribution = new HashMap<>();
        for (LogEvent.Severity severity : LogEvent.Severity.values()) {
            long count = largeEventList.stream()
                    .filter(e -> e.getSeverity() == severity)
                    .count();
            severityDistribution.put(severity.getValue(), count);
        }
        
        Map<String, Long> sourceDistribution = new HashMap<>();
        for (LogEvent event : largeEventList) {
            sourceDistribution.merge(event.getSource(), 1L, Long::sum);
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        
        assertEquals(4, severityDistribution.size());
        assertEquals(1, sourceDistribution.size());
        assertTrue("Statistics calculation should be fast", durationMs < 200);
    }

    @Test
    public void testBuilderPatternPerformance() {
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 1000; i++) {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("test", "value");
            
            LogEvent event = LogEvent.builder()
                    .timestamp(System.currentTimeMillis())
                    .source("BUILDER_TEST")
                    .message("Builder test")
                    .severity(LogEvent.Severity.INFO)
                    .metadata(metadata)
                    .build();
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        
        assertTrue("Builder pattern should be fast", durationMs < 100);
    }

    @Test
    public void testMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        
        // Force garbage collection
        System.gc();
        
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
        // Create large number of events
        List<LogEvent> events = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            events.add(new LogEvent(
                    System.currentTimeMillis(),
                    "MEMORY_TEST",
                    "Memory test event " + i,
                    LogEvent.Severity.INFO
            ));
        }
        
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsedMB = (memoryAfter - memoryBefore) / (1024 * 1024);
        
        // Memory usage should be reasonable (< 50MB for 10,000 events)
        assertTrue("Memory usage should be reasonable", memoryUsedMB < 50);
        
        events.clear();
    }

    @Test
    public void testConcurrentAccess() {
        long startTime = System.nanoTime();
        
        // Simulate concurrent access
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (LogEvent event : largeEventList) {
                    event.toJson();
                    event.verifyIntegrity();
                }
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                fail("Thread interrupted");
            }
        }
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        
        assertTrue("Concurrent access should be efficient", durationMs < 2000);
    }

    @Test
    public void testWriteLatencyBenchmark() {
        List<Long> latencies = new ArrayList<>();
        
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            
            LogEvent event = new LogEvent(
                    System.currentTimeMillis(),
                    "LATENCY_TEST",
                    "Latency test event",
                    LogEvent.Severity.INFO
            );
            event.toJson();
            
            long end = System.nanoTime();
            latencies.add((end - start) / 1_000_000); // Convert to ms
        }
        
        // Calculate 95th percentile
        latencies.sort(Long::compareTo);
        int percentile95Index = (int) (latencies.size() * 0.95);
        long percentile95 = latencies.get(percentile95Index);
        
        // 95th percentile should be < 10ms
        assertTrue("95th percentile latency should be < 10ms", percentile95 < 10);
    }

    @Test
    public void testReadLatencyBenchmark() {
        // Create events first
        List<LogEvent> events = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            events.add(new LogEvent(
                    System.currentTimeMillis(),
                    "READ_TEST",
                    "Read test event " + i,
                    LogEvent.Severity.INFO
            ));
        }
        
        List<Long> latencies = new ArrayList<>();
        
        for (LogEvent event : events) {
            long start = System.nanoTime();
            
            event.toJson();
            event.getTimestamp();
            event.getSource();
            event.getMessage();
            event.getSeverity();
            event.getMetadata();
            
            long end = System.nanoTime();
            latencies.add((end - start) / 1_000_000); // Convert to ms
        }
        
        // Calculate 95th percentile
        latencies.sort(Long::compareTo);
        int percentile95Index = (int) (latencies.size() * 0.95);
        long percentile95 = latencies.get(percentile95Index);
        
        // 95th percentile should be < 100ms
        assertTrue("95th percentile read latency should be < 100ms", percentile95 < 100);
    }
}
