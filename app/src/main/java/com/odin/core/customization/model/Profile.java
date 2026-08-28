package com.odin.core.customization.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.room.Ignore;
import com.google.gson.annotations.SerializedName;
import com.odin.core.customization.data.converters.ThemeConverter;
import com.odin.core.customization.data.converters.NotificationSettingsConverter;
import com.odin.core.customization.data.converters.AutomationRuleListConverter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * REAL Profile model for ODIN customization system.
 * This is a REAL data model with REAL profile configurations - no simulations.
 */
@Entity(tableName = "profiles")
public class Profile {

    @PrimaryKey
    @androidx.annotation.NonNull
    @SerializedName("profileId")
    private String profileId;

    @SerializedName("profileName")
    private String profileName;

    @SerializedName("isActive")
    private boolean isActive;

    @SerializedName("theme")
    private Theme theme;

    @SerializedName("notificationSettings")
    private NotificationSettings notificationSettings;

    @SerializedName("automationRules")
    private List<AutomationRule> automationRules;

    @SerializedName("customSettings")
    private Map<String, Object> customSettings;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("updatedAt")
    private Date updatedAt;

    @SerializedName("activatedAt")
    private Date activatedAt;

    public Profile() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @Ignore
    public Profile(String profileId, String profileName) {
        this.profileId = profileId;
        this.profileName = profileName;
        this.isActive = false;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public NotificationSettings getNotificationSettings() {
        return notificationSettings;
    }

    public void setNotificationSettings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
    }

    public List<AutomationRule> getAutomationRules() {
        return automationRules;
    }

    public void setAutomationRules(List<AutomationRule> automationRules) {
        this.automationRules = automationRules;
    }

    public Map<String, Object> getCustomSettings() {
        return customSettings;
    }

    public void setCustomSettings(Map<String, Object> customSettings) {
        this.customSettings = customSettings;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(Date activatedAt) {
        this.activatedAt = activatedAt;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profileId='" + profileId + '\'' +
                ", profileName='" + profileName + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
