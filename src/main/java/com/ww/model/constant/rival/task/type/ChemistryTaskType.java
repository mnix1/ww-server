package com.ww.model.constant.rival.task.type;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

public enum ChemistryTaskType {
    MAX_ATOMIC_MASS,
    MIN_ATOMIC_MASS,
    NAME_FROM_SYMBOL,
    SYMBOL_FROM_NAME,
    NAME_FROM_SHELL_COUNT,
    SYMBOL_FROM_SHELL_COUNT,
    NUMBER_FROM_SHELL_COUNT,
    NAME_FROM_NUMBER,
    SYMBOL_FROM_NUMBER;

    public static ChemistryTaskType random() {
        return randomElement(Arrays.asList(values()));
    }

    public static boolean aboutAtomicMass(ChemistryTaskType type) {
        return type == MAX_ATOMIC_MASS || type == MIN_ATOMIC_MASS;
    }
}
