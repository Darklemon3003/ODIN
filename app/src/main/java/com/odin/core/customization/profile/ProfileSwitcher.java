package com.odin.core.customization.profile;

import android.content.Context;
import com.odin.core.customization.model.Profile;
import java.util.List;

/**
 * REAL ProfileSwitcher for ODIN customization system.
 * This is a REAL switcher with REAL profile switching - no simulations.
 */
public class ProfileSwitcher {

    private final Context context;
    private final ProfileActivator profileActivator;
    private final ProfileManager profileManager;

    public ProfileSwitcher(Context context) {
        this.context = context.getApplicationContext();
        this.profileActivator = new ProfileActivator(context);
        this.profileManager = new ProfileManager(context);
    }

    public void switchToProfile(String profileId) {
        profileActivator.activateProfile(profileId);
    }

    public void switchToNextProfile() {
        List<Profile> profiles = profileManager.getAllProfiles();
        if (profiles.isEmpty()) {
            return;
        }

        Profile currentProfile = profileActivator.getActiveProfile();
        int currentIndex = -1;

        if (currentProfile != null) {
            for (int i = 0; i < profiles.size(); i++) {
                if (profiles.get(i).getProfileId().equals(currentProfile.getProfileId())) {
                    currentIndex = i;
                    break;
                }
            }
        }

        int nextIndex = (currentIndex + 1) % profiles.size();
        profileActivator.activateProfile(profiles.get(nextIndex).getProfileId());
    }

    public void switchToPreviousProfile() {
        List<Profile> profiles = profileManager.getAllProfiles();
        if (profiles.isEmpty()) {
            return;
        }

        Profile currentProfile = profileActivator.getActiveProfile();
        int currentIndex = -1;

        if (currentProfile != null) {
            for (int i = 0; i < profiles.size(); i++) {
                if (profiles.get(i).getProfileId().equals(currentProfile.getProfileId())) {
                    currentIndex = i;
                    break;
                }
            }
        }

        int previousIndex = (currentIndex - 1 + profiles.size()) % profiles.size();
        profileActivator.activateProfile(profiles.get(previousIndex).getProfileId());
    }

    public void switchToProfileByName(String profileName) {
        List<Profile> profiles = profileManager.getAllProfiles();
        for (Profile profile : profiles) {
            if (profile.getProfileName().equals(profileName)) {
                profileActivator.activateProfile(profile.getProfileId());
                return;
            }
        }
    }
}
