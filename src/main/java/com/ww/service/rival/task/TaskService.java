package com.ww.service.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.ProfileQuestion;
import com.ww.model.entity.rival.task.Question;
import com.ww.repository.rival.task.AnswerRepository;
import com.ww.repository.rival.task.ProfileQuestionRepository;
import com.ww.repository.rival.task.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;
import static com.ww.helper.RandomHelper.randomInteger;

@Service
public class TaskService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ProfileQuestionRepository profileQuestionRepository;

    @Autowired
    private TaskGenerateService taskGenerateService;

    public Question prepareNotUsedQuestion(Category category, Long profileId) {
        Question question = null;
        if (randomInteger(1, 4) == 4) {
            List<Question> questions = questionRepository.findAllByCategory(category);
            question = randomElement(questions);
        }
        if (question == null || isProfileUsedQuestion(profileId, question.getId())) {
            Question generatedQuestion = taskGenerateService.generate(category);
            if (generatedQuestion != null) {
                save(generatedQuestion);
                question = generatedQuestion;
            }
        }
        return question;
    }

    public boolean isProfileUsedQuestion(Long profileId, Long questionId) {
        return profileQuestionRepository.findByProfileIdAndQuestionId(profileId, questionId) != null;
    }

    public void saveProfileUsedQuestion(Long profileId, Long questionId) {
        profileQuestionRepository.save(new ProfileQuestion(profileId, questionId));
    }

    Boolean isCorrectAnswer(Long answerId) {
        // if not found return false
        return answerRepository.findById(answerId).orElseGet(() -> Answer.FALSE_EMPTY_ANSWER).getCorrect();
    }

    public void save(Question question) {
        questionRepository.save(question);
        question.getAnswers().forEach(answer -> answer.setQuestion(question));
        answerRepository.saveAll(question.getAnswers());
    }

}
