package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum RiddleTaskType {
    FIND_DIFFERENCE_LEFT_MISSING,
    FIND_DIFFERENCE_RIGHT_MISSING,
    FIND_CLIPART,
    MISSING_CLIPART;

    public static RiddleTaskType random() {
        return randomElement(Arrays.asList(values()));
    }

    public static boolean aboutFindDifference(RiddleTaskType type) {
        return type == FIND_DIFFERENCE_LEFT_MISSING
                || type == FIND_DIFFERENCE_RIGHT_MISSING;
    }
}
