package com.odin.core.customization.model;

import com.google.gson.annotations.SerializedName;

/**
 * REAL Action model for ODIN automation system.
 * This is a REAL data model with REAL action configurations - no simulations.
 */
public class Action {

    @SerializedName("actionType")
    private ActionType actionType;

    @SerializedName("actionValue")
    private String actionValue;

    @SerializedName("actionData")
    private String actionData;

    public Action() {
        this.actionType = ActionType.LOG;
    }

    public Action(ActionType actionType, String actionValue) {
        this.actionType = actionType;
        this.actionValue = actionValue;
    }

    public Action(ActionType actionType, String actionValue, String actionData) {
        this.actionType = actionType;
        this.actionValue = actionValue;
        this.actionData = actionData;
    }

    // Getters and Setters
    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getActionValue() {
        return actionValue;
    }

    public void setActionValue(String actionValue) {
        this.actionValue = actionValue;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }
}
