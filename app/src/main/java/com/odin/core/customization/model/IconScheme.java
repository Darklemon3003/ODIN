package com.odin.core.customization.model;

import com.google.gson.annotations.SerializedName;

/**
 * REAL IconScheme model for ODIN customization system.
 * This is a REAL data model with REAL icon configurations - no simulations.
 */
public class IconScheme {

    @SerializedName("iconPack")
    private String iconPack;

    @SerializedName("iconSize")
    private int iconSize;

    @SerializedName("iconTintEnabled")
    private boolean iconTintEnabled;

    @SerializedName("iconTint")
    private int iconTint;

    @SerializedName("iconShape")
    private String iconShape;

    public IconScheme() {
        // Default icon settings
        this.iconPack = "material";
        this.iconSize = 24;
        this.iconTintEnabled = false;
        this.iconTint = 0xFF000000;
        this.iconShape = "rounded";
    }

    public IconScheme(String iconPack, int iconSize, boolean iconTintEnabled, int iconTint, String iconShape) {
        this.iconPack = iconPack;
        this.iconSize = iconSize;
        this.iconTintEnabled = iconTintEnabled;
        this.iconTint = iconTint;
        this.iconShape = iconShape;
    }

    // Getters and Setters
    public String getIconPack() {
        return iconPack;
    }

    public void setIconPack(String iconPack) {
        this.iconPack = iconPack;
    }

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }

    public boolean isIconTintEnabled() {
        return iconTintEnabled;
    }

    public void setIconTintEnabled(boolean iconTintEnabled) {
        this.iconTintEnabled = iconTintEnabled;
    }

    public int getIconTint() {
        return iconTint;
    }

    public void setIconTint(int iconTint) {
        this.iconTint = iconTint;
    }

    public String getIconShape() {
        return iconShape;
    }

    public void setIconShape(String iconShape) {
        this.iconShape = iconShape;
    }
}
