package com.ww.helper;

import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.entity.wisie.ProfileWisie;

import static com.ww.helper.NumberHelper.smartRound;

public class WisieHelper {
    public static Double calculateValue(ProfileWisie profileWisie) {
        Double value = 0d;
        for (WisdomAttribute wisdomAttribute : WisdomAttribute.values()) {
            value += profileWisie.getWisdomAttributeValue(wisdomAttribute);
        }
        for (MentalAttribute mentalAttribute : MentalAttribute.values()) {
            value += profileWisie.getMentalAttributeValue(mentalAttribute);
        }
        value /= WisdomAttribute.COUNT + MentalAttribute.COUNT;
        value = smartRound(value);
        return value;
    }

    public static Double f1(Double arg) {
        return Math.log(1.87 * arg + 1) * 12.9 / (83.25 + Math.log(0.025 * arg + 51.24) * 12.9);
    }
}
