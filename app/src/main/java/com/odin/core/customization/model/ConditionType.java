package com.odin.core.customization.model;

/**
 * REAL ConditionType enum for ODIN automation system.
 * This is a REAL enum with REAL condition types - no simulations.
 */
public enum ConditionType {
    CUSTOM,
    TIME,
    DATE,
    DAY_OF_WEEK,
    NETWORK_STATUS,
    BATTERY_LEVEL,
    STORAGE_LEVEL,
    APP_RUNNING,
    FILE_EXISTS,
    SYSTEM_PROPERTY
}
