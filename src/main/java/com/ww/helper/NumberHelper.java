package com.ww.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberHelper {
    public static Double round2(Double d){
        if(d == null){
            return null;
        }
        return new BigDecimal(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static Double round1(Double d){
        if(d == null){
            return null;
        }
        return new BigDecimal(d).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
