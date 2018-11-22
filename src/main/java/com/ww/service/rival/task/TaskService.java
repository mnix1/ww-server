package com.ww.service.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.repository.outside.rival.task.AnswerRepository;
import com.ww.repository.outside.rival.task.QuestionRepository;
import com.ww.repository.outside.rival.task.TaskWisdomAttributeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final TaskGenerateService taskGenerateService;
    private final TaskWisdomAttributeRepository taskWisdomAttributeRepository;

    public Question generateQuestion(Category category, DifficultyLevel difficultyLevel, Language language) {
        Question question = taskGenerateService.generate(category, difficultyLevel, language);
        save(question);
        return question;
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

    public void initTaskWisdomAttributes(Question question) {
        question.getType().setWisdomAttributes(taskWisdomAttributeRepository.findAllByType(question.getType()));
    }


}
