package com.ww.service.rival.task.color;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.ColorTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.ColorHelper.colorToHex;

@Service
public class ColorTaskService {
    @Autowired
    ColorMixingTaskService colorMixingTaskService;
    @Autowired
    ColorMatchAnswerTaskService colorMatchAnswerTaskService;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel) {
        ColorTaskType typeValue = ColorTaskType.valueOf(type.getValue());
        if (typeValue == ColorTaskType.COLOR_MIXING) {
            return colorMixingTaskService.generate(type, difficultyLevel, typeValue);
        }
        return colorMatchAnswerTaskService.generate(type, difficultyLevel, typeValue);
    }

    public static List<Answer> prepareAnswers(Color correctColor, List<Color> wrongColors) {
        Answer correctAnswer = new Answer(true);
        fillAnswerContent(correctAnswer, correctColor);
        List<Answer> wrongAnswers = wrongColors.stream().map(clipart -> {
            Answer answer = new Answer(false);
            fillAnswerContent(answer, clipart);
            return answer;
        }).collect(Collectors.toList());
        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.addAll(wrongAnswers);
        return answers;
    }

    public static void fillAnswerContent(Answer answer, Color color) {
        answer.setHtmlContent(colorToHtml(color));
    }


    public static String colorToHtml(Color color) {
        return "<div class=\"color\" style=\"background:" + colorToHex(color) + "\"></div>";
    }

}
