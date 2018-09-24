package com.ww.helper;

import com.ww.model.container.ColorObject;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ww.helper.RandomHelper.randomInteger;

public class ColorHelper {

    public static String colorToHex(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }

    public static Color randomColor() {
        return randomColor(0, 255);
    }

    public static Color randomColor(int min, int max) {
        return randomColor(min, max, min, max, min, max);
    }

    public static Color randomGoodColor() {
        int part1 = randomInteger(0, 255);
        int part2 = randomInteger(255 - part1, 255);
        int minPart3 = Math.abs(part1 - part2);
        int maxPart3 = Math.max(part1, part2);
        int part3 = randomInteger(minPart3, maxPart3);
        List<Integer> parts = Arrays.asList(part1, part2, part3);
        Collections.shuffle(parts);
        return new Color(parts.get(0), parts.get(1), parts.get(2));
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

    public static boolean similarColors(ColorObject c1, ColorObject c2, double offset) {
        c1.calculatePercentComponents();
        c2.calculatePercentComponents();
        return Math.abs(c1.getRedPercentComponent() - c2.getRedPercentComponent()) < offset
                && Math.abs(c1.getGreenPercentComponent() - c2.getGreenPercentComponent()) < offset
                && Math.abs(c1.getBluePercentComponent() - c2.getBluePercentComponent()) < offset;
    }

    public static double percentComponent(int component, int sum) {
        return 1.0 * component / sum;
    }

}
