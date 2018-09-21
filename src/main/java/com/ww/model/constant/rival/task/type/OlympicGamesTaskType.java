package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum OlympicGamesTaskType {
    YEAR_FROM_ATHLETE,
    CITY_FROM_ATHLETE,
    WHICH_ATHLETE_FROM_MEDAL_YEAR,
    WHICH_ATHLETE_FROM_MEDAL_CITY,
    WHICH_ATHLETE_FROM_MEDAL_YEAR_SPORT,
    WHICH_ATHLETE_FROM_MEDAL_CITY_SPORT,
    YEAR_FROM_CITY,
    CITY_FROM_YEAR;

    public static OlympicGamesTaskType random() {
        return randomElement(Arrays.asList(values()));
    }

}
