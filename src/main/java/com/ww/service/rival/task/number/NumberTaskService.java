package com.ww.service.rival.task.number;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.NumberTaskType;
import com.ww.model.container.NumberObject;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomInteger;

@Service
public class NumberTaskService {

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRendererService taskRendererService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        NumberTaskType typeValue = NumberTaskType.valueOf(type.getValue());
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        List<NumberObject> numberObjects = prepareNumbers(answersCount);
        Question question = prepareQuestion(type, difficultyLevel, typeValue);
        List<Answer> answers = prepareAnswers(typeValue, numberObjects);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private List<NumberObject> prepareNumbers(int count) {
        List<Double> values = new ArrayList<>(count);
        List<NumberObject> numbers = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            NumberObject numberObject = generateRandomInteger();
            if (isValueDistanceEnough(numberObject.getValue(), values)) {
                numbers.add(numberObject);
                values.add(numberObject.getValue());
            }
        }
        return numbers;
    }

    private boolean isValueDistanceEnough(Double value, List<Double> values) {
        return values.stream().noneMatch(e -> Math.abs(value - e) / value < 0.05);
    }

    private NumberObject generateRandomInteger() {
        int number = randomInteger(1, 99);
        String html = "<span>" + number + "</span>";
        return new NumberObject((double) number, html);
    }

    private Question prepareQuestion(TaskType type, TaskDifficultyLevel difficultyLevel, NumberTaskType typeValue) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == NumberTaskType.MAX) {
            question.setTextContentPolish("Która z liczb jest największa?");
            question.setTextContentEnglish("Which number is the biggest");
        }
        if (typeValue == NumberTaskType.MIN) {
            question.setTextContentPolish("Która z liczb jest najmniejsza?");
            question.setTextContentEnglish("Which number is the lowest");
        }
        return question;
    }

    private List<Answer> prepareAnswers(NumberTaskType typeValue, List<NumberObject> numbers) {
        Double correctValue = null;
        int correctIndex = 0;
        if (typeValue == NumberTaskType.MAX) {
            for (int i = 0; i < numbers.size(); i++) {
                if (correctValue == null || correctValue < numbers.get(i).getValue()) {
                    correctValue = numbers.get(i).getValue();
                    correctIndex = i;
                }
            }
        } else if (typeValue == NumberTaskType.MIN) {
            for (int i = 0; i < numbers.size(); i++) {
                if (correctValue == null || correctValue > numbers.get(i).getValue()) {
                    correctValue = numbers.get(i).getValue();
                    correctIndex = i;
                }
            }
        }
        NumberObject correctNumber = numbers.get(correctIndex);
        return numbers.stream().map(numberObject -> {
            Answer answer = new Answer(correctNumber == numberObject);
            answer.setTextContent(numberObject.getHtml());
            return answer;
        }).collect(Collectors.toList());
    }

}
