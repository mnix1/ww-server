package com.ww.service.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.practise.PractiseResult;
import com.ww.model.dto.task.PractiseDTO;
import com.ww.model.dto.task.QuestionDTO;
import com.ww.model.entity.rival.practise.Practise;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import com.ww.repository.rival.practise.PractiseRepository;
import com.ww.service.SessionService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PractiseService {

    @Autowired
    private PractiseRepository practiseRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRendererService taskRendererService;

    @Autowired
    private SessionService sessionService;
    @Autowired
    private ProfileService profileService;

    public PractiseDTO start(Category category) {
        Profile profile = profileService.getProfileOnlyWithId();
        Question question = taskService.prepareNotUsedQuestion(category, profile.getId());
        taskService.saveProfileUsedQuestion(profile, question);
        QuestionDTO questionDTO = taskRendererService.prepareQuestionDTO(question);
        Practise practise = create(question);
        return new PractiseDTO(practise, questionDTO);
    }

    private Practise create(Question question) {
        Practise practise = new Practise();
        practise.setProfileId(sessionService.getProfileId());
        practise.setQuestion(question);
        practiseRepository.save(practise);
        return practise;
    }

    public Map end(Long practiseId, Long answerId) {
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
            Question question = practise.getQuestion();
            Boolean answerForQuestion = question.getAnswers().stream().map(answer -> answer.getId()).collect(Collectors.toList()).contains(answerId);
            if (!answerForQuestion) {
                updatePractiseResult(practise, false, closeDate);
                return null;
            }
            Answer correctAnswer = taskService.findCorrectAnswer(question);
            boolean result = correctAnswer.getId().equals(answerId);
            updatePractiseResult(practise, result, closeDate);
            Map<String,Object> model = new HashMap<>();
            model.put("correctAnswerId", correctAnswer.getId());
            model.put("answerInterval", practise.inProgressInterval());
            return model;
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
