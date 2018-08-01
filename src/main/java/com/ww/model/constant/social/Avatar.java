package com.ww.model.constant.social;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum Avatar {
    ALPAKA,
    ANT,
    BEE,
    CAT,
    COW,
    DUCK,
    EAGLE,
    GIRAFFE,
    GORILLA,
    HORSE,
    KITEK,
    OSMIORNICA,
    OWL,
    PANDA,
    PENGUIN,
    ROBO,
    RUMCIA,
    SNAKE,
    SHEEP,
    SKUNK,
    SZERYF,
    ZAROWA,
    ZUBR;

    public static Avatar random() {
        List<Avatar> possible = Arrays.asList(values());
        return randomElement(possible);
    }
}
