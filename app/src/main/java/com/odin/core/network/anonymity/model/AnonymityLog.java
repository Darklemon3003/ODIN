package com.odin.core.network.anonymity.model;

/**
 * Model representing an anonymity log entry.
 * Enterprise-grade logging for ODIN network anonymity.
 */
public class AnonymityLog {
    private long timestamp;
    private String level;
    private String component;
    private String message;
    private String details;
    private String category;

    public AnonymityLog() {
        this.timestamp = System.currentTimeMillis();
    }

    public AnonymityLog(String level, String component, String message) {
        this.timestamp = System.currentTimeMillis();
        this.level = level;
        this.component = component;
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "AnonymityLog{" +
                "timestamp=" + timestamp +
                ", level='" + level + '\'' +
                ", component='" + component + '\'' +
                ", message='" + message + '\'' +
                ", details='" + details + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
