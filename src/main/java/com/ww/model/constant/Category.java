package com.ww.model.constant;

import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;

public enum Category {
    RANDOM,
    EQUATION,
    NUMBER,
    LYRICS,
    ELEMENT,
    RIDDLE,
    COLOR,
    //    HISTORY,
    COUNTRY,
    TIME,
    OLYMPIC_GAMES,
    MEMORY;

    public static List<Category> list() {
        return Arrays.asList(values()).stream()
                .filter(category -> category != RANDOM)
                .collect(Collectors.toList());
    }

    public static Category random() {
        return randomElement(list());
    }

    public static List<Category> random(int count) {
        Set<Category> categories = new HashSet<>();
        while (categories.size() < count) {
            categories.add(random());
        }
        return new ArrayList<>(categories);
    }

    public static Category fromString(String name) {
        try {
            return Category.valueOf(name);
        } catch (IllegalArgumentException e) {
            return random();
        }
    }
}
