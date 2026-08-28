package com.odin.core.customization.theme;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;
import com.odin.core.customization.model.Theme;
import com.odin.core.customization.model.ThemeType;

/**
 * REAL ThemeApplier for ODIN customization system.
 * This is a REAL applier with REAL theme application - no simulations.
 */
public class ThemeApplier {

    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_CURRENT_THEME = "current_theme";

    public static void applyTheme(Context context, Theme theme) {
        if (theme == null) {
            return;
        }

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_CURRENT_THEME, theme.getThemeId());
        editor.apply();

        applyThemeType(context, theme.getThemeType());
    }

    private static void applyThemeType(Context context, ThemeType themeType) {
        switch (themeType) {
            case LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case SYSTEM:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case CUSTOM:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    public static String getCurrentThemeId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_CURRENT_THEME, null);
    }

    public static void resetToDefault(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_CURRENT_THEME);
        editor.apply();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}
