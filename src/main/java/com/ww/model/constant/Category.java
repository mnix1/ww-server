package com.ww.model.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;

public enum Category {
    RANDOM,
    EQUATION,
    NUMBER,
    LYRICS,
    ELEMENT,
    RIDDLE,
//    HISTORY,
    COUNTRY,
    TIME,
    MEMORY;

    public static Category random() {
        List<Category> possible = Arrays.asList(values()).stream()
                .filter(category -> category != RANDOM)
                .collect(Collectors.toList());
        return randomElement(possible);
    }

    public static Category fromString(String name){
        try {
            return Category.valueOf(name);
        } catch (IllegalArgumentException e){
            return random();
        }
    }
}
