package com.ww.model.constant.wisie;

public enum HeroType {
    WISIE,
    WISOR;

    public static boolean isWisie(HeroType type) {
        return WISIE == type;
    }
}
