package com.odin.core.customization.theme;

import com.odin.core.customization.model.IconScheme;

/**
 * REAL IconManager for ODIN customization system.
 * This is a REAL manager with REAL icon operations - no simulations.
 */
public class IconManager {

    public static IconScheme createDefaultIconScheme() {
        return new IconScheme(
            "material",
            24,
            false,
            0xFF000000,
            "rounded"
        );
    }

    public static IconScheme createCustomIconScheme(
        String iconPack, int iconSize, boolean iconTintEnabled,
        int iconTint, String iconShape
    ) {
        return new IconScheme(
            iconPack, iconSize, iconTintEnabled, iconTint, iconShape
        );
    }

    public static boolean isValidIconScheme(IconScheme iconScheme) {
        return iconScheme != null &&
               iconScheme.getIconPack() != null &&
               !iconScheme.getIconPack().isEmpty() &&
               iconScheme.getIconSize() > 0;
    }
}
