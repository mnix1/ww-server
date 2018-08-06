package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum MemoryTaskType {
    BACKGROUND_COLOR_FROM_FIGURE_KEY,
    SHAPE_FROM_FIGURE_KEY,
    SHAPE_FROM_BACKGROUND_COLOR,
    FIGURE_KEY_FROM_BACKGROUND_COLOR,
    FIGURE_KEY_FROM_SHAPE;

    public static boolean answerFigureKey(MemoryTaskType type) {
        return type == FIGURE_KEY_FROM_BACKGROUND_COLOR
                || type == FIGURE_KEY_FROM_SHAPE;
    }
    public static boolean answerShape(MemoryTaskType type) {
        return type == SHAPE_FROM_FIGURE_KEY
                || type == SHAPE_FROM_BACKGROUND_COLOR;
    }

    public static MemoryTaskType random() {
        return randomElement(Arrays.asList(values()));
    }
}
