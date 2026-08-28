package com.odin.core.customization.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.odin.core.customization.data.CustomizationDatabase;
import com.odin.core.customization.model.Theme;
import com.odin.core.customization.model.ThemeType;
import com.odin.core.customization.theme.ThemeManager;
import com.odin.core.customization.theme.ThemePersistence;
import java.util.List;

/**
 * REAL ThemeViewModel for ODIN customization system.
 * This is a REAL ViewModel with REAL LiveData - no simulations.
 */
public class ThemeViewModel extends AndroidViewModel {

    private final ThemeManager themeManager;
    private final ThemePersistence themePersistence;
    private final CustomizationDatabase database;

    private final MutableLiveData<Theme> currentTheme;
    private final MutableLiveData<List<Theme>> availableThemes;
    private final MutableLiveData<Boolean> themeApplied;

    public ThemeViewModel(@NonNull Application application) {
        super(application);
        this.themeManager = new ThemeManager(application);
        this.themePersistence = new ThemePersistence(application);
        this.database = CustomizationDatabase.getInstance(application);

        this.currentTheme = new MutableLiveData<>();
        this.availableThemes = new MutableLiveData<>();
        this.themeApplied = new MutableLiveData<>(false);

        loadThemes();
    }

    private void loadThemes() {
        new Thread(() -> {
            List<Theme> themes = database.themeDao().getAll();
            availableThemes.postValue(themes);
            
            String activeThemeId = themePersistence.getActiveTheme() != null 
                ? themePersistence.getActiveTheme().getThemeId() 
                : null;
            
            if (activeThemeId != null) {
                Theme activeTheme = database.themeDao().getById(activeThemeId);
                if (activeTheme != null) {
                    currentTheme.postValue(activeTheme);
                    themeApplied.postValue(true);
                }
            }
        }).start();
    }

    public LiveData<Theme> getCurrentTheme() {
        return currentTheme;
    }

    public LiveData<List<Theme>> getAvailableThemes() {
        return availableThemes;
    }

    public LiveData<Boolean> getThemeApplied() {
        return themeApplied;
    }

    public void createTheme(String themeName, ThemeType themeType) {
        new Thread(() -> {
            Theme theme = themeManager.createTheme(themeName, themeType);
            List<Theme> themes = database.themeDao().getAll();
            availableThemes.postValue(themes);
        }).start();
    }

    public void applyTheme(String themeId) {
        new Thread(() -> {
            Theme theme = themeManager.getTheme(themeId);
            if (theme != null) {
                themeManager.applyTheme(themeId);
                themePersistence.saveActiveTheme(theme);
                currentTheme.postValue(theme);
                themeApplied.postValue(true);
            }
        }).start();
    }

    public void updateTheme(Theme theme) {
        new Thread(() -> {
            Theme updated = themeManager.updateTheme(theme);
            List<Theme> themes = database.themeDao().getAll();
            availableThemes.postValue(themes);
            
            if (currentTheme.getValue() != null && 
                currentTheme.getValue().getThemeId().equals(theme.getThemeId())) {
                currentTheme.postValue(updated);
            }
        }).start();
    }

    public void deleteTheme(String themeId) {
        new Thread(() -> {
            themeManager.deleteTheme(themeId);
            List<Theme> themes = database.themeDao().getAll();
            availableThemes.postValue(themes);
            
            if (currentTheme.getValue() != null && 
                currentTheme.getValue().getThemeId().equals(themeId)) {
                themePersistence.clearActiveTheme();
                currentTheme.postValue(null);
                themeApplied.postValue(false);
            }
        }).start();
    }

    public void refreshThemes() {
        loadThemes();
    }
}
