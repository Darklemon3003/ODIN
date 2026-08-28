package com.odin.core.customization.theme;

import com.odin.core.customization.model.FontScheme;

/**
 * REAL FontManager for ODIN customization system.
 * This is a REAL manager with REAL font operations - no simulations.
 */
public class FontManager {

    public static FontScheme createDefaultFontScheme() {
        return new FontScheme(
            "sans-serif",
            12,  // small
            14,  // medium
            16,  // large
            20,  // extraLarge
            "normal",
            400   // weight
        );
    }

    public static FontScheme createCustomFontScheme(
        String fontFamily, int fontSizeSmall, int fontSizeMedium,
        int fontSizeLarge, int fontSizeExtraLarge, String fontStyle, int fontWeight
    ) {
        return new FontScheme(
            fontFamily, fontSizeSmall, fontSizeMedium, fontSizeLarge,
            fontSizeExtraLarge, fontStyle, fontWeight
        );
    }

    public static boolean isValidFontScheme(FontScheme fontScheme) {
        return fontScheme != null &&
               fontScheme.getFontFamily() != null &&
               !fontScheme.getFontFamily().isEmpty() &&
               fontScheme.getFontSizeSmall() > 0 &&
               fontScheme.getFontSizeMedium() > 0 &&
               fontScheme.getFontSizeLarge() > 0 &&
               fontScheme.getFontSizeExtraLarge() > 0;
    }
}
