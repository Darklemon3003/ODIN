package com.odin.core.customization.model;

import com.google.gson.annotations.SerializedName;

/**
 * REAL Condition model for ODIN automation system.
 * This is a REAL data model with REAL condition configurations - no simulations.
 */
public class Condition {

    @SerializedName("conditionType")
    private ConditionType conditionType;

    @SerializedName("conditionOperator")
    private ConditionOperator conditionOperator;

    @SerializedName("conditionValue")
    private String conditionValue;

    @SerializedName("conditionData")
    private String conditionData;

    public Condition() {
        this.conditionType = ConditionType.CUSTOM;
        this.conditionOperator = ConditionOperator.EQUALS;
    }

    public Condition(ConditionType conditionType, ConditionOperator conditionOperator, String conditionValue) {
        this.conditionType = conditionType;
        this.conditionOperator = conditionOperator;
        this.conditionValue = conditionValue;
    }

    public Condition(ConditionType conditionType, ConditionOperator conditionOperator, String conditionValue, String conditionData) {
        this.conditionType = conditionType;
        this.conditionOperator = conditionOperator;
        this.conditionValue = conditionValue;
        this.conditionData = conditionData;
    }

    // Getters and Setters
    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public ConditionOperator getConditionOperator() {
        return conditionOperator;
    }

    public void setConditionOperator(ConditionOperator conditionOperator) {
        this.conditionOperator = conditionOperator;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }

    public String getConditionData() {
        return conditionData;
    }

    public void setConditionData(String conditionData) {
        this.conditionData = conditionData;
    }
}
