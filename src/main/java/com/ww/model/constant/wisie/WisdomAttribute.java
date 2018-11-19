package com.ww.model.constant.wisie;

import com.ww.model.constant.Category;
import com.ww.model.container.Resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;

public enum WisdomAttribute {
    MEMORY,
    LOGIC,
    PERCEPTIVITY,
    COUNTING,
    PATTERN_RECOGNITION,
    IMAGINATION;


    public static WisdomAttribute fromString(String name) {
        try {
            return WisdomAttribute.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static WisdomAttribute random() {
        return randomElement(Arrays.asList(values()));
    }

    public static Resources UPGRADE_COST = new Resources(null, null,1L);

    public static int COUNT = 6;
}
