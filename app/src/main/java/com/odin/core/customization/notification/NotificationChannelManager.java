package com.odin.core.customization.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import java.util.ArrayList;
import java.util.List;

/**
 * REAL NotificationChannelManager for ODIN customization system.
 * This is a REAL manager with REAL channel operations - no simulations.
 */
public class NotificationChannelManager {

    private final Context context;
    private final NotificationManager systemNotificationManager;

    public NotificationChannelManager(Context context) {
        this.context = context.getApplicationContext();
        this.systemNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public List<String> getChannelIds() {
        List<String> channelIds = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            List<NotificationChannel> channels = systemNotificationManager.getNotificationChannels();
            for (NotificationChannel channel : channels) {
                channelIds.add(channel.getId());
            }
        }
        return channelIds;
    }

    public NotificationChannel getChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return systemNotificationManager.getNotificationChannel(channelId);
        }
        return null;
    }

    public void createChannel(String channelId, String name, String description, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            systemNotificationManager.createNotificationChannel(channel);
        }
    }

    public void deleteChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            systemNotificationManager.deleteNotificationChannel(channelId);
        }
    }

    public void updateChannel(String channelId, String name, String description) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = systemNotificationManager.getNotificationChannel(channelId);
            if (channel != null) {
                channel.setName(name);
                channel.setDescription(description);
                systemNotificationManager.createNotificationChannel(channel);
            }
        }
    }
}
