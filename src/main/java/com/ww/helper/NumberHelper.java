package com.ww.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberHelper {
    public static Double smartRound(Double d) {
        if (d == null) {
            return null;
        }
        if (d >= 1000) {
            return new BigDecimal(d).setScale(0, RoundingMode.HALF_UP).doubleValue();
        }
        if (d >= 100) {
            return new BigDecimal(d).setScale(1, RoundingMode.HALF_UP).doubleValue();
        }
        return new BigDecimal(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
