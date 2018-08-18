package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum NumberTaskType {
    PRIME,
    GCD,
    LCM,
    MAX,
    MIN;

    public static NumberTaskType random() {
        return randomElement(Arrays.asList(values()));
    }
}
