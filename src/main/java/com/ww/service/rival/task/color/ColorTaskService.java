package com.ww.service.rival.task.color;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.ColorTaskType;
import com.ww.model.container.rival.task.ColorComponents;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.ColorHelper.colorToHex;

@Service
@AllArgsConstructor
public class ColorTaskService {

    private final ColorMixingTaskService colorMixingTaskService;
    private final ColorMatchAnswerTaskService colorMatchAnswerTaskService;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel) {
        ColorTaskType typeValue = ColorTaskType.valueOf(type.getValue());
        if (typeValue == ColorTaskType.COLOR_MIXING) {
            return colorMixingTaskService.generate(type, difficultyLevel, typeValue);
        }
        return colorMatchAnswerTaskService.generate(type, difficultyLevel, typeValue);
    }

    public static List<Answer> prepareAnswers(ColorComponents correctColor, List<ColorComponents> wrongColors) {
        Answer correctAnswer = new Answer(true);
        fillAnswerContent(correctAnswer, correctColor);
        List<Answer> wrongAnswers = wrongColors.stream().map(color -> {
            Answer answer = new Answer(false);
            fillAnswerContent(answer, color);
            return answer;
        }).collect(Collectors.toList());
        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.addAll(wrongAnswers);
        return answers;
    }

    public static void fillAnswerContent(Answer answer, ColorComponents color) {
        answer.setHtmlContent(colorToHtml(color));
    }

    public static String colorToHtml(ColorComponents color) {
        return "<div class=\"color\" style=\"background:" + colorToHex(color.getColor()) + "\"></div>";
//        return "<div class=\"color\" style=\"background:" + colorToHex(color.getColor()) + "\"><div>"
//                + color.getRedPercentComponent() + "</div><div>"
//                + color.getGreenPercentComponent() + "</div><div>"
//                + color.getBluePercentComponent() + "</div></div>";
    }

}
