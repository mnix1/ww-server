package com.ww.helper;

import java.awt.Color;

import static com.ww.helper.RandomHelper.randomInteger;

public class ColorHelper {

    public static String colorToHex(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }

    public static Color randomColor(int min, int max) {
        return randomColor(min, max, min, max, min, max);
    }

    public static Color randomColor(int minR, int maxR, int minG, int maxG, int minB, int maxB) {
        return new Color(randomInteger(minR, maxR), randomInteger(minG, maxG), randomInteger(minB, maxB));
    }

    public static int colorToInt(Color c) {
        int red = (c.getRed() << 16) & 0x00FF0000;
        int green = (c.getGreen() << 8) & 0x0000FF00;
        int blue = c.getBlue() & 0x000000FF;
        return red | green | blue;
    }

    public static int colorToSumInt(Color c) {
        return c.getRed() + c.getGreen() + c.getBlue();
    }

}
