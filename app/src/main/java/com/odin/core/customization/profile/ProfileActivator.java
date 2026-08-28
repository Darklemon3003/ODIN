package com.odin.core.customization.profile;

import android.content.Context;
import com.odin.core.customization.data.CustomizationDatabase;
import com.odin.core.customization.model.Profile;
import com.odin.core.customization.model.Theme;
import com.odin.core.customization.model.NotificationSettings;
import com.odin.core.customization.model.AutomationRule;
import com.odin.core.customization.theme.ThemeApplier;
import com.odin.core.customization.notification.NotificationManager;
import com.odin.core.customization.automation.AutomationRulesManager;
import java.util.Date;
import java.util.List;

/**
 * REAL ProfileActivator for ODIN customization system.
 * This is a REAL activator with REAL profile activation - no simulations.
 */
public class ProfileActivator {

    private final Context context;
    private final CustomizationDatabase database;
    private final ProfileManager profileManager;

    public ProfileActivator(Context context) {
        this.context = context.getApplicationContext();
        this.database = CustomizationDatabase.getInstance(context);
        this.profileManager = new ProfileManager(context);
    }

    public void activateProfile(String profileId) {
        Profile profile = profileManager.getProfile(profileId);
        if (profile == null) {
            return;
        }

        deactivateAllProfiles();

        profile.setActive(true);
        profile.setActivatedAt(new Date());
        profile.setUpdatedAt(new Date());
        database.profileDao().update(profile);

        applyProfileSettings(profile);
    }

    private void deactivateAllProfiles() {
        List<Profile> profiles = database.profileDao().getByActiveStatus(true);
        for (Profile profile : profiles) {
            profile.setActive(false);
            profile.setUpdatedAt(new Date());
            database.profileDao().update(profile);
        }
    }

    private void applyProfileSettings(Profile profile) {
        if (profile.getTheme() != null) {
            ThemeApplier.applyTheme(context, profile.getTheme());
        }

        if (profile.getNotificationSettings() != null) {
            NotificationManager notificationManager = new NotificationManager(context);
            notificationManager.updateSettings(profile.getNotificationSettings());
        }

        if (profile.getAutomationRules() != null && !profile.getAutomationRules().isEmpty()) {
            AutomationRulesManager rulesManager = new AutomationRulesManager(context);
            for (AutomationRule rule : profile.getAutomationRules()) {
                rulesManager.enableRule(rule.getRuleId());
            }
        }
    }

    public void deactivateProfile(String profileId) {
        Profile profile = profileManager.getProfile(profileId);
        if (profile != null) {
            profile.setActive(false);
            profile.setUpdatedAt(new Date());
            database.profileDao().update(profile);
        }
    }

    public Profile getActiveProfile() {
        return database.profileDao().getActiveProfile();
    }
}
