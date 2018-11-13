package com.ww.model.constant;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum Skill {
    TEACHER,
    MOTIVATOR,
    HINT,
    WATER_PISTOL,
    LIFEBUOY,
    NINJA,
    GHOST,
    COVERALL,
    CHANGE_TASK,
    PIZZA;

    public static List<Skill> alwaysAvailableSkills() {
        return Arrays.asList(HINT, WATER_PISTOL, LIFEBUOY);
    }

    public static List<Skill> optionalAvailableSkills() {
        return Arrays.asList(NINJA, GHOST, CHANGE_TASK, PIZZA);
    }

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
