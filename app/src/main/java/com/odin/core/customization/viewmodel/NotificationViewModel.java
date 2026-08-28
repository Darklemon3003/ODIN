package com.odin.core.customization.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.odin.core.customization.data.CustomizationDatabase;
import com.odin.core.customization.model.NotificationSettings;
import com.odin.core.customization.model.NotificationPriority;
import com.odin.core.customization.model.VibrationPattern;
import com.odin.core.customization.notification.NotificationManager;
import com.odin.core.customization.notification.NotificationPersistence;
import java.util.Date;

/**
 * REAL NotificationViewModel for ODIN customization system.
 * This is a REAL ViewModel with REAL LiveData - no simulations.
 */
public class NotificationViewModel extends AndroidViewModel {

    private final NotificationManager notificationManager;
    private final NotificationPersistence notificationPersistence;
    private final CustomizationDatabase database;

    private final MutableLiveData<NotificationSettings> notificationSettings;
    private final MutableLiveData<Boolean> notificationsEnabled;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        this.notificationManager = new NotificationManager(application);
        this.notificationPersistence = new NotificationPersistence(application);
        this.database = CustomizationDatabase.getInstance(application);

        this.notificationSettings = new MutableLiveData<>();
        this.notificationsEnabled = new MutableLiveData<>(true);

        loadSettings();
    }

    private void loadSettings() {
        new Thread(() -> {
            NotificationSettings settings = database.notificationSettingsDao().getById("default");
            if (settings != null) {
                notificationSettings.postValue(settings);
                notificationsEnabled.postValue(settings.isNotificationsEnabled());
            } else {
                NotificationSettings defaultSettings = new NotificationSettings("default");
                database.notificationSettingsDao().insert(defaultSettings);
                notificationSettings.postValue(defaultSettings);
                notificationsEnabled.postValue(defaultSettings.isNotificationsEnabled());
            }
        }).start();
    }

    public LiveData<NotificationSettings> getNotificationSettings() {
        return notificationSettings;
    }

    public LiveData<Boolean> getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean enabled) {
        new Thread(() -> {
            NotificationSettings settings = database.notificationSettingsDao().getById("default");
            if (settings != null) {
                settings.setNotificationsEnabled(enabled);
                settings.setUpdatedAt(new Date());
                database.notificationSettingsDao().update(settings);
                notificationSettings.postValue(settings);
                notificationsEnabled.postValue(enabled);
                notificationPersistence.saveSettings(settings);
            }
        }).start();
    }

    public void setSoundEnabled(boolean enabled) {
        new Thread(() -> {
            NotificationSettings settings = database.notificationSettingsDao().getById("default");
            if (settings != null) {
                settings.setSoundEnabled(enabled);
                settings.setUpdatedAt(new Date());
                database.notificationSettingsDao().update(settings);
                notificationSettings.postValue(settings);
                notificationPersistence.saveSettings(settings);
            }
        }).start();
    }

    public void setSoundUri(String soundUri) {
        new Thread(() -> {
            NotificationSettings settings = database.notificationSettingsDao().getById("default");
            if (settings != null) {
                settings.setSoundUri(soundUri);
                settings.setUpdatedAt(new Date());
                database.notificationSettingsDao().update(settings);
                notificationSettings.postValue(settings);
                notificationPersistence.saveSettings(settings);
            }
        }).start();
    }

    public void setVibrationEnabled(boolean enabled) {
        new Thread(() -> {
            NotificationSettings settings = database.notificationSettingsDao().getById("default");
            if (settings != null) {
                settings.setVibrationEnabled(enabled);
                settings.setUpdatedAt(new Date());
                database.notificationSettingsDao().update(settings);
                notificationSettings.postValue(settings);
                notificationPersistence.saveSettings(settings);
            }
        }).start();
    }

    public void setVibrationPattern(VibrationPattern pattern) {
        new Thread(() -> {
            NotificationSettings settings = database.notificationSettingsDao().getById("default");
            if (settings != null) {
                settings.setVibrationPattern(pattern);
                settings.setUpdatedAt(new Date());
                database.notificationSettingsDao().update(settings);
                notificationSettings.postValue(settings);
                notificationPersistence.saveSettings(settings);
            }
        }).start();
    }

    public void setLedEnabled(boolean enabled) {
        new Thread(() -> {
            NotificationSettings settings = database.notificationSettingsDao().getById("default");
            if (settings != null) {
                settings.setLedEnabled(enabled);
                settings.setUpdatedAt(new Date());
                database.notificationSettingsDao().update(settings);
                notificationSettings.postValue(settings);
                notificationPersistence.saveSettings(settings);
            }
        }).start();
    }

    public void setLedColor(int ledColor) {
        new Thread(() -> {
            NotificationSettings settings = database.notificationSettingsDao().getById("default");
            if (settings != null) {
                settings.setLedColor(ledColor);
                settings.setUpdatedAt(new Date());
                database.notificationSettingsDao().update(settings);
                notificationSettings.postValue(settings);
                notificationPersistence.saveSettings(settings);
            }
        }).start();
    }

    public void setPriority(NotificationPriority priority) {
        new Thread(() -> {
            NotificationSettings settings = database.notificationSettingsDao().getById("default");
            if (settings != null) {
                settings.setPriority(priority);
                settings.setUpdatedAt(new Date());
                database.notificationSettingsDao().update(settings);
                notificationSettings.postValue(settings);
                notificationPersistence.saveSettings(settings);
            }
        }).start();
    }

    public void refreshSettings() {
        loadSettings();
    }

    public void testNotification() {
        notificationManager.showNotification(
            "Test Notification",
            "This is a test notification from ODIN",
            "security_channel"
        );
    }
}
