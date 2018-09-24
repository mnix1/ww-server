package com.ww.helper;

import org.apache.commons.lang3.StringUtils;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomInteger;

public class NameHelper {

    private static final String[] BEGIN = {"Kr", "Ca", "Ra", "Mrok", "Cru",
            "Ray", "Bre", "Zed", "Drak", "Mor", "Jag", "Mer", "Jar", "Mjol",
            "Zork", "Mad", "Cry", "Zur", "Creo", "Azak", "Azur", "Rei", "Cro",
            "Mar", "Luk"};
    private static final String[] MIDDLE = {"air", "ir", "mi", "sor", "mee", "clo",
            "red", "cra", "ark", "arc", "miri", "lori", "cres", "mur", "zer",
            "marac", "zoir", "slamar", "salmar", "urak"};
    private static final String[] END = {"d", "ed", "ark", "arc", "es", "er", "der",
            "tron", "med", "ure", "zur", "cred", "mur"};
    private static final String[] PAUSE = {"1", "5", "-", "_", "9", "", "", "", "", "", "", "!"};

    public static String generateName() {
        String begin = BEGIN[randomInteger(0, BEGIN.length - 1)];
        String middle = MIDDLE[randomInteger(0, MIDDLE.length - 1)];
        String end = END[randomInteger(0, END.length - 1)];
        String pause1 = PAUSE[randomInteger(0, PAUSE.length - 1)];
        String pause2 = PAUSE[randomInteger(0, PAUSE.length - 1)];
        String pause3 = PAUSE[randomInteger(0, PAUSE.length - 1)];
        if (randomDouble(0, 1) > 0.6) {
            begin = begin.toLowerCase();
        }
        if (randomDouble(0, 1) > 0.6) {
            middle = StringUtils.capitalize(middle);
        }
        if (randomDouble(0, 1) > 0.6) {
            end = StringUtils.capitalize(end);
        }
        return begin + pause1 + middle + pause2 + end + pause3;

    }
}
