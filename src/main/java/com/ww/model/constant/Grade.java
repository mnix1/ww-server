package com.ww.model.constant;

import com.ww.model.container.Resources;
import lombok.Getter;

@Getter
public enum Grade {
    A(5, 1600L, null, new Resources(100L, 100L, 100L, 100L)),
    B(4, 800L, 1599L, new Resources(50L, 50L, 50L, 50L)),
    C(3, 400L, 799L, new Resources(25L, 25L, 25L, 25L)),
    D(2, 200L, 399L, new Resources(10L, 10L, 10L, 10L)),
    E(1, 100L, 199L, new Resources(5L, 5L, 5L, 5L)),
    F(0, 0L, 99L, null);

    private int level;
    private Long rangeFrom;
    private Long rangeTo;
    private Resources resources;

    private Grade(int level, Long rangeFrom, Long rangeTo, Resources resources) {
        this.level = level;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
        this.resources = resources;
    }

    public static Grade fromElo(Long elo) {
        for (Grade grade : values()) {
            if (elo >= grade.rangeFrom && grade.rangeTo != null && elo < grade.rangeTo) {
                return grade;
            }
        }
        return A;
    }
}
