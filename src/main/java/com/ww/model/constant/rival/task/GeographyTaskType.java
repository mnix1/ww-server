package com.ww.model.constant.rival.task;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum GeographyTaskType {
    COUNTRY_NAME_FROM_CAPITAL_NAME,
    COUNTRY_NAME_FROM_MAP,
    COUNTRY_NAME_FROM_FLAG,
    CAPITAL_NAME_FROM_COUNTRY_NAME,
    CAPITAL_NAME_FROM_MAP,
    CAPITAL_NAME_FROM_FLAG,
    MAX_POPULATION,
    MIN_POPULATION,
    MAX_AREA,
    MIN_AREA;

    public static GeographyTaskType random() {
        return randomElement(Arrays.asList(values()));
    }

    public static boolean usesPopulation(GeographyTaskType type) {
        return type == MAX_POPULATION || type == MIN_POPULATION;
    }

    public static boolean usesArea(GeographyTaskType type) {
        return type == MAX_AREA || type == MIN_AREA;
    }
}
