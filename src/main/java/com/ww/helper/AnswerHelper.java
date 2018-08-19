package com.ww.helper;

import java.util.List;

public class AnswerHelper {
    public static boolean isValueDistanceEnough(Double value, List<Double> values, double rate) {
        return values.stream().noneMatch(e -> Math.abs(value - e) / value < rate);
    }

    public static boolean isValueDistanceEnough(Double value, List<Double> values) {
        return isValueDistanceEnough(value, values, 0.05);
    }

    public static int difficultyCalibration(int remainedDifficulty) {
        if (remainedDifficulty < 0) {
            return 0;
        }
        if (remainedDifficulty < 13) {
            return 1;
        }
        if (remainedDifficulty < 25) {
            return 2;
        }
        if (remainedDifficulty < 38) {
            return 3;
        }
        if (remainedDifficulty < 50) {
            return 4;
        }
        if (remainedDifficulty < 63) {
            return 5;
        }
        return 6;
    }

    public static String numbersToString(int[] numbers, String andWorld) {
        if (numbers.length == 2) {
            return numbers[0] + " " + andWorld + " " + numbers[1];
        }
        StringBuilder r = new StringBuilder("" + numbers[0]);
        for (int i = 1; i < numbers.length; i++) {
            r.append(", ").append(numbers[i]);
        }
        return r.toString();
    }
}
