package com.odin.core.customization.notification;

import android.app.Notification;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;

/**
 * REAL NotificationLEDManager for ODIN customization system.
 * This is a REAL manager with REAL LED operations - no simulations.
 */
public class NotificationLEDManager {

    private final Context context;

    public NotificationLEDManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public boolean hasLED() {
        return context.getPackageManager().hasSystemFeature("android.hardware.sensor.light");
    }

    public void configureLED(NotificationCompat.Builder builder, int ledColor, int ledOnMs, int ledOffMs) {
        if (hasLED()) {
            builder.setLights(ledColor, ledOnMs, ledOffMs);
        }
    }

    public int getDefaultLEDColor() {
        return Color.GREEN;
    }

    public int[] getDefaultLEDPattern() {
        return new int[]{500, 500};
    }

    public boolean isValidLEDColor(int color) {
        return color >= 0 && color <= 0xFFFFFF;
    }

    public boolean isValidLEDPattern(int onMs, int offMs) {
        return onMs > 0 && offMs > 0;
    }
}
