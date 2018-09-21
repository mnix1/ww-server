package com.ww.model.constant.rival.task.olympicgames;

import lombok.Getter;

@Getter
public enum OlympicGamesMedal {
    GOLD("gold", "złoty"),
    SILVER("silver", "srebrny"),
    BRONZE("bronze", "brązowy");

    private String nameEnglish;
    private String namePolish;

    private OlympicGamesMedal(String nameEnglish, String namePolish) {
        this.namePolish = namePolish;
        this.nameEnglish = nameEnglish;
    }

    public static OlympicGamesMedal fromString(String value){
        return valueOf(value.toUpperCase());
    }
}
