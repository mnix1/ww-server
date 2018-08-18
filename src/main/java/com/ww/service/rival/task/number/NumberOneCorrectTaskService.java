package com.ww.service.rival.task.number;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.ElementTaskType;
import com.ww.model.constant.rival.task.type.EquationTaskType;
import com.ww.model.constant.rival.task.type.NumberTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Element;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.category.ElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.ww.helper.AnswerHelper.difficultyCalibration;
import static com.ww.helper.AnswerHelper.numbersToString;
import static com.ww.helper.RandomHelper.*;

@Service
public class NumberOneCorrectTaskService {

    @Autowired
    ElementRepository elementRepository;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel, NumberTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        int[] numbers = prepareNumbers(typeValue, remainedDifficulty);
        Question question = prepareQuestion(type, difficultyLevel, typeValue, numbers);
        List<Answer> answers = prepareAnswers(typeValue, numbers, answersCount);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private int[] prepareNumbers(NumberTaskType typeValue, int difficulty) {
        int[] numbers = null;
        if (typeValue == NumberTaskType.GCD) {
            int gcd = randomInteger(2, difficultyCalibration(difficulty) / 2 + 9);
            int count = difficultyCalibration(difficulty) / 2 + 2;
            int bound = 9 + difficultyCalibration(difficulty) * 5;
            numbers = randomDistinctIntegers(count, 2, bound);
            for (int i = 0; i < numbers.length; i++) {
                numbers[i] = gcd * numbers[i];
            }
        }
        if (typeValue == NumberTaskType.LCM) {
            int count = difficultyCalibration(difficulty) / 2 + 2;
            int bound = 9 + difficultyCalibration(difficulty) * 2;
            numbers = randomDistinctIntegers(count, 1, bound);
        }
        return numbers;
    }


    private Question prepareQuestion(TaskType type, TaskDifficultyLevel difficultyLevel, NumberTaskType typeValue, int[] numbers) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == NumberTaskType.GCD) {
            question.setTextContentPolish("Największy wspólny dzielnik liczb " + numbersToString(numbers, "i") + " wynosi");
            question.setTextContentEnglish("The greatest common divisor of numbers" + numbersToString(numbers, "and") + " is");
        }
        if (typeValue == NumberTaskType.LCM) {
            question.setTextContentPolish("Najmniejsza wspólna wielokrotność liczb " + numbersToString(numbers, "i") + " to");
            question.setTextContentEnglish("The least common multiple of numbers " + numbersToString(numbers, "and") + " is");
        }
        return question;
    }

    private List<Answer> prepareAnswers(NumberTaskType typeValue, int[] numbers, int answersCount) {
        int correctResult = 0;
        if (typeValue == NumberTaskType.GCD) {
            correctResult = gcd(numbers);
        }
        if (typeValue == NumberTaskType.LCM) {
            correctResult = lcm(numbers);
        }
        Answer correctAnswer = new Answer(true);
        correctAnswer.setTextContent("" + correctResult);

        List<Answer> wrongAnswers = new ArrayList<>();
        List<Integer> wrongResults = new ArrayList<>();
        while (wrongAnswers.size() < answersCount - 1) {
            int wrongResult = correctResult + randomInteger(-correctResult + 1, 20);
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

    private static int gcd(int x, int y) {
        return (y == 0) ? x : gcd(y, x % y);
    }

    public static int gcd(int... numbers) {
        return Arrays.stream(numbers).reduce(0, (x, y) -> gcd(x, y));
    }

    public static int lcm(int... numbers) {
        return Arrays.stream(numbers).reduce(1, (x, y) -> x * (y / gcd(x, y)));
    }

}
