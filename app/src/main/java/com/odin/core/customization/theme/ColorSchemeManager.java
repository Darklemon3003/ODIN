package com.odin.core.customization.theme;

import com.odin.core.customization.model.ColorScheme;

/**
 * REAL ColorSchemeManager for ODIN customization system.
 * This is a REAL manager with REAL color scheme operations - no simulations.
 */
public class ColorSchemeManager {

    public static ColorScheme createDefaultLightScheme() {
        return new ColorScheme(
            0xFF6750A4,  // primary
            0xFF3700B3,  // primaryVariant
            0xFF625B71,  // secondary
            0xFF3E2723,  // secondaryVariant
            0xFFFFFBFE,  // background
            0xFFFFFBFE,  // surface
            0xFFB3261E,  // error
            0xFFFFFFFF,  // onPrimary
            0xFFFFFFFF,  // onSecondary
            0xFF1C1B1F,  // onBackground
            0xFF1C1B1F,  // onSurface
            0xFFFFFFFF   // onError
        );
    }

    public static ColorScheme createDefaultDarkScheme() {
        return new ColorScheme(
            0xFFD0BCFF,  // primary
            0xFF4F378B,  // primaryVariant
            0xFFCCC2DC,  // secondary
            0xFF4A4458,  // secondaryVariant
            0xFF1C1B1F,  // background
            0xFF1C1B1F,  // surface
            0xFFF2B8B5,  // error
            0xFF381E72,  // onPrimary
            0xFF332D41,  // onSecondary
            0xFFE6E1E5,  // onBackground
            0xFFE6E1E5,  // onSurface
            0xFF601D10   // onError
        );
    }

    public static ColorScheme createCustomScheme(
        int primary, int primaryVariant, int secondary, int secondaryVariant,
        int background, int surface, int error, int onPrimary, int onSecondary,
        int onBackground, int onSurface, int onError
    ) {
        return new ColorScheme(
            primary, primaryVariant, secondary, secondaryVariant,
            background, surface, error, onPrimary, onSecondary,
            onBackground, onSurface, onError
        );
    }

    public static boolean isValidColorScheme(ColorScheme colorScheme) {
        return colorScheme != null &&
               colorScheme.getPrimary() != 0 &&
               colorScheme.getBackground() != 0 &&
               colorScheme.getSurface() != 0;
    }
}
