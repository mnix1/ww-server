package com.ww.model.constant.rival.task;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum MusicTaskType {
    NEXT_LINE,
    PREVIOUS_LINE;
//    MIDDLE_LINE;// TODO add

    public static MusicTaskType random() {
        return randomElement(Arrays.asList(values()));
    }
}
