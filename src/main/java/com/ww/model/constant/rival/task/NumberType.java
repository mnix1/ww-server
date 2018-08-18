package com.ww.model.constant.rival.task;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum NumberType {
    FRACTION,
    INTEGER,
    DECIMAL,
    IRRATIONAL;

    public static NumberType random() {
        return randomElement(Arrays.asList(values()));
    }
}
