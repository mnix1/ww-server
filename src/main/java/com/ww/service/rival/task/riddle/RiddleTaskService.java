package com.ww.service.rival.task.riddle;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.RiddleTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Clipart;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.category.ClipartRepository;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiddleTaskService {

    @Autowired
    ClipartRepository clipartRepository;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        RiddleTaskType typeValue = RiddleTaskType.valueOf(type.getValue());
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        List<Clipart> cliparts = prepareCliparts(answersCount);
        Clipart correctClipart = cliparts.get(0);
        List<Clipart> wrongCliparts = cliparts.subList(1, cliparts.size());
        Question question = prepareQuestion(type, difficultyLevel, typeValue, wrongCliparts);
        List<Answer> answers = prepareAnswers(typeValue, correctClipart, wrongCliparts);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private List<Clipart> prepareCliparts(int answersCount) {
        List<Clipart> allCliparts = clipartRepository.findAll();
        Collections.shuffle(allCliparts);
        return allCliparts.stream().limit(answersCount).collect(Collectors.toList());
    }

    private Question prepareQuestion(TaskType type, TaskDifficultyLevel difficultyLevel, RiddleTaskType typeValue, List<Clipart> wrongCliparts) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == RiddleTaskType.MISSING) {
            question.setImageContent(StringUtils.join(wrongCliparts.stream().map(Clipart::getResourcePath).collect(Collectors.toList()), ","));
            question.setTextContentPolish("Czego brakuje na obrazku?");
            question.setTextContentEnglish("What is missing in the picture?");
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
        if (typeValue == RiddleTaskType.MISSING) {
            answer.setTextContentPolish(clipart.getNamePolish());
            answer.setTextContentEnglish(clipart.getNameEnglish());
        }
    }

}
