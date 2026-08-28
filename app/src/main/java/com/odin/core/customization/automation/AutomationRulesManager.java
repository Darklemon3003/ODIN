package com.odin.core.customization.automation;

import android.content.Context;
import com.odin.core.customization.data.CustomizationDatabase;
import com.odin.core.customization.model.AutomationRule;
import com.odin.core.customization.model.RulePriority;
import com.odin.core.customization.model.Trigger;
import com.odin.core.customization.model.TriggerType;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * REAL AutomationRulesManager for ODIN customization system.
 * This is a REAL manager with REAL automation rule operations - no simulations.
 */
public class AutomationRulesManager {

    private final Context context;
    private final CustomizationDatabase database;
    private final TriggerEvaluator triggerEvaluator;
    private final ConditionEvaluator conditionEvaluator;
    private final ActionExecutor actionExecutor;

    public AutomationRulesManager(Context context) {
        this.context = context.getApplicationContext();
        this.database = CustomizationDatabase.getInstance(context);
        this.triggerEvaluator = new TriggerEvaluator(context);
        this.conditionEvaluator = new ConditionEvaluator(context);
        this.actionExecutor = new ActionExecutor(context);
    }

    public AutomationRule createRule(String ruleName, Trigger trigger, RulePriority priority) {
        AutomationRule rule = new AutomationRule(UUID.randomUUID().toString(), ruleName);
        rule.setTrigger(trigger);
        rule.setPriority(priority);
        rule.setEnabled(true);
        rule.setCreatedAt(new Date());
        rule.setUpdatedAt(new Date());
        database.automationRuleDao().insert(rule);
        return rule;
    }

    public AutomationRule getRule(String ruleId) {
        return database.automationRuleDao().getById(ruleId);
    }

    public List<AutomationRule> getAllRules() {
        return database.automationRuleDao().getAllOrderedByPriority();
    }

    public List<AutomationRule> getEnabledRules() {
        return database.automationRuleDao().getByEnabledStatus(true);
    }

    public List<AutomationRule> getRulesByPriority(RulePriority priority) {
        return database.automationRuleDao().getByPriority(priority.name());
    }

    public AutomationRule updateRule(AutomationRule rule) {
        rule.setUpdatedAt(new Date());
        database.automationRuleDao().update(rule);
        return rule;
    }

    public void deleteRule(String ruleId) {
        database.automationRuleDao().deleteById(ruleId);
    }

    public void enableRule(String ruleId) {
        AutomationRule rule = getRule(ruleId);
        if (rule != null) {
            rule.setEnabled(true);
            rule.setUpdatedAt(new Date());
            database.automationRuleDao().update(rule);
        }
    }

    public void disableRule(String ruleId) {
        AutomationRule rule = getRule(ruleId);
        if (rule != null) {
            rule.setEnabled(false);
            rule.setUpdatedAt(new Date());
            database.automationRuleDao().update(rule);
        }
    }

    public boolean evaluateRule(AutomationRule rule) {
        if (!rule.isEnabled()) {
            return false;
        }

        boolean triggerMatched = triggerEvaluator.evaluate(rule.getTrigger());
        if (!triggerMatched) {
            return false;
        }

        if (rule.getConditions() != null && !rule.getConditions().isEmpty()) {
            boolean allConditionsMet = true;
            for (com.odin.core.customization.model.Condition condition : rule.getConditions()) {
                if (!conditionEvaluator.evaluate(condition)) {
                    allConditionsMet = false;
                    break;
                }
            }
            if (!allConditionsMet) {
                return false;
            }
        }

        return true;
    }

    public void executeRule(AutomationRule rule) {
        if (evaluateRule(rule)) {
            if (rule.getActions() != null && !rule.getActions().isEmpty()) {
                for (com.odin.core.customization.model.Action action : rule.getActions()) {
                    actionExecutor.execute(action);
                }
            }
            rule.setLastExecuted(new Date());
            updateRule(rule);
        }
    }

    public void executeAllEnabledRules() {
        List<AutomationRule> enabledRules = getEnabledRules();
        for (AutomationRule rule : enabledRules) {
            executeRule(rule);
        }
    }
}
