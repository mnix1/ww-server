package com.ww.service.rival.task.number;

import com.ww.helper.PrimeHelper;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.NumberTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ww.helper.AnswerHelper.difficultyCalibration;
import static com.ww.helper.AnswerHelper.numbersToString;
import static com.ww.helper.RandomHelper.*;

@Service
public class NumberOneCorrectTaskService {

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, NumberTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = DifficultyLevel.answersCount(remainedDifficulty);
        int[] numbers = prepareNumbers(typeValue, remainedDifficulty, answersCount);
        Question question = prepareQuestion(type, difficultyLevel, typeValue, numbers);
        List<Answer> answers = prepareAnswers(typeValue, numbers, answersCount);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private int[] prepareNumbers(NumberTaskType typeValue, int difficulty, int answersCount) {
        int[] numbers = null;
        if (typeValue == NumberTaskType.GCD) {
            int gcd = randomInteger(2 + difficultyCalibration(difficulty) / 3, difficultyCalibration(difficulty) + 9);
            int count = Math.min(3, difficultyCalibration(difficulty) / 2 + 2);
            int bound = 9 + difficultyCalibration(difficulty);
            numbers = randomDistinctIntegers(count, 2, bound);
            for (int i = 0; i < numbers.length; i++) {
                numbers[i] = gcd * numbers[i];
            }
        }
        if (typeValue == NumberTaskType.LCM) {
            int count = Math.min(3, difficultyCalibration(difficulty) / 2 + 2);
            int bound = 9 + difficultyCalibration(difficulty) * 2;
            numbers = randomDistinctIntegers(count, 2 + difficultyCalibration(difficulty), bound);
        }
        if (typeValue == NumberTaskType.PRIME) {
            numbers = new int[answersCount];
            numbers[0] = PrimeHelper.getPrimeFrom(difficultyCalibration(difficulty) * (11 + randomInteger(1, 9)) + randomInteger(1, difficultyCalibration(difficulty) * 100 + 99));
            Set<Integer> numbersSet = new HashSet<>();
            numbersSet.add(numbers[0]);
            int index = 1;
            while (index < answersCount) {
                int offset = randomInteger(-numbers[0] + 1, 30);
                if (offset % 2 != 0) {
                    offset += 1;
                }
                int number = numbers[0] + offset;
                if (!PrimeHelper.isPrime(number) && !numbersSet.contains(number)) {
                    numbersSet.add(number);
                    numbers[index++] = number;
                }
            }
        }
        return numbers;
    }


    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, NumberTaskType typeValue, int[] numbers) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == NumberTaskType.GCD) {
            question.setTextContentPolish("Największy wspólny dzielnik liczb " + numbersToString(numbers, "i") + " wynosi");
            question.setTextContentEnglish("The greatest common divisor of numbers" + numbersToString(numbers, "and") + " is");
        }
        if (typeValue == NumberTaskType.LCM) {
            question.setTextContentPolish("Najmniejsza wspólna wielokrotność liczb " + numbersToString(numbers, "i") + " to");
            question.setTextContentEnglish("The least common multiple of numbers " + numbersToString(numbers, "and") + " is");
        }
        if (typeValue == NumberTaskType.PRIME) {
            question.setTextContentPolish("Która liczba jest liczbą pierwszą?");
            question.setTextContentEnglish("Which number is the prime number?");
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
        if (typeValue == NumberTaskType.PRIME) {
            correctResult = numbers[0];
        }
        Answer correctAnswer = new Answer(true);
        correctAnswer.setTextContent("" + correctResult);

        List<Answer> wrongAnswers = new ArrayList<>();
        List<Integer> wrongResults = new ArrayList<>();
        while (wrongAnswers.size() < answersCount - 1) {
            int wrongResult;
            if (typeValue == NumberTaskType.PRIME) {
                wrongResult = numbers[wrongAnswers.size() + 1];
            } else {
                wrongResult = correctResult + randomInteger(-correctResult + 1, 20);
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
