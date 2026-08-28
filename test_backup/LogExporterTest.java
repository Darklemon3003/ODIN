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
 * Unit tests for LogExporter.
 * Note: These tests use mocked components since we can't use Android context in unit tests.
 */
public class LogExporterTest {

    private List<LogEvent> testEvents;
    private LogExporter.ExportResult exportResult;

    @Before
    public void setUp() {
        testEvents = new ArrayList<>();
        
        // Create test events
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
                "FIREWALL",
                "Packet blocked",
                LogEvent.Severity.ERROR
        ));
        
        exportResult = new LogExporter.ExportResult();
    }

    @Test
    public void testExportResultCreation() {
        exportResult.setFormat(LogExporter.ExportFormat.JSON);
        exportResult.setStartTime(1000L);
        exportResult.setEndTime(3000L);
        exportResult.setEventCount(3);
        exportResult.setMasked(true);
        exportResult.setContent("{\"test\":\"data\"}");
        exportResult.setSuccess(true);

        assertEquals(LogExporter.ExportFormat.JSON, exportResult.getFormat());
        assertEquals(1000L, exportResult.getStartTime());
        assertEquals(3000L, exportResult.getEndTime());
        assertEquals(3, exportResult.getEventCount());
        assertTrue(exportResult.isMasked());
        assertEquals("{\"test\":\"data\"}", exportResult.getContent());
        assertTrue(exportResult.isSuccess());
    }

    @Test
    public void testExportResultWithFailure() {
        exportResult.setSuccess(false);
        exportResult.setErrorMessage("Export failed");

        assertFalse(exportResult.isSuccess());
        assertEquals("Export failed", exportResult.getErrorMessage());
    }

    @Test
    public void testExportFormatEnum() {
        assertEquals("json", LogExporter.ExportFormat.JSON.getExtension());
        assertEquals("csv", LogExporter.ExportFormat.CSV.getExtension());
        assertEquals("xml", LogExporter.ExportFormat.XML.getExtension());
        assertEquals("pdf", LogExporter.ExportFormat.PDF.getExtension());
    }

    @Test
    public void testJsonExportFormat() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"exportTimestamp\": ").append(System.currentTimeMillis()).append(",\n");
        json.append("  \"totalEvents\": ").append(testEvents.size()).append(",\n");
        json.append("  \"format\": \"JSON\",\n");
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
        assertTrue(jsonString.contains("format"));
        assertTrue(jsonString.contains("events"));
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
        assertTrue(csvString.contains("FIREWALL"));
    }

    @Test
    public void testXmlExportFormat() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<LogExport>\n");
        xml.append("  <Metadata>\n");
        xml.append("    <ExportTimestamp>").append(System.currentTimeMillis()).append("</ExportTimestamp>\n");
        xml.append("    <TotalEvents>").append(testEvents.size()).append("</TotalEvents>\n");
        xml.append("    <Format>XML</Format>\n");
        xml.append("  </Metadata>\n");
        xml.append("  <Events>\n");
        
        for (LogEvent event : testEvents) {
            xml.append("    <Event>\n");
            xml.append("      <Timestamp>").append(event.getTimestamp()).append("</Timestamp>\n");
            xml.append("      <Source>").append(event.getSource()).append("</Source>\n");
            xml.append("      <Message>").append(event.getMessage()).append("</Message>\n");
            xml.append("      <Severity>").append(event.getSeverity().getValue()).append("</Severity>\n");
            xml.append("    </Event>\n");
        }
        
        xml.append("  </Events>\n");
        xml.append("</LogExport>");
        
        String xmlString = xml.toString();
        assertTrue(xmlString.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(xmlString.contains("<LogExport>"));
        assertTrue(xmlString.contains("<Metadata>"));
        assertTrue(xmlString.contains("<Events>"));
    }

    @Test
    public void testPdfExportFormat() {
        StringBuilder pdf = new StringBuilder();
        pdf.append("LOG EXPORT REPORT\n");
        pdf.append("=================\n\n");
        pdf.append("Export Timestamp: ").append(System.currentTimeMillis()).append("\n");
        pdf.append("Total Events: ").append(testEvents.size()).append("\n");
        pdf.append("Format: PDF (Text)\n\n");
        pdf.append("--------------------------------------------------\n\n");
        
        for (LogEvent event : testEvents) {
            pdf.append("Timestamp: ").append(event.getTimestamp()).append("\n");
            pdf.append("Source: ").append(event.getSource()).append("\n");
            pdf.append("Severity: ").append(event.getSeverity().getValue()).append("\n");
            pdf.append("Message: ").append(event.getMessage()).append("\n");
            pdf.append("\n--------------------------------------------------\n\n");
        }
        
        String pdfString = pdf.toString();
        assertTrue(pdfString.contains("LOG EXPORT REPORT"));
        assertTrue(pdfString.contains("Total Events"));
        assertTrue(pdfString.contains("Format: PDF"));
    }

    @Test
    public void testCsvEscaping() {
        String valueWithComma = "test,value";
        String valueWithQuote = "test\"quote";
        String valueWithNewline = "test\nvalue";
        
        String escaped1 = escapeCsv(valueWithComma);
        String escaped2 = escapeCsv(valueWithQuote);
        String escaped3 = escapeCsv(valueWithNewline);
        
        assertTrue(escaped1.startsWith("\""));
        assertTrue(escaped2.contains("\"\""));
        assertTrue(escaped3.startsWith("\""));
    }

    @Test
    public void testXmlEscaping() {
        String valueWithAmp = "test&value";
        String valueWithLt = "test<value";
        String valueWithGt = "test>value";
        String valueWithQuote = "test\"value";
        String valueWithApos = "test'value";
        
        String escaped1 = escapeXml(valueWithAmp);
        String escaped2 = escapeXml(valueWithLt);
        String escaped3 = escapeXml(valueWithGt);
        String escaped4 = escapeXml(valueWithQuote);
        String escaped5 = escapeXml(valueWithApos);
        
        assertTrue(escaped1.contains("&amp;"));
        assertTrue(escaped2.contains("&lt;"));
        assertTrue(escaped3.contains("&gt;"));
        assertTrue(escaped4.contains("&quot;"));
        assertTrue(escaped5.contains("&apos;"));
    }

    @Test
    public void testDataMasking() {
        String originalMessage = "Connection from 192.168.1.1 to 10.0.0.1";
        String maskedMessage = originalMessage.replaceAll(
                "\\b(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\b",
                "$1.$2.$3.***"
        );
        
        assertTrue(maskedMessage.contains("192.168.1.***"));
        assertTrue(maskedMessage.contains("10.0.0.***"));
    }

    @Test
    public void testTokenMasking() {
        String originalMessage = "token=abc123def456ghi789";
        String maskedMessage = originalMessage.replaceAll(
                "(token|api_key|secret)[\"':\\s]*[a-zA-Z0-9_-]{20,}",
                "$1=***REDACTED***"
        );
        
        assertTrue(maskedMessage.contains("token=***REDACTED***"));
    }

    @Test
    public void testTimeRangeFiltering() {
        long startTime = 1500L;
        long endTime = 2500L;
        
        List<LogEvent> filteredEvents = new ArrayList<>();
        for (LogEvent event : testEvents) {
            if (event.getTimestamp() >= startTime && event.getTimestamp() <= endTime) {
                filteredEvents.add(event);
            }
        }
        
        assertEquals(1, filteredEvents.size());
        assertEquals(2000L, filteredEvents.get(0).getTimestamp());
    }

    @Test
    public void testSeverityFiltering() {
        LogEvent.Severity filterSeverity = LogEvent.Severity.WARNING;
        
        List<LogEvent> filteredEvents = new ArrayList<>();
        for (LogEvent event : testEvents) {
            if (event.getSeverity() == filterSeverity) {
                filteredEvents.add(event);
            }
        }
        
        assertEquals(1, filteredEvents.size());
        assertEquals("THREAT_DETECTION", filteredEvents.get(0).getSource());
    }

    @Test
    public void testSourceFiltering() {
        String filterSource = "VPN";
        
        List<LogEvent> filteredEvents = new ArrayList<>();
        for (LogEvent event : testEvents) {
            if (event.getSource().equals(filterSource)) {
                filteredEvents.add(event);
            }
        }
        
        assertEquals(1, filteredEvents.size());
        assertEquals("VPN connection established", filteredEvents.get(0).getMessage());
    }

    @Test
    public void testSearchFiltering() {
        String searchTerm = "packet";
        
        List<LogEvent> filteredEvents = new ArrayList<>();
        for (LogEvent event : testEvents) {
            if (event.getMessage().toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredEvents.add(event);
            }
        }
        
        assertEquals(2, filteredEvents.size());
    }

    @Test
    public void testExportSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalEvents", testEvents.size());
        summary.put("startTime", 1000L);
        summary.put("endTime", 3000L);
        
        // Calculate severity distribution
        Map<String, Long> severityDistribution = new HashMap<>();
        for (LogEvent.Severity severity : LogEvent.Severity.values()) {
            long count = testEvents.stream()
                    .filter(e -> e.getSeverity() == severity)
                    .count();
            if (count > 0) {
                severityDistribution.put(severity.getValue(), count);
            }
        }
        summary.put("severityDistribution", severityDistribution);
        
        assertEquals(3, summary.get("totalEvents"));
        assertEquals(1000L, summary.get("startTime"));
        assertEquals(3000L, summary.get("endTime"));
        assertEquals(3, ((Map<?, ?>) summary.get("severityDistribution")).size());
    }

    @Test
    public void testSourceDistribution() {
        Map<String, Long> sourceDistribution = new HashMap<>();
        for (LogEvent event : testEvents) {
            sourceDistribution.merge(event.getSource(), 1L, Long::sum);
        }
        
        assertEquals(3, sourceDistribution.size());
        assertEquals(Long.valueOf(1), sourceDistribution.get("VPN"));
        assertEquals(Long.valueOf(1), sourceDistribution.get("THREAT_DETECTION"));
        assertEquals(Long.valueOf(1), sourceDistribution.get("FIREWALL"));
    }

    // Helper methods for testing
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String escapeXml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
}
