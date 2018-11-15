package com.ww.model.constant.rival;

import lombok.Getter;
import org.apache.commons.lang3.builder.Diff;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

@Getter
public enum DifficultyLevel {
    EXTREMELY_EASY(0, 0, 1),
    VERY_EASY(16, 1, 2),
    EASY(33, 2, 3),
    NORMAL(50, 3, 4),
    HARD(66, 4, 5),
    VERY_HARD(83, 5, 6),
    EXTREMELY_HARD(100, 6, 7);

    private int level;
    private int rating;
    private int points;

    private DifficultyLevel(int level, int rating, int points) {
        this.level = level;
        this.rating = rating;
        this.points = points;
    }

    public static DifficultyLevel random() {
        return randomElement(Arrays.asList(values()));
    }

    public static int answersCount(int remainedDifficulty) {
        if (remainedDifficulty <= 0) {
            return 2;
        }
        if (remainedDifficulty <= 16) {
            return 3;
        }
        if (remainedDifficulty <= 33) {
            return 4;
        }
        if (remainedDifficulty <= 50) {
            return 5;
        }
        if (remainedDifficulty <= 66) {
            return 6;
        }
        if (remainedDifficulty <= 83) {
            return 7;
        }
        return 8;
    }

    public static DifficultyLevel fromString(String name) {
        try {
            return DifficultyLevel.valueOf(name);
        } catch (IllegalArgumentException e) {
            return random();
        }
    }
}
