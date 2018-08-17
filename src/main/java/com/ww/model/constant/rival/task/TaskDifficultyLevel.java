package com.ww.model.constant.rival.task;

import com.ww.model.constant.Category;
import lombok.Getter;

import java.util.Arrays;

import static com.ww.helper.RandomHelper.randomElement;

@Getter
public enum TaskDifficultyLevel {
    EXTREMELY_EASY(1, 1),
    VERY_EASY(3, 2),
    EASY(5, 3),
    NORMAL(7, 4),
    HARD(9, 5),
    VERY_HARD(11, 6),
    EXTREMELY_HARD(13, 7);

    private int level;
    private int points;

    private TaskDifficultyLevel(int level, int points) {
        this.level = level;
        this.points = points;
    }

    public static TaskDifficultyLevel random() {
        return randomElement(Arrays.asList(values()));
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
        return 8;
    }

    public static int answersCount(TaskDifficultyLevel difficultyLevel, int remainedDifficulty) {
        return answersCount(remainedDifficulty);
    }

    public static TaskDifficultyLevel fromLevel(Integer level) {
        if (level == null) {
            return random();
        }
        for (TaskDifficultyLevel difficultyLevel : values()) {
            if (difficultyLevel.getLevel() == level) {
                return difficultyLevel;
            }
        }
        return random();
    }

    public static TaskDifficultyLevel fromString(String name){
        try {
            return TaskDifficultyLevel.valueOf(name);
        } catch (IllegalArgumentException e){
            return random();
        }
    }
}
