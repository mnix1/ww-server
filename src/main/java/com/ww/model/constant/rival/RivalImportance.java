package com.ww.model.constant.rival;

public enum RivalImportance {
    RANKING,
    FAST,
    FRIEND;

    public boolean isRanking() {
        return this == RANKING;
    }

    public boolean isFriend() {
        return this == FRIEND;
    }
}
