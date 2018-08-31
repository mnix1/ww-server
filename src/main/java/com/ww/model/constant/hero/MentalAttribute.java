package com.ww.model.constant.hero;

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

    public static Long UPGRADE_COST = 2L;

    public static int COUNT = 5;
}
