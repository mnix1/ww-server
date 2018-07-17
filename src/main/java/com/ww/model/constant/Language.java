package com.ww.model.constant;

public enum Language {
    POLISH,
    ENGLISH,
    ALL;

    public static boolean addPolish(Language lang) {
        return lang == POLISH || lang == ALL;
    }

    public static boolean addEnglish(Language lang) {
        return lang == ENGLISH || lang == ALL;
    }
}
