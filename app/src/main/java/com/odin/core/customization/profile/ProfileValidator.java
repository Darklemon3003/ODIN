package com.odin.core.customization.profile;

import com.odin.core.customization.model.Profile;

/**
 * REAL ProfileValidator for ODIN customization system.
 * This is a REAL validator with REAL profile validation - no simulations.
 */
public class ProfileValidator {

    public static boolean isValidProfile(Profile profile) {
        if (profile == null) {
            return false;
        }

        if (profile.getProfileId() == null || profile.getProfileId().isEmpty()) {
            return false;
        }

        if (profile.getProfileName() == null || profile.getProfileName().isEmpty()) {
            return false;
        }

        return true;
    }

    public static boolean isValidProfileName(String profileName) {
        return profileName != null && !profileName.trim().isEmpty() && profileName.length() <= 100;
    }

    public static boolean isValidProfileId(String profileId) {
        return profileId != null && !profileId.trim().isEmpty();
    }

    public static String validateProfileName(String profileName) {
        if (profileName == null || profileName.trim().isEmpty()) {
            return "Profile name cannot be empty";
        }

        if (profileName.length() > 100) {
            return "Profile name cannot exceed 100 characters";
        }

        if (profileName.contains("/") || profileName.contains("\\") || 
            profileName.contains(":") || profileName.contains("*") || 
            profileName.contains("?") || profileName.contains("\"") || 
            profileName.contains("<") || profileName.contains(">") || 
            profileName.contains("|")) {
            return "Profile name contains invalid characters";
        }

        return null;
    }

    public static boolean canDeleteProfile(Profile profile) {
        if (profile == null) {
            return false;
        }

        return !profile.isActive();
    }

    public static boolean canActivateProfile(Profile profile) {
        return isValidProfile(profile);
    }
}
