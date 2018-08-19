package com.ww.helper;

import java.awt.Color;

public class ColorHelper {

    public static String colorToHex(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }
}
