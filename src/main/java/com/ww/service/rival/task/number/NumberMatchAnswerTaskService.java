package com.ww.service.rival.task.number;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.NumberType;
import com.ww.model.constant.rival.task.type.NumberTaskType;
import com.ww.model.container.rival.task.NumberValues;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.AnswerHelper.difficultyCalibration;
import static com.ww.helper.AnswerHelper.isValueDistanceEnough;
import static com.ww.helper.RandomHelper.*;

@Service
public class NumberMatchAnswerTaskService {

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, NumberTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = Math.max(3, DifficultyLevel.answersCount(remainedDifficulty));
        List<NumberValues> numberValues = prepareNumbers(answersCount, remainedDifficulty);
        Question question = prepareQuestion(type, difficultyLevel, typeValue);
        List<Answer> answers = prepareAnswers(typeValue, numberValues);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private List<NumberType> difficultyRelatedNumberTypes(int count, int difficulty) {
        List<NumberType> types = new ArrayList<>(count);
        int difficultyCalibration = difficultyCalibration(difficulty);
        for (int i = 0; i < count; i++) {
            if (difficultyCalibration < 2) {
                types.add(NumberType.INTEGER);
                types.add(NumberType.DECIMAL);
            } else {
                types.add(NumberType.FRACTION);
                types.add(NumberType.DECIMAL);
            }
        }
        return types;
    }

    private List<NumberValues> prepareNumbers(int count, int difficulty) {
        List<Double> values = new ArrayList<>(count);
        List<NumberType> types = difficultyRelatedNumberTypes(count, difficulty);
        List<NumberValues> numbers = new ArrayList<>(count);
        while (numbers.size() < count) {
            NumberValues numberValues = generateRandomNumber(randomElement(types), difficulty);
            if (isValueDistanceEnough(numberValues.getValue(), values)) {
                numbers.add(numberValues);
                values.add(numberValues.getValue());
            }
        }
        return numbers;
    }

    private NumberValues generateRandomNumber(NumberType type, int difficulty) {
        if (type == NumberType.INTEGER) {
            return generateRandomInteger(difficulty);
        }
        if (type == NumberType.DECIMAL) {
            return generateRandomDecimal(difficulty);
        }
        if (type == NumberType.FRACTION) {
            return generateRandomFraction(difficulty);
        }
        return generateRandomInteger(difficulty);
    }

    private NumberValues generateRandomFraction(int difficulty) {
        int upBound = 17 + difficultyCalibration(difficulty) * 3;
        int downBound = 1;
        int numerator = randomInteger(downBound, upBound);
        int denominator = numerator;
        while (denominator == numerator) {
            denominator = randomInteger(2, upBound);
        }
        String stringValue = numerator + "/" + denominator;
        double value = numerator * 1.0 / denominator;
        return new NumberValues(value, stringValue);
    }

    private NumberValues generateRandomDecimal(int difficulty) {
        double upBound = 9 - difficultyCalibration(difficulty);
        double downBound = 0.01;
        double number = randomDouble(downBound, upBound);
        BigDecimal bigDecimal = new BigDecimal(number);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        if (bigDecimal.equals(bigDecimal.setScale(0, RoundingMode.HALF_UP))) {
            bigDecimal = new BigDecimal(bigDecimal.doubleValue() + 0.01);
        }
        number = bigDecimal.doubleValue();
        return new NumberValues(number, number + "");
    }

    private NumberValues generateRandomInteger(int difficulty) {
        int upBound = 9 + difficultyCalibration(difficulty);
        int downBound = 1;
        int number = randomInteger(downBound, upBound);
        return new NumberValues((double) number, number + "");
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, NumberTaskType typeValue) {
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

    private List<Answer> prepareAnswers(NumberTaskType typeValue, List<NumberValues> numbers) {
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
        NumberValues correctNumber = numbers.get(correctIndex);
        return numbers.stream().map(numberValues -> {
            Answer answer = new Answer(correctNumber == numberValues);
            answer.setTextContent(numberValues.getStringValue());
            return answer;
        }).collect(Collectors.toList());
    }

}
