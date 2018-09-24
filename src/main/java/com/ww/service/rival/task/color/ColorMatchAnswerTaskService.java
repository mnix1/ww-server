package com.ww.service.rival.task.color;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.ColorTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.util.*;

import static com.ww.helper.AnswerHelper.difficultyCalibration;
import static com.ww.helper.AnswerHelper.isValueDistanceEnough;
import static com.ww.helper.ColorHelper.*;
import static com.ww.service.rival.task.color.ColorTaskService.prepareAnswers;

@Service
public class ColorMatchAnswerTaskService {

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, ColorTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = DifficultyLevel.answersCount(remainedDifficulty);
        Color correctColor = prepareCorrectColor();
        List<Color> wrongColors = prepareWrongColors(typeValue, answersCount - 1, correctColor);
        Question question = prepareQuestion(type, difficultyLevel, typeValue);
        List<Answer> answers = prepareAnswers(correctColor, wrongColors);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Color prepareCorrectColor() {
        return randomColor(40, 210);
    }

    private List<Color> prepareWrongColors(ColorTaskType typeValue, int count, Color correctColor) {
        List<Color> wrongColors = new ArrayList<>(count);
        int correctColorSum = colorToSumInt(correctColor);
        int correctComp = findComponent(typeValue, correctColor);
        double correctPercentComp = 1.0 * correctComp / correctColorSum;
        while (wrongColors.size() < count) {
            Color wrongColor = null;
            if (typeValue == ColorTaskType.BIGGEST_R) {
                wrongColor = randomColor(0, correctComp - 1, 0, 255, 0, 255);
            } else if (typeValue == ColorTaskType.BIGGEST_G) {
                wrongColor = randomColor(0, 255, 0, correctComp - 1, 0, 255);
            } else if (typeValue == ColorTaskType.BIGGEST_B) {
                wrongColor = randomColor(0, 255, 0, 255, 0, correctComp - 1);
            } else if (typeValue == ColorTaskType.LOWEST_R) {
                wrongColor = randomColor(correctComp + 1, 255, 0, 255, 0, 255);
            } else if (typeValue == ColorTaskType.LOWEST_G) {
                wrongColor = randomColor(0, 255, correctComp + 1, 255, 0, 255);
            } else if (typeValue == ColorTaskType.LOWEST_B) {
                wrongColor = randomColor(0, 255, 0, 255, correctComp + 1, 255);
            }
            int wrongColorSum = colorToSumInt(wrongColor);
            int wrongComp = findComponent(typeValue, wrongColor);
            double wrongPercentComp = 1.0 * wrongComp / wrongColorSum;
            if (ColorTaskType.aboutLowest(typeValue)) {
                if (wrongPercentComp > correctPercentComp + 0.05) {
                    wrongColors.add(wrongColor);
                }
            } else {
                if (wrongPercentComp < correctPercentComp - 0.05) {
                    wrongColors.add(wrongColor);
                }
            }
        }
        return wrongColors;
    }

    private int findComponent(ColorTaskType typeValue, Color color) {
        if (typeValue == ColorTaskType.BIGGEST_R
                || typeValue == ColorTaskType.LOWEST_R) {
            return color.getRed();
        }
        if (typeValue == ColorTaskType.BIGGEST_G
                || typeValue == ColorTaskType.LOWEST_G) {
            return color.getGreen();
        }
        if (typeValue == ColorTaskType.BIGGEST_B
                || typeValue == ColorTaskType.LOWEST_B) {
            return color.getBlue();
        }
        throw new IllegalArgumentException("No typeValue handled");
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, ColorTaskType typeValue) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == ColorTaskType.BIGGEST_R) {
            question.setTextContentPolish("Który kolor składa się procentowo z największej ilości czerwieni?");
            question.setTextContentEnglish("Which color consists of the percentage of the largest amount of red?");
        } else if (typeValue == ColorTaskType.BIGGEST_G) {
            question.setTextContentPolish("Który kolor składa się procentowo z największej ilości zieleni?");
            question.setTextContentEnglish("Which color consists of the percentage of the largest amount of green?");
        } else if (typeValue == ColorTaskType.BIGGEST_B) {
            question.setTextContentPolish("Który kolor składa się procentowo z największej ilości niebieskiego?");
            question.setTextContentEnglish("Which color consists of the percentage of the largest amount of blue?");
        } else if (typeValue == ColorTaskType.LOWEST_R) {
            question.setTextContentPolish("Który kolor składa się procentowo z najmniejszej ilości czerwieni?");
            question.setTextContentEnglish("Which color consists of the percentage of the smallest amount of red?");
        } else if (typeValue == ColorTaskType.LOWEST_G) {
            question.setTextContentPolish("Który kolor składa się procentowo z najmniejszej ilości zieleni?");
            question.setTextContentEnglish("Which color consists of the percentage of the smallest amount of green?");
        } else if (typeValue == ColorTaskType.LOWEST_B) {
            question.setTextContentPolish("Który kolor składa się procentowo z najmniejszej ilości niebieskiego?");
            question.setTextContentEnglish("Which color consists of the percentage of the smallest amount of blue?");
        }
        return question;
    }


}
