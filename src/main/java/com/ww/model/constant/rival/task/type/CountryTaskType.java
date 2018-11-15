package com.ww.model.constant.rival.task.type;

public enum CountryTaskType {
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

    public static boolean aboutPopulation(CountryTaskType type) {
        return type == MAX_POPULATION || type == MIN_POPULATION;
    }

    public static boolean aboutArea(CountryTaskType type) {
        return type == MAX_AREA || type == MIN_AREA;
    }
}
