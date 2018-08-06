package com.ww.service.rival.task.math;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.MathTaskType;
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

import static com.ww.helper.RandomHelper.randomInteger;

@Service
public class MathTaskService {

    @Autowired
    TaskService taskService;
    @Autowired
    TaskRendererService taskRendererService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        MathTaskType typeValue = MathTaskType.valueOf(type.getValue());
        int[] numbers = prepareNumbers(typeValue);
        Question question = prepareQuestion(type, typeValue, numbers);
        List<Answer> answers = prepareAnswers(typeValue, numbers);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private int[] prepareNumbers(MathTaskType typeValue) {
        int[] numbers = null;
        if (typeValue == MathTaskType.ADDITION) {
            int count = randomInteger(2, 5);
            int bound = count > 4 ? 19 : (count > 2 ? 99 : 999);
            numbers = prepareNumbers(count, -bound, bound);
        }
        if (typeValue == MathTaskType.MULTIPLICATION) {
            int count = randomInteger(2, 3);
            int bound = count > 2 ? 7 : 99;
            numbers = prepareNumbers(count, -bound, bound);
        }
        if (typeValue == MathTaskType.MODULO) {
            numbers = new int[2];
            numbers[0] = randomInteger(4, 99);
            numbers[1] = randomInteger(1, numbers[0] - 1);
        }
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == 0) {
                numbers[i] = randomInteger(1, 10);
            }
        }
        return numbers;
    }

    private Question prepareQuestion(TaskType type, MathTaskType typeValue, int[] numbers) {
        Question question = new Question(type);
        if (typeValue == MathTaskType.ADDITION) {
            question.setTextContentPolish("Suma następujących liczb " + numbersToString(numbers, "i") + " wynosi");
            question.setTextContentEnglish("The result of adding numbers " + numbersToString(numbers, "and") + " is");
        }
        if (typeValue == MathTaskType.MULTIPLICATION) {
            question.setTextContentPolish("Wynikiem mnożenia liczb " + numbersToString(numbers, "i") + " jest");
            question.setTextContentEnglish("The result of multiplying numbers " + numbersToString(numbers, "and") + " is");
        }
        if (typeValue == MathTaskType.MODULO) {
            question.setTextContentPolish("Resztą z dzielenia liczby " + numbers[0] + " przez " + numbers[1] + " jest");
            question.setTextContentEnglish("The remainder of the dividing the number " + numbers[0] + " by " + numbers[1] + " is");
        }
        return question;
    }

    private String numbersToString(int[] numbers, String andWorld) {
        if (numbers.length == 2) {
            return numbers[0] + " " + andWorld + " " + numbers[1];
        }
        StringBuilder r = new StringBuilder("" + numbers[0]);
        for (int i = 1; i < numbers.length; i++) {
            r.append(", ").append(numbers[i]);
        }
        return r.toString();
    }

    private int[] prepareNumbers(int count, int from, int to) {
        int[] numbers = new int[count];
        for (int i = 0; i < count; i++) {
            numbers[i] = randomInteger(from, to);
        }
        return numbers;
    }

    private List<Answer> prepareAnswers(MathTaskType typeValue, int[] numbers) {
        int correctResult = numbers[0];
        if (typeValue == MathTaskType.ADDITION || typeValue == MathTaskType.MULTIPLICATION) {
            for (int i = 1; i < numbers.length; i++) {
                if (typeValue == MathTaskType.ADDITION) {
                    correctResult += numbers[i];
                }
                if (typeValue == MathTaskType.MULTIPLICATION) {
                    correctResult *= numbers[i];
                }
            }
        }
        if (typeValue == MathTaskType.MODULO) {
            correctResult = numbers[0] % numbers[1];
        }
        Answer correctAnswer = new Answer(true);
        correctAnswer.setTextContent("" + correctResult);

        List<Answer> wrongAnswers = new ArrayList<>();
        List<Integer> wrongResults = new ArrayList<>();
        while (wrongAnswers.size() < 3) {
            int wrongResult = correctResult + randomInteger(-20, 20);
            if (typeValue == MathTaskType.MODULO && wrongResult < 0) {
                wrongResult = correctResult + randomInteger(0, 20);
            }
            if (correctResult != wrongResult && !wrongResults.contains(wrongResult)) {
                wrongResults.add(wrongResult);
                Answer wrongAnswer = new Answer(false);
                wrongAnswer.setTextContent("" + wrongResult);
                wrongAnswers.add(wrongAnswer);
            }
        }
        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.addAll(wrongAnswers);
        return answers;
    }

}
