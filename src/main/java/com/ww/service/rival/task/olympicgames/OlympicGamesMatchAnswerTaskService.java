package com.ww.service.rival.task.olympicgames;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.ElementTaskType;
import com.ww.model.constant.rival.task.type.OlympicGamesTaskType;
import com.ww.model.entity.rival.task.*;
import com.ww.repository.rival.task.category.OlympicMedalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

//@Service
//public class OlympicGamesMatchAnswerTaskService {
//
//    @Autowired
//    private OlympicMedalRepository olympicMedalRepository;
//
//    public Question generate(TaskType type, DifficultyLevel difficultyLevel, OlympicGamesTaskType typeValue) {
//        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
//        int answersCount = DifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
//
//        Question question = prepareQuestion(type, difficultyLevel, typeValue);
//        List<Answer> answers = prepareAnswers(typeValue, elements);
//        question.setAnswers(new HashSet<>(answers));
//        return question;
//    }
//
//    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, OlympicGamesTaskType typeValue) {
//        Question question = new Question(type, difficultyLevel);
//        if (typeValue == OlympicGamesTaskType.MAX_COUNTRY) {
//            question.setTextContentPolish("Reprezentaci którego kraju zdobyli najwięcej medali?");
////            question.setTextContentEnglish("Which of the elements has the highest atomic mass?");
//        }
//        return question;
//    }
//
//}
