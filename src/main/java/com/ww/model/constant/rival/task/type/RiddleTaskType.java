package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum RiddleTaskType {
//    FIND_DIFFERENCE,
    FIND_CLIPART,
    MISSING_CLIPART;

    public static RiddleTaskType random() {
        return randomElement(Arrays.asList(values()));
    }
}
