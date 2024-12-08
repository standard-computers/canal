package org.Canal.UI;

import java.awt.Color;

public class ColorUtil {

    /**
     * Adjusts the brightness of a color.
     *
     * @param color   the original color
     * @param factor  the factor to adjust brightness (e.g., 0.8 for darker, 1.2 for lighter)
     * @return the adjusted color
     */
    public static Color adjustBrightness(Color color, float factor) {
        int red = Math.min(255, Math.max(0, (int) (color.getRed() * factor)));
        int green = Math.min(255, Math.max(0, (int) (color.getGreen() * factor)));
        int blue = Math.min(255, Math.max(0, (int) (color.getBlue() * factor)));
        return new Color(red, green, blue);
    }
}
