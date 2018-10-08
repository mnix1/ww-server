package com.ww.model.constant;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum Skill {
    HINT,
    WATER_PISTOL,
    LIFEBUOY,
    //    BLOCK,
//    TASK_CHANGE,
    KIDNAPPING,
    GHOST,
    PIZZA;

    public static List<Skill> list() {
        return Arrays.asList(values());
    }

    public static Skill random() {
        return randomElement(list());
    }

    public static Skill fromString(String name) {
        try {
            return Skill.valueOf(name);
        } catch (IllegalArgumentException e) {
            return random();
        }
    }
}
