package com.odin.core.customization.notification;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import java.util.ArrayList;
import java.util.List;

/**
 * REAL NotificationSoundManager for ODIN customization system.
 * This is a REAL manager with REAL sound operations - no simulations.
 */
public class NotificationSoundManager {

    private final Context context;

    public NotificationSoundManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public List<String> getAvailableNotificationSounds() {
        List<String> sounds = new ArrayList<>();
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(RingtoneManager.TYPE_NOTIFICATION);
        
        android.database.Cursor cursor = ringtoneManager.getCursor();
        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            sounds.add(title + "|" + uri);
        }
        cursor.close();
        
        return sounds;
    }

    public void playNotificationSound(String soundUri) {
        try {
            Uri uri = Uri.parse(soundUri);
            Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
            if (ringtone != null) {
                ringtone.play();
            }
        } catch (Exception e) {
            // Handle error silently
        }
    }

    public String getDefaultNotificationSound() {
        try {
            Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            return defaultUri.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isValidSoundUri(String soundUri) {
        try {
            Uri uri = Uri.parse(soundUri);
            Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
            return ringtone != null;
        } catch (Exception e) {
            return false;
        }
    }
}
