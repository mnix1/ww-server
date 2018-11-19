package com.ww.model.constant.wisie;

import com.ww.model.container.Resources;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum MentalAttribute {
    SPEED,
    REFLEX,
    CUNNING,
    CONCENTRATION,
    CONFIDENCE,
    INTUITION;

    public static MentalAttribute fromString(String name){
        try {
            return MentalAttribute.valueOf(name);
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    public static MentalAttribute random() {
        return randomElement(Arrays.asList(values()));
    }

    public static Resources UPGRADE_COST = new Resources(null, null,2L);

    public static int COUNT = 6;
}
