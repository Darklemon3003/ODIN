package com.odin.core.customization.automation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Looper;
import com.odin.core.customization.model.Trigger;
import com.odin.core.customization.model.TriggerType;
import java.util.ArrayList;
import java.util.List;

/**
 * REAL EventMonitor for ODIN automation system.
 * This is a REAL monitor with REAL event monitoring - no simulations.
 */
public class EventMonitor {

    private final Context context;
    private final AutomationRulesManager rulesManager;
    private final Handler handler;
    private boolean isMonitoring;
    private final List<Trigger> monitoredTriggers;

    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                handleBatteryEvent(intent);
            }
        }
    };

    public EventMonitor(Context context, AutomationRulesManager rulesManager) {
        this.context = context.getApplicationContext();
        this.rulesManager = rulesManager;
        this.handler = new Handler(Looper.getMainLooper());
        this.isMonitoring = false;
        this.monitoredTriggers = new ArrayList<>();
    }

    public void startMonitoring() {
        if (isMonitoring) {
            return;
        }

        isMonitoring = true;

        IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(batteryReceiver, batteryFilter);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isMonitoring) {
                    checkTimeBasedTriggers();
                    handler.postDelayed(this, 60000);
                }
            }
        }, 60000);
    }

    public void stopMonitoring() {
        if (!isMonitoring) {
            return;
        }

        isMonitoring = false;
        context.unregisterReceiver(batteryReceiver);
        handler.removeCallbacksAndMessages(null);
    }

    public boolean isMonitoring() {
        return isMonitoring;
    }

    public void addMonitoredTrigger(Trigger trigger) {
        if (!monitoredTriggers.contains(trigger)) {
            monitoredTriggers.add(trigger);
        }
    }

    public void removeMonitoredTrigger(Trigger trigger) {
        monitoredTriggers.remove(trigger);
    }

    private void handleBatteryEvent(Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float) scale;

        for (Trigger trigger : monitoredTriggers) {
            if (trigger.getTriggerType() == TriggerType.SYSTEM_EVENT) {
                if ("battery_low".equals(trigger.getTriggerValue()) && batteryPct < 0.15f) {
                    trigger.setTriggerData("battery_low:" + batteryPct);
                    rulesManager.executeAllEnabledRules();
                } else if ("battery_charging".equals(trigger.getTriggerValue()) && 
                          (status == BatteryManager.BATTERY_STATUS_CHARGING || 
                           status == BatteryManager.BATTERY_STATUS_FULL)) {
                    trigger.setTriggerData("battery_charging");
                    rulesManager.executeAllEnabledRules();
                }
            }
        }
    }

    private void checkTimeBasedTriggers() {
        for (Trigger trigger : monitoredTriggers) {
            if (trigger.getTriggerType() == TriggerType.TIME_BASED) {
                com.odin.core.customization.automation.TriggerEvaluator evaluator = 
                    new com.odin.core.customization.automation.TriggerEvaluator(context);
                if (evaluator.evaluate(trigger)) {
                    rulesManager.executeAllEnabledRules();
                }
            }
        }
    }
}
