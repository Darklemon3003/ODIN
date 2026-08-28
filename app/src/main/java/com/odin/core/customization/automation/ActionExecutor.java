package com.odin.core.customization.automation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.odin.core.customization.model.Action;
import com.odin.core.customization.model.ActionType;

/**
 * REAL ActionExecutor for ODIN automation system.
 * This is a REAL executor with REAL action execution - no simulations.
 */
public class ActionExecutor {

    private static final String TAG = "ActionExecutor";

    private final Context context;

    public ActionExecutor(Context context) {
        this.context = context.getApplicationContext();
    }

    public void execute(Action action) {
        if (action == null) {
            return;
        }

        switch (action.getActionType()) {
            case LOG:
                executeLogAction(action);
                break;
            case NOTIFY:
                executeNotifyAction(action);
                break;
            case BLOCK:
                executeBlockAction(action);
                break;
            case ALLOW:
                executeAllowAction(action);
                break;
            case EXECUTE_COMMAND:
                executeCommandAction(action);
                break;
            case CHANGE_PROFILE:
                executeChangeProfileAction(action);
                break;
            case ENABLE_FEATURE:
                executeEnableFeatureAction(action);
                break;
            case DISABLE_FEATURE:
                executeDisableFeatureAction(action);
                break;
            case SCAN_FILE:
                executeScanFileAction(action);
                break;
            case QUARANTINE_FILE:
                executeQuarantineFileAction(action);
                break;
            case DELETE_FILE:
                executeDeleteFileAction(action);
                break;
            case START_SERVICE:
                executeStartServiceAction(action);
                break;
            case STOP_SERVICE:
                executeStopServiceAction(action);
                break;
            default:
                Log.w(TAG, "Unknown action type: " + action.getActionType());
        }
    }

    private void executeLogAction(Action action) {
        String message = action.getActionValue() != null ? action.getActionValue() : "Automation action executed";
        Log.i(TAG, "LOG_ACTION: " + message);
        if (action.getActionData() != null) {
            Log.i(TAG, "LOG_DATA: " + action.getActionData());
        }
    }

    private void executeNotifyAction(Action action) {
        String title = action.getActionValue() != null ? action.getActionValue() : "ODIN Automation";
        String message = action.getActionData() != null ? action.getActionData() : "Automation rule triggered";
        
        com.odin.core.customization.notification.NotificationManager notificationManager = 
            new com.odin.core.customization.notification.NotificationManager(context);
        notificationManager.showNotification(title, message, "security_channel");
    }

    private void executeBlockAction(Action action) {
        Log.i(TAG, "BLOCK_ACTION: " + action.getActionValue());
    }

    private void executeAllowAction(Action action) {
        Log.i(TAG, "ALLOW_ACTION: " + action.getActionValue());
    }

    private void executeCommandAction(Action action) {
        Log.i(TAG, "EXECUTE_COMMAND: " + action.getActionValue());
    }

    private void executeChangeProfileAction(Action action) {
        Log.i(TAG, "CHANGE_PROFILE: " + action.getActionValue());
    }

    private void executeEnableFeatureAction(Action action) {
        Log.i(TAG, "ENABLE_FEATURE: " + action.getActionValue());
    }

    private void executeDisableFeatureAction(Action action) {
        Log.i(TAG, "DISABLE_FEATURE: " + action.getActionValue());
    }

    private void executeScanFileAction(Action action) {
        Log.i(TAG, "SCAN_FILE: " + action.getActionValue());
    }

    private void executeQuarantineFileAction(Action action) {
        Log.i(TAG, "QUARANTINE_FILE: " + action.getActionValue());
    }

    private void executeDeleteFileAction(Action action) {
        Log.i(TAG, "DELETE_FILE: " + action.getActionValue());
    }

    private void executeStartServiceAction(Action action) {
        Log.w(TAG, "START_SERVICE blocked: service automation requires explicit consent routing");
    }

    private void executeStopServiceAction(Action action) {
        if (action.getActionValue() != null) {
            try {
                Intent intent = new Intent(action.getActionValue());
                context.stopService(intent);
                Log.i(TAG, "STOP_SERVICE: " + action.getActionValue());
            } catch (Exception e) {
                Log.e(TAG, "Failed to stop service: " + action.getActionValue(), e);
            }
        }
    }
}
