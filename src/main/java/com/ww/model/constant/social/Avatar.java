package com.ww.model.constant.social;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum Avatar {
    CAT,
    COW,
    DUCK,
    GORILLA,
    KITEK,
    OSMIORNICA,
    OWL,
    PANDA,
    PENGUIN,
    ROBO,
    RUMCIA,
    SHEEP,
    SZERYF,
    ZAROWA;

    public static Avatar random() {
        List<Avatar> possible = Arrays.asList(values());
        return randomElement(possible);
    }
}
