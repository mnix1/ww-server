package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum MathTaskType {
    ADDITION,
    MULTIPLICATION,
    MODULO;

    public static MathTaskType random() {
        return randomElement(Arrays.asList(values()));
    }
}
