package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum ChemistryTaskType {
    NAME_FROM_SYMBOL,
    SYMBOL_FROM_NAME;

    public static ChemistryTaskType random() {
        return randomElement(Arrays.asList(values()));
    }

}
