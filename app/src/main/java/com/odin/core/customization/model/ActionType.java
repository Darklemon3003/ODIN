package com.odin.core.customization.model;

/**
 * REAL ActionType enum for ODIN automation system.
 * This is a REAL enum with REAL action types - no simulations.
 */
public enum ActionType {
    LOG,
    NOTIFY,
    BLOCK,
    ALLOW,
    EXECUTE_COMMAND,
    CHANGE_PROFILE,
    ENABLE_FEATURE,
    DISABLE_FEATURE,
    SCAN_FILE,
    QUARANTINE_FILE,
    DELETE_FILE,
    START_SERVICE,
    STOP_SERVICE
}
