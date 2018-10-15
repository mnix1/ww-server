package com.ww.model.constant.social;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum ResourceType {
    GOLD,
    CRYSTAL,
    WISDOM,
    ELIXIR,
    ;

    public static ResourceType random() {
        return randomElement(Arrays.asList(values()));
    }

}
