package com.ww.model.constant;

public enum Gender {
    MEN,
    WOMEN;

    public static Gender fromString(String value){
        return valueOf(value.toUpperCase());
    }
}
