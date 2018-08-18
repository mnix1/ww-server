package com.ww.service.rival.task.equation;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.EquationTaskType;
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

import static com.ww.helper.AnswerHelper.difficultyCalibration;
import static com.ww.helper.AnswerHelper.numbersToString;
import static com.ww.helper.RandomHelper.randomInteger;
import static com.ww.helper.RandomHelper.randomIntegers;

@Service
public class EquationTaskService {

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRendererService taskRendererService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        EquationTaskType typeValue = EquationTaskType.valueOf(type.getValue());
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        int[] numbers = prepareNumbers(typeValue, remainedDifficulty);
        Question question = prepareQuestion(type, difficultyLevel, typeValue, numbers);
        List<Answer> answers = prepareAnswers(typeValue, numbers, answersCount);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private int[] prepareNumbers(EquationTaskType typeValue, int difficulty) {
        int[] numbers = null;
        if (typeValue == EquationTaskType.ADDITION) {
            int count = difficultyCalibration(difficulty) + 2;
            int bound = 9 + difficultyCalibration(difficulty) * 5;
            numbers = randomIntegers(count, -bound, bound);
        }
        if (typeValue == EquationTaskType.MULTIPLICATION) {
            int count = difficultyCalibration(difficulty) / 2 + 2;
            int bound = 9 + difficultyCalibration(difficulty) * 2;
            numbers = randomIntegers(count, -bound, bound);
        }
        if (typeValue == EquationTaskType.MODULO) {
            numbers = new int[2];
            int max = Math.max(5, difficultyCalibration(difficulty) * 49);
            numbers[0] = randomInteger(4, max);
            numbers[1] = randomInteger(1, numbers[0] - 1);
        }
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == 0) {
                numbers[i] = randomInteger(1, 10);
            }
        }
        return numbers;
    }

    private Question prepareQuestion(TaskType type, TaskDifficultyLevel difficultyLevel, EquationTaskType typeValue, int[] numbers) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == EquationTaskType.ADDITION) {
            question.setTextContentPolish("Suma następujących liczb " + numbersToString(numbers, "i") + " wynosi");
            question.setTextContentEnglish("The result of adding numbers " + numbersToString(numbers, "and") + " is");
        }
        if (typeValue == EquationTaskType.MULTIPLICATION) {
            question.setTextContentPolish("Wynikiem mnożenia liczb " + numbersToString(numbers, "i") + " jest");
            question.setTextContentEnglish("The result of multiplying numbers " + numbersToString(numbers, "and") + " is");
        }
        if (typeValue == EquationTaskType.MODULO) {
            question.setTextContentPolish("Resztą z dzielenia liczby " + numbers[0] + " przez " + numbers[1] + " jest");
            question.setTextContentEnglish("The remainder of the dividing the number " + numbers[0] + " by " + numbers[1] + " is");
        }
        return question;
    }

    private List<Answer> prepareAnswers(EquationTaskType typeValue, int[] numbers, int answersCount) {
        int correctResult = numbers[0];
        if (typeValue == EquationTaskType.ADDITION || typeValue == EquationTaskType.MULTIPLICATION) {
            for (int i = 1; i < numbers.length; i++) {
                if (typeValue == EquationTaskType.ADDITION) {
                    correctResult += numbers[i];
                }
                if (typeValue == EquationTaskType.MULTIPLICATION) {
                    correctResult *= numbers[i];
                }
            }
        }
        if (typeValue == EquationTaskType.MODULO) {
            correctResult = numbers[0] % numbers[1];
        }
        Answer correctAnswer = new Answer(true);
        correctAnswer.setTextContent("" + correctResult);

        List<Answer> wrongAnswers = new ArrayList<>();
        List<Integer> wrongResults = new ArrayList<>();
        while (wrongAnswers.size() < answersCount - 1) {
            int wrongResult = correctResult + randomInteger(-20, 20);
            if (typeValue == EquationTaskType.MODULO && wrongResult < 0) {
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
