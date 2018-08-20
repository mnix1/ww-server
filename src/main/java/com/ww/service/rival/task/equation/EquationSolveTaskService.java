package com.ww.service.rival.task.equation;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.EquationTaskType;
import com.ww.model.container.EquationObject;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.ww.helper.AnswerHelper.difficultyCalibration;
import static com.ww.helper.RandomHelper.randomInteger;

@Service
public class EquationSolveTaskService {

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel, EquationTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        int calibration = difficultyCalibration(remainedDifficulty);
        EquationObject equationObject = calibration > 2 ? prepareEquationType2(calibration) : prepareEquationType1(calibration);
        Question question = prepareQuestion(type, difficultyLevel, typeValue, equationObject);
        List<Answer> answers = prepareAnswers(typeValue, equationObject.getValue(), answersCount);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private EquationObject prepareEquationType1(int calibration) {
        int bound = calibration * 10 + 40;
        int value = randomInteger(-bound, bound);
        EquationObject eq = new EquationObject(value);
        int multiplier = randomInteger(-calibration, calibration * 2 + 2);
        if (multiplier == 0) {
            multiplier = randomInteger(1, calibration * 2 + 2);
        }
        eq.appendLeftSide(Math.abs(multiplier) > 1 ? multiplier + "*x" : "x");
        eq.addLeftSide(-value * multiplier);
        eq.appendRightSide("0");
        return eq;
    }

    private EquationObject prepareEquationType2(int calibration) {
        int bound = calibration * 10 + 40;
        int value = randomInteger(-bound, bound);
        EquationObject eq = new EquationObject(value);
        int multiplier = randomInteger(-calibration * 2 - 6, calibration * 2 + 6);
        if (Math.abs(multiplier) < 2) {
            multiplier = randomInteger(2, calibration * 2 + 6);
        }
        int leftConstant = randomInteger(2, calibration * 2 + 5);
        int leftMultiplier = randomInteger(2, calibration + 6);
        int rightMultiplier = multiplier - leftMultiplier;
        eq.appendLeftSide(leftMultiplier + "*x");
        eq.addLeftSide(leftConstant * multiplier);
        if (rightMultiplier != 0) {
            eq.appendRightSide(Math.abs(rightMultiplier) > 1 ? -rightMultiplier + "*x" : "x");
            eq.addRightSide((value + leftConstant) * multiplier);
        } else {
            eq.appendRightSide("" + (value + leftConstant) * multiplier);
        }

        return eq;
    }

    private Question prepareQuestion(TaskType type, TaskDifficultyLevel difficultyLevel, EquationTaskType typeValue, EquationObject equationObject) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == EquationTaskType.FIND_X) {
            question.setHtmlContent(equationObject.getEquation());
            question.setTextContentPolish("Jaką liczbą jest x?");
            question.setTextContentEnglish("What number is x?");
        }
        return question;
    }

    private List<Answer> prepareAnswers(EquationTaskType typeValue, int correctResult, int answersCount) {
        Answer correctAnswer = new Answer(true);
        correctAnswer.setTextContent("" + correctResult);

        List<Answer> wrongAnswers = new ArrayList<>();
        List<Integer> wrongResults = new ArrayList<>();
        while (wrongAnswers.size() < answersCount - 1) {
            int wrongResult = correctResult + randomInteger(-20, 20);
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
