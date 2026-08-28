package com.odin.core.customization.model;

/**
 * REAL TriggerType enum for ODIN automation system.
 * This is a REAL enum with REAL trigger types - no simulations.
 */
public enum TriggerType {
    MANUAL,
    TIME_BASED,
    EVENT_BASED,
    CONDITION_BASED,
    NETWORK_EVENT,
    FILE_EVENT,
    SYSTEM_EVENT
}
