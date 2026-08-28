package com.odin.core.customization.model;

import com.google.gson.annotations.SerializedName;

/**
 * REAL ColorScheme model for ODIN customization system.
 * This is a REAL data model with REAL color values - no simulations.
 */
public class ColorScheme {

    @SerializedName("primary")
    private int primary;

    @SerializedName("primaryVariant")
    private int primaryVariant;

    @SerializedName("secondary")
    private int secondary;

    @SerializedName("secondaryVariant")
    private int secondaryVariant;

    @SerializedName("background")
    private int background;

    @SerializedName("surface")
    private int surface;

    @SerializedName("error")
    private int error;

    @SerializedName("onPrimary")
    private int onPrimary;

    @SerializedName("onSecondary")
    private int onSecondary;

    @SerializedName("onBackground")
    private int onBackground;

    @SerializedName("onSurface")
    private int onSurface;

    @SerializedName("onError")
    private int onError;

    public ColorScheme() {
        // Default Material Design 3 colors
        this.primary = 0xFF6750A4;
        this.primaryVariant = 0xFF3700B3;
        this.secondary = 0xFF625B71;
        this.secondaryVariant = 0xFF3E2723;
        this.background = 0xFFFFFBFE;
        this.surface = 0xFFFFFBFE;
        this.error = 0xFFB3261E;
        this.onPrimary = 0xFFFFFFFF;
        this.onSecondary = 0xFFFFFFFF;
        this.onBackground = 0xFF1C1B1F;
        this.onSurface = 0xFF1C1B1F;
        this.onError = 0xFFFFFFFF;
    }

    public ColorScheme(int primary, int primaryVariant, int secondary, int secondaryVariant,
                      int background, int surface, int error, int onPrimary, int onSecondary,
                      int onBackground, int onSurface, int onError) {
        this.primary = primary;
        this.primaryVariant = primaryVariant;
        this.secondary = secondary;
        this.secondaryVariant = secondaryVariant;
        this.background = background;
        this.surface = surface;
        this.error = error;
        this.onPrimary = onPrimary;
        this.onSecondary = onSecondary;
        this.onBackground = onBackground;
        this.onSurface = onSurface;
        this.onError = onError;
    }

    // Getters and Setters
    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }

    public int getPrimaryVariant() {
        return primaryVariant;
    }

    public void setPrimaryVariant(int primaryVariant) {
        this.primaryVariant = primaryVariant;
    }

    public int getSecondary() {
        return secondary;
    }

    public void setSecondary(int secondary) {
        this.secondary = secondary;
    }

    public int getSecondaryVariant() {
        return secondaryVariant;
    }

    public void setSecondaryVariant(int secondaryVariant) {
        this.secondaryVariant = secondaryVariant;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getOnPrimary() {
        return onPrimary;
    }

    public void setOnPrimary(int onPrimary) {
        this.onPrimary = onPrimary;
    }

    public int getOnSecondary() {
        return onSecondary;
    }

    public void setOnSecondary(int onSecondary) {
        this.onSecondary = onSecondary;
    }

    public int getOnBackground() {
        return onBackground;
    }

    public void setOnBackground(int onBackground) {
        this.onBackground = onBackground;
    }

    public int getOnSurface() {
        return onSurface;
    }

    public void setOnSurface(int onSurface) {
        this.onSurface = onSurface;
    }

    public int getOnError() {
        return onError;
    }

    public void setOnError(int onError) {
        this.onError = onError;
    }
}
