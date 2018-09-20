package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum OlympicGamesTaskType {
    YEAR_FROM_WHERE,
    WHERE_FROM_YEAR;

    public static OlympicGamesTaskType random() {
        return randomElement(Arrays.asList(values()));
    }

}
