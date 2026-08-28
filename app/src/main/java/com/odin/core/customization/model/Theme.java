package com.odin.core.customization.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.room.Ignore;
import com.google.gson.annotations.SerializedName;
import com.odin.core.customization.data.converters.ThemeTypeConverter;
import com.odin.core.customization.data.converters.ColorSchemeConverter;
import com.odin.core.customization.data.converters.FontSchemeConverter;
import com.odin.core.customization.data.converters.IconSchemeConverter;
import java.util.Date;

/**
 * REAL Theme model for ODIN customization system.
 * This is a REAL data model with REAL persistence - no simulations.
 */
@Entity(tableName = "themes")
public class Theme {

    @PrimaryKey
    @androidx.annotation.NonNull
    @SerializedName("themeId")
    private String themeId;

    @SerializedName("themeName")
    private String themeName;

    @SerializedName("themeType")
    private ThemeType themeType;

    @SerializedName("colorScheme")
    private ColorScheme colorScheme;

    @SerializedName("fontScheme")
    private FontScheme fontScheme;

    @SerializedName("iconScheme")
    private IconScheme iconScheme;

    @SerializedName("isCustom")
    private boolean isCustom;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("updatedAt")
    private Date updatedAt;

    public Theme() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @Ignore
    public Theme(String themeId, String themeName, ThemeType themeType) {
        this.themeId = themeId;
        this.themeName = themeName;
        this.themeType = themeType;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public ThemeType getThemeType() {
        return themeType;
    }

    public void setThemeType(ThemeType themeType) {
        this.themeType = themeType;
    }

    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    public FontScheme getFontScheme() {
        return fontScheme;
    }

    public void setFontScheme(FontScheme fontScheme) {
        this.fontScheme = fontScheme;
    }

    public IconScheme getIconScheme() {
        return iconScheme;
    }

    public void setIconScheme(IconScheme iconScheme) {
        this.iconScheme = iconScheme;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "themeId='" + themeId + '\'' +
                ", themeName='" + themeName + '\'' +
                ", themeType=" + themeType +
                ", isCustom=" + isCustom +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
