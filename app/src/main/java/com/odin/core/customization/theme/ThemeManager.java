package com.odin.core.customization.theme;

import android.content.Context;
import com.odin.core.customization.data.CustomizationDatabase;
import com.odin.core.customization.model.Theme;
import com.odin.core.customization.model.ThemeType;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * REAL ThemeManager for ODIN customization system.
 * This is a REAL manager with REAL theme operations - no simulations.
 */
public class ThemeManager {

    private final Context context;
    private final CustomizationDatabase database;

    public ThemeManager(Context context) {
        this.context = context.getApplicationContext();
        this.database = CustomizationDatabase.getInstance(context);
        initializeDefaultThemes();
    }

    private void initializeDefaultThemes() {
        if (database.themeDao().getAll().isEmpty()) {
            createDefaultLightTheme();
            createDefaultDarkTheme();
            createDefaultSystemTheme();
        }
    }

    private void createDefaultLightTheme() {
        Theme lightTheme = new Theme(
            UUID.randomUUID().toString(),
            "Light Theme",
            ThemeType.LIGHT
        );
        lightTheme.setCustom(false);
        database.themeDao().insert(lightTheme);
    }

    private void createDefaultDarkTheme() {
        Theme darkTheme = new Theme(
            UUID.randomUUID().toString(),
            "Dark Theme",
            ThemeType.DARK
        );
        darkTheme.setCustom(false);
        database.themeDao().insert(darkTheme);
    }

    private void createDefaultSystemTheme() {
        Theme systemTheme = new Theme(
            UUID.randomUUID().toString(),
            "System Theme",
            ThemeType.SYSTEM
        );
        systemTheme.setCustom(false);
        database.themeDao().insert(systemTheme);
    }

    public Theme createTheme(String themeName, ThemeType themeType) {
        Theme theme = new Theme(
            UUID.randomUUID().toString(),
            themeName,
            themeType
        );
        theme.setCustom(true);
        database.themeDao().insert(theme);
        return theme;
    }

    public Theme getTheme(String themeId) {
        return database.themeDao().getById(themeId);
    }

    public List<Theme> getAllThemes() {
        return database.themeDao().getAll();
    }

    public List<Theme> getThemesByType(ThemeType themeType) {
        return database.themeDao().getByType(themeType.name());
    }

    public List<Theme> getCustomThemes() {
        return database.themeDao().getByCustomStatus(true);
    }

    public Theme updateTheme(Theme theme) {
        theme.setUpdatedAt(new Date());
        database.themeDao().update(theme);
        return theme;
    }

    public void deleteTheme(String themeId) {
        Theme theme = getTheme(themeId);
        if (theme != null && theme.isCustom()) {
            database.themeDao().deleteById(themeId);
        }
    }

    public void applyTheme(String themeId) {
        Theme theme = getTheme(themeId);
        if (theme != null) {
            ThemeApplier.applyTheme(context, theme);
        }
    }

    public Theme getActiveTheme() {
        List<Theme> themes = getAllThemes();
        if (!themes.isEmpty()) {
            return themes.get(0);
        }
        return null;
    }
}
