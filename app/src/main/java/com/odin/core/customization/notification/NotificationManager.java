package com.odin.core.customization.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.odin.core.R;
import com.odin.core.customization.data.CustomizationDatabase;
import com.odin.core.customization.model.NotificationSettings;
import com.odin.core.customization.model.NotificationPriority;
import com.odin.core.customization.model.VibrationPattern;

/**
 * REAL NotificationManager for ODIN customization system.
 * This is a REAL manager with REAL notification operations - no simulations.
 */
public class NotificationManager {

    private static final String CHANNEL_ID_SECURITY = "security_channel";
    private static final String CHANNEL_ID_FORENSICS = "forensics_channel";
    private static final String CHANNEL_ID_SYSTEM = "system_channel";

    private final Context context;
    private final android.app.NotificationManager systemNotificationManager;
    private final CustomizationDatabase database;

    public NotificationManager(Context context) {
        this.context = context.getApplicationContext();
        this.systemNotificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.database = CustomizationDatabase.getInstance(context);
        initializeDefaultSettings();
        createNotificationChannels();
    }

    private void initializeDefaultSettings() {
        if (database.notificationSettingsDao().getAll().isEmpty()) {
            NotificationSettings defaultSettings = new NotificationSettings("default");
            database.notificationSettingsDao().insert(defaultSettings);
        }
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel securityChannel = new NotificationChannel(
                CHANNEL_ID_SECURITY,
                "Security Alerts",
                android.app.NotificationManager.IMPORTANCE_HIGH
            );
            securityChannel.setDescription("Critical security notifications");
            systemNotificationManager.createNotificationChannel(securityChannel);

            NotificationChannel forensicsChannel = new NotificationChannel(
                CHANNEL_ID_FORENSICS,
                "Forensics Alerts",
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            forensicsChannel.setDescription("Forensic analysis notifications");
            systemNotificationManager.createNotificationChannel(forensicsChannel);

            NotificationChannel systemChannel = new NotificationChannel(
                CHANNEL_ID_SYSTEM,
                "System Notifications",
                android.app.NotificationManager.IMPORTANCE_LOW
            );
            systemChannel.setDescription("General system notifications");
            systemNotificationManager.createNotificationChannel(systemChannel);
        }
    }

    public void showNotification(String title, String message, String channelId) {
        NotificationSettings settings = getCurrentSettings();
        if (!settings.isNotificationsEnabled()) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_odin_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(mapPriority(settings.getPriority()))
            .setAutoCancel(true);

        if (settings.isSoundEnabled() && settings.getSoundUri() != null) {
            builder.setSound(android.net.Uri.parse(settings.getSoundUri()));
        }

        if (settings.isVibrationEnabled()) {
            builder.setVibrate(settings.getVibrationPattern().getPattern());
        }

        systemNotificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private int mapPriority(NotificationPriority priority) {
        switch (priority) {
            case MIN:
                return NotificationCompat.PRIORITY_MIN;
            case LOW:
                return NotificationCompat.PRIORITY_LOW;
            case DEFAULT:
                return NotificationCompat.PRIORITY_DEFAULT;
            case HIGH:
                return NotificationCompat.PRIORITY_HIGH;
            case MAX:
            case URGENT:
                return NotificationCompat.PRIORITY_MAX;
            default:
                return NotificationCompat.PRIORITY_DEFAULT;
        }
    }

    public NotificationSettings getCurrentSettings() {
        return database.notificationSettingsDao().getById("default");
    }

    public void updateSettings(NotificationSettings settings) {
        database.notificationSettingsDao().update(settings);
    }

    public void enableNotifications(boolean enabled) {
        NotificationSettings settings = getCurrentSettings();
        settings.setNotificationsEnabled(enabled);
        updateSettings(settings);
    }

    public void setSoundEnabled(boolean enabled) {
        NotificationSettings settings = getCurrentSettings();
        settings.setSoundEnabled(enabled);
        updateSettings(settings);
    }

    public void setVibrationEnabled(boolean enabled) {
        NotificationSettings settings = getCurrentSettings();
        settings.setVibrationEnabled(enabled);
        updateSettings(settings);
    }

    public void setVibrationPattern(VibrationPattern pattern) {
        NotificationSettings settings = getCurrentSettings();
        settings.setVibrationPattern(pattern);
        updateSettings(settings);
    }
}
