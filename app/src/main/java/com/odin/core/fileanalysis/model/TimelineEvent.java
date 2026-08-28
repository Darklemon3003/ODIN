package com.odin.core.fileanalysis.model;

import java.util.Date;
import java.util.Map;

/**
 * Real timeline event from actual analysis timeline.
 * No simulated events - only actual recorded events.
 */
public class TimelineEvent {
    private String eventId;
    private String eventType;
    private String eventDescription;
    private Date eventTimestamp;
    private String sourceComponent;
    private Map<String, String> eventDetails;

    public TimelineEvent() {
        this.eventTimestamp = new Date();
    }

    // Getters and Setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getEventDescription() { return eventDescription; }
    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }

    public Date getEventTimestamp() { return eventTimestamp; }
    public void setEventTimestamp(Date eventTimestamp) { this.eventTimestamp = eventTimestamp; }

    public String getSourceComponent() { return sourceComponent; }
    public void setSourceComponent(String sourceComponent) { this.sourceComponent = sourceComponent; }

    public Map<String, String> getEventDetails() { return eventDetails; }
    public void setEventDetails(Map<String, String> eventDetails) { this.eventDetails = eventDetails; }
}
