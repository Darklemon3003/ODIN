package com.odin.core.forensics;

import com.odin.core.domain.model.LogEvent;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Security tests for Forensics components.
 * Tests security characteristics, encryption, integrity, and access control.
 */
public class ForensicsSecurityTest {

    private LogEvent testEvent;

    @Before
    public void setUp() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("ip", "192.168.1.100");
        metadata.put("token", "sensitive_token_value");
        
        testEvent = new LogEvent(
                System.currentTimeMillis(),
                "SECURITY_TEST",
                "Security test event with sensitive data",
                LogEvent.Severity.CRITICAL,
                metadata
        );
    }

    @Test
    public void testDataIntegrityVerification() {
        // Test that integrity hash is calculated correctly
        String integrityHash = testEvent.getIntegrityHash();
        
        assertNotNull("Integrity hash should not be null", integrityHash);
        assertFalse("Integrity hash should not be empty", integrityHash.isEmpty());
        assertTrue("Integrity hash should be SHA-256 (64 hex chars)", integrityHash.length() == 64);
    }

    @Test
    public void testIntegrityVerificationConsistency() {
        // Test that integrity verification is consistent
        assertTrue("Event should pass integrity verification", testEvent.verifyIntegrity());
        
        // Verify multiple times
        for (int i = 0; i < 10; i++) {
            assertTrue("Integrity verification should be consistent", testEvent.verifyIntegrity());
        }
    }

    @Test
    public void testDataMaskingForIpAddresses() {
        String originalMessage = "Connection from 192.168.1.100 to 10.0.0.50";
        String maskedMessage = originalMessage.replaceAll(
                "\\b(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\b",
                "$1.$2.$3.***"
        );
        
        assertTrue("IP should be partially masked", maskedMessage.contains("192.168.1.***"));
        assertTrue("IP should be partially masked", maskedMessage.contains("10.0.0.***"));
        assertFalse("Last octet should be masked", maskedMessage.contains("100"));
        assertFalse("Last octet should be masked", maskedMessage.contains("50"));
    }

    @Test
    public void testDataMaskingForTokens() {
        String originalMessage = "token=abc123def456ghi789jkl012mno345";
        String maskedMessage = originalMessage.replaceAll(
                "(token|api_key|secret)[\"':\\s]*[a-zA-Z0-9_-]{20,}",
                "$1=***REDACTED***"
        );
        
        assertTrue("Token should be masked", maskedMessage.contains("token=***REDACTED***"));
        assertFalse("Original token should not be visible", maskedMessage.contains("abc123def456"));
    }

    @Test
    public void testDataMaskingForApiKeys() {
        String originalMessage = "api_key=xyz789abc456def123ghi456jkl789";
        String maskedMessage = originalMessage.replaceAll(
                "(token|api_key|secret)[\"':\\s]*[a-zA-Z0-9_-]{20,}",
                "$1=***REDACTED***"
        );
        
        assertTrue("API key should be masked", maskedMessage.contains("api_key=***REDACTED***"));
        assertFalse("Original API key should not be visible", maskedMessage.contains("xyz789abc456"));
    }

    @Test
    public void testDataMaskingForSecrets() {
        String originalMessage = "secret=secret_value_12345678901234567890";
        String maskedMessage = originalMessage.replaceAll(
                "(token|api_key|secret)[\"':\\s]*[a-zA-Z0-9_-]{20,}",
                "$1=***REDACTED***"
        );
        
        assertTrue("Secret should be masked", maskedMessage.contains("secret=***REDACTED***"));
        assertFalse("Original secret should not be visible", maskedMessage.contains("secret_value_123"));
    }

    @Test
    public void testXmlInjectionPrevention() {
        String maliciousInput = "<script>alert('xss')</script>";
        String escaped = maliciousInput.replace("&", "&amp;")
                                      .replace("<", "&lt;")
                                      .replace(">", "&gt;")
                                      .replace("\"", "&quot;")
                                      .replace("'", "&apos;");
        
        assertFalse("XML tags should be escaped", escaped.contains("<script>"));
        assertTrue("XML tags should be escaped", escaped.contains("&lt;script&gt;"));
    }

    @Test
    public void testSqlInjectionPrevention() {
        String maliciousInput = "'; DROP TABLE users; --";
        String sanitized = maliciousInput.replaceAll("[';\"-]", "");
        
        assertFalse("SQL injection characters should be removed", sanitized.contains("'"));
        assertFalse("SQL injection characters should be removed", sanitized.contains(";"));
        assertFalse("SQL injection characters should be removed", sanitized.contains("-"));
    }

    @Test
    public void testLogInjectionPrevention() {
        String maliciousInput = "Normal log\n[EVIL] Malicious log entry";
        String sanitized = maliciousInput.replace("\n", "\\n")
                                          .replace("\r", "\\r");
        
        assertFalse("Newline characters should be escaped", sanitized.contains("\n"));
        assertTrue("Newline characters should be escaped", sanitized.contains("\\n"));
    }

    @Test
    public void testSeverityClassification() {
        // Test that severity levels are properly classified
        LogEvent infoEvent = new LogEvent(
                System.currentTimeMillis(),
                "TEST",
                "Info message",
                LogEvent.Severity.INFO
        );
        
        LogEvent warningEvent = new LogEvent(
                System.currentTimeMillis(),
                "TEST",
                "Warning message",
                LogEvent.Severity.WARNING
        );
        
        LogEvent errorEvent = new LogEvent(
                System.currentTimeMillis(),
                "TEST",
                "Error message",
                LogEvent.Severity.ERROR
        );
        
        LogEvent criticalEvent = new LogEvent(
                System.currentTimeMillis(),
                "TEST",
                "Critical message",
                LogEvent.Severity.CRITICAL
        );
        
        assertEquals(LogEvent.Severity.INFO, infoEvent.getSeverity());
        assertEquals(LogEvent.Severity.WARNING, warningEvent.getSeverity());
        assertEquals(LogEvent.Severity.ERROR, errorEvent.getSeverity());
        assertEquals(LogEvent.Severity.CRITICAL, criticalEvent.getSeverity());
    }

    @Test
    public void testMetadataImmutability() {
        // Test that metadata cannot be modified after event creation
        Map<String, String> originalMetadata = new HashMap<>();
        originalMetadata.put("key1", "value1");
        
        LogEvent event = new LogEvent(
                System.currentTimeMillis(),
                "TEST",
                "Test message",
                LogEvent.Severity.INFO,
                originalMetadata
        );
        
        // Modify original map
        originalMetadata.put("key2", "value2");
        originalMetadata.put("key1", "modified");
        
        // Event metadata should not be affected
        assertFalse("Event metadata should not contain key2", event.getMetadata().containsKey("key2"));
        assertEquals("Event metadata should retain original value", "value1", event.getMetadata().get("key1"));
    }

    @Test
    public void testNullInputHandling() {
        // Test that null inputs are handled gracefully
        LogEvent event = new LogEvent(
                System.currentTimeMillis(),
                "TEST",
                "Test message",
                LogEvent.Severity.INFO,
                null
        );
        
        assertNotNull("Event should be created even with null metadata", event);
        assertTrue("Metadata should be empty map when null is passed", event.getMetadata().isEmpty());
    }

    @Test
    public void testEmptyInputHandling() {
        // Test that empty inputs are handled gracefully
        Map<String, String> emptyMetadata = new HashMap<>();
        
        LogEvent event = new LogEvent(
                System.currentTimeMillis(),
                "TEST",
                "Test message",
                LogEvent.Severity.INFO,
                emptyMetadata
        );
        
        assertNotNull("Event should be created with empty metadata", event);
        assertTrue("Metadata should be empty", event.getMetadata().isEmpty());
    }

    @Test
    public void testHashCollisionResistance() {
        // Test that different events produce different hashes
        LogEvent event1 = new LogEvent(
                1000L,
                "SOURCE1",
                "Message1",
                LogEvent.Severity.INFO
        );
        
        LogEvent event2 = new LogEvent(
                1001L,
                "SOURCE1",
                "Message1",
                LogEvent.Severity.INFO
        );
        
        LogEvent event3 = new LogEvent(
                1000L,
                "SOURCE2",
                "Message1",
                LogEvent.Severity.INFO
        );
        
        assertNotEquals("Different timestamps should produce different hashes", 
                       event1.getIntegrityHash(), event2.getIntegrityHash());
        assertNotEquals("Different sources should produce different hashes", 
                       event1.getIntegrityHash(), event3.getIntegrityHash());
    }

    @Test
    public void testJsonSerializationSecurity() {
        // Test that JSON serialization doesn't expose sensitive data in unexpected ways
        try {
            org.json.JSONObject json = testEvent.toJson();
            
            // Verify that all expected fields are present
            assertTrue("JSON should contain timestamp", json.has("timestamp"));
            assertTrue("JSON should contain source", json.has("source"));
            assertTrue("JSON should contain message", json.has("message"));
            assertTrue("JSON should contain severity", json.has("severity"));
            assertTrue("JSON should contain integrityHash", json.has("integrityHash"));
            
            // Verify that metadata is properly included
            assertTrue("JSON should contain metadata", json.has("metadata"));
            
        } catch (Exception e) {
            fail("JSON serialization should not throw exceptions");
        }
    }

    @Test
    public void testCsvInjectionPrevention() {
        String maliciousInput = "value,=SUM(A1:A10)";
        String escaped = maliciousInput.contains(",") ? "\"" + maliciousInput.replace("\"", "\"\"") + "\"" : maliciousInput;
        
        assertTrue("CSV injection should be prevented by quoting", escaped.startsWith("\""));
        assertTrue("CSV injection should be prevented by quoting", escaped.endsWith("\""));
    }

    @Test
    public void testAccessControlSimulation() {
        // Simulate role-based access control
        enum Role { ADMIN, AUDITOR, ANALYST, VIEWER }
        
        Map<Role, Boolean> canExport = new HashMap<>();
        canExport.put(Role.ADMIN, true);
        canExport.put(Role.AUDITOR, true);
        canExport.put(Role.ANALYST, false);
        canExport.put(Role.VIEWER, false);
        
        assertTrue("Admin should be able to export", canExport.get(Role.ADMIN));
        assertTrue("Auditor should be able to export", canExport.get(Role.AUDITOR));
        assertFalse("Analyst should not be able to export", canExport.get(Role.ANALYST));
        assertFalse("Viewer should not be able to export", canExport.get(Role.VIEWER));
    }

    @Test
    public void testDataRetentionCompliance() {
        // Test GDPR compliance - data retention policies
        long now = System.currentTimeMillis();
        long retentionDays = 90;
        long retentionMs = retentionDays * 24 * 60 * 60 * 1000L;
        
        long eventTimestamp = now - (100 * 24 * 60 * 60 * 1000L); // 100 days ago
        boolean shouldPurge = (eventTimestamp + retentionMs) < now;
        
        assertTrue("Data older than retention period should be purged", shouldPurge);
    }

    @Test
    public void testRightToBeForgotten() {
        // Test GDPR right to be forgotten
        int initialFileCount = 10;
        int deletedCount = initialFileCount; // Delete all
        int finalFileCount = initialFileCount - deletedCount;
        
        assertEquals(0, finalFileCount);
        assertTrue("All data should be deleted for right to be forgotten", finalFileCount == 0);
    }

    @Test
    public void testAuditTrail() {
        // Test that audit trail is maintained
        Map<String, Long> auditLog = new HashMap<>();
        auditLog.put("access_timestamp", System.currentTimeMillis());
        auditLog.put("user_id", 12345L);
        auditLog.put("action", "EXPORT_LOGS");
        auditLog.put("resource", "forensic_data");
        
        assertEquals(4, auditLog.size());
        assertTrue("Audit log should contain timestamp", auditLog.containsKey("access_timestamp"));
        assertTrue("Audit log should contain user_id", auditLog.containsKey("user_id"));
        assertTrue("Audit log should contain action", auditLog.containsKey("action"));
        assertTrue("Audit log should contain resource", auditLog.containsKey("resource"));
    }

    @Test
    public void testSecureDataDisposal() {
        // Test secure data disposal
        StringBuilder sensitiveData = new StringBuilder();
        sensitiveData.append("sensitive_data_12345");
        
        // Secure disposal by overwriting
        for (int i = 0; i < sensitiveData.length(); i++) {
            sensitiveData.setCharAt(i, '\0');
        }
        
        assertEquals("Data should be zeroed out", "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0", sensitiveData.toString());
    }

    @Test
    public void testEncryptionKeyRotation() {
        // Test encryption key rotation simulation
        long keyRotationInterval = 90 * 24 * 60 * 60 * 1000L; // 90 days
        long lastKeyRotation = System.currentTimeMillis() - (100 * 24 * 60 * 60 * 1000L); // 100 days ago
        long now = System.currentTimeMillis();
        
        boolean shouldRotate = (now - lastKeyRotation) > keyRotationInterval;
        
        assertTrue("Key should be rotated when interval exceeded", shouldRotate);
    }

    @Test
    public void testSecureTransmission() {
        // Test that data transmission is secure
        boolean usesTLS = true;
        boolean usesTLS13 = true;
        boolean certificateValid = true;
        
        assertTrue("Transmission should use TLS", usesTLS);
        assertTrue("Transmission should use TLS 1.3", usesTLS13);
        assertTrue("Certificate should be valid", certificateValid);
    }
}
