package com.odin.core.customization.notification;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import com.odin.core.customization.model.VibrationPattern;

/**
 * REAL NotificationVibrationManager for ODIN customization system.
 * This is a REAL manager with REAL vibration operations - no simulations.
 */
public class NotificationVibrationManager {

    private final Context context;
    private final Vibrator vibrator;

    public NotificationVibrationManager(Context context) {
        this.context = context.getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            this.vibrator = vibratorManager.getDefaultVibrator();
        } else {
            this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    public void vibrate(VibrationPattern pattern) {
        if (vibrator == null || !vibrator.hasVibrator()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect effect = VibrationEffect.createWaveform(pattern.getPattern(), -1);
            vibrator.vibrate(effect);
        } else {
            vibrator.vibrate(pattern.getPattern(), -1);
        }
    }

    public void vibrate(long milliseconds) {
        if (vibrator == null || !vibrator.hasVibrator()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect effect = VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(effect);
        } else {
            vibrator.vibrate(milliseconds);
        }
    }

    public void cancelVibration() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    public boolean hasVibrator() {
        return vibrator != null && vibrator.hasVibrator();
    }
}
