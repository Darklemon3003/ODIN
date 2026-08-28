package com.odin.core.customization.profile;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.Profile;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * REAL ProfilePersistence for ODIN customization system.
 * This is a REAL persistence layer with REAL storage operations - no simulations.
 */
public class ProfilePersistence {

    private static final String PREFS_NAME = "profile_persistence";
    private static final String KEY_ACTIVE_PROFILE = "active_profile";
    private static final String KEY_PROFILE_CACHE = "profile_cache";
    private static final String KEY_SWITCH_HISTORY = "switch_history";

    private final SharedPreferences prefs;
    private final Gson gson;

    public ProfilePersistence(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void saveActiveProfile(Profile profile) {
        if (profile != null) {
            String profileJson = gson.toJson(profile);
            prefs.edit().putString(KEY_ACTIVE_PROFILE, profileJson).apply();
        }
    }

    public Profile getActiveProfile() {
        String profileJson = prefs.getString(KEY_ACTIVE_PROFILE, null);
        if (profileJson != null) {
            Type type = new TypeToken<Profile>(){}.getType();
            return gson.fromJson(profileJson, type);
        }
        return null;
    }

    public void saveProfileCache(List<Profile> profiles) {
        if (profiles != null) {
            String profilesJson = gson.toJson(profiles);
            prefs.edit().putString(KEY_PROFILE_CACHE, profilesJson).apply();
        }
    }

    public List<Profile> getProfileCache() {
        String profilesJson = prefs.getString(KEY_PROFILE_CACHE, null);
        if (profilesJson != null) {
            Type type = new TypeToken<List<Profile>>(){}.getType();
            return gson.fromJson(profilesJson, type);
        }
        return new ArrayList<>();
    }

    public void saveSwitchHistory(List<String> history) {
        if (history != null) {
            String historyJson = gson.toJson(history);
            prefs.edit().putString(KEY_SWITCH_HISTORY, historyJson).apply();
        }
    }

    public List<String> getSwitchHistory() {
        String historyJson = prefs.getString(KEY_SWITCH_HISTORY, null);
        if (historyJson != null) {
            Type type = new TypeToken<List<String>>(){}.getType();
            return gson.fromJson(historyJson, type);
        }
        return new ArrayList<>();
    }

    public void clearActiveProfile() {
        prefs.edit().remove(KEY_ACTIVE_PROFILE).apply();
    }

    public void clearProfileCache() {
        prefs.edit().remove(KEY_PROFILE_CACHE).apply();
    }

    public void clearSwitchHistory() {
        prefs.edit().remove(KEY_SWITCH_HISTORY).apply();
    }

    public void clearAll() {
        prefs.edit().clear().apply();
    }
}
