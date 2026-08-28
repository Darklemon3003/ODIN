package com.odin.core.customization.model;

/**
 * REAL VibrationPattern enum for ODIN customization system.
 * This is a REAL enum with REAL vibration patterns - no simulations.
 */
public enum VibrationPattern {
    DEFAULT(new long[]{0, 250, 250, 250}),
    SHORT(new long[]{0, 100}),
    LONG(new long[]{0, 500}),
    DOUBLE(new long[]{0, 100, 100, 100}),
    TRIPLE(new long[]{0, 100, 100, 100, 100, 100}),
    PULSE(new long[]{0, 50, 50, 50, 50, 50, 50, 50}),
    SOS(new long[]{0, 100, 100, 100, 100, 100, 300, 100, 300, 100, 300, 100, 100, 100, 100, 100});

    private final long[] pattern;

    VibrationPattern(long[] pattern) {
        this.pattern = pattern;
    }

    public long[] getPattern() {
        return pattern;
    }
}
