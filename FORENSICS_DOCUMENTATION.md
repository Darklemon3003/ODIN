# ODIN Forensics & Logging System - Sprint 4 Documentation

## Overview

The ODIN Forensics & Logging system provides comprehensive digital forensics capabilities, secure logging, and incident analysis for the ODIN security platform. This system enables complete capture, storage, analysis, and export of security events with enterprise-grade encryption and compliance features.

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                   Application Layer                     │
│  NetShieldVpnService │ ThreatDetectionEngine │ UI     │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│                  SentryCore (Singleton)                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ Log Manager  │  │ Retention    │  │ Exporter     │  │
│  │              │  │ Manager      │  │              │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ Forensic     │  │ Health       │  │ Exception    │  │
│  │ Analyzer     │  │ Monitor      │  │ Handler      │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│              CryptographicManager (AES-256-GCM)          │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│         EnhancedSecureLocalStorage (Encrypted)           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ Log Events   │  │ System       │  │ Firewall     │  │
│  │ (Encrypted)  │  │ Status       │  │ Rules        │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
```

## Components

### 1. SentryCore

**Package:** `com.odin.core.persistence.SentryCore`

**Purpose:** Central persistence orchestrator for all security events and system data.

**Key Features:**
- Singleton thread-safe pattern with lazy initialization
- Automatic encryption/decryption via CryptographicManager
- Log rotation with configurable size limits (default: 5,000 events)
- System health monitoring with heartbeat
- Auto-healing on persistence failures
- Thread-safe operations using synchronized blocks and atomic variables

**API Methods:**

```java
// Get singleton instance
SentryCore sentryCore = SentryCore.getInstance(context);

// Append a log event
sentryCore.appendEvent(logEvent);

// Fetch recent events
List<LogEvent> events = sentryCore.fetchRecentEvents(100);

// Save/load system status
sentryCore.saveSystemStatus(systemStatus);
SystemStatus status = sentryCore.loadSystemStatus();

// Save/load firewall rules
sentryCore.saveFirewallRules(rules);
List<FirewallRule> rules = sentryCore.loadFirewallRules();

// Get system statistics
JSONObject stats = sentryCore.getSystemStatistics();

// Check system health
boolean healthy = sentryCore.isHealthy();
```

**Thread Safety:**
- All I/O operations are synchronized
- Atomic variables for counters (crashCount, lastHeartbeat)
- AtomicBoolean for health status

### 2. LogEvent

**Package:** `com.odin.core.domain.model.LogEvent`

**Purpose:** Immutable model for security log events with enhanced forensics capabilities.

**Key Features:**
- Severity levels: INFO, WARNING, ERROR, CRITICAL
- Optional metadata for additional context
- JSON serialization/deserialization
- SHA-256 integrity hash calculation
- Builder pattern for fluent construction

**Severity Levels:**

```java
public enum Severity {
    INFO,      // Normal informational events
    WARNING,   // Potentially concerning events
    ERROR,     // Error conditions
    CRITICAL   // Critical security incidents
}
```

**API Methods:**

```java
// Constructor with all fields
LogEvent event = new LogEvent(
    timestamp,
    source,
    message,
    severity,
    metadata
);

// Builder pattern
LogEvent event = LogEvent.builder()
    .timestamp(System.currentTimeMillis())
    .source("VPN")
    .message("Connection established")
    .severity(LogEvent.Severity.INFO)
    .addMetadata("ip", "192.168.1.1")
    .build();

// JSON serialization
JSONObject json = event.toJson();
String jsonString = event.toJsonString();

// Integrity verification
boolean valid = event.verifyIntegrity();

// Getters
long timestamp = event.getTimestamp();
String source = event.getSource();
String message = event.getMessage();
LogEvent.Severity severity = event.getSeverity();
Map<String, String> metadata = event.getMetadata();
String integrityHash = event.getIntegrityHash();
```

### 3. ForensicAnalyzer

**Package:** `com.odin.core.forensics.ForensicAnalyzer`

**Purpose:** Advanced forensic analysis for incident investigation and timeline reconstruction.

**Key Features:**
- Query logs by time range, severity, source, or custom filters
- Timeline reconstruction with event grouping
- Attack pattern detection
- Forensic report generation
- Multi-format evidence export (JSON, CSV, XML)

**API Methods:**

```java
// Get singleton instance
ForensicAnalyzer analyzer = ForensicAnalyzer.getInstance(context);

// Query by time range
List<LogEvent> events = analyzer.queryLogsByTimeRange(startTime, endTime);

// Query by severity
List<LogEvent> criticalEvents = analyzer.queryLogsBySeverity(LogEvent.Severity.CRITICAL);

// Query by source
List<LogEvent> vpnEvents = analyzer.queryLogsBySource("VPN");

// Query with custom filters
Map<String, Object> filters = new HashMap<>();
filters.put("severity", LogEvent.Severity.ERROR);
filters.put("source", "THREAT_DETECTION");
List<LogEvent> filteredEvents = analyzer.queryLogsWithFilters(filters);

// Reconstruct timeline
ForensicTimeline timeline = analyzer.reconstructTimeline(startTime, endTime);

// Generate forensic report
JSONObject report = analyzer.generateForensicReport(startTime, endTime);

// Export evidence
String jsonExport = analyzer.exportEvidenceAsJson(startTime, endTime);
String csvExport = analyzer.exportEvidenceAsCsv(startTime, endTime);
String xmlExport = analyzer.exportEvidenceAsXml(startTime, endTime);
```

**Timeline Structure:**

```java
public static class ForensicTimeline {
    private long startTime;
    private long endTime;
    private int totalEvents;
    private Map<String, List<LogEvent>> eventsBySource;
    private List<AttackPattern> attackPatterns;
    private Map<String, Object> statistics;
}
```

### 4. LogRetentionManager

**Package:** `com.odin.core.forensics.LogRetentionManager`

**Purpose:** Manages log retention policies, archiving, and GDPR compliance.

**Key Features:**
- Configurable retention policies by severity
- Automatic archiving of old logs with GZIP compression
- Secure purging of expired data
- GDPR compliance (right to be forgotten)
- Archive size management

**API Methods:**

```java
// Get singleton instance
LogRetentionManager retentionManager = LogRetentionManager.getInstance(context);

// Set retention policy for a severity
retentionManager.setRetentionPolicy(LogEvent.Severity.ERROR, 180); // 180 days

// Get retention policy
long days = retentionManager.getRetentionPolicy(LogEvent.Severity.CRITICAL);

// Execute retention process
RetentionResult result = retentionManager.executeRetention();

// Cleanup old archives
int deletedCount = retentionManager.cleanupOldArchives();

// Get statistics
Map<String, Object> stats = retentionManager.getRetentionStatistics();

// GDPR compliance - purge all archives
int purgedCount = retentionManager.purgeAllArchives();
```

**Default Retention Policies:**
- INFO: 90 days
- WARNING: 90 days
- ERROR: 180 days
- CRITICAL: Permanent (Long.MAX_VALUE)

### 5. LogExporter

**Package:** `com.odin.core.forensics.LogExporter`

**Purpose:** Multi-format log export with data masking and filtering capabilities.

**Key Features:**
- Export to JSON, CSV, XML, PDF formats
- Advanced filtering (time range, severity, source, text search)
- Data masking for sensitive information (IPs, tokens)
- SIEM integration support
- Digital signature support

**API Methods:**

```java
// Get singleton instance
LogExporter exporter = LogExporter.getInstance(context);

// Export with filters
Map<String, Object> filters = new HashMap<>();
filters.put("severity", LogEvent.Severity.ERROR);
filters.put("search", "threat");

ExportResult result = exporter.exportLogs(
    LogExporter.ExportFormat.JSON,
    startTime,
    endTime,
    filters,
    true  // mask sensitive data
);

// Save to file
String filePath = exporter.saveToFile(result, "forensic_export");

// Get export summary
Map<String, Object> summary = exporter.getExportSummary(startTime, endTime);
```

**Export Formats:**

```java
public enum ExportFormat {
    JSON,  // Structured JSON with metadata
    CSV,   // Comma-separated values
    XML,   // XML format with schema
    PDF    // Text-based PDF format
}
```

## Security Features

### Encryption

- **Algorithm:** AES-256-GCM
- **Key Management:** AndroidKeyStore with hardware-backed keys (API 23+)
- **IV:** Unique 12-byte IV per encryption operation
- **Key Rotation:** Every 90 days (configurable)

### Integrity

- **Hash Algorithm:** SHA-256
- **Digital Signature:** RSA-2048 (for critical logs)
- **Verification:** Automatic on every read operation

### Data Masking

- **IP Addresses:** Partial masking (e.g., 192.168.1.***)
- **Tokens/Keys:** Complete redaction (e.g., token=***REDACTED***)
- **Personal Data:** Anonymization for GDPR compliance

### Access Control

- **RBAC:** Roles (Admin, Auditor, Analyst, Viewer)
- **Authentication:** Integrated with system authentication
- **Authorization:** Per-operation verification
- **Audit Trail:** 100% of accesses logged

## Performance Metrics

### Targets

- **Write Latency:** <10ms (95th percentile)
- **Read Latency:** <100ms (95th percentile)
- **Throughput:** >10,000 logs/second
- **Encryption/Decryption:** <5ms per operation
- **Compression:** >70% size reduction
- **Search:** <100ms for simple queries

### Benchmarks

All performance tests in `ForensicsPerformanceTest.java` verify:
- Event creation: <100ms for 1,000 events
- JSON serialization: >10,000 events/second
- Integrity verification: >10,000 events/second
- Filtering: <100ms for 10,000 events
- CSV export: >10,000 events/second

## Compliance

### GDPR

- **Right to Access:** Complete export capabilities
- **Right to Rectification:** Editable metadata
- **Right to Erasure:** `purgeAllArchives()` method
- **Data Portability:** Multi-format export
- **Breach Notification:** Automatic critical event logging

### Security Standards

- **ISO 27001:** Information security management
- **NIST 800-53:** Security and privacy controls
- **PCI DSS:** Payment card industry compliance (if applicable)

## Integration Points

### NetShieldVpnService

```java
// Enhanced logging with severity and metadata
LogEvent event = LogEvent.builder()
    .timestamp(System.currentTimeMillis())
    .source("THREAT_DETECTION")
    .message("Threat detected")
    .severity(mapThreatLevelToSeverity(threatLevel))
    .metadata(metadata)
    .build();

sentryCore.appendEvent(event);
```

### ThreatDetectionEngine

```java
// Context-based SentryCore initialization
ThreatDetectionEngine engine = new ThreatDetectionEngine(context);

// Automatic forensic logging on threat detection
private void logDetectionToSentryCore(DetectionResult detection) {
    LogEvent event = LogEvent.builder()
        .timestamp(System.currentTimeMillis())
        .source("THREAT_DETECTION_ENGINE")
        .message("Threat detected: " + detection.getThreatType())
        .severity(mapSeverity(detection.getThreatLevel()))
        .metadata(createMetadata(detection))
        .build();
    
    sentryCore.appendEvent(event);
}
```

## Testing

### Unit Tests

- `LogEventTest.java`: 18 tests for LogEvent model
- `ForensicAnalyzerTest.java`: 15 tests for forensic analysis
- `LogRetentionManagerTest.java`: 17 tests for retention management
- `LogExporterTest.java`: 18 tests for export functionality

### Integration Tests

- `ForensicsIntegrationTest.java`: 10 tests for component interaction

### Performance Tests

- `ForensicsPerformanceTest.java`: 13 benchmarks for performance verification

### Security Tests

- `ForensicsSecurityTest.java`: 20 tests for security features

## Usage Examples

### Example 1: Basic Logging

```java
// Initialize SentryCore
SentryCore sentryCore = SentryCore.getInstance(context);

// Create and log an event
LogEvent event = LogEvent.builder()
    .timestamp(System.currentTimeMillis())
    .source("VPN")
    .message("VPN connection established")
    .severity(LogEvent.Severity.INFO)
    .build();

sentryCore.appendEvent(event);
```

### Example 2: Incident Investigation

```java
// Initialize ForensicAnalyzer
ForensicAnalyzer analyzer = ForensicAnalyzer.getInstance(context);

// Define incident time range
long startTime = incidentStartTime;
long endTime = incidentEndTime;

// Reconstruct timeline
ForensicTimeline timeline = analyzer.reconstructTimeline(startTime, endTime);

// Generate forensic report
JSONObject report = analyzer.generateForensicReport(startTime, endTime);

// Export evidence
String evidence = analyzer.exportEvidenceAsJson(startTime, endTime);
```

### Example 3: Compliance Export

```java
// Initialize LogExporter
LogExporter exporter = LogExporter.getInstance(context);

// Define audit period
long auditStart = System.currentTimeMillis() - (90 * 24 * 60 * 60 * 1000L);
long auditEnd = System.currentTimeMillis();

// Export with data masking
ExportResult result = exporter.exportLogs(
    LogExporter.ExportFormat.CSV,
    auditStart,
    auditEnd,
    new HashMap<>(),
    true  // mask sensitive data
);

// Save to file
String filePath = exporter.saveToFile(result, "audit_export_2024");
```

### Example 4: Retention Management

```java
// Initialize LogRetentionManager
LogRetentionManager retentionManager = LogRetentionManager.getInstance(context);

// Set custom retention policies
retentionManager.setRetentionPolicy(LogEvent.Severity.ERROR, 365); // 1 year
retentionManager.setRetentionPolicy(LogEvent.Severity.CRITICAL, Long.MAX_VALUE); // Permanent

// Execute retention process
RetentionResult result = retentionManager.executeRetention();

if (result.isSuccess()) {
    Log.i(TAG, "Archived: " + result.getEventsToArchive() + 
                 ", Purged: " + result.getPurgedCount());
}
```

## Troubleshooting

### Common Issues

**Issue:** SentryCore initialization fails
- **Solution:** Ensure AndroidKeyStore is available (API 23+)
- **Check:** Verify context is ApplicationContext

**Issue:** Events not being persisted
- **Solution:** Check storage permissions
- **Check:** Verify CryptographicManager key availability

**Issue:** Export fails with large datasets
- **Solution:** Use time range filtering to reduce dataset size
- **Check:** Verify available storage space

**Issue:** Integrity verification fails
- **Solution:** Data may be corrupted; SentryCore will auto-heal
- **Check:** Review SentryCore logs for corruption markers

## Best Practices

1. **Always use severity levels** appropriately to enable proper filtering
2. **Include relevant metadata** for better forensic analysis
3. **Set appropriate retention policies** based on compliance requirements
4. **Use data masking** when exporting for external parties
5. **Regularly execute retention** to manage storage
6. **Monitor system health** via SentryCore statistics
7. **Export evidence promptly** after incident detection
8. **Verify integrity** of critical logs before analysis
9. **Test backup and restore** procedures regularly
10. **Document custom retention policies** for audit purposes

## Future Enhancements

- Real-time log streaming to SIEM
- Machine learning-based anomaly detection
- Blockchain-based immutable audit trail
- Distributed log storage
- Advanced visualization dashboard
- Automated incident response integration
- Cross-platform log aggregation

## Support

For issues or questions regarding the Forensics & Logging system, refer to:
- Sprint 4 requirements document
- API documentation (Javadoc)
- Test cases for usage examples
- Architecture diagrams in this document
