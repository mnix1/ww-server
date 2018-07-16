package com.ww.service;

import com.ww.model.constant.Category;
import com.ww.model.dto.task.QuestionDTO;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.repository.rival.task.AnswerRepository;
import com.ww.repository.rival.task.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    Question findQuestion(Category category) {
        List<Question> questions = questionRepository.findAllByCategory(category);
        return questions.get(new SecureRandom().nextInt(questions.size()));
    }

    Boolean isCorrectAnswer(Long answerId) {
        // if not found return false
        return answerRepository.findById(answerId).orElseGet(() -> Answer.FALSE_EMPTY_ANSWER).getCorrect();
    }

}
