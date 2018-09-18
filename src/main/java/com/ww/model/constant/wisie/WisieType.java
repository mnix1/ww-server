package com.ww.model.constant.wisie;

import com.ww.model.constant.Category;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum WisieType {
    ALLIGATOR,
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
    CHICK,
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
    ELEPHANT_CHILD,
    FOX,
    FOX_MAN,
    FROG,
    GIRAFFE,
    GORILLA,
    HORSE,
    KANGAROO,
    KOALA,
    LAMPARD,
    LION,
    MONKEY,
    OCTOPUS,
    OSTRICH,
    OWL,
    OWL_HAPPY,
    PANDA_EAT,
    PARROT,
    PENGUIN,
    POLAR_BEAR,
    RABBIT,
    RACCOON,
    RACCOON_BROWN,
    SHARK,
    SHEEP,
    SQUIRREL,
    SNAKE,
    STORK,
    TIGER,
    TURKEY,
    TURTLE,
    WALRUS,
    WORM,
    WOLF;

    public static WisieType random() {
        List<WisieType> possible = Arrays.asList(values());
        return randomElement(possible);
    }

    public static WisieType fromString(String name){
        try {
            return WisieType.valueOf(name);
        } catch (IllegalArgumentException e){
            return null;
        }
    }
}
