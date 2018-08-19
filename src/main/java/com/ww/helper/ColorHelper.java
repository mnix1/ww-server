package com.ww.helper;

import java.awt.Color;

import static com.ww.helper.RandomHelper.randomInteger;

public class ColorHelper {

    public static String colorToHex(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }

    public static Color randomColor(int min, int max) {
        return new Color(randomInteger(min, max), randomInteger(min, max), randomInteger(min, max));
    }
}
