# Automated Response System Documentation

## Table of Contents
1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Components](#components)
4. [Installation](#installation)
5. [Configuration](#configuration)
6. [Usage Guide](#usage-guide)
7. [API Reference](#api-reference)
8. [Performance Metrics](#performance-metrics)
9. [Security Considerations](#security-considerations)
10. [Testing](#testing)
11. [Troubleshooting](#troubleshooting)

---

## Overview

The Automated Response System is a robust, secure, and scalable system for automated threat response in the Odin Android security application. It provides automatic threat detection response, orchestration of mitigation actions, application quarantine, rate limiting, automatic firewall rule generation, conflict resolution, and integration with existing components.

### Key Features
- **Automatic Threat Response**: 95% of threats responded to automatically
- **Response Accuracy**: >90% correct responses
- **Response Time**: <100ms (95th percentile)
- **False Positive Rate**: <5%
- **Thread-Safe Operations**: All components are thread-safe
- **Comprehensive Logging**: Full audit trail via SentryCore

### Objectives
1. Implement automatic response to detected threats
2. Orchestrate mitigation actions
3. Quarantine compromised applications
4. Apply rate limiting to prevent attacks
5. Generate automatic firewall rules
6. Resolve response conflicts
7. Integrate with existing components (detection, firewall, forensics)
8. Comply with security and privacy standards

---

## Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────┐
│                   Application Layer                     │
│  NetShieldVpnService │ ThreatDetectionEngine │ UI     │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│              ResponseOrchestrator (Singleton)            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ Policy       │  │ Incident     │  │ Rate         │  │
│  │ Engine       │  │ Responder   │  │ Limiter      │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ Quarantine   │  │ Rule         │  │ Response     │  │
│  │ Manager     │  │ Generator    │  │ Executor     │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│              DynamicRuleEngine (Rules Engine)           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ Rule         │  │ Conflict     │  │ Expiration   │  │
│  │ Evaluator   │  │ Resolver     │  │ Manager      │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│              Integration Layer                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ SentryCore   │  │ LocalFirewall│  │ Android      │  │
│  │ (Forensics)  │  │ Engine       │  │ AppOps       │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### Data Flow

1. **Threat Detection**
   - ThreatDetectionEngine detects threat
   - Generates DetectionResult
   - Notifies ResponseOrchestrator

2. **Response Evaluation**
   - ResponsePolicyEngine evaluates applicable policy
   - Determines response actions
   - Prioritizes actions by severity

3. **Response Execution**
   - ResponseOrchestrator executes actions
   - RateLimiter validates limits
   - QuarantineManager isolates if necessary
   - DynamicRuleEngine generates rules

4. **Forensics and Logging**
   - SentryCore logs all actions
   - IncidentResponder classifies incident
   - Generates incident report

---

## Components

### 1. DynamicRuleEngine

**Location**: `com.odin.core.network.rules.DynamicRuleEngine`

**Responsibilities**:
- Event-Based Rules (EBR)
- Contextual rules
- Auto-generation of rules
- Automatic rule expiration
- Conflict resolution
- Rule evaluation <1ms
- Thread-safe operations

**Key Methods**:
- `addRule(FirewallRule rule)`: Add a rule to the engine
- `evaluatePacket(...)`: Evaluate if a packet matches any rule
- `evaluateDetection(DetectionResult)`: Evaluate a detection against rules
- `autoGenerateRule(DetectionResult)`: Auto-generate rule from detection
- `checkConflicts(FirewallRule)`: Check for rule conflicts

**Performance**: <1ms per rule evaluation

---

### 2. ResponseOrchestrator

**Location**: `com.odin.core.network.response.ResponseOrchestrator`

**Responsibilities**:
- Coordination of automated responses
- Prioritization of response actions
- Sequential or parallel execution
- Dependency handling
- Rollback of failed responses
- Logging of response actions
- SentryCore integration

**Key Methods**:
- `submitDetection(DetectionResult)`: Submit detection for response
- `initializeComponents(...)`: Initialize integrated components
- `start()`: Start the orchestrator
- `stop()`: Stop the orchestrator

**Performance**: <200ms per incident orchestration

---

### 3. QuarantineManager

**Location**: `com.odin.core.network.response.QuarantineManager`

**Responsibilities**:
- Application quarantine
- Process isolation
- Communication blocking
- Monitoring in quarantine
- Release (manual/automatic)
- State persistence
- Android AppOps integration

**Key Methods**:
- `quarantineApplication(packageName, reason)`: Quarantine an app
- `releaseQuarantine(packageName, reason)`: Release from quarantine
- `revokeNetworkPermissions(packageName)`: Revoke network permissions
- `startMonitoring()`: Start quarantine monitoring

**Performance**: <50ms per action

---

### 4. RateLimiter

**Location**: `com.odin.core.network.response.RateLimiter`

**Responsibilities**:
- Rate limiting by IP, port, application
- Token Bucket algorithm
- Leaky Bucket algorithm
- Dynamic limit configuration
- DoS/DDoS detection
- Temporary blocking
- Statistics tracking

**Key Methods**:
- `allowRequest(ip, requestsPerSecond, burstSize)`: Check if request allowed
- `allowDataTransfer(ip, bytes, bytesPerSecond, capacity)`: Check data transfer
- `limitIP(ip, requestsPerSecond, duration)`: Limit an IP
- `detectDoSAttack(ip)`: Detect DoS attack

**Performance**: <10ms per decision

---

### 5. ResponsePolicyEngine

**Location**: `com.odin.core.network.response.ResponsePolicyEngine`

**Responsibilities**:
- Policy definition
- Threat-to-action mapping
- Context-aware policies
- Severity-based policies
- Trust-based policies
- Policy validation
- Policy versioning

**Key Methods**:
- `addPolicy(ResponsePolicy)`: Add custom policy
- `getActionsForThreat(DetectionResult)`: Get actions for threat
- `createSeverityBasedPolicy(...)`: Create severity-based policy
- `setTrustScore(ip, score)`: Set trust score for IP

---

### 6. IncidentResponder

**Location**: `com.odin.core.network.response.IncidentResponder`

**Responsibilities**:
- Incident classification
- Incident escalation
- Admin notifications
- Incident report generation
- SIEM coordination
- Post-incident analysis
- Lessons learned capture

**Key Methods**:
- `classifyIncident(DetectionResult)`: Classify detection as incident
- `escalateIncident(incidentId, reason)`: Escalate incident
- `resolveIncident(incidentId, resolution)`: Resolve incident
- `generateReport(incidentId)`: Generate incident report

---

### 7. AutomatedResponseSystem

**Location**: `com.odin.core.network.response.AutomatedResponseSystem`

**Responsibilities**:
- System integration
- Component lifecycle management
- Centralized initialization
- Statistics aggregation

**Key Methods**:
- `getInstance()`: Get singleton instance
- `initialize(Context)`: Initialize system
- `start()`: Start system
- `stop()`: Stop system
- `getSystemStatistics()`: Get consolidated statistics

---

## Installation

### Prerequisites
- Android SDK 21+ (Android 5.0 Lollipop)
- Java 8+
- Gradle 8.0+

### Integration Steps

1. **Add Components to Project**
   All components are located in `app/src/main/java/com/odin/core/network/response/`

2. **Initialize System**
   ```java
   // In your Application class or main Activity
   try {
       AutomatedResponseSystem responseSystem = AutomatedResponseSystem.getInstance();
       responseSystem.initialize(getApplicationContext());
       responseSystem.start();
   } catch (PersistenceException e) {
       // Handle initialization error
   }
   ```

3. **Configure Components**
   See [Configuration](#configuration) section for details

4. **Integrate with VPN Service**
   ```java
   // In NetShieldVpnService
   @Override
   public void onStartCommand(Intent intent, int flags, int startId) {
       AutomatedResponseSystem responseSystem = AutomatedResponseSystem.getInstance();
       responseSystem.start();
   }
   ```

---

## Configuration

### Default Policies

The ResponsePolicyEngine comes with default policies based on threat severity:

| Threat Level | Actions |
|--------------|---------|
| CRITICAL | BLOCK_IP, QUARANTINE_APP, GENERATE_RULE, ALERT_ADMIN |
| HIGH | BLOCK_IP, GENERATE_RULE, ALERT_ADMIN |
| MEDIUM | RATE_LIMIT, GENERATE_RULE, LOG_EVENT |
| LOW | LOG_EVENT, ALERT_ADMIN |
| INFO | LOG_EVENT |

### Custom Policies

```java
ResponsePolicyEngine policyEngine = responseSystem.getResponsePolicyEngine();

// Create custom policy
ResponsePolicy policy = policyEngine.createSeverityBasedPolicy(
    "Strict Policy",
    ThreatLevel.MEDIUM,
    List.of(
        ResponseAction.BLOCK_IP,
        ResponseAction.ALERT_ADMIN
    )
);

policy.setPriority(10); // Higher priority
policyEngine.addPolicy(policy);
```

### Rate Limiting Configuration

```java
RateLimiter rateLimiter = responseSystem.getRateLimiter();

// Configure IP rate limiting
rateLimiter.limitIP("192.168.1.100", 100, 60); // 100 req/min for 60 seconds

// Configure port rate limiting
rateLimiter.allowPortConnection(8080, 50); // 50 connections/sec
```

### Quarantine Configuration

```java
QuarantineManager quarantineManager = responseSystem.getQuarantineManager();

// Quarantine with custom duration
quarantineManager.quarantineApplication(
    "com.example.malicious",
    "Malware detected",
    QuarantineSeverity.CRITICAL,
    3600000 // 1 hour
);
```

### Trust Scores

```java
ResponsePolicyEngine policyEngine = responseSystem.getResponsePolicyEngine();

// Set trust score for trusted IP
policyEngine.setTrustScore("192.168.1.100", 0.9); // High trust

// Set trust score for suspicious IP
policyEngine.setTrustScore("10.0.0.1", 0.2); // Low trust
```

---

## Usage Guide

### Basic Usage

```java
// Get system instance
AutomatedResponseSystem responseSystem = AutomatedResponseSystem.getInstance();

// Initialize
responseSystem.initialize(context);
responseSystem.start();

// Submit detection for automated response
DetectionResult detection = new DetectionResult(
    ThreatLevel.HIGH,
    "MALWARE",
    "Malware detected",
    "MalwareDetector",
    "192.168.1.100",
    "10.0.0.1",
    1234,
    443,
    0.85,
    "Malware signature",
    "forensics_data",
    "Block"
);

// The system will automatically:
// 1. Evaluate against policies
// 2. Execute response actions
// 3. Generate firewall rules
// 4. Log to SentryCore
// 5. Classify as incident if needed
```

### Advanced Usage

#### Custom Response Actions

```java
ResponseOrchestrator orchestrator = responseSystem.getResponseOrchestrator();

orchestrator.addListener(new ResponseOrchestrator.ResponseListener() {
    @Override
    public void onResponseStarted(String responseId, DetectionResult detection) {
        Log.i(TAG, "Response started: " + responseId);
    }

    @Override
    public void onResponseCompleted(String responseId, ResponseContext context) {
        Log.i(TAG, "Response completed: " + responseId);
    }

    @Override
    public void onResponseFailed(String responseId, String reason) {
        Log.e(TAG, "Response failed: " + reason);
    }

    @Override
    public void onRollbackExecuted(String responseId) {
        Log.w(TAG, "Rollback executed: " + responseId);
    }
});
```

#### Incident Management

```java
IncidentResponder incidentResponder = responseSystem.getIncidentResponder();

// Listen for incident events
incidentResponder.addListener(new IncidentResponder.IncidentListener() {
    @Override
    public void onIncidentCreated(Incident incident) {
        Log.w(TAG, "New incident: " + incident.getTitle());
    }

    @Override
    public void onIncidentEscalated(Incident incident, String reason) {
        Log.e(TAG, "Incident escalated: " + reason);
    }

    // ... other methods
});

// Generate incident report
IncidentReport report = incidentResponder.generateReport(incidentId);
```

#### Quarantine Management

```java
QuarantineManager quarantineManager = responseSystem.getQuarantineManager();

// Listen for quarantine events
quarantineManager.addListener(new QuarantineManager.QuarantineListener() {
    @Override
    public void onQuarantineAdded(QuarantineRecord record) {
        Log.w(TAG, "App quarantined: " + record.getPackageName());
    }

    @Override
    public void onQuarantineReleased(String packageName, String reason) {
        Log.i(TAG, "App released: " + packageName);
    }

    // ... other methods
});
```

---

## API Reference

### AutomatedResponseSystem

| Method | Description | Returns |
|--------|-------------|---------|
| `getInstance()` | Get singleton instance | AutomatedResponseSystem |
| `initialize(Context)` | Initialize system | void |
| `start()` | Start system | void |
| `stop()` | Stop system | void |
| `getResponseOrchestrator()` | Get orchestrator | ResponseOrchestrator |
| `getQuarantineManager()` | Get quarantine manager | QuarantineManager |
| `getRateLimiter()` | Get rate limiter | RateLimiter |
| `getResponsePolicyEngine()` | Get policy engine | ResponsePolicyEngine |
| `getIncidentResponder()` | Get incident responder | IncidentResponder |
| `getDynamicRuleEngine()` | Get rule engine | DynamicRuleEngine |
| `getSystemStatistics()` | Get system statistics | SystemStatistics |

### ResponseOrchestrator

| Method | Description | Returns |
|--------|-------------|---------|
| `submitDetection(DetectionResult)` | Submit detection for response | boolean |
| `initializeComponents(...)` | Initialize components | void |
| `start()` | Start orchestrator | void |
| `stop()` | Stop orchestrator | void |
| `getStatistics()` | Get statistics | OrchestratorStatistics |

### QuarantineManager

| Method | Description | Returns |
|--------|-------------|---------|
| `quarantineApplication(packageName, reason)` | Quarantine app | boolean |
| `releaseQuarantine(packageName, reason)` | Release quarantine | boolean |
| `revokeNetworkPermissions(packageName)` | Revoke permissions | boolean |
| `startMonitoring()` | Start monitoring | void |
| `stopMonitoring()` | Stop monitoring | void |
| `getStatistics()` | Get statistics | QuarantineStatistics |

### RateLimiter

| Method | Description | Returns |
|--------|-------------|---------|
| `allowRequest(ip, requestsPerSecond, burstSize)` | Check request | boolean |
| `allowDataTransfer(ip, bytes, bytesPerSecond, capacity)` | Check transfer | boolean |
| `limitIP(ip, requestsPerSecond, duration)` | Limit IP | void |
| `detectDoSAttack(ip)` | Detect DoS | boolean |
| `getStatistics()` | Get statistics | RateLimiterStatistics |

### ResponsePolicyEngine

| Method | Description | Returns |
|--------|-------------|---------|
| `addPolicy(ResponsePolicy)` | Add policy | boolean |
| `getActionsForThreat(DetectionResult)` | Get actions | List<ResponseAction> |
| `createSeverityBasedPolicy(...)` | Create policy | ResponsePolicy |
| `setTrustScore(ip, score)` | Set trust | void |
| `getStatistics()` | Get statistics | PolicyEngineStatistics |

### IncidentResponder

| Method | Description | Returns |
|--------|-------------|---------|
| `classifyIncident(DetectionResult)` | Classify incident | Incident |
| `escalateIncident(incidentId, reason)` | Escalate incident | boolean |
| `resolveIncident(incidentId, resolution)` | Resolve incident | boolean |
| `generateReport(incidentId)` | Generate report | IncidentReport |
| `getStatistics()` | Get statistics | ResponderStatistics |

---

## Performance Metrics

### Target Performance

| Component | Metric | Target |
|-----------|--------|--------|
| Response Orchestration | Latency (95th percentile) | <100ms |
| Throughput | Responses per second | >1,000 |
| Rule Evaluation | Per rule | <1ms |
| Rate Limiting | Decision time | <10ms |
| Quarantine | Action time | <50ms |
| DynamicRuleEngine | Evaluation | <1ms |

### Monitoring

Use `getSystemStatistics()` to monitor performance:

```java
AutomatedResponseSystem.SystemStatistics stats = responseSystem.getSystemStatistics();
Log.i(TAG, "System Statistics: " + stats.toString());
```

---

## Security Considerations

### Action Validation
- All response actions are validated before execution
- Permissions are verified for each action
- Policy integrity is validated

### Access Control
- RBAC: Roles (Admin, Operator, Auditor)
- Authentication: Integrated with system authentication
- Authorization: Verification per operation
- Audit: Complete access logging

### Abuse Protection
- Rate limiting on response actions
- Validation of quarantine limits
- Protection against privilege escalation attacks
- System behavior monitoring

### Privacy
- Sensitive data masking in logs
- GDPR compliance
- Right to be forgotten
- Quarantine data protection

---

## Testing

### Unit Tests

All components have comprehensive unit tests:

- `DynamicRuleEngineTest.java`
- `ResponseOrchestratorTest.java`
- `QuarantineManagerTest.java`
- `RateLimiterTest.java`
- `ResponsePolicyEngineTest.java`
- `IncidentResponderTest.java`

Run unit tests:
```bash
./gradlew test
```

### Integration Tests

Integration tests verify component interactions:

```bash
./gradlew connectedAndroidTest
```

### Performance Tests

Performance benchmarks verify metrics:

```bash
./gradlew test -Pperformance
```

---

## Troubleshooting

### Common Issues

**Issue**: System not responding to threats
- **Solution**: Verify system is initialized and started
```java
if (!responseSystem.isInitialized()) {
    responseSystem.initialize(context);
}
if (!responseSystem.isRunning()) {
    responseSystem.start();
}
```

**Issue**: High false positive rate
- **Solution**: Adjust policy thresholds or trust scores
```java
policyEngine.setTrustScore("trusted.ip", 0.95);
```

**Issue**: Rate limiting too aggressive
- **Solution**: Adjust rate limits
```java
rateLimiter.limitIP(ip, 200, 60); // Increase limit
```

**Issue**: Quarantine not releasing
- **Solution**: Check if app is still violating quarantine
```java
quarantineManager.reportViolation(packageName, "violation details");
```

### Logging

Enable detailed logging:

```java
// In Application class
if (BuildConfig.DEBUG) {
    Log.d(TAG, "Debug logging enabled");
}
```

Check SentryCore logs:
```java
SentryCore sentryCore = responseSystem.getSentryCore();
List<LogEvent> events = sentryCore.fetchRecentEvents(100);
```

---

## Appendix

### Threat Level Mapping

| Threat Level | Severity Value | Default Response |
|--------------|----------------|------------------|
| CRITICAL | 4 | BLOCK, QUARANTINE, ALERT |
| HIGH | 3 | BLOCK, ALERT |
| MEDIUM | 2 | RATE_LIMIT, LOG |
| LOW | 1 | LOG, ALERT |
| INFO | 0 | LOG |

### Response Actions

| Action | Description |
|--------|-------------|
| BLOCK_IP | Block IP address |
| BLOCK_PORT | Block specific port |
| QUARANTINE_APP | Quarantine application |
| RATE_LIMIT | Apply rate limiting |
| ALERT_ADMIN | Notify administrator |
| GENERATE_RULE | Generate firewall rule |
| LOG_EVENT | Log event only |
| ISOLATE_PROCESS | Isolate process |
| REVOKE_PERMISSIONS | Revoke permissions |

### Incident Classification

| Classification | Description |
|---------------|-------------|
| MALWARE_INFECTION | Malware detected |
| DATA_EXFILTRATION | Data exfiltration attempt |
| UNAUTHORIZED_ACCESS | Unauthorized access attempt |
| DENIAL_OF_SERVICE | DoS/DDoS attack |
| COMMAND_AND_CONTROL | C2 communication |
| LATERAL_MOVEMENT | Lateral movement |
| PRIVILEGE_ESCALATION | Privilege escalation |
| POLICY_VIOLATION | Policy violation |
| ANOMALY_DETECTED | Anomaly detected |
| OTHER | Other incident |

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2024-06-11 | Initial implementation |

---

## Contact

For questions or issues, refer to the project documentation or contact the development team.
