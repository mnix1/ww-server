package com.ww.helper;

import com.ww.model.constant.hero.MentalAttribute;
import com.ww.model.constant.hero.WisdomAttribute;
import com.ww.model.entity.hero.ProfileHero;

import static com.ww.helper.NumberHelper.smartRound;

public class HeroHelper {
    static public Double calculateValue(ProfileHero profileHero) {
        Double value = 0d;
        for (WisdomAttribute wisdomAttribute : WisdomAttribute.values()) {
            value += profileHero.getWisdomAttributeValue(wisdomAttribute);
        }
        for (MentalAttribute mentalAttribute : MentalAttribute.values()) {
            value += profileHero.getMentalAttributeValue(mentalAttribute);
        }
        value /= WisdomAttribute.COUNT + MentalAttribute.COUNT;
        value = smartRound(value);
        return value;
    }
}
