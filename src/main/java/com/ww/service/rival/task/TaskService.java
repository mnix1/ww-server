package com.ww.service.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.ProfileQuestion;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import com.ww.repository.rival.task.AnswerRepository;
import com.ww.repository.rival.task.ProfileQuestionRepository;
import com.ww.repository.rival.task.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Question> generateQuestions(List<Category> categories) {
        List<Question> questions = categories.stream()
                .map(category -> taskGenerateService.generate(category))
                .collect(Collectors.toList());
        save(questions);
        return questions;
    }


    public Question prepareNotUsedQuestion(Category category, Long profileId) {
        Question question = null;
        if (randomInteger(1, 4) == 4) {
            List<Question> questions = questionRepository.findAllByCategory(category);
            question = randomElement(questions);
        }
        if (question == null || isProfileUsedQuestion(profileId, question.getId())) {
            question = taskGenerateService.generate(category);
            save(question);
        }
        return question;
    }

    public boolean isProfileUsedQuestion(Long profileId, Long questionId) {
        return profileQuestionRepository.findByProfile_IdAndQuestion_Id(profileId, questionId) != null;
    }

    public void saveProfileUsedQuestion(Profile profile, Question question) {
        profileQuestionRepository.save(new ProfileQuestion(profile, question));
    }

    public void saveProfilesUsedQuestions(List<Profile> profiles, List<Question> questions) {
        List<ProfileQuestion> profileQuestions = new ArrayList<>();
        profiles.forEach(profile -> {
            questions.forEach(question -> {
                profileQuestions.add(new ProfileQuestion(profile, question));
            });
        });
        profileQuestionRepository.saveAll(profileQuestions);
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

    public void save(List<Question> questions) {
        questionRepository.saveAll(questions);
        questions.forEach(question -> {
            question.getAnswers().forEach(answer -> answer.setQuestion(question));
            answerRepository.saveAll(question.getAnswers());
        });
    }

}
