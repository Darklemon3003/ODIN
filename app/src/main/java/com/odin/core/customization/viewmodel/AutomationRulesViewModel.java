package com.odin.core.customization.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.odin.core.customization.data.CustomizationDatabase;
import com.odin.core.customization.automation.AutomationRulesManager;
import com.odin.core.customization.automation.EventMonitor;
import com.odin.core.customization.automation.RulesPersistence;
import com.odin.core.customization.model.AutomationRule;
import com.odin.core.customization.model.RulePriority;
import com.odin.core.customization.model.Trigger;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * REAL AutomationRulesViewModel for ODIN customization system.
 * This is a REAL ViewModel with REAL LiveData - no simulations.
 */
public class AutomationRulesViewModel extends AndroidViewModel {

    private final AutomationRulesManager rulesManager;
    private final RulesPersistence rulesPersistence;
    private final EventMonitor eventMonitor;
    private final CustomizationDatabase database;

    private final MutableLiveData<List<AutomationRule>> automationRules;
    private final MutableLiveData<Boolean> isMonitoring;

    public AutomationRulesViewModel(@NonNull Application application) {
        super(application);
        this.rulesManager = new AutomationRulesManager(application);
        this.rulesPersistence = new RulesPersistence(application);
        this.eventMonitor = new EventMonitor(application, rulesManager);
        this.database = CustomizationDatabase.getInstance(application);

        this.automationRules = new MutableLiveData<>();
        this.isMonitoring = new MutableLiveData<>(false);

        loadRules();
    }

    private void loadRules() {
        new Thread(() -> {
            List<AutomationRule> rules = database.automationRuleDao().getAllOrderedByPriority();
            automationRules.postValue(rules);
            rulesPersistence.saveRulesCache(rules);
        }).start();
    }

    public LiveData<List<AutomationRule>> getAutomationRules() {
        return automationRules;
    }

    public LiveData<Boolean> getIsMonitoring() {
        return isMonitoring;
    }

    public void createRule(String ruleName, Trigger trigger, RulePriority priority) {
        new Thread(() -> {
            AutomationRule rule = rulesManager.createRule(ruleName, trigger, priority);
            List<AutomationRule> rules = database.automationRuleDao().getAllOrderedByPriority();
            automationRules.postValue(rules);
            rulesPersistence.saveRulesCache(rules);
        }).start();
    }

    public void updateRule(AutomationRule rule) {
        new Thread(() -> {
            AutomationRule updated = rulesManager.updateRule(rule);
            List<AutomationRule> rules = database.automationRuleDao().getAllOrderedByPriority();
            automationRules.postValue(rules);
            rulesPersistence.saveRulesCache(rules);
        }).start();
    }

    public void deleteRule(String ruleId) {
        new Thread(() -> {
            rulesManager.deleteRule(ruleId);
            List<AutomationRule> rules = database.automationRuleDao().getAllOrderedByPriority();
            automationRules.postValue(rules);
            rulesPersistence.saveRulesCache(rules);
        }).start();
    }

    public void enableRule(String ruleId) {
        new Thread(() -> {
            rulesManager.enableRule(ruleId);
            List<AutomationRule> rules = database.automationRuleDao().getAllOrderedByPriority();
            automationRules.postValue(rules);
            rulesPersistence.saveRulesCache(rules);
        }).start();
    }

    public void disableRule(String ruleId) {
        new Thread(() -> {
            rulesManager.disableRule(ruleId);
            List<AutomationRule> rules = database.automationRuleDao().getAllOrderedByPriority();
            automationRules.postValue(rules);
            rulesPersistence.saveRulesCache(rules);
        }).start();
    }

    public void executeRule(String ruleId) {
        new Thread(() -> {
            AutomationRule rule = rulesManager.getRule(ruleId);
            if (rule != null) {
                rulesManager.executeRule(rule);
                List<AutomationRule> rules = database.automationRuleDao().getAllOrderedByPriority();
                automationRules.postValue(rules);
            }
        }).start();
    }

    public void executeAllEnabledRules() {
        new Thread(() -> {
            rulesManager.executeAllEnabledRules();
            List<AutomationRule> rules = database.automationRuleDao().getAllOrderedByPriority();
            automationRules.postValue(rules);
        }).start();
    }

    public void startMonitoring() {
        eventMonitor.startMonitoring();
        isMonitoring.postValue(true);
    }

    public void stopMonitoring() {
        eventMonitor.stopMonitoring();
        isMonitoring.postValue(false);
    }

    public void refreshRules() {
        loadRules();
    }
}
