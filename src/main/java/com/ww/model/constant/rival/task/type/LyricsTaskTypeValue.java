package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum LyricsTaskTypeValue {
    NEXT_LINE,
    PREVIOUS_LINE;
//    MIDDLE_LINE;// TODO add

    public static LyricsTaskTypeValue random() {
        return randomElement(Arrays.asList(values()));
    }
}
