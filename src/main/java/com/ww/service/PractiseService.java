package com.ww.service;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.practise.PractiseResult;
import com.ww.model.dto.task.PractiseDTO;
import com.ww.model.entity.rival.practise.Practise;
import com.ww.model.entity.rival.practise.PractiseQuestion;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.repository.rival.practise.PractiseQuestionRepository;
import com.ww.repository.rival.practise.PractiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PractiseService {

    @Autowired
    private PractiseRepository practiseRepository;

    @Autowired
    private PractiseQuestionRepository practiseQuestionRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private SessionService sessionService;

    public PractiseDTO start(Category category) {
        Question question = taskService.findQuestion(category);
        Practise practise = create(Arrays.asList(question));
        return new PractiseDTO(practise, question);
    }

    private Practise create(List<Question> questions) {
        Practise practise = new Practise();
        practise.setProfileId(sessionService.getProfileId());
        practiseRepository.save(practise);
        List<PractiseQuestion> practiseQuestions = questions.stream().map(question -> {
            PractiseQuestion practiseQuestion = new PractiseQuestion();
            practiseQuestion.setPractise(practise);
            practiseQuestion.setQuestion(question);
            return practiseQuestion;
        }).collect(Collectors.toList());
        practiseQuestionRepository.saveAll(practiseQuestions);
        return practise;
    }

    public Long end(Long practiseId, Long answerId) {
        if (practiseId == null || answerId == null) {
            return null;
        }
        try {
            Date closeDate = new Date();
            Practise practise = practiseRepository.findById(practiseId).orElseThrow(() -> new Exception("Not found practise with id " + practiseId));
            Boolean isPractiseForActualSessionProfile = sessionService.getProfileId().equals(practise.getProfileId());
            if (!isPractiseForActualSessionProfile || !practise.isOpen()) {
                return null;
            }
            Question question = practise.getQuestions().stream().findFirst().orElseThrow(() -> new Exception("No questions for practise")).getQuestion();
            Boolean answerForQuestion = question.getAnswers().stream().map(answer -> answer.getId()).collect(Collectors.toList()).contains(answerId);
            if (!answerForQuestion) {
                updatePractiseResult(practise, false, closeDate);
                return null;
            }
            Answer correctAnswer = question.getAnswers().stream().filter(answer -> answer.getCorrect()).findFirst().orElseThrow(() -> new Exception("No correct answers"));
            boolean result = correctAnswer.getId().equals(answerId);
            updatePractiseResult(practise, result, closeDate);
            return correctAnswer.getId();
        } catch (Exception e) {
            //log
        }
        return null;
    }

    public void updatePractiseResult(Practise practise, Boolean result, Date closeDate) {
        practise.setCloseDate(closeDate);
        practise.setResult(PractiseResult.fromBoolean(result));
        practiseRepository.save(practise);
    }

}
