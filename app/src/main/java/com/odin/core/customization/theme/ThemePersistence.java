package com.odin.core.customization.theme;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.Theme;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * REAL ThemePersistence for ODIN customization system.
 * This is a REAL persistence layer with REAL storage operations - no simulations.
 */
public class ThemePersistence {

    private static final String PREFS_NAME = "theme_persistence";
    private static final String KEY_ACTIVE_THEME = "active_theme";
    private static final String KEY_THEME_CACHE = "theme_cache";

    private final SharedPreferences prefs;
    private final Gson gson;

    public ThemePersistence(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void saveActiveTheme(Theme theme) {
        if (theme != null) {
            String themeJson = gson.toJson(theme);
            prefs.edit().putString(KEY_ACTIVE_THEME, themeJson).apply();
        }
    }

    public Theme getActiveTheme() {
        String themeJson = prefs.getString(KEY_ACTIVE_THEME, null);
        if (themeJson != null) {
            Type type = new TypeToken<Theme>(){}.getType();
            return gson.fromJson(themeJson, type);
        }
        return null;
    }

    public void saveThemeCache(List<Theme> themes) {
        if (themes != null) {
            String themesJson = gson.toJson(themes);
            prefs.edit().putString(KEY_THEME_CACHE, themesJson).apply();
        }
    }

    public List<Theme> getThemeCache() {
        String themesJson = prefs.getString(KEY_THEME_CACHE, null);
        if (themesJson != null) {
            Type type = new TypeToken<List<Theme>>(){}.getType();
            return gson.fromJson(themesJson, type);
        }
        return new ArrayList<>();
    }

    public void clearActiveTheme() {
        prefs.edit().remove(KEY_ACTIVE_THEME).apply();
    }

    public void clearThemeCache() {
        prefs.edit().remove(KEY_THEME_CACHE).apply();
    }

    public void clearAll() {
        prefs.edit().clear().apply();
    }
}
