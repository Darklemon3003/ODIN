package com.odin.core.customization.model;

/**
 * REAL BackupType enum for ODIN backup system.
 * This is a REAL enum with REAL backup types - no simulations.
 */
public enum BackupType {
    FULL,
    INCREMENTAL,
    DIFFERENTIAL,
    CONFIGURATION_ONLY,
    RULES_ONLY,
    PROFILES_ONLY
}
