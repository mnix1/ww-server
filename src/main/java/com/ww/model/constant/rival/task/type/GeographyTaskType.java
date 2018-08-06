package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum GeographyTaskType {
    COUNTRY_NAME_FROM_ALPHA_2,
    COUNTRY_NAME_FROM_CAPITAL_NAME,
    COUNTRY_NAME_FROM_MAP,
    COUNTRY_NAME_FROM_FLAG,
    CAPITAL_NAME_FROM_ALPHA_3,
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

    public static boolean aboutPopulation(GeographyTaskType type) {
        return type == MAX_POPULATION || type == MIN_POPULATION;
    }

    public static boolean aboutArea(GeographyTaskType type) {
        return type == MAX_AREA || type == MIN_AREA;
    }
}
