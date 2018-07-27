package com.ww.model.constant.social;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum Avatar {
    ROBO,
    KITEK,
    SOWA,
    RUMCIA,
    PANDA,
    SZERYF,
    ZAROWA;

    public static Avatar random() {
        List<Avatar> possible = Arrays.asList(values());
        return randomElement(possible);
    }
}
