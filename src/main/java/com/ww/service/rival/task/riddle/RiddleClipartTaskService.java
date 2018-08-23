package com.ww.service.rival.task.riddle;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.RiddleTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Clipart;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.category.ClipartRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiddleClipartTaskService {

    @Autowired
    ClipartRepository clipartRepository;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel, RiddleTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        List<Clipart> cliparts = prepareCliparts(answersCount);
        Clipart correctClipart = cliparts.get(0);
        List<Clipart> questionCliparts = prepareQuestionCliparts(typeValue, cliparts, answersCount);
        List<Clipart> wrongCliparts = prepareWrongCliparts(typeValue, cliparts, answersCount);
        Question question = prepareQuestion(type, difficultyLevel, typeValue, questionCliparts);
        List<Answer> answers = prepareAnswers(typeValue, correctClipart, wrongCliparts);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private List<Clipart> prepareCliparts(int answersCount) {
        List<Clipart> allCliparts = clipartRepository.findAll();
        Collections.shuffle(allCliparts);
        return allCliparts.stream().limit(answersCount * 2).collect(Collectors.toList());
    }

    private List<Clipart> prepareQuestionCliparts(RiddleTaskType typeValue, List<Clipart> cliparts, int answersCount) {
        if (typeValue == RiddleTaskType.MISSING_CLIPART) {
            return cliparts.subList(1, answersCount);
        }
        if (typeValue == RiddleTaskType.FIND_CLIPART) {
            return cliparts.subList(0, answersCount);
        }
        throw new IllegalArgumentException("No RiddleTaskType handled");
    }

    private List<Clipart> prepareWrongCliparts(RiddleTaskType typeValue, List<Clipart> cliparts, int answersCount) {
        if (typeValue == RiddleTaskType.MISSING_CLIPART) {
            return cliparts.subList(1, answersCount);
        }
        if (typeValue == RiddleTaskType.FIND_CLIPART) {
            return cliparts.subList(answersCount + 1, cliparts.size());
        }
        throw new IllegalArgumentException("No RiddleTaskType handled");
    }

    private Question prepareQuestion(TaskType type, TaskDifficultyLevel difficultyLevel, RiddleTaskType typeValue, List<Clipart> questionCliparts) {
        Question question = new Question(type, difficultyLevel);
        question.setImageContent(StringUtils.join(questionCliparts.stream().map(Clipart::getPngResourcePath).collect(Collectors.toList()), ","));
        if (typeValue == RiddleTaskType.MISSING_CLIPART) {
            question.setTextContentPolish("Czego brakuje na obrazku?");
            question.setTextContentEnglish("What is missing in the picture?");
        }
        if (typeValue == RiddleTaskType.FIND_CLIPART) {
            question.setTextContentPolish("Co znajduje się na obrazku?");
            question.setTextContentEnglish("What is in the picture?");
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
        if (typeValue == RiddleTaskType.MISSING_CLIPART
                || typeValue == RiddleTaskType.FIND_CLIPART) {
            answer.setTextContentPolish(clipart.getNamePolish());
            answer.setTextContentEnglish(clipart.getNameEnglish());
        }
    }

}
