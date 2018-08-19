package com.ww.service.rival.task.riddle;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.RiddleTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.AnswerHelper.difficultyCalibration;
import static com.ww.helper.AnswerHelper.isValueDistanceEnough;
import static com.ww.helper.ColorHelper.colorToHex;
import static com.ww.helper.RandomHelper.randomInteger;

@Service
public class RiddleColorTaskService {

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel, RiddleTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        int colorsToMixCount = difficultyCalibration(remainedDifficulty) / 3 + 2;
        List<Color> colorsToMix = prepareColorsToMix(colorsToMixCount, 255 / colorsToMixCount);
        Color correctColor = prepareMixColor(colorsToMix);
        List<Color> answerColors = prepareWrongColors(answersCount - 1, 255, correctColor);
        Question question = prepareQuestion(type, difficultyLevel, typeValue, colorsToMix);
        List<Answer> answers = prepareAnswers(typeValue, correctColor, answerColors);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Color prepareMixColor(List<Color> colorsToMix) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (Color color : colorsToMix) {
            r += color.getRed();
            g += color.getGreen();
            b += color.getBlue();
        }
        return new Color(r, g, b);
    }

    private List<Color> prepareColorsToMix(int count, int maxComponent) {
        return prepareWrongColors(count, maxComponent, null);
    }

    private List<Color> prepareWrongColors(int count, int maxComponent, Color correctColor) {
        List<Double> values = new ArrayList<>(count);
        if (correctColor != null) {
            double value = correctColor.getRed() + correctColor.getGreen() + correctColor.getBlue() / 3d;
            values.add(value);
        }
        List<Color> wrongColors = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int r = randomInteger(0, maxComponent);
            int g = randomInteger(0, maxComponent);
            int b = randomInteger(0, maxComponent);
            double value = r + g + b / 3d;
            if (isValueDistanceEnough(value, values)) {
                values.add(value);
                Color c = new Color(r, g, b);
                wrongColors.add(c);
            }
        }
        return wrongColors;
    }

    private Question prepareQuestion(TaskType type, TaskDifficultyLevel difficultyLevel, RiddleTaskType typeValue, List<Color> colorsToMix) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == RiddleTaskType.COLOR_MIXING) {
            question.setHtmlContent("<div style=\"display:flex;\">" + StringUtils.join(colorsToMix.stream().map(this::colorToHtml).collect(Collectors.toList()), "") + "</div>");
            question.setTextContentPolish("Jaki kolor powstanie z połączenia następujących kolorów?");
            question.setTextContentEnglish("What color will result from the combination of the following colors?");
        }
        return question;
    }

    private List<Answer> prepareAnswers(RiddleTaskType typeValue, Color correctColor, List<Color> wrongColors) {
        Answer correctAnswer = new Answer(true);
        fillAnswerContent(typeValue, correctAnswer, correctColor);
        List<Answer> wrongAnswers = wrongColors.stream().map(clipart -> {
            Answer answer = new Answer(false);
            fillAnswerContent(typeValue, answer, clipart);
            return answer;
        }).collect(Collectors.toList());
        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.addAll(wrongAnswers);
        return answers;
    }

    private void fillAnswerContent(RiddleTaskType typeValue, Answer answer, Color color) {
        if (typeValue == RiddleTaskType.COLOR_MIXING) {
            answer.setHtmlContent(colorToHtml(color));
        }
    }


    private String colorToHtml(Color color) {
        return "<div class=\"colorMixing\" style=\"background:" + colorToHex(color) + "\"></div>";
    }

}
