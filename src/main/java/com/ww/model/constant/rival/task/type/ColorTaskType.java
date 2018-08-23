package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum ColorTaskType {
    COLOR_MIXING;

    public static ColorTaskType random() {
        return randomElement(Arrays.asList(values()));
    }
}
