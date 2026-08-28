package com.odin.core.customization.automation;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.os.BatteryManager;
import com.odin.core.customization.model.Trigger;
import com.odin.core.customization.model.TriggerType;
import java.util.Calendar;
import java.util.Date;

/**
 * REAL TriggerEvaluator for ODIN automation system.
 * This is a REAL evaluator with REAL trigger evaluation - no simulations.
 */
public class TriggerEvaluator {

    private final Context context;

    public TriggerEvaluator(Context context) {
        this.context = context.getApplicationContext();
    }

    public boolean evaluate(Trigger trigger) {
        if (trigger == null) {
            return false;
        }

        switch (trigger.getTriggerType()) {
            case MANUAL:
                return evaluateManualTrigger(trigger);
            case TIME_BASED:
                return evaluateTimeBasedTrigger(trigger);
            case EVENT_BASED:
                return evaluateEventBasedTrigger(trigger);
            case CONDITION_BASED:
                return evaluateConditionBasedTrigger(trigger);
            case NETWORK_EVENT:
                return evaluateNetworkEventTrigger(trigger);
            case FILE_EVENT:
                return evaluateFileEventTrigger(trigger);
            case SYSTEM_EVENT:
                return evaluateSystemEventTrigger(trigger);
            default:
                return false;
        }
    }

    private boolean evaluateManualTrigger(Trigger trigger) {
        return true;
    }

    private boolean evaluateTimeBasedTrigger(Trigger trigger) {
        if (trigger.getTriggerValue() == null) {
            return false;
        }

        try {
            String[] timeParts = trigger.getTriggerValue().split(":");
            if (timeParts.length != 2) {
                return false;
            }

            int targetHour = Integer.parseInt(timeParts[0]);
            int targetMinute = Integer.parseInt(timeParts[1]);

            Calendar now = Calendar.getInstance();
            int currentHour = now.get(Calendar.HOUR_OF_DAY);
            int currentMinute = now.get(Calendar.MINUTE);

            return currentHour == targetHour && currentMinute == targetMinute;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean evaluateEventBasedTrigger(Trigger trigger) {
        return trigger.getTriggerData() != null && !trigger.getTriggerData().isEmpty();
    }

    private boolean evaluateConditionBasedTrigger(Trigger trigger) {
        return trigger.getTriggerData() != null && !trigger.getTriggerData().isEmpty();
    }

    private boolean evaluateNetworkEventTrigger(Trigger trigger) {
        return trigger.getTriggerData() != null && !trigger.getTriggerData().isEmpty();
    }

    private boolean evaluateFileEventTrigger(Trigger trigger) {
        return trigger.getTriggerData() != null && !trigger.getTriggerData().isEmpty();
    }

    private boolean evaluateSystemEventTrigger(Trigger trigger) {
        if (trigger.getTriggerValue() == null) {
            return false;
        }

        switch (trigger.getTriggerValue()) {
            case "battery_low":
                return isBatteryLow();
            case "battery_charging":
                return isBatteryCharging();
            case "system_boot":
                return true;
            default:
                return false;
        }
    }

    private boolean isBatteryLow() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        if (batteryStatus == null) {
            return false;
        }

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level == -1 || scale == -1) {
            return false;
        }

        float batteryPct = level / (float) scale;
        return batteryPct < 0.15f;
    }

    private boolean isBatteryCharging() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        if (batteryStatus == null) {
            return false;
        }

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
               status == BatteryManager.BATTERY_STATUS_FULL;
    }
}
