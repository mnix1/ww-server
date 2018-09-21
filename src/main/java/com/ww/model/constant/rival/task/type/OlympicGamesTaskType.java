package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum OlympicGamesTaskType {
    WHICH_ATHLETE_FROM_MEDAL_YEAR,
    WHICH_ATHLETE_FROM_YEAR,
    WHICH_ATHLETE_FROM_CITY,
    YEAR_FROM_WHERE,
    WHERE_FROM_YEAR;

    public static OlympicGamesTaskType random() {
        return randomElement(Arrays.asList(values()));
    }

}
