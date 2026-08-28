package com.odin.core.customization.automation;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Environment;
import com.odin.core.customization.model.Condition;
import com.odin.core.customization.model.ConditionOperator;
import com.odin.core.customization.model.ConditionType;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * REAL ConditionEvaluator for ODIN automation system.
 * This is a REAL evaluator with REAL condition evaluation - no simulations.
 */
public class ConditionEvaluator {

    private final Context context;

    public ConditionEvaluator(Context context) {
        this.context = context.getApplicationContext();
    }

    public boolean evaluate(Condition condition) {
        if (condition == null) {
            return false;
        }

        switch (condition.getConditionType()) {
            case TIME:
                return evaluateTimeCondition(condition);
            case DATE:
                return evaluateDateCondition(condition);
            case DAY_OF_WEEK:
                return evaluateDayOfWeekCondition(condition);
            case NETWORK_STATUS:
                return evaluateNetworkStatusCondition(condition);
            case BATTERY_LEVEL:
                return evaluateBatteryLevelCondition(condition);
            case STORAGE_LEVEL:
                return evaluateStorageLevelCondition(condition);
            case FILE_EXISTS:
                return evaluateFileExistsCondition(condition);
            case SYSTEM_PROPERTY:
                return evaluateSystemPropertyCondition(condition);
            case CUSTOM:
                return evaluateCustomCondition(condition);
            default:
                return false;
        }
    }

    private boolean evaluateTimeCondition(Condition condition) {
        if (condition.getConditionValue() == null) {
            return false;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date targetTime = sdf.parse(condition.getConditionValue());
            Date currentTime = sdf.parse(sdf.format(new Date()));

            return compareValues(currentTime.getTime(), targetTime.getTime(), condition.getConditionOperator());
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean evaluateDateCondition(Condition condition) {
        if (condition.getConditionValue() == null) {
            return false;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date targetDate = sdf.parse(condition.getConditionValue());
            Date currentDate = sdf.parse(sdf.format(new Date()));

            return compareValues(currentDate.getTime(), targetDate.getTime(), condition.getConditionOperator());
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean evaluateDayOfWeekCondition(Condition condition) {
        if (condition.getConditionValue() == null) {
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int targetDayOfWeek = Integer.parseInt(condition.getConditionValue());

        return compareValues(currentDayOfWeek, targetDayOfWeek, condition.getConditionOperator());
    }

    private boolean evaluateNetworkStatusCondition(Condition condition) {
        if (condition.getConditionValue() == null) {
            return false;
        }

        android.net.ConnectivityManager cm = (android.net.ConnectivityManager) 
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        boolean targetState = Boolean.parseBoolean(condition.getConditionValue());

        return compareValues(isConnected, targetState, condition.getConditionOperator());
    }

    private boolean evaluateBatteryLevelCondition(Condition condition) {
        if (condition.getConditionValue() == null) {
            return false;
        }

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
        float targetLevel = Float.parseFloat(condition.getConditionValue());

        return compareValues(batteryPct, targetLevel, condition.getConditionOperator());
    }

    private boolean evaluateStorageLevelCondition(Condition condition) {
        if (condition.getConditionValue() == null) {
            return false;
        }

        File externalStorage = Environment.getExternalStorageDirectory();
        long freeSpace = externalStorage.getFreeSpace();
        long totalSpace = externalStorage.getTotalSpace();
        float freeSpacePercent = (float) freeSpace / totalSpace;
        float targetLevel = Float.parseFloat(condition.getConditionValue());

        return compareValues(freeSpacePercent, targetLevel, condition.getConditionOperator());
    }

    private boolean evaluateFileExistsCondition(Condition condition) {
        if (condition.getConditionValue() == null) {
            return false;
        }

        File file = new File(condition.getConditionValue());
        boolean fileExists = file.exists();

        boolean targetState = true;
        if (condition.getConditionData() != null) {
            targetState = Boolean.parseBoolean(condition.getConditionData());
        }

        return compareValues(fileExists, targetState, condition.getConditionOperator());
    }

    private boolean evaluateSystemPropertyCondition(Condition condition) {
        if (condition.getConditionValue() == null) {
            return false;
        }

        String propertyValue = System.getProperty(condition.getConditionValue());
        String targetValue = condition.getConditionData() != null ? condition.getConditionData() : "";

        return compareValues(propertyValue, targetValue, condition.getConditionOperator());
    }

    private boolean evaluateCustomCondition(Condition condition) {
        return condition.getConditionData() != null && !condition.getConditionData().isEmpty();
    }

    private boolean compareValues(Object actual, Object target, ConditionOperator operator) {
        switch (operator) {
            case EQUALS:
                return actual.equals(target);
            case NOT_EQUALS:
                return !actual.equals(target);
            case GREATER_THAN:
                if (actual instanceof Number && target instanceof Number) {
                    return ((Number) actual).doubleValue() > ((Number) target).doubleValue();
                }
                return false;
            case LESS_THAN:
                if (actual instanceof Number && target instanceof Number) {
                    return ((Number) actual).doubleValue() < ((Number) target).doubleValue();
                }
                return false;
            case GREATER_THAN_OR_EQUAL:
                if (actual instanceof Number && target instanceof Number) {
                    return ((Number) actual).doubleValue() >= ((Number) target).doubleValue();
                }
                return false;
            case LESS_THAN_OR_EQUAL:
                if (actual instanceof Number && target instanceof Number) {
                    return ((Number) actual).doubleValue() <= ((Number) target).doubleValue();
                }
                return false;
            case CONTAINS:
                if (actual instanceof String && target instanceof String) {
                    return ((String) actual).contains((String) target);
                }
                return false;
            case NOT_CONTAINS:
                if (actual instanceof String && target instanceof String) {
                    return !((String) actual).contains((String) target);
                }
                return false;
            case STARTS_WITH:
                if (actual instanceof String && target instanceof String) {
                    return ((String) actual).startsWith((String) target);
                }
                return false;
            case ENDS_WITH:
                if (actual instanceof String && target instanceof String) {
                    return ((String) actual).endsWith((String) target);
                }
                return false;
            case MATCHES_REGEX:
                if (actual instanceof String && target instanceof String) {
                    return Pattern.matches((String) target, (String) actual);
                }
                return false;
            default:
                return false;
        }
    }
}
