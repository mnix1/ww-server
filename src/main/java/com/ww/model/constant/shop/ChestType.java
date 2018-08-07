package com.ww.model.constant.shop;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum ChestType {
    HERO;

    public static ChestType random() {
        List<ChestType> possible = Arrays.asList(values());
        return randomElement(possible);
    }
}
