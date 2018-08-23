package com.ww.service.rival.task.color;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.ColorTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.AnswerHelper.difficultyCalibration;
import static com.ww.helper.AnswerHelper.isValueDistanceEnough;
import static com.ww.helper.ColorHelper.*;
import static com.ww.helper.RandomHelper.randomDistinctIntegers;
import static com.ww.helper.RandomHelper.randomInteger;
import static com.ww.service.rival.task.color.ColorTaskService.prepareAnswers;

@Service
public class ColorMatchAnswerTaskService {

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel, ColorTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        List<Color> colors = prepareColors(answersCount);
        Color correctColor = findCorrectColor(typeValue, colors);
        List<Color> wrongColors = findWrongColors(colors, correctColor);
        Question question = prepareQuestion(type, difficultyLevel, typeValue);
        List<Answer> answers = prepareAnswers(correctColor, wrongColors);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private List<Color> prepareColors(int count) {
        List<Color> colors = new ArrayList<>(count);
        int[] numbers = randomDistinctIntegers(count * 3, 0, 255);
        for (int i = 0; i < count * 3; i += 3) {
            colors.add(new Color(numbers[i], numbers[i + 1], numbers[i + 2]));
        }
        return colors;
    }

    private Color findCorrectColor(ColorTaskType typeValue, List<Color> colors) {
        int[] red = new int[colors.size()];
        int[] green = new int[colors.size()];
        int[] blue = new int[colors.size()];
        for (int i = 0; i < colors.size(); i++) {
            Color c = colors.get(i);
            red[i] = c.getRed();
            green[i] = c.getGreen();
            blue[i] = c.getBlue();
        }
        if (typeValue == ColorTaskType.BIGGEST_R) {
            int max = Arrays.stream(red).max().getAsInt();
            return colors.stream().filter(color -> color.getRed() == max).findFirst().get();
        }
        if (typeValue == ColorTaskType.BIGGEST_G) {
            int max = Arrays.stream(green).max().getAsInt();
            return colors.stream().filter(color -> color.getGreen() == max).findFirst().get();
        }
        if (typeValue == ColorTaskType.BIGGEST_B) {
            int max = Arrays.stream(blue).max().getAsInt();
            return colors.stream().filter(color -> color.getBlue() == max).findFirst().get();
        }
        if (typeValue == ColorTaskType.LOWEST_R) {
            int min = Arrays.stream(red).min().getAsInt();
            return colors.stream().filter(color -> color.getRed() == min).findFirst().get();
        }
        if (typeValue == ColorTaskType.LOWEST_G) {
            int min = Arrays.stream(green).min().getAsInt();
            return colors.stream().filter(color -> color.getGreen() == min).findFirst().get();
        }
        if (typeValue == ColorTaskType.LOWEST_B) {
            int min = Arrays.stream(blue).min().getAsInt();
            return colors.stream().filter(color -> color.getBlue() == min).findFirst().get();
        }
        return null;
    }

    private List<Color> findWrongColors(List<Color> colors, Color correctColor) {
        return colors.stream().filter(color -> !color.equals(correctColor)).collect(Collectors.toList());
    }

    private Question prepareQuestion(TaskType type, TaskDifficultyLevel difficultyLevel, ColorTaskType typeValue) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == ColorTaskType.BIGGEST_R) {
            question.setTextContentPolish("Który kolor składa się procentowo z największej ilości czerwieni?");
            question.setTextContentEnglish("Which color consists of the percentage of the largest amount of red?");
        }
        if (typeValue == ColorTaskType.BIGGEST_G) {
            question.setTextContentPolish("Który kolor składa się procentowo z największej ilości zieleni?");
            question.setTextContentEnglish("Which color consists of the percentage of the largest amount of green?");
        }
        if (typeValue == ColorTaskType.BIGGEST_B) {
            question.setTextContentPolish("Który kolor składa się procentowo z największej ilości niebieskiego?");
            question.setTextContentEnglish("Which color consists of the percentage of the largest amount of blue?");
        }
        if (typeValue == ColorTaskType.LOWEST_R) {
            question.setTextContentPolish("Który kolor składa się procentowo z najmniejszej ilości czerwieni?");
            question.setTextContentEnglish("Which color consists of the percentage of the smallest amount of red?");
        }
        if (typeValue == ColorTaskType.LOWEST_G) {
            question.setTextContentPolish("Który kolor składa się procentowo z najmniejszej ilości zieleni?");
            question.setTextContentEnglish("Which color consists of the percentage of the smallest amount of green?");
        }
        if (typeValue == ColorTaskType.LOWEST_B) {
            question.setTextContentPolish("Który kolor składa się procentowo z najmniejszej ilości niebieskiego?");
            question.setTextContentEnglish("Which color consists of the percentage of the smallest amount of blue?");
        }
        return question;
    }


}
