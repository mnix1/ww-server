package com.ww.model.constant.wisie;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum WisorType {
    WISOR_1,
    WISOR_2,
    WISOR_3,
    WISOR_4,
    WISOR_5,
    WISOR_6,
    WISOR_7,
    WISOR_8,
    WISOR_9,
    WISOR_10,
    WISOR_11,
    WISOR_12,
    WISOR_13,
    WISOR_14,
    WISOR_15,
    WISOR_16,
    WISOR_17,
    WISOR_18,
    WISOR_19,
    WISOR_20,
    WISOR_21,
    WISOR_22,
    WISOR_23,
    WISOR_24,
    WISOR_25,
    WISOR_26,
    WISOR_27,
    WISOR_28,
    WISOR_29,
    WISOR_30,
    WISOR_31,
    WISOR_32,
    WISOR_33,
    WISOR_34,
    WISOR_35,
    WISOR_36,
    WISOR_37,
    WISOR_38,
    WISOR_39,
    WISOR_40,
    WISOR_41,
    WISOR_42,
    WISOR_43,
    WISOR_44,
    WISOR_45,
    WISOR_46,
    WISOR_47,
    WISOR_48;

    public static WisorType random() {
        List<WisorType> possible = Arrays.asList(values());
        return randomElement(possible);
    }
}
