package com.ww.service.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.repository.rival.task.AnswerRepository;
import com.ww.repository.rival.task.QuestionRepository;
import com.ww.repository.rival.task.TaskWisdomAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private TaskGenerateService taskGenerateService;

    @Autowired
    private TaskWisdomAttributeRepository taskWisdomAttributeRepository;

    public List<Question> generateQuestions(List<Category> categories) {
        return generateQuestions(categories, DifficultyLevel.random());
    }

    public List<Question> generateQuestions(List<Category> categories, DifficultyLevel difficultyLevel) {
        List<Question> questions = categories.stream()
                .map(category -> taskGenerateService.generate(category, difficultyLevel))
                .collect(Collectors.toList());
        save(questions);
        return questions;
    }

    public Question generateQuestion(Category category, DifficultyLevel difficultyLevel) {
        Question question = taskGenerateService.generate(category, difficultyLevel);
        save(question);
        return question;
    }

    public Answer findCorrectAnswer(Question question) {
        return question.getAnswers().stream().filter(answer -> answer.getCorrect()).findFirst().orElseThrow(() -> new IllegalArgumentException("No correct answers"));
    }

    public void save(Question question) {
        questionRepository.save(question);
        question.getAnswers().forEach(answer -> answer.setQuestion(question));
        answerRepository.saveAll(question.getAnswers());
    }

    public void save(List<Question> questions) {
        questionRepository.saveAll(questions);
        List<Answer> answers = new ArrayList<>();
        questions.forEach(question -> {
            question.getAnswers().forEach(answer -> answer.setQuestion(question));
            answers.addAll(question.getAnswers());
        });
        answerRepository.saveAll(answers);
    }

    public void initTaskWisdomAtributes(Question question) {
        question.getType().setWisdomAttributes(taskWisdomAttributeRepository.findAllByType(question.getType()));
    }


}
