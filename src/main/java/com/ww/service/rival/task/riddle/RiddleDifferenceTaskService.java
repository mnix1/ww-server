package com.ww.service.rival.task.riddle;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.RiddleTaskType;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.inside.task.Clipart;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import com.ww.repository.inside.category.ClipartRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiddleDifferenceTaskService {

    @Autowired
    ClipartRepository clipartRepository;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, RiddleTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = Math.min(7,DifficultyLevel.answersCount(remainedDifficulty));
        List<Clipart> cliparts = prepareCliparts(answersCount);
        Clipart correctClipart = cliparts.get(0);
        List<Clipart> questionClipartsLeft = prepareQuestionClipartsLeft(typeValue, cliparts, answersCount);
        List<Clipart> questionClipartsRight = prepareQuestionClipartsRight(typeValue, cliparts, answersCount);
        List<Clipart> wrongCliparts = prepareWrongCliparts(typeValue, cliparts, answersCount);
        Collections.shuffle(questionClipartsLeft);
        Collections.shuffle(questionClipartsRight);
        Question question = prepareQuestion(type, difficultyLevel, typeValue, questionClipartsLeft, questionClipartsRight);
        List<Answer> answers = prepareAnswers(typeValue, correctClipart, wrongCliparts);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private List<Clipart> prepareCliparts(int answersCount) {
        List<Clipart> allCliparts = clipartRepository.findAll();
        Collections.shuffle(allCliparts);
        return allCliparts.stream().limit(answersCount + 1).collect(Collectors.toList());
    }

    private List<Clipart> prepareQuestionClipartsLeft(RiddleTaskType typeValue, List<Clipart> cliparts, int answersCount) {
        if (typeValue == RiddleTaskType.FIND_DIFFERENCE_RIGHT_MISSING) {
            return new ArrayList<>(cliparts.subList(0, answersCount - 1));
        }
        if (typeValue == RiddleTaskType.FIND_DIFFERENCE_LEFT_MISSING) {
            return new ArrayList<>(cliparts.subList(1, answersCount));
        }
        throw new IllegalArgumentException("No RiddleTaskType handled");
    }

    private List<Clipart> prepareQuestionClipartsRight(RiddleTaskType typeValue, List<Clipart> cliparts, int answersCount) {
        if (typeValue == RiddleTaskType.FIND_DIFFERENCE_LEFT_MISSING) {
            return new ArrayList<>(cliparts.subList(0, answersCount - 1));
        }
        if (typeValue == RiddleTaskType.FIND_DIFFERENCE_RIGHT_MISSING) {
            return new ArrayList<>(cliparts.subList(1, answersCount));
        }
        throw new IllegalArgumentException("No RiddleTaskType handled");
    }

    private List<Clipart> prepareWrongCliparts(RiddleTaskType typeValue, List<Clipart> cliparts, int answersCount) {
        return new ArrayList<>(cliparts.subList(1, answersCount));
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, RiddleTaskType typeValue, List<Clipart> questionClipartsLeft, List<Clipart> questionClipartsRight) {
        Question question = new Question(type, difficultyLevel);
        String left = StringUtils.join(questionClipartsLeft.stream().map(Clipart::getPngResourcePath).collect(Collectors.toList()), ",");
        String right = StringUtils.join(questionClipartsRight.stream().map(Clipart::getPngResourcePath).collect(Collectors.toList()), ",");
        question.setImageContent(left + ";" + right);
        if (typeValue == RiddleTaskType.FIND_DIFFERENCE_LEFT_MISSING) {
            question.setTextContentPolish("Jest na prawym obrazku, a na lewym nie");
            question.setTextContentEnglish("It's on the right picture and not on the left");
        }
        if (typeValue == RiddleTaskType.FIND_DIFFERENCE_RIGHT_MISSING) {
            question.setTextContentPolish("Jest na lewym obrazku, a na prawym nie");
            question.setTextContentEnglish("It's on the left picture and not on the right");
        }
        return question;
    }

    private List<Answer> prepareAnswers(RiddleTaskType typeValue, Clipart correctClipart, List<Clipart> wrongCliparts) {
        Answer correctAnswer = new Answer(true);
        fillAnswerContent(typeValue, correctAnswer, correctClipart);
        List<Answer> wrongAnswers = wrongCliparts.stream().map(clipart -> {
            Answer answer = new Answer(false);
            fillAnswerContent(typeValue, answer, clipart);
            return answer;
        }).collect(Collectors.toList());
        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.addAll(wrongAnswers);
        return answers;
    }

    private void fillAnswerContent(RiddleTaskType typeValue, Answer answer, Clipart clipart) {
        answer.setTextContentPolish(clipart.getNamePolish());
        answer.setTextContentEnglish(clipart.getNameEnglish());
    }

}
