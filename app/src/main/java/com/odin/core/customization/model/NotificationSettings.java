package com.odin.core.customization.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.room.Ignore;
import com.google.gson.annotations.SerializedName;
import com.odin.core.customization.data.converters.VibrationPatternConverter;
import com.odin.core.customization.data.converters.NotificationPriorityConverter;
import java.util.Date;

/**
 * REAL NotificationSettings model for ODIN customization system.
 * This is a REAL data model with REAL notification configurations - no simulations.
 */
@Entity(tableName = "notification_settings")
public class NotificationSettings {

    @PrimaryKey
    @androidx.annotation.NonNull
    @SerializedName("settingsId")
    private String settingsId;

    @SerializedName("notificationsEnabled")
    private boolean notificationsEnabled;

    @SerializedName("soundEnabled")
    private boolean soundEnabled;

    @SerializedName("soundUri")
    private String soundUri;

    @SerializedName("vibrationEnabled")
    private boolean vibrationEnabled;

    @SerializedName("vibrationPattern")
    private VibrationPattern vibrationPattern;

    @SerializedName("ledEnabled")
    private boolean ledEnabled;

    @SerializedName("ledColor")
    private int ledColor;

    @SerializedName("ledOnMs")
    private int ledOnMs;

    @SerializedName("ledOffMs")
    private int ledOffMs;

    @SerializedName("priority")
    private NotificationPriority priority;

    @SerializedName("updatedAt")
    private Date updatedAt;

    public NotificationSettings() {
        this.settingsId = "default";
        this.notificationsEnabled = true;
        this.soundEnabled = true;
        this.soundUri = "android.resource://com.odin.core/raw/notification_default";
        this.vibrationEnabled = true;
        this.vibrationPattern = VibrationPattern.DEFAULT;
        this.ledEnabled = true;
        this.ledColor = 0xFF00FF00;
        this.ledOnMs = 500;
        this.ledOffMs = 500;
        this.priority = NotificationPriority.HIGH;
        this.updatedAt = new Date();
    }

    @Ignore
    public NotificationSettings(String settingsId) {
        this.settingsId = settingsId;
        this.notificationsEnabled = true;
        this.soundEnabled = true;
        this.soundUri = "android.resource://com.odin.core/raw/notification_default";
        this.vibrationEnabled = true;
        this.vibrationPattern = VibrationPattern.DEFAULT;
        this.ledEnabled = true;
        this.ledColor = 0xFF00FF00;
        this.ledOnMs = 500;
        this.ledOffMs = 500;
        this.priority = NotificationPriority.HIGH;
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(String settingsId) {
        this.settingsId = settingsId;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public String getSoundUri() {
        return soundUri;
    }

    public void setSoundUri(String soundUri) {
        this.soundUri = soundUri;
    }

    public boolean isVibrationEnabled() {
        return vibrationEnabled;
    }

    public void setVibrationEnabled(boolean vibrationEnabled) {
        this.vibrationEnabled = vibrationEnabled;
    }

    public VibrationPattern getVibrationPattern() {
        return vibrationPattern;
    }

    public void setVibrationPattern(VibrationPattern vibrationPattern) {
        this.vibrationPattern = vibrationPattern;
    }

    public boolean isLedEnabled() {
        return ledEnabled;
    }

    public void setLedEnabled(boolean ledEnabled) {
        this.ledEnabled = ledEnabled;
    }

    public int getLedColor() {
        return ledColor;
    }

    public void setLedColor(int ledColor) {
        this.ledColor = ledColor;
    }

    public int getLedOnMs() {
        return ledOnMs;
    }

    public void setLedOnMs(int ledOnMs) {
        this.ledOnMs = ledOnMs;
    }

    public int getLedOffMs() {
        return ledOffMs;
    }

    public void setLedOffMs(int ledOffMs) {
        this.ledOffMs = ledOffMs;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "NotificationSettings{" +
                "settingsId='" + settingsId + '\'' +
                ", notificationsEnabled=" + notificationsEnabled +
                ", soundEnabled=" + soundEnabled +
                ", vibrationEnabled=" + vibrationEnabled +
                ", ledEnabled=" + ledEnabled +
                ", priority=" + priority +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
