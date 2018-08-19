package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum TimeTaskType {
    CLOCK_ADD,
    CLOCK_SUBTRACT
    ;

    public static TimeTaskType random() {
        return randomElement(Arrays.asList(values()));
    }
}
