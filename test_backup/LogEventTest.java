package com.odin.core.domain.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for LogEvent.
 */
public class LogEventTest {

    private LogEvent basicEvent;
    private LogEvent eventWithSeverity;
    private LogEvent eventWithMetadata;

    @Before
    public void setUp() {
        basicEvent = new LogEvent(1234567890L, "TEST_SOURCE", "Test message");
        
        eventWithSeverity = new LogEvent(
                1234567890L,
                "TEST_SOURCE",
                "Test warning message",
                LogEvent.Severity.WARNING
        );
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key1", "value1");
        metadata.put("key2", "value2");
        
        eventWithMetadata = new LogEvent(
                1234567890L,
                "TEST_SOURCE",
                "Test message with metadata",
                LogEvent.Severity.ERROR,
                metadata
        );
    }

    @Test
    public void testBasicConstructor() {
        assertEquals(1234567890L, basicEvent.getTimestamp());
        assertEquals("TEST_SOURCE", basicEvent.getSource());
        assertEquals("Test message", basicEvent.getMessage());
        assertEquals(LogEvent.Severity.INFO, basicEvent.getSeverity());
        assertTrue(basicEvent.getMetadata().isEmpty());
    }

    @Test
    public void testConstructorWithSeverity() {
        assertEquals(LogEvent.Severity.WARNING, eventWithSeverity.getSeverity());
        assertEquals("Test warning message", eventWithSeverity.getMessage());
    }

    @Test
    public void testConstructorWithMetadata() {
        assertEquals(LogEvent.Severity.ERROR, eventWithMetadata.getSeverity());
        assertEquals(2, eventWithMetadata.getMetadata().size());
        assertEquals("value1", eventWithMetadata.getMetadata().get("key1"));
        assertEquals("value2", eventWithMetadata.getMetadata().get("key2"));
    }

    @Test
    public void testBuilderPattern() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("test", "data");
        
        LogEvent builtEvent = LogEvent.builder()
                .timestamp(9876543210L)
                .source("BUILDER_TEST")
                .message("Built with builder")
                .severity(LogEvent.Severity.CRITICAL)
                .metadata(metadata)
                .build();
        
        assertEquals(9876543210L, builtEvent.getTimestamp());
        assertEquals("BUILDER_TEST", builtEvent.getSource());
        assertEquals("Built with builder", builtEvent.getMessage());
        assertEquals(LogEvent.Severity.CRITICAL, builtEvent.getSeverity());
        assertEquals("data", builtEvent.getMetadata().get("test"));
    }

    @Test
    public void testBuilderWithAutoTimestamp() {
        long beforeBuild = System.currentTimeMillis();
        LogEvent event = LogEvent.builder()
                .source("AUTO_TIMESTAMP")
                .message("Auto timestamp test")
                .build();
        long afterBuild = System.currentTimeMillis();
        
        assertTrue(event.getTimestamp() >= beforeBuild);
        assertTrue(event.getTimestamp() <= afterBuild);
    }

    @Test
    public void testJsonSerialization() throws JSONException {
        JSONObject json = eventWithMetadata.toJson();
        
        assertEquals(1234567890L, json.getLong("timestamp"));
        assertEquals("TEST_SOURCE", json.getString("source"));
        assertEquals("Test message with metadata", json.getString("message"));
        assertEquals("ERROR", json.getString("severity"));
        assertTrue(json.has("integrityHash"));
        assertTrue(json.has("metadata"));
    }

    @Test
    public void testJsonDeserialization() throws JSONException {
        JSONObject json = eventWithMetadata.toJson();
        LogEvent deserializedEvent = new LogEvent(json);
        
        assertEquals(eventWithMetadata.getTimestamp(), deserializedEvent.getTimestamp());
        assertEquals(eventWithMetadata.getSource(), deserializedEvent.getSource());
        assertEquals(eventWithMetadata.getMessage(), deserializedEvent.getMessage());
        assertEquals(eventWithMetadata.getSeverity(), deserializedEvent.getSeverity());
        assertEquals(eventWithMetadata.getMetadata(), deserializedEvent.getMetadata());
    }

    @Test
    public void testJsonStringSerialization() {
        String jsonString = eventWithMetadata.toJsonString();
        assertNotNull(jsonString);
        assertFalse(jsonString.isEmpty());
        assertTrue(jsonString.contains("TEST_SOURCE"));
        assertTrue(jsonString.contains("Test message with metadata"));
    }

    @Test
    public void testIntegrityHashCalculation() {
        assertNotNull(basicEvent.getIntegrityHash());
        assertFalse(basicEvent.getIntegrityHash().isEmpty());
    }

    @Test
    public void testIntegrityVerification() {
        assertTrue(basicEvent.verifyIntegrity());
        assertTrue(eventWithSeverity.verifyIntegrity());
        assertTrue(eventWithMetadata.verifyIntegrity());
    }

    @Test
    public void testSeverityFromString() {
        assertEquals(LogEvent.Severity.INFO, LogEvent.Severity.fromString("INFO"));
        assertEquals(LogEvent.Severity.WARNING, LogEvent.Severity.fromString("WARNING"));
        assertEquals(LogEvent.Severity.ERROR, LogEvent.Severity.fromString("ERROR"));
        assertEquals(LogEvent.Severity.CRITICAL, LogEvent.Severity.fromString("CRITICAL"));
        assertEquals(LogEvent.Severity.INFO, LogEvent.Severity.fromString("INVALID"));
    }

    @Test
    public void testEquals() {
        LogEvent event1 = new LogEvent(1234567890L, "SOURCE", "Message", LogEvent.Severity.INFO);
        LogEvent event2 = new LogEvent(1234567890L, "SOURCE", "Message", LogEvent.Severity.INFO);
        LogEvent event3 = new LogEvent(1234567891L, "SOURCE", "Message", LogEvent.Severity.INFO);
        
        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
    }

    @Test
    public void testHashCode() {
        LogEvent event1 = new LogEvent(1234567890L, "SOURCE", "Message", LogEvent.Severity.INFO);
        LogEvent event2 = new LogEvent(1234567890L, "SOURCE", "Message", LogEvent.Severity.INFO);
        
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        String toString = eventWithMetadata.toString();
        assertTrue(toString.contains("TEST_SOURCE"));
        assertTrue(toString.contains("Test message with metadata"));
        assertTrue(toString.contains("ERROR"));
    }

    @Test
    public void testMetadataImmutability() {
        Map<String, String> originalMetadata = new HashMap<>();
        originalMetadata.put("key", "value");
        
        LogEvent event = new LogEvent(
                1234567890L,
                "SOURCE",
                "Message",
                LogEvent.Severity.INFO,
                originalMetadata
        );
        
        // Modify original map
        originalMetadata.put("newKey", "newValue");
        
        // Event metadata should not be affected
        assertFalse(event.getMetadata().containsKey("newKey"));
    }

    @Test
    public void testNullHandlingInConstructor() {
        Map<String, String> nullMetadata = null;
        LogEvent event = new LogEvent(
                1234567890L,
                "SOURCE",
                "Message",
                LogEvent.Severity.INFO,
                nullMetadata
        );
        
        assertTrue(event.getMetadata().isEmpty());
    }
}
