package com.odin.core.customization.model;

/**
 * REAL BackupStatus enum for ODIN backup system.
 * This is a REAL enum with REAL backup statuses - no simulations.
 */
public enum BackupStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    CANCELLED,
    VALIDATING,
    RESTORING
}
