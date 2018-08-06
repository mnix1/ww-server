package com.ww.model.constant.rival.task;

import lombok.Getter;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

@Getter
public enum TaskDifficultyLevel {
    VERY_EASY(1),
    EASY(3),
    NORMAL(5),
    HARD(7),
    VERY_HARD(9),
    EXTREMELY_HARD(11),
    NONE(-1);

    private int level;

    private TaskDifficultyLevel(int level) {
        this.level = level;
    }

    public static TaskDifficultyLevel random() {
        TaskDifficultyLevel e = randomElement(Arrays.asList(values()));
        if (e == NONE) {
            return random();
        }
        return e;
    }

    public static int answersCount(int remainedDifficulty) {
        if (remainedDifficulty < 0) {
            return 2;
        }
        if (remainedDifficulty < 2) {
            return 3;
        }
        if (remainedDifficulty < 4) {
            return 4;
        }
        if (remainedDifficulty < 6) {
            return 5;
        }
        if (remainedDifficulty < 8) {
            return 6;
        }
        if (remainedDifficulty < 10) {
            return 7;
        }
        return 6;
    }

    public static int answersCount(TaskDifficultyLevel difficultyLevel, int remainedDifficulty) {
        if (difficultyLevel == TaskDifficultyLevel.NONE) {
            return 4;
        }
        return answersCount(remainedDifficulty);
    }
}
