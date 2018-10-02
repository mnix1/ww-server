package com.ww.service.rival.task.color;

import com.ww.helper.ColorHelper;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.ColorTaskType;
import com.ww.model.container.rival.task.ColorComponents;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.ColorHelper.similarColors;
import static com.ww.model.constant.rival.task.type.ColorTaskType.aboutLowest;
import static com.ww.service.rival.task.color.ColorTaskService.prepareAnswers;

@Service
public class ColorMatchAnswerTaskService {

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, ColorTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = Math.min(6, DifficultyLevel.answersCount(remainedDifficulty));
        List<ColorComponents> colors = prepareColors(typeValue, answersCount);
        ColorComponents correctColor = findCorrectColor(typeValue, colors);
        List<ColorComponents> wrongColors = colors.stream().filter(colorComponents -> colorComponents != correctColor).collect(Collectors.toList());
        Question question = prepareQuestion(type, difficultyLevel, typeValue);
        List<Answer> answers = prepareAnswers(correctColor, wrongColors);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private ColorComponents findCorrectColor(ColorTaskType typeValue, List<ColorComponents> colors) {
        boolean aboutLowers = aboutLowest(typeValue);
        ColorComponents correct = null;
        double component = 0;
        for (ColorComponents color : colors) {
            double possibleComponent = findComponent(typeValue, color);
            if (correct == null
                    || (aboutLowers && possibleComponent < component)
                    || (!aboutLowers && possibleComponent > component)) {
                correct = color;
                component = possibleComponent;
            }
        }
        return correct;
    }

    private List<ColorComponents> prepareColors(ColorTaskType typeValue, int count) {
        List<ColorComponents> colors = new ArrayList<>(count);
        List<Double> components = new ArrayList<>(count);
        int index = 0;
        double offset = .3;
        while (colors.size() < count) {
            if (index++ > 100) {
                offset -= 0.001;
            }
            if (offset < 0.1) {
                return prepareColors(typeValue, count);
            }
            ColorComponents possibleColor = new ColorComponents(ColorHelper.randomColor());
            boolean accepted = true;
            for (ColorComponents color : colors) {
                if (similarColors(color, possibleColor, offset)) {
                    accepted = false;
                    break;
                }
            }
            if (!accepted) {
                continue;
            }
            accepted = true;
            double possibleComponent = findComponent(typeValue, possibleColor);
            for (double component : components) {
                if (Math.abs(component - possibleComponent) < .1) {
                    accepted = false;
                }
            }
            if (!accepted) {
                continue;
            }
            colors.add(possibleColor);
            components.add(possibleComponent);
        }
        return colors;
    }

    private double findComponent(ColorTaskType typeValue, ColorComponents color) {
        if (typeValue == ColorTaskType.BIGGEST_R
                || typeValue == ColorTaskType.LOWEST_R) {
            return color.getRedPercentComponent();
        }
        if (typeValue == ColorTaskType.BIGGEST_G
                || typeValue == ColorTaskType.LOWEST_G) {
            return color.getGreenPercentComponent();
        }
        if (typeValue == ColorTaskType.BIGGEST_B
                || typeValue == ColorTaskType.LOWEST_B) {
            return color.getBluePercentComponent();
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
