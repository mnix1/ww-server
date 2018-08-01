package com.ww.model.constant.social;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum Avatar {
    ALPACA,
    ANT,
    BEE,
    BEAR,
    BULL,
    CAT_BLUE,
    CAT_PRESENTER,
    CAT_TEACHER,
    COW,
    CROCODILE,
    DOG,
    DRAGON,
    DUCK,
    EAGLE,
    ELEPHANT,
    FOX,
    GIRAFFE,
    GORILLA,
    HORSE,
    KANGAROO,
    KITEK,
    LION,
    OSMIORNICA,
    OSTRICH,
    OWL,
    PANDA,
    PARROT,
    PENGUIN,
    POLAR_BEAR,
    RABBIT,
    ROBO,
    RUMCIA,
    SHARK,
    SHEEP,
    SKUNK,
    SNAKE,
    SZERYF,
    TIGER,
    TURKEY,
    TURTLE,
    WOLF,
    ZAROWA,
    ZUBR;

    public static Avatar random() {
        List<Avatar> possible = Arrays.asList(values());
        return randomElement(possible);
    }
}
