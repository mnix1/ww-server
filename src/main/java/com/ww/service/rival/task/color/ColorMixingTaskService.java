package com.ww.service.rival.task.color;

import com.ww.helper.ColorHelper;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.ColorTaskType;
import com.ww.model.container.rival.task.ColorObject;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.ColorHelper.randomGoodColor;
import static com.ww.helper.ColorHelper.similarColors;
import static com.ww.helper.RandomHelper.randomInteger;
import static com.ww.service.rival.task.color.ColorTaskService.prepareAnswers;

@Service
public class ColorMixingTaskService {

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, ColorTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = DifficultyLevel.answersCount(remainedDifficulty);
        int colorsToMixCount = 2;
        ColorObject correctColor = prepareCorrectColor();
        List<ColorObject> colorsToMix = prepareMixColors(colorsToMixCount, correctColor);
        List<ColorObject> wrongColors = prepareWrongColors(answersCount - 1, correctColor);
        Question question = prepareQuestion(type, difficultyLevel, typeValue, colorsToMix);
        List<Answer> answers = prepareAnswers(correctColor, wrongColors);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private ColorObject prepareCorrectColor() {
        return new ColorObject(randomGoodColor());
    }

    private List<ColorObject> prepareMixColors(int count, ColorObject correctColor) {
        List<ColorObject> colors = new ArrayList<>(count);
        int totalR = correctColor.getRed();
        int totalG = correctColor.getGreen();
        int totalB = correctColor.getBlue();
        int r = randomInteger(0, totalR);
        int g = randomInteger(Math.max(0, totalG - r), totalG);
        int b = randomInteger(Math.max(0, totalB - g), totalB);
        colors.add(new ColorObject(r, g, b));
        totalR -= r;
        totalG -= g;
        totalB -= b;
        colors.add(new ColorObject(totalR, totalG, totalB));
        Collections.shuffle(colors);
        return colors;
    }

    private List<ColorObject> prepareWrongColors(int count, ColorObject correctColor) {
        List<ColorObject> wrongColors = new ArrayList<>(count);
        int index = 0;
        double offset = .3;
        while (wrongColors.size() < count) {
            if (index++ > 100) {
                offset -= 0.005;
            }
            ColorObject possibleWrongColor = new ColorObject(ColorHelper.randomColor());
            if (similarColors(correctColor, possibleWrongColor, offset)) {
                continue;
            }
            boolean accepted = true;
            for (ColorObject wrongColor : wrongColors) {
                if (similarColors(wrongColor, possibleWrongColor, offset)) {
                    accepted = false;
                    break;
                }
            }
            if (accepted) {
                wrongColors.add(possibleWrongColor);
            }
        }
        return wrongColors;
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, ColorTaskType typeValue, List<ColorObject> colorsToMix) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == ColorTaskType.COLOR_MIXING) {
            question.setHtmlContent("<div style=\"display:flex;\">" + StringUtils.join(colorsToMix.stream().map(ColorTaskService::colorToHtml).collect(Collectors.toList()), "") + "</div>");
            question.setTextContentPolish("Jaki kolor powstanie z dodania następujących kolorów w systemie RGB?");
            question.setTextContentEnglish("What color will be created by adding the following colors in the RGB system?");
        }
        return question;
    }


}
