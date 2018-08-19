package com.ww.service.rival.task.riddle;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.RiddleTaskType;
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
import static com.ww.helper.ColorHelper.colorToHex;
import static com.ww.helper.ColorHelper.randomColor;
import static com.ww.helper.RandomHelper.randomInteger;

@Service
public class RiddleColorTaskService {

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel, RiddleTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        int colorsToMixCount = difficultyCalibration(remainedDifficulty) / 3 + 2;
        Color correctColor = prepareCorrectColor();
        List<Color> colorsToMix = prepareMixColors(colorsToMixCount, correctColor);
        List<Color> answerColors = prepareWrongColors(answersCount - 1, correctColor);
        Question question = prepareQuestion(type, difficultyLevel, typeValue, colorsToMix);
        List<Answer> answers = prepareAnswers(typeValue, correctColor, answerColors);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Color prepareCorrectColor() {
        return randomColor(90, 255);
    }

    private List<Color> prepareMixColors(int count, Color correctColor) {
        List<Color> colors = new ArrayList<>(count);
        int totalR = correctColor.getRed();
        int totalG = correctColor.getGreen();
        int totalB = correctColor.getBlue();
        for (int i = 0; i < count - 1; i++) {
            int r = randomInteger(0, totalR);
            int g = randomInteger(0, totalG);
            int b = randomInteger(0, totalB);
            colors.add(new Color(r, g, b));
            totalR -= r;
            totalG -= g;
            totalB -= b;
        }
        colors.add(new Color(totalR, totalG, totalB));
        Collections.shuffle(colors);
        return colors;
    }

    private List<Color> prepareWrongColors(int count, Color correctColor) {
        List<Double> values = new ArrayList<>(count);
        double correctValue = correctColor.getRed() + correctColor.getGreen() + correctColor.getBlue() / 3d;
        values.add(correctValue);
        List<Color> wrongColors = new ArrayList<>(count);
        while (wrongColors.size() < count) {
            int v1 = randomInteger(0, 255);
            int v2 = randomInteger(0, 255);
            int v3 = randomInteger(0, 255);
            double value = (v1 + v2 + v3) / 3d;
            if (isValueDistanceEnough(value, values)) {
                values.add(value);
                List<Integer> v = Arrays.asList(v1, v2, v3);
                Collections.shuffle(v);
                Color c = new Color(v.get(0), v.get(1), v.get(2));
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
