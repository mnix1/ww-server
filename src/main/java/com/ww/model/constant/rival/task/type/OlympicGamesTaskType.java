package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum OlympicGamesTaskType {
    COUNTRY_FROM_MEDAL_YEAR_FOR_POPULAR_ONLY_TEAM_SPORT,
    COUNTRY_FROM_ATHLETE,
    ATHLETE_FROM_COUNTRY,
    YEAR_FROM_ATHLETE,
    CITY_FROM_ATHLETE,
    ATHLETE_FROM_MEDAL_YEAR,
    ATHLETE_FROM_MEDAL_CITY,
    ATHLETE_FROM_MEDAL_YEAR_SPORT,
    ATHLETE_FROM_MEDAL_CITY_SPORT,
    YEAR_FROM_CITY,
    CITY_FROM_YEAR;

    public static OlympicGamesTaskType random() {
        return randomElement(Arrays.asList(values()));
    }

}
