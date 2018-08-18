package com.ww.service.rival.task.number;

import com.ww.model.constant.rival.task.NumberType;
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

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRendererService taskRendererService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel, NumberTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        List<NumberObject> numberObjects = prepareNumbers(answersCount, remainedDifficulty);
        Question question = prepareQuestion(type, difficultyLevel, typeValue);
        List<Answer> answers = prepareAnswers(typeValue, numberObjects);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private List<NumberType> difficultyRelatedNumberTypes(int count, int difficulty) {
        List<NumberType> types = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            if (difficulty > 40) {
                types.add(NumberType.FRACTION);
            }
            if (difficulty > 20) {
                types.add(NumberType.DECIMAL);
            }
            types.add(NumberType.INTEGER);
        }
        return types;
    }

    private List<NumberObject> prepareNumbers(int count, int difficulty) {
        List<Double> values = new ArrayList<>(count);
        List<NumberType> types = difficultyRelatedNumberTypes(count, difficulty);
        List<NumberObject> numbers = new ArrayList<>(count);
        while (numbers.size() < count) {
            NumberObject numberObject = generateRandomNumber(randomElement(types), difficulty);
            if (isValueDistanceEnough(numberObject.getValue(), values)) {
                numbers.add(numberObject);
                values.add(numberObject.getValue());
            }
        }
        return numbers;
    }

    private NumberObject generateRandomNumber(NumberType type, int difficulty) {
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

    private NumberObject generateRandomFraction(int difficulty) {
        int upBound = 9 + difficultyCalibration(difficulty) * 11;
        int downBound = difficultyCalibration(difficulty) < 3 ? 1 : -upBound;
        int numerator = randomInteger(downBound, upBound);
        int denominator = randomInteger(2, upBound);
        String html = "<div>" + numerator + "</div>" + "<hr style=\"width:12px;margin:0 auto;\"/>" + " <div> " + denominator + " </div > ";
        double value = numerator * 1.0 / denominator;
        return new NumberObject(value, html);
    }

    private NumberObject generateRandomDecimal(int difficulty) {
        int upBound = 9 + difficultyCalibration(difficulty) * 11;
        int downBound = difficultyCalibration(difficulty) < 3 ? 1 : -upBound;
        double number = randomDouble(downBound, upBound);
        BigDecimal bigDecimal = new BigDecimal(number);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        if (bigDecimal.equals(bigDecimal.setScale(0, RoundingMode.HALF_UP))) {
            bigDecimal = new BigDecimal(bigDecimal.doubleValue() + 0.01);
        }
        number = bigDecimal.doubleValue();
        String html = number + "";
        return new NumberObject(number, html);
    }

    private NumberObject generateRandomInteger(int difficulty) {
        int upBound = 9 + difficultyCalibration(difficulty) * 11;
        int downBound = difficultyCalibration(difficulty) < 3 ? 1 : -upBound;
        int number = randomInteger(downBound, upBound);
        String html = number + "";
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
