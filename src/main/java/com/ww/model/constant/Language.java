package com.ww.model.constant;

public enum Language {
    POLISH,
    ENGLISH,
    NONE,
    NO_COMMON;

    public static Language fromLocale(String locale) {
        if (locale.contains("pl")) {
            return POLISH;
        }
        return ENGLISH;
    }

    public static boolean available(Language language) {
        return language == POLISH || language == ENGLISH;
    }

    public static boolean addPolish(Language lang) {
        return lang == POLISH;
    }

    public static boolean addEnglish(Language lang) {
        return lang == ENGLISH || lang == NO_COMMON || lang == NONE;
    }
}
