package com.ww.model.constant.hero;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum HeroType {
//    ALPACA,
    ANT,
    AUROCHS,
    BEE,
    BEAR,
    BULL,
    BULLDOG,
    CAMEL,
    CAT_BLUE,
    CAT_PRESENTER,
    CAT_TEACHER,
//    COW,
    CROCODILE,
    DOG,
    DOG_FAT,
    DRAGON,
    DRAGON_BLUE,
    DRAGON_FAT,
//    DUCK,
    EAGLE,
    ELEPHANT,
    FOX,
    FOX_MAN,
//    GIRAFFE,
    GORILLA,
    HORSE,
    KANGAROO,
//    KITEK,
    LION,
//    OCTOPUS,
    OSTRICH,
//    OWL,
//    PANDA,
    PANDA_EAT,
    PARROT,
//    PENGUIN,
    POLAR_BEAR,
    RABBIT,
//    ROBO,
//    RUMCIA,
    SHARK,
    SHEEP,
    SKUNK,
    SNAKE,
//    SZERYF,
    TIGER,
    TURKEY,
    TURTLE,
    WOLF;
//    ZAROWA;

    public static HeroType random() {
        List<HeroType> possible = Arrays.asList(values());
        return randomElement(possible);
    }
}
