package com.ww.model.constant.social;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum Avatar {
    ALPAKA,
    ANT,
    BEE,
    CAT,
    CAT_TEACHER,
    COW,
    DRAGON,
    DUCK,
    EAGLE,
    ELEPHANT,
    FOX,
    GIRAFFE,
    GORILLA,
    HORSE,
    KITEK,
    OSMIORNICA,
    OWL,
    PANDA,
    PENGUIN,
    RABBIT,
    ROBO,
    RUMCIA,
    SHARK,
    SNAKE,
    SHEEP,
    SKUNK,
    SZERYF,
    TURKEY,
    WOLF,
    ZAROWA,
    ZUBR;

    public static Avatar random() {
        List<Avatar> possible = Arrays.asList(values());
        return randomElement(possible);
    }
}
