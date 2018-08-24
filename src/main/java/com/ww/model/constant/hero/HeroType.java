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
    DOG_SWEET,
    DRAGON,
    DRAGON_BLUE,
    DRAGON_FAT,
    DRAGON_RED,
//    DUCK,
    EAGLE,
    ELEPHANT,
    FOX,
    FOX_MAN,
    FROG,
//    GIRAFFE,
    GORILLA,
    HORSE,
    KANGAROO,
    LAMPARD,
    LION,
//    OCTOPUS,
    OSTRICH,
    OWL,
//    PANDA,
    PANDA_EAT,
    PARROT,
    PENGUIN,
    POLAR_BEAR,
    RABBIT,
    SHARK,
    SHEEP,
    SQUIRREL,
    RACCOON,
    SNAKE,
    TIGER,
    TURKEY,
    TURTLE,
    WALRUS,
    WOLF;

    public static HeroType random() {
        List<HeroType> possible = Arrays.asList(values());
        return randomElement(possible);
    }
}
