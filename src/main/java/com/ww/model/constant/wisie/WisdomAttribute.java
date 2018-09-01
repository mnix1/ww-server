package com.ww.model.constant.wisie;

public enum WisdomAttribute {
    MEMORY,
    LOGIC,
    PERCEPTIVITY,
    COUNTING,
    COMBINING_FACTS,
    PATTERN_RECOGNITION,
    IMAGINATION;


    public static WisdomAttribute fromString(String name) {
        try {
            return WisdomAttribute.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Long UPGRADE_COST = 1L;

    public static int COUNT = 7;
}
