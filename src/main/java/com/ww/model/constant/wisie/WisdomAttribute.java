package com.ww.model.constant.wisie;

import com.ww.model.container.Resources;

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

    public static Resources UPGRADE_COST = new Resources(null, null,1L);

    public static int COUNT = 7;
}
