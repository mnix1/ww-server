package com.ww.service.rival.task.color;

import com.ww.model.constant.rival.DifficultyLevel;
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
import static com.ww.helper.ColorHelper.colorToInt;
import static com.ww.helper.ColorHelper.randomColor;
import static com.ww.helper.ColorHelper.randomGoodColor;
import static com.ww.helper.RandomHelper.randomDistinctIntegers;
import static com.ww.helper.RandomHelper.randomInteger;
import static com.ww.service.rival.task.color.ColorTaskService.prepareAnswers;

@Service
public class ColorMixingTaskService {

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, ColorTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = DifficultyLevel.answersCount(remainedDifficulty);
        int colorsToMixCount = 2;
        Color correctColor = prepareCorrectColor();
        List<Color> colorsToMix = prepareMixColors(colorsToMixCount, correctColor);
        List<Color> wrongColors = prepareWrongColors(answersCount - 1, correctColor);
        Question question = prepareQuestion(type, difficultyLevel, typeValue, colorsToMix);
        List<Answer> answers = prepareAnswers(correctColor, wrongColors);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Color prepareCorrectColor() {
        return randomGoodColor();
    }

    private List<Color> prepareMixColors(int count, Color correctColor) {
        List<Color> colors = new ArrayList<>(count);
        int totalR = correctColor.getRed();
        int totalG = correctColor.getGreen();
        int totalB = correctColor.getBlue();
        int r = randomInteger(0, totalR);
        int g = randomInteger(Math.max(0, totalG - r), totalG);
        int b = randomInteger(Math.max(0, totalB - g), totalB);
        colors.add(new Color(r, g, b));
        totalR -= r;
        totalG -= g;
        totalB -= b;
        colors.add(new Color(totalR, totalG, totalB));
        Collections.shuffle(colors);
        return colors;
    }

    private List<Color> prepareWrongColors(int count, Color correctColor) {
        List<Double> values = new ArrayList<>(count);
        double correctValue = colorToInt(correctColor);
        values.add(correctValue);
        List<Color> wrongColors = new ArrayList<>(count);
        int[] numbers = randomDistinctIntegers(count, 0, 255 * 255 * 255);
        int index = 0;
        while (wrongColors.size() < count) {
            Color c = new Color(numbers[index]);
            double value = colorToInt(c);
            if (isValueDistanceEnough(value, values, Math.max(0.08, 0.3 - count * 0.05))) {
                wrongColors.add(c);
                values.add(value);
                index++;
            } else {
                numbers[index] = randomInteger(0, 255 * 255 * 255);
            }
        }
        return wrongColors;
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, ColorTaskType typeValue, List<Color> colorsToMix) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == ColorTaskType.COLOR_MIXING) {
            question.setHtmlContent("<div style=\"display:flex;\">" + StringUtils.join(colorsToMix.stream().map(ColorTaskService::colorToHtml).collect(Collectors.toList()), "") + "</div>");
            question.setTextContentPolish("Jaki kolor powstanie z dodania następujących kolorów w systemie RGB?");
            question.setTextContentEnglish("What color will be created by adding the following colors in the RGB system?");
        }
        return question;
    }


}
