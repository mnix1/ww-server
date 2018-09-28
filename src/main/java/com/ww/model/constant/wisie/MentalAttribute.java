package com.ww.model.constant.wisie;

import com.ww.model.container.Resources;

public enum MentalAttribute {
    SPEED,
    REFLEX,
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

    public static Resources UPGRADE_COST = new Resources(null, null,2L);

    public static int COUNT = 5;
}
