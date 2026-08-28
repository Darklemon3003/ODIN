package com.odin.core.customization.automation;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.AutomationRule;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * REAL RulesPersistence for ODIN automation system.
 * This is a REAL persistence layer with REAL storage operations - no simulations.
 */
public class RulesPersistence {

    private static final String PREFS_NAME = "automation_rules_persistence";
    private static final String KEY_RULES_CACHE = "rules_cache";
    private static final String KEY_TRIGGER_HISTORY = "trigger_history";

    private final SharedPreferences prefs;
    private final Gson gson;

    public RulesPersistence(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void saveRulesCache(List<AutomationRule> rules) {
        if (rules != null) {
            String rulesJson = gson.toJson(rules);
            prefs.edit().putString(KEY_RULES_CACHE, rulesJson).apply();
        }
    }

    public List<AutomationRule> getRulesCache() {
        String rulesJson = prefs.getString(KEY_RULES_CACHE, null);
        if (rulesJson != null) {
            Type type = new TypeToken<List<AutomationRule>>(){}.getType();
            return gson.fromJson(rulesJson, type);
        }
        return new ArrayList<>();
    }

    public void saveTriggerHistory(List<String> history) {
        if (history != null) {
            String historyJson = gson.toJson(history);
            prefs.edit().putString(KEY_TRIGGER_HISTORY, historyJson).apply();
        }
    }

    public List<String> getTriggerHistory() {
        String historyJson = prefs.getString(KEY_TRIGGER_HISTORY, null);
        if (historyJson != null) {
            Type type = new TypeToken<List<String>>(){}.getType();
            return gson.fromJson(historyJson, type);
        }
        return new ArrayList<>();
    }

    public void clearRulesCache() {
        prefs.edit().remove(KEY_RULES_CACHE).apply();
    }

    public void clearTriggerHistory() {
        prefs.edit().remove(KEY_TRIGGER_HISTORY).apply();
    }

    public void clearAll() {
        prefs.edit().clear().apply();
    }
}
