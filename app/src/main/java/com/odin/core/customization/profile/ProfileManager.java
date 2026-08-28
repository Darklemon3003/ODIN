package com.odin.core.customization.profile;

import android.content.Context;
import com.odin.core.customization.data.CustomizationDatabase;
import com.odin.core.customization.model.Profile;
import com.odin.core.customization.model.Theme;
import com.odin.core.customization.model.NotificationSettings;
import com.odin.core.customization.model.AutomationRule;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * REAL ProfileManager for ODIN customization system.
 * This is a REAL manager with REAL profile operations - no simulations.
 */
public class ProfileManager {

    private final Context context;
    private final CustomizationDatabase database;

    public ProfileManager(Context context) {
        this.context = context.getApplicationContext();
        this.database = CustomizationDatabase.getInstance(context);
        initializeDefaultProfile();
    }

    private void initializeDefaultProfile() {
        if (database.profileDao().getAll().isEmpty()) {
            Profile defaultProfile = new Profile(
                UUID.randomUUID().toString(),
                "Default Profile"
            );
            defaultProfile.setActive(true);
            defaultProfile.setCreatedAt(new Date());
            defaultProfile.setUpdatedAt(new Date());
            database.profileDao().insert(defaultProfile);
        }
    }

    public Profile createProfile(String profileName) {
        Profile profile = new Profile(UUID.randomUUID().toString(), profileName);
        profile.setActive(false);
        profile.setCreatedAt(new Date());
        profile.setUpdatedAt(new Date());
        database.profileDao().insert(profile);
        return profile;
    }

    public Profile getProfile(String profileId) {
        return database.profileDao().getById(profileId);
    }

    public List<Profile> getAllProfiles() {
        return database.profileDao().getAll();
    }

    public Profile getActiveProfile() {
        return database.profileDao().getActiveProfile();
    }

    public Profile updateProfile(Profile profile) {
        profile.setUpdatedAt(new Date());
        database.profileDao().update(profile);
        return profile;
    }

    public void deleteProfile(String profileId) {
        Profile profile = getProfile(profileId);
        if (profile != null && !profile.isActive()) {
            database.profileDao().deleteById(profileId);
        }
    }

    public void setProfileTheme(String profileId, Theme theme) {
        Profile profile = getProfile(profileId);
        if (profile != null) {
            profile.setTheme(theme);
            profile.setUpdatedAt(new Date());
            database.profileDao().update(profile);
        }
    }

    public void setProfileNotificationSettings(String profileId, NotificationSettings settings) {
        Profile profile = getProfile(profileId);
        if (profile != null) {
            profile.setNotificationSettings(settings);
            profile.setUpdatedAt(new Date());
            database.profileDao().update(profile);
        }
    }

    public void setProfileAutomationRules(String profileId, List<AutomationRule> rules) {
        Profile profile = getProfile(profileId);
        if (profile != null) {
            profile.setAutomationRules(rules);
            profile.setUpdatedAt(new Date());
            database.profileDao().update(profile);
        }
    }

    public void setProfileCustomSettings(String profileId, String customSettings) {
        Profile profile = getProfile(profileId);
        if (profile != null) {
            java.util.Map<String, Object> settingsMap = new java.util.HashMap<>();
            settingsMap.put("custom", customSettings);
            profile.setCustomSettings(settingsMap);
            profile.setUpdatedAt(new Date());
            database.profileDao().update(profile);
        }
    }
}
