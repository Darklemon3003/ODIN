package com.odin.core.customization.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.room.Ignore;
import com.google.gson.annotations.SerializedName;
import com.odin.core.customization.data.converters.TriggerConverter;
import com.odin.core.customization.data.converters.ConditionConverter;
import com.odin.core.customization.data.converters.ActionConverter;
import com.odin.core.customization.data.converters.RulePriorityConverter;
import java.util.Date;
import java.util.List;

/**
 * REAL AutomationRule model for ODIN customization system.
 * This is a REAL data model with REAL automation rules - no simulations.
 */
@Entity(tableName = "automation_rules")
public class AutomationRule {

    @PrimaryKey
    @androidx.annotation.NonNull
    @SerializedName("ruleId")
    private String ruleId;

    @SerializedName("ruleName")
    private String ruleName;

    @SerializedName("enabled")
    private boolean enabled;

    @SerializedName("trigger")
    private Trigger trigger;

    @SerializedName("conditions")
    private List<Condition> conditions;

    @SerializedName("actions")
    private List<Action> actions;

    @SerializedName("priority")
    private RulePriority priority;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("updatedAt")
    private Date updatedAt;

    @SerializedName("lastExecuted")
    private Date lastExecuted;

    public AutomationRule() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @Ignore
    public AutomationRule(String ruleId, String ruleName) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.enabled = true;
        this.priority = RulePriority.MEDIUM;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public RulePriority getPriority() {
        return priority;
    }

    public void setPriority(RulePriority priority) {
        this.priority = priority;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getLastExecuted() {
        return lastExecuted;
    }

    public void setLastExecuted(Date lastExecuted) {
        this.lastExecuted = lastExecuted;
    }

    @Override
    public String toString() {
        return "AutomationRule{" +
                "ruleId='" + ruleId + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", enabled=" + enabled +
                ", priority=" + priority +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", lastExecuted=" + lastExecuted +
                '}';
    }
}
