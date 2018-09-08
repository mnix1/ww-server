package com.ww.model.constant.wisie;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum WisieType {
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
    CROCODILE,
    DOG,
    DOG_FAT,
    DOG_SWEET,
    DRAGON,
    DRAGON_BLUE,
    DRAGON_FAT,
    DRAGON_RED,
    EAGLE,
    ELEPHANT,
    FOX,
    FOX_MAN,
    FROG,
    GORILLA,
    HORSE,
    KANGAROO,
    LAMPARD,
    LION,
    OSTRICH,
    OWL,
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

    public static WisieType random() {
        List<WisieType> possible = Arrays.asList(values());
        return randomElement(possible);
    }
}
