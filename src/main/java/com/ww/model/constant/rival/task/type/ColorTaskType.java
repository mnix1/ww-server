package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum ColorTaskType {
    BIGGEST_R,
    BIGGEST_G,
    BIGGEST_B,
    LOWEST_R,
    LOWEST_G,
    LOWEST_B,
    COLOR_MIXING;

    public static ColorTaskType random() {
        return randomElement(Arrays.asList(values()));
    }
}
