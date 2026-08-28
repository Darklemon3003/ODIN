package com.odin.core.network.response;

import androidx.annotation.NonNull;

import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatLevel;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for IncidentResponder.
 * 
 * Tests:
 * - Incident classification
 * - Escalation of incidents
 * - Admin notifications
 * - Incident reports
 * - SIEM coordination
 * - Post-incident analysis
 * - Lessons learned
 */
public class IncidentResponderTest {

    private IncidentResponder incidentResponder;

    @Before
    public void setUp() {
        incidentResponder = new IncidentResponder();
    }

    @Test
    public void testClassifyIncident() {
        DetectionResult detection = createDetectionResult(ThreatLevel.CRITICAL, "MALWARE");
        
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        assertNotNull("Incident should be created", incident);
        assertEquals("Status should be OPEN", IncidentResponder.IncidentStatus.OPEN, incident.getStatus());
        assertNotNull("Should have initial detection", incident.getInitialDetection());
    }

    @Test
    public void testClassifyIncidentLowSeverity() {
        DetectionResult detection = createDetectionResult(ThreatLevel.INFO, "INFO_THREAT");
        
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        assertNull("INFO threats should not create incidents", incident);
    }

    @Test
    public void testIncidentClassificationMapping() {
        // Test different threat types map to correct classifications
        DetectionResult malware = createDetectionResult(ThreatLevel.HIGH, "MALWARE_VARIANT");
        DetectionResult exfil = createDetectionResult(ThreatLevel.HIGH, "EXFILTRATION_DETECTED");
        DetectionResult c2 = createDetectionResult(ThreatLevel.CRITICAL, "C2_BEACON");
        DetectionResult dos = createDetectionResult(ThreatLevel.HIGH, "DOS_ATTACK");

        IncidentResponder.Incident malwareIncident = incidentResponder.classifyIncident(malware);
        IncidentResponder.Incident exfilIncident = incidentResponder.classifyIncident(exfil);
        IncidentResponder.Incident c2Incident = incidentResponder.classifyIncident(c2);
        IncidentResponder.Incident dosIncident = incidentResponder.classifyIncident(dos);

        assertNotNull("Malware should create incident", malwareIncident);
        assertEquals("Should be MALWARE_INFECTION", 
                IncidentResponder.IncidentClassification.MALWARE_INFECTION, malwareIncident.getClassification());

        assertNotNull("Exfiltration should create incident", exfilIncident);
        assertEquals("Should be DATA_EXFILTRATION", 
                IncidentResponder.IncidentClassification.DATA_EXFILTRATION, exfilIncident.getClassification());

        assertNotNull("C2 should create incident", c2Incident);
        assertEquals("Should be COMMAND_AND_CONTROL", 
                IncidentResponder.IncidentClassification.COMMAND_AND_CONTROL, c2Incident.getClassification());

        assertNotNull("DoS should create incident", dosIncident);
        assertEquals("Should be DENIAL_OF_SERVICE", 
                IncidentResponder.IncidentClassification.DENIAL_OF_SERVICE, dosIncident.getClassification());
    }

    @Test
    public void testEscalateIncident() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        assertNotNull("Incident should be created", incident);
        
        boolean escalated = incidentResponder.escalateIncident(incident.getIncidentId(), "Threat spreading rapidly");
        
        assertTrue("Incident should be escalated", escalated);
        assertTrue("Incident should be marked as escalated", incident.isEscalated());
        assertEquals("Status should be ESCALATED", IncidentResponder.IncidentStatus.ESCALATED, incident.getStatus());
    }

    @Test
    public void testResolveIncident() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        assertNotNull("Incident should be created", incident);
        
        boolean resolved = incidentResponder.resolveIncident(incident.getIncidentId(), "Malware removed and system cleaned");
        
        assertTrue("Incident should be resolved", resolved);
        assertEquals("Status should be RESOLVED", IncidentResponder.IncidentStatus.RESOLVED, incident.getStatus());
        assertNotNull("Should have resolution", incident.getResolution());
        assertTrue("Resolved time should be set", incident.getResolvedTime() > 0);
    }

    @Test
    public void testCloseIncident() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        assertNotNull("Incident should be created", incident);
        
        boolean closed = incidentResponder.closeIncident(incident.getIncidentId());
        
        assertTrue("Incident should be closed", closed);
        assertEquals("Status should be CLOSED", IncidentResponder.IncidentStatus.CLOSED, incident.getStatus());
        
        // Should be in closed incidents
        List<IncidentResponder.Incident> closedIncidents = incidentResponder.getClosedIncidents();
        assertTrue("Should be in closed incidents", closedIncidents.size() > 0);
        
        // Should not be in active incidents
        List<IncidentResponder.Incident> activeIncidents = incidentResponder.getActiveIncidents();
        assertFalse("Should not be in active incidents", 
                activeIncidents.stream().anyMatch(i -> i.getIncidentId().equals(incident.getIncidentId())));
    }

    @Test
    public void testAddRelatedDetection() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        assertNotNull("Incident should be created", incident);
        
        DetectionResult relatedDetection = createDetectionResult(ThreatLevel.MEDIUM, "ANOMALY");
        boolean added = incidentResponder.addRelatedDetection(incident.getIncidentId(), relatedDetection);
        
        assertTrue("Related detection should be added", added);
        assertEquals("Should have 1 related detection", 1, incident.getRelatedDetections().size());
    }

    @Test
    public void testAddActionTaken() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        assertNotNull("Incident should be created", incident);
        
        boolean added = incidentResponder.addActionTaken(incident.getIncidentId(), "Blocked malicious IP");
        
        assertTrue("Action should be added", added);
        assertEquals("Should have 1 action", 1, incident.getActionsTaken().size());
    }

    @Test
    public void testGenerateReport() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        assertNotNull("Incident should be created", incident);
        
        IncidentResponder.IncidentReport report = incidentResponder.generateReport(incident.getIncidentId());
        
        assertNotNull("Report should be generated", report);
        assertEquals("Report should reference incident", incident.getIncidentId(), report.getIncidentId());
        assertNotNull("Report should have summary", report.getSummary());
        assertNotNull("Report should have timeline", report.getTimeline());
        assertNotNull("Report should have root cause analysis", report.getRootCauseAnalysis());
        assertNotNull("Report should have impact assessment", report.getImpactAssessment());
        assertNotNull("Report should have recommendations", report.getRecommendations());
        assertNotNull("Report should have lessons learned", report.getLessonsLearned());
    }

    @Test
    public void testGetActiveIncidents() {
        DetectionResult detection1 = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        DetectionResult detection2 = createDetectionResult(ThreatLevel.MEDIUM, "ANOMALY");
        
        incidentResponder.classifyIncident(detection1);
        incidentResponder.classifyIncident(detection2);
        
        List<IncidentResponder.Incident> activeIncidents = incidentResponder.getActiveIncidents();
        
        assertTrue("Should have active incidents", activeIncidents.size() >= 2);
    }

    @Test
    public void testGetClosedIncidents() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        assertNotNull("Incident should be created", incident);
        incidentResponder.closeIncident(incident.getIncidentId());
        
        List<IncidentResponder.Incident> closedIncidents = incidentResponder.getClosedIncidents();
        
        assertTrue("Should have closed incidents", closedIncidents.size() >= 1);
    }

    @Test
    public void testGetIncident() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        assertNotNull("Incident should be created", incident);
        
        IncidentResponder.Incident retrieved = incidentResponder.getIncident(incident.getIncidentId());
        
        assertNotNull("Incident should be retrievable", retrieved);
        assertEquals("Should match original", incident.getIncidentId(), retrieved.getIncidentId());
    }

    @Test
    public void testIncidentSeverityMapping() {
        DetectionResult critical = createDetectionResult(ThreatLevel.CRITICAL, "MALWARE");
        DetectionResult high = createDetectionResult(ThreatLevel.HIGH, "ANOMALY");
        DetectionResult medium = createDetectionResult(ThreatLevel.MEDIUM, "SUSPICIOUS");
        DetectionResult low = createDetectionResult(ThreatLevel.LOW, "INFO");

        IncidentResponder.Incident criticalIncident = incidentResponder.classifyIncident(critical);
        IncidentResponder.Incident highIncident = incidentResponder.classifyIncident(high);
        IncidentResponder.Incident mediumIncident = incidentResponder.classifyIncident(medium);
        IncidentResponder.Incident lowIncident = incidentResponder.classifyIncident(low);

        assertNotNull("Critical should create incident", criticalIncident);
        assertEquals("Should be CRITICAL severity", IncidentResponder.IncidentSeverity.CRITICAL, criticalIncident.getSeverity());

        assertNotNull("High should create incident", highIncident);
        assertEquals("Should be HIGH severity", IncidentResponder.IncidentSeverity.HIGH, highIncident.getSeverity());

        assertNotNull("Medium should create incident", mediumIncident);
        assertEquals("Should be MEDIUM severity", IncidentResponder.IncidentSeverity.MEDIUM, mediumIncident.getSeverity());

        assertNotNull("Low should create incident", lowIncident);
        assertEquals("Should be LOW severity", IncidentResponder.IncidentSeverity.LOW, lowIncident.getSeverity());
    }

    @Test
    public void testIncidentMetadata() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        assertNotNull("Incident should be created", incident);
        
        incident.addMetadata("customKey", "customValue");
        
        assertEquals("Should have custom metadata", "customValue", incident.getMetadata().get("customKey"));
    }

    @Test
    public void testIncidentDuration() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        assertNotNull("Incident should be created", incident);
        
        long initialDuration = incident.getDuration();
        assertTrue("Duration should be positive", initialDuration >= 0);
        
        incidentResponder.resolveIncident(incident.getIncidentId(), "Resolved");
        
        long resolvedDuration = incident.getDuration();
        assertTrue("Resolved duration should be >= initial", resolvedDuration >= initialDuration);
    }

    @Test
    public void testIncidentListener() {
        final boolean[] incidentCreated = {false};
        final boolean[] incidentEscalated = {false};
        final boolean[] incidentResolved = {false};
        final boolean[] incidentClosed = {false};
        final boolean[] adminNotified = {false};

        IncidentResponder.IncidentListener listener = new IncidentResponder.IncidentListener() {
            @Override
            public void onIncidentCreated(IncidentResponder.Incident incident) {
                incidentCreated[0] = true;
            }

            @Override
            public void onIncidentEscalated(IncidentResponder.Incident incident, String reason) {
                incidentEscalated[0] = true;
            }

            @Override
            public void onIncidentResolved(IncidentResponder.Incident incident) {
                incidentResolved[0] = true;
            }

            @Override
            public void onIncidentClosed(IncidentResponder.Incident incident) {
                incidentClosed[0] = true;
            }

            @Override
            public void onAdminNotified(String incidentId, String message) {
                adminNotified[0] = true;
            }
        };

        incidentResponder.addListener(listener);

        DetectionResult detection = createDetectionResult(ThreatLevel.CRITICAL, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);

        assertTrue("Incident created listener should be called", incidentCreated[0]);
        assertTrue("Admin notified listener should be called for critical", adminNotified[0]);

        incidentResponder.escalateIncident(incident.getIncidentId(), "Test escalation");
        assertTrue("Incident escalated listener should be called", incidentEscalated[0]);

        incidentResponder.resolveIncident(incident.getIncidentId(), "Test resolution");
        assertTrue("Incident resolved listener should be called", incidentResolved[0]);

        incidentResponder.closeIncident(incident.getIncidentId());
        assertTrue("Incident closed listener should be called", incidentClosed[0]);

        incidentResponder.removeListener(listener);
    }

    @Test
    public void testStatistics() {
        DetectionResult detection1 = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        DetectionResult detection2 = createDetectionResult(ThreatLevel.MEDIUM, "ANOMALY");

        incidentResponder.classifyIncident(detection1);
        incidentResponder.classifyIncident(detection2);

        IncidentResponder.Incident incident = incidentResponder.getActiveIncidents().get(0);
        incidentResponder.escalateIncident(incident.getIncidentId(), "Test");

        IncidentResponder.ResponderStatistics stats = incidentResponder.getStatistics();

        assertNotNull(stats);
        assertTrue("Total incidents should be positive", stats.totalIncidents >= 2);
        assertTrue("Active incidents should be positive", stats.activeIncidents >= 2);
        assertTrue("Escalated incidents should be non-negative", stats.escalatedIncidents >= 1);
        assertTrue("Notifications sent should be non-negative", stats.notificationsSent >= 0);
    }

    @Test
    public void testSIEMIntegration() {
        final boolean[] incidentSent = {false};
        final boolean[] reportSent = {false};

        IncidentResponder.SIEMIntegration siemIntegration = new IncidentResponder.SIEMIntegration() {
            @Override
            public void sendIncident(IncidentResponder.Incident incident) {
                incidentSent[0] = true;
            }

            @Override
            public void sendReport(IncidentResponder.IncidentReport report) {
                reportSent[0] = true;
            }
        };

        incidentResponder.initializeSIEMIntegration(siemIntegration);

        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);

        assertTrue("Incident should be sent to SIEM", incidentSent[0]);

        IncidentResponder.IncidentReport report = incidentResponder.generateReport(incident.getIncidentId());
        assertTrue("Report should be sent to SIEM", reportSent[0]);
    }

    @Test
    public void testInitializeSentryCore() {
        // Test that SentryCore can be initialized
        incidentResponder.initializeSentryCore(null);
        
        // Should not throw exception
        assertTrue("SentryCore initialization should not throw", true);
    }

    @Test
    public void testIncidentReportContent() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);
        
        incidentResponder.addActionTaken(incident.getIncidentId(), "Blocked IP");
        incidentResponder.addActionTaken(incident.getIncidentId(), "Quarantined app");

        IncidentResponder.IncidentReport report = incidentResponder.generateReport(incident.getIncidentId());

        assertTrue("Timeline should contain actions", report.getTimeline().contains("Blocked IP"));
        assertTrue("Timeline should contain actions", report.getTimeline().contains("Quarantined app"));
        assertTrue("Recommendations should not be empty", report.getRecommendations().size() > 0);
        assertTrue("Lessons learned should not be empty", report.getLessonsLearned().size() > 0);
    }

    @Test
    public void testIncidentStatusTransitions() {
        DetectionResult detection = createDetectionResult(ThreatLevel.HIGH, "MALWARE");
        IncidentResponder.Incident incident = incidentResponder.classifyIncident(detection);

        assertEquals("Initial status should be OPEN", IncidentResponder.IncidentStatus.OPEN, incident.getStatus());

        incidentResponder.escalateIncident(incident.getIncidentId(), "Test");
        assertEquals("Status should be ESCALATED", IncidentResponder.IncidentStatus.ESCALATED, incident.getStatus());

        incidentResponder.resolveIncident(incident.getIncidentId(), "Test");
        assertEquals("Status should be RESOLVED", IncidentResponder.IncidentStatus.RESOLVED, incident.getStatus());

        incidentResponder.closeIncident(incident.getIncidentId());
        assertEquals("Status should be CLOSED", IncidentResponder.IncidentStatus.CLOSED, incident.getStatus());
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
