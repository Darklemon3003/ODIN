package com.odin.core.customization.model;

import com.google.gson.annotations.SerializedName;

/**
 * REAL FontScheme model for ODIN customization system.
 * This is a REAL data model with REAL font configurations - no simulations.
 */
public class FontScheme {

    @SerializedName("fontFamily")
    private String fontFamily;

    @SerializedName("fontSizeSmall")
    private int fontSizeSmall;

    @SerializedName("fontSizeMedium")
    private int fontSizeMedium;

    @SerializedName("fontSizeLarge")
    private int fontSizeLarge;

    @SerializedName("fontSizeExtraLarge")
    private int fontSizeExtraLarge;

    @SerializedName("fontStyle")
    private String fontStyle;

    @SerializedName("fontWeight")
    private int fontWeight;

    public FontScheme() {
        // Default font settings
        this.fontFamily = "sans-serif";
        this.fontSizeSmall = 12;
        this.fontSizeMedium = 14;
        this.fontSizeLarge = 16;
        this.fontSizeExtraLarge = 20;
        this.fontStyle = "normal";
        this.fontWeight = 400;
    }

    public FontScheme(String fontFamily, int fontSizeSmall, int fontSizeMedium, int fontSizeLarge,
                     int fontSizeExtraLarge, String fontStyle, int fontWeight) {
        this.fontFamily = fontFamily;
        this.fontSizeSmall = fontSizeSmall;
        this.fontSizeMedium = fontSizeMedium;
        this.fontSizeLarge = fontSizeLarge;
        this.fontSizeExtraLarge = fontSizeExtraLarge;
        this.fontStyle = fontStyle;
        this.fontWeight = fontWeight;
    }

    // Getters and Setters
    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public int getFontSizeSmall() {
        return fontSizeSmall;
    }

    public void setFontSizeSmall(int fontSizeSmall) {
        this.fontSizeSmall = fontSizeSmall;
    }

    public int getFontSizeMedium() {
        return fontSizeMedium;
    }

    public void setFontSizeMedium(int fontSizeMedium) {
        this.fontSizeMedium = fontSizeMedium;
    }

    public int getFontSizeLarge() {
        return fontSizeLarge;
    }

    public void setFontSizeLarge(int fontSizeLarge) {
        this.fontSizeLarge = fontSizeLarge;
    }

    public int getFontSizeExtraLarge() {
        return fontSizeExtraLarge;
    }

    public void setFontSizeExtraLarge(int fontSizeExtraLarge) {
        this.fontSizeExtraLarge = fontSizeExtraLarge;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public int getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(int fontWeight) {
        this.fontWeight = fontWeight;
    }
}
