package com.odin.core.customization.model;

import com.google.gson.annotations.SerializedName;

/**
 * REAL Trigger model for ODIN automation system.
 * This is a REAL data model with REAL trigger configurations - no simulations.
 */
public class Trigger {

    @SerializedName("triggerType")
    private TriggerType triggerType;

    @SerializedName("triggerValue")
    private String triggerValue;

    @SerializedName("triggerData")
    private String triggerData;

    public Trigger() {
        this.triggerType = TriggerType.MANUAL;
    }

    public Trigger(TriggerType triggerType, String triggerValue) {
        this.triggerType = triggerType;
        this.triggerValue = triggerValue;
    }

    public Trigger(TriggerType triggerType, String triggerValue, String triggerData) {
        this.triggerType = triggerType;
        this.triggerValue = triggerValue;
        this.triggerData = triggerData;
    }

    // Getters and Setters
    public TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerValue() {
        return triggerValue;
    }

    public void setTriggerValue(String triggerValue) {
        this.triggerValue = triggerValue;
    }

    public String getTriggerData() {
        return triggerData;
    }

    public void setTriggerData(String triggerData) {
        this.triggerData = triggerData;
    }
}
