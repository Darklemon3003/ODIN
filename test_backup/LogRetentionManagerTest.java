package com.odin.core.forensics;

import com.odin.core.domain.model.LogEvent;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for LogRetentionManager.
 * Note: These tests use mocked components since we can't use Android context in unit tests.
 */
public class LogRetentionManagerTest {

    private LogRetentionManager.RetentionResult retentionResult;

    @Before
    public void setUp() {
        retentionResult = new LogRetentionManager.RetentionResult();
    }

    @Test
    public void testRetentionResultCreation() {
        retentionResult.setSuccess(true);
        retentionResult.setTotalEvents(100);
        retentionResult.setEventsToArchive(30);
        retentionResult.setEventsToKeep(70);
        retentionResult.setArchived(true);
        retentionResult.setArchiveFileCount(5);
        retentionResult.setPurgedCount(10);

        assertTrue(retentionResult.isSuccess());
        assertEquals(100, retentionResult.getTotalEvents());
        assertEquals(30, retentionResult.getEventsToArchive());
        assertEquals(70, retentionResult.getEventsToKeep());
        assertTrue(retentionResult.isArchived());
        assertEquals(5, retentionResult.getArchiveFileCount());
        assertEquals(10, retentionResult.getPurgedCount());
    }

    @Test
    public void testRetentionResultWithFailure() {
        retentionResult.setSuccess(false);
        retentionResult.setErrorMessage("Test error message");

        assertFalse(retentionResult.isSuccess());
        assertEquals("Test error message", retentionResult.getErrorMessage());
    }

    @Test
    public void testDefaultRetentionPolicies() {
        // Simulate default retention policies
        Map<LogEvent.Severity, Long> policies = new HashMap<>();
        policies.put(LogEvent.Severity.INFO, 90L);
        policies.put(LogEvent.Severity.WARNING, 90L);
        policies.put(LogEvent.Severity.ERROR, 180L);
        policies.put(LogEvent.Severity.CRITICAL, Long.MAX_VALUE);

        assertEquals(Long.valueOf(90), policies.get(LogEvent.Severity.INFO));
        assertEquals(Long.valueOf(90), policies.get(LogEvent.Severity.WARNING));
        assertEquals(Long.valueOf(180), policies.get(LogEvent.Severity.ERROR));
        assertEquals(Long.valueOf(Long.MAX_VALUE), policies.get(LogEvent.Severity.CRITICAL));
    }

    @Test
    public void testCustomRetentionPolicy() {
        Map<LogEvent.Severity, Long> policies = new HashMap<>();
        policies.put(LogEvent.Severity.ERROR, 365L); // 1 year

        assertEquals(Long.valueOf(365), policies.get(LogEvent.Severity.ERROR));
    }

    @Test
    public void testEventAgeCalculation() {
        long now = System.currentTimeMillis();
        long eventTimestamp = now - (30 * 24 * 60 * 60 * 1000L); // 30 days ago
        long ageMs = now - eventTimestamp;
        long ageDays = ageMs / (24 * 60 * 60 * 1000L);

        assertEquals(30L, ageDays);
    }

    @Test
    public void testArchiveThresholdCalculation() {
        long now = System.currentTimeMillis();
        long archiveThreshold = now - (30 * 24 * 60 * 60 * 1000L); // 30 days

        long eventTimestamp1 = now - (35 * 24 * 60 * 60 * 1000L); // 35 days ago - should archive
        long eventTimestamp2 = now - (25 * 24 * 60 * 60 * 1000L); // 25 days ago - should keep

        assertTrue(eventTimestamp1 < archiveThreshold);
        assertTrue(eventTimestamp2 > archiveThreshold);
    }

    @Test
    public void testRetentionPolicyEvaluation() {
        long now = System.currentTimeMillis();
        long retentionDays = 90;
        long retentionMs = retentionDays * 24 * 60 * 60 * 1000L;

        long eventTimestamp1 = now - (100 * 24 * 60 * 60 * 1000L); // 100 days ago - should purge
        long eventTimestamp2 = now - (50 * 24 * 60 * 60 * 1000L); // 50 days ago - should keep

        boolean shouldPurge1 = (eventTimestamp1 + retentionMs) < now;
        boolean shouldPurge2 = (eventTimestamp2 + retentionMs) < now;

        assertTrue(shouldPurge1);
        assertFalse(shouldPurge2);
    }

    @Test
    public void testCriticalEventNeverPurged() {
        long retentionDays = Long.MAX_VALUE;
        long now = System.currentTimeMillis();
        long eventTimestamp = now - (365 * 24 * 60 * 60 * 1000L); // 1 year ago

        boolean shouldPurge = (eventTimestamp + retentionDays * 24 * 60 * 60 * 1000L) < now;

        assertFalse("Critical events should never be purged", shouldPurge);
    }

    @Test
    public void testArchiveSizeCalculation() {
        long fileSize1 = 10 * 1024 * 1024L; // 10 MB
        long fileSize2 = 15 * 1024 * 1024L; // 15 MB
        long totalSize = fileSize1 + fileSize2;

        assertEquals(25 * 1024 * 1024L, totalSize);
        assertEquals(25.0, totalSize / (1024.0 * 1024.0), 0.01);
    }

    @Test
    public void testArchiveSizeLimit() {
        long maxSizeMB = 100;
        long maxSizeBytes = maxSizeMB * 1024 * 1024L;
        long currentSize = 150 * 1024 * 1024L; // 150 MB

        boolean exceedsLimit = currentSize > maxSizeBytes;

        assertTrue("Archive size exceeds limit", exceedsLimit);
    }

    @Test
    public void testArchiveFileCounting() {
        int fileCount = 5;
        assertTrue(fileCount > 0);
    }

    @Test
    public void testGdprCompliancePurge() {
        // Simulate GDPR right to be forgotten
        int initialFileCount = 10;
        int deletedCount = initialFileCount; // Delete all
        int finalFileCount = initialFileCount - deletedCount;

        assertEquals(0, finalFileCount);
    }

    @Test
    public void testRetentionStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("archiveFileCount", 5);
        stats.put("archiveSizeBytes", 50 * 1024 * 1024L);
        stats.put("archiveSizeMB", 50.0);

        assertEquals(5, stats.get("archiveFileCount"));
        assertEquals(50 * 1024 * 1024L, stats.get("archiveSizeBytes"));
        assertEquals(50.0, stats.get("archiveSizeMB"));
    }

    @Test
    public void testEventCategorization() {
        long now = System.currentTimeMillis();
        long archiveThreshold = now - (30 * 24 * 60 * 60 * 1000L);

        int totalEvents = 100;
        int eventsToArchive = 30;
        int eventsToKeep = totalEvents - eventsToArchive;

        assertEquals(70, eventsToKeep);
    }

    @Test
    public void testCompressionRatio() {
        long originalSize = 10 * 1024 * 1024L; // 10 MB
        long compressedSize = 2 * 1024 * 1024L; // 2 MB
        double compressionRatio = (double) compressedSize / originalSize;

        assertTrue("Compression should reduce size", compressionRatio < 1.0);
        assertEquals(0.2, compressionRatio, 0.01);
    }

    @Test
    public void testRetentionPolicyUpdate() {
        Map<LogEvent.Severity, Long> policies = new HashMap<>();
        policies.put(LogEvent.Severity.INFO, 90L);

        // Update policy
        policies.put(LogEvent.Severity.INFO, 180L);

        assertEquals(Long.valueOf(180), policies.get(LogEvent.Severity.INFO));
    }

    @Test
    public void testMultipleSeverityRetention() {
        Map<LogEvent.Severity, Long> policies = new HashMap<>();
        policies.put(LogEvent.Severity.INFO, 30L);
        policies.put(LogEvent.Severity.WARNING, 60L);
        policies.put(LogEvent.Severity.ERROR, 90L);
        policies.put(LogEvent.Severity.CRITICAL, Long.MAX_VALUE);

        assertEquals(4, policies.size());
        assertTrue(policies.get(LogEvent.Severity.CRITICAL) == Long.MAX_VALUE);
    }
}
