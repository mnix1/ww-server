package com.ww.model.constant;

import com.ww.model.container.Resources;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Grade {
    A(5, 1600L, null, new Resources(60L, 40L, 40L, 80L)),
    B(4, 800L, 1599L, new Resources(30L, 20L, 20L, 30L)),
    C(3, 400L, 799L, new Resources(15L, 10L, 10L, 10L)),
    D(2, 200L, 399L, new Resources(10L, 5L, 5L, 4L)),
    E(1, 100L, 199L, new Resources(5L, 2L, 2L, 1L)),
    F(0, 0L, 99L, new Resources(0L, 0L, 0L, 0L));

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

    public static Map<Grade, Resources> rewards() {
        Map<Grade, Resources> rewards = new HashMap<>();
        for (Grade grade : values()) {
            rewards.put(grade, grade.resources);
        }
        return rewards;
    }
}
