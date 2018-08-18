package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum EquationTaskType {
    ADDITION,
    MULTIPLICATION,
    MODULO;

    public static EquationTaskType random() {
        return randomElement(Arrays.asList(values()));
    }
}
