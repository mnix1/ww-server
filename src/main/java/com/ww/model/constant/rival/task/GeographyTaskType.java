package com.ww.model.constant.rival.task;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum GeographyTaskType {
    COUNTRY_NAME_FROM_CAPITAL_NAME,
    COUNTRY_NAME_FROM_MAP,
    COUNTRY_NAME_FROM_FLAG,
    CAPITAL_NAME_FROM_COUNTRY_NAME,
    CAPITAL_NAME_FROM_MAP,
    CAPITAL_NAME_FROM_FLAG;

    public static GeographyTaskType random() {
        return randomElement(Arrays.asList(values()));
    }
}
