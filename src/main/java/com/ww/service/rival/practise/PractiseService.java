package com.ww.service.rival.practise;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.practise.PractiseResult;
import com.ww.model.dto.rival.task.PractiseDTO;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.practise.Practise;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.repository.outside.rival.practise.PractiseRepository;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PractiseService {

    private final PractiseRepository practiseRepository;
    private final TaskService taskService;
    private final TaskRendererService taskRendererService;
    private final ProfileService profileService;

    public PractiseDTO start(Category category, DifficultyLevel difficultyLevel) {
        Language language = profileService.getProfileLanguage();
        Question question = taskService.generateQuestion(category, difficultyLevel, language);
        TaskDTO taskDTO = taskRendererService.prepareTaskDTO(question);
        Practise practise = create(question);
        return new PractiseDTO(practise, taskDTO);
    }

    private Practise create(Question question) {
        Practise practise = new Practise();
        practise.setProfileId(profileService.getProfileId());
        practise.setQuestion(question);
        practiseRepository.save(practise);
        return practise;
    }

    public Map end(Long practiseId, Long answerId) {
        if (practiseId == null || answerId == null) {
            return null;
        }
        Date closeDate = new Date();
        Practise practise = practiseRepository.findById(practiseId).orElseThrow(() -> new IllegalArgumentException("Not found practise with id " + practiseId));
        Boolean isPractiseForActualSessionProfile = profileService.getProfileId().equals(practise.getProfileId());
        if (!isPractiseForActualSessionProfile || !practise.isOpen()) {
            return null;
        }
        Question question = practise.getQuestion();
        Boolean answerForQuestion = question.getAnswers().stream().map(answer -> answer.getId()).collect(Collectors.toList()).contains(answerId);
        if (!answerForQuestion) {
            updatePractiseResult(practise, false, closeDate);
            return null;
        }
        Answer correctAnswer = question.findCorrectAnswer();
        boolean result = correctAnswer.getId().equals(answerId);
        updatePractiseResult(practise, result, closeDate);
        Map<String, Object> model = new HashMap<>();
        model.put("correctAnswerId", correctAnswer.getId());
        model.put("answerInterval", practise.inProgressInterval());
        return model;
    }

    public void updatePractiseResult(Practise practise, Boolean result, Date closeDate) {
        practise.setCloseDate(closeDate);
        practise.setResult(PractiseResult.fromBoolean(result));
        practiseRepository.save(practise);
    }

}
