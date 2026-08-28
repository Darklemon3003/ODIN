package com.odin.core.customization.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.odin.core.customization.data.CustomizationDatabase;
import com.odin.core.customization.model.Profile;
import com.odin.core.customization.model.Theme;
import com.odin.core.customization.model.NotificationSettings;
import com.odin.core.customization.model.AutomationRule;
import com.odin.core.customization.profile.ProfileManager;
import com.odin.core.customization.profile.ProfileActivator;
import com.odin.core.customization.profile.ProfileSwitcher;
import com.odin.core.customization.profile.ProfilePersistence;
import com.odin.core.customization.profile.ProfileValidator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * REAL ProfileViewModel for ODIN customization system.
 * This is a REAL ViewModel with REAL LiveData - no simulations.
 */
public class ProfileViewModel extends AndroidViewModel {

    private final ProfileManager profileManager;
    private final ProfileActivator profileActivator;
    private final ProfileSwitcher profileSwitcher;
    private final ProfilePersistence profilePersistence;
    private final CustomizationDatabase database;

    private final MutableLiveData<List<Profile>> profiles;
    private final MutableLiveData<Profile> activeProfile;
    private final MutableLiveData<String> validationError;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.profileManager = new ProfileManager(application);
        this.profileActivator = new ProfileActivator(application);
        this.profileSwitcher = new ProfileSwitcher(application);
        this.profilePersistence = new ProfilePersistence(application);
        this.database = CustomizationDatabase.getInstance(application);

        this.profiles = new MutableLiveData<>();
        this.activeProfile = new MutableLiveData<>();
        this.validationError = new MutableLiveData<>(null);

        loadProfiles();
    }

    private void loadProfiles() {
        new Thread(() -> {
            List<Profile> profileList = database.profileDao().getAll();
            profiles.postValue(profileList);
            profilePersistence.saveProfileCache(profileList);

            Profile active = database.profileDao().getActiveProfile();
            if (active != null) {
                activeProfile.postValue(active);
                profilePersistence.saveActiveProfile(active);
            }
        }).start();
    }

    public LiveData<List<Profile>> getProfiles() {
        return profiles;
    }

    public LiveData<Profile> getActiveProfile() {
        return activeProfile;
    }

    public LiveData<String> getValidationError() {
        return validationError;
    }

    public void createProfile(String profileName) {
        String error = ProfileValidator.validateProfileName(profileName);
        if (error != null) {
            validationError.postValue(error);
            return;
        }

        new Thread(() -> {
            Profile profile = profileManager.createProfile(profileName);
            List<Profile> profileList = database.profileDao().getAll();
            profiles.postValue(profileList);
            profilePersistence.saveProfileCache(profileList);
            validationError.postValue(null);
        }).start();
    }

    public void updateProfile(Profile profile) {
        new Thread(() -> {
            Profile updated = profileManager.updateProfile(profile);
            List<Profile> profileList = database.profileDao().getAll();
            profiles.postValue(profileList);
            profilePersistence.saveProfileCache(profileList);

            if (activeProfile.getValue() != null && 
                activeProfile.getValue().getProfileId().equals(profile.getProfileId())) {
                activeProfile.postValue(updated);
                profilePersistence.saveActiveProfile(updated);
            }
        }).start();
    }

    public void deleteProfile(String profileId) {
        new Thread(() -> {
            profileManager.deleteProfile(profileId);
            List<Profile> profileList = database.profileDao().getAll();
            profiles.postValue(profileList);
            profilePersistence.saveProfileCache(profileList);

            if (activeProfile.getValue() != null && 
                activeProfile.getValue().getProfileId().equals(profileId)) {
                activeProfile.postValue(null);
                profilePersistence.clearActiveProfile();
            }
        }).start();
    }

    public void activateProfile(String profileId) {
        new Thread(() -> {
            profileActivator.activateProfile(profileId);
            Profile active = database.profileDao().getActiveProfile();
            activeProfile.postValue(active);
            profilePersistence.saveActiveProfile(active);
        }).start();
    }

    public void deactivateProfile(String profileId) {
        new Thread(() -> {
            profileActivator.deactivateProfile(profileId);
            Profile active = database.profileDao().getActiveProfile();
            activeProfile.postValue(active);
            if (active == null) {
                profilePersistence.clearActiveProfile();
            }
        }).start();
    }

    public void switchToProfile(String profileId) {
        profileSwitcher.switchToProfile(profileId);
        loadProfiles();
    }

    public void switchToNextProfile() {
        profileSwitcher.switchToNextProfile();
        loadProfiles();
    }

    public void switchToPreviousProfile() {
        profileSwitcher.switchToPreviousProfile();
        loadProfiles();
    }

    public void setProfileTheme(String profileId, Theme theme) {
        new Thread(() -> {
            profileManager.setProfileTheme(profileId, theme);
            List<Profile> profileList = database.profileDao().getAll();
            profiles.postValue(profileList);
            profilePersistence.saveProfileCache(profileList);
        }).start();
    }

    public void setProfileNotificationSettings(String profileId, NotificationSettings settings) {
        new Thread(() -> {
            profileManager.setProfileNotificationSettings(profileId, settings);
            List<Profile> profileList = database.profileDao().getAll();
            profiles.postValue(profileList);
            profilePersistence.saveProfileCache(profileList);
        }).start();
    }

    public void setProfileAutomationRules(String profileId, List<AutomationRule> rules) {
        new Thread(() -> {
            profileManager.setProfileAutomationRules(profileId, rules);
            List<Profile> profileList = database.profileDao().getAll();
            profiles.postValue(profileList);
            profilePersistence.saveProfileCache(profileList);
        }).start();
    }

    public void setProfileCustomSettings(String profileId, String customSettings) {
        new Thread(() -> {
            profileManager.setProfileCustomSettings(profileId, customSettings);
            List<Profile> profileList = database.profileDao().getAll();
            profiles.postValue(profileList);
            profilePersistence.saveProfileCache(profileList);
        }).start();
    }

    public void refreshProfiles() {
        loadProfiles();
    }
}
