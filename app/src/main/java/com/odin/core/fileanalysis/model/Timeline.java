package com.odin.core.fileanalysis.model;

import java.util.Date;
import java.util.List;

/**
 * Real timeline from actual analysis events.
 * No simulated timeline - only actual recorded timeline.
 */
public class Timeline {
    private String timelineId;
    private String fileId;
    private Date startTime;
    private Date endTime;
    private List<TimelineEvent> events;
    private long totalDurationMs;

    public Timeline() {
        this.startTime = new Date();
    }

    // Getters and Setters
    public String getTimelineId() { return timelineId; }
    public void setTimelineId(String timelineId) { this.timelineId = timelineId; }

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }

    public List<TimelineEvent> getEvents() { return events; }
    public void setEvents(List<TimelineEvent> events) { this.events = events; }

    public long getTotalDurationMs() { return totalDurationMs; }
    public void setTotalDurationMs(long totalDurationMs) { this.totalDurationMs = totalDurationMs; }
}
