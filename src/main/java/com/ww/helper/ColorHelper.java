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

    public static int colorToInt(Color c){
        int red = (c.getRed() << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        int green = (c.getGreen() << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        int blue = c.getBlue() & 0x000000FF; //Mask out anything not blue.

        return red | green | blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

//    public static int colorToInt(Color c) {
//        return c.getRed() * 255 * 255 + c.getGreen() * 255 + c.getBlue();
//    }
}
