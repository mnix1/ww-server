package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum TimeTaskType {
    DIGITAL_CLOCK_ADD,
    DIGITAL_CLOCK_SUBTRACT,
    ANALOG_CLOCK_ADD,
    ANALOG_CLOCK_SUBTRACT;

    public static TimeTaskType random() {
        return randomElement(Arrays.asList(values()));
    }
}
