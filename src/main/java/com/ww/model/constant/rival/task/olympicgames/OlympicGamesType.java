package com.ww.model.constant.rival.task.olympicgames;

import lombok.Getter;

@Getter
public enum OlympicGamesType {
    SUMMER("summer Olympic Games", "letnie Igrzyska Olimpijskie"),
    WINTER("winter Olympic Games", "zimowe Igrzyska Olimpijskie");

    private String nameEnglish;
    private String namePolish;

    private OlympicGamesType(String nameEnglish, String namePolish) {
        this.namePolish = namePolish;
        this.nameEnglish = nameEnglish;
    }
}
