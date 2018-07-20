package com.ww.service.rival.task.geography;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.GeographyTaskType;
import com.ww.model.dto.task.QuestionDTO;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.GeographyCountry;
import com.ww.model.entity.rival.task.Question;
import com.ww.repository.rival.task.category.GeographyCountryRepository;
import com.ww.service.rival.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class GeographyTaskService {

    @Autowired
    TaskService taskService;

    @Autowired
    GeographyCountryRepository geographyCountryRepository;

    public QuestionDTO generate(GeographyTaskType type) {
        type = GeographyTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME;

        List<GeographyCountry> allCountries = geographyCountryRepository.findAll();
        GeographyCountry correctCountry = randomElement(allCountries);

        Question question = prepareQuestion(type, correctCountry);
        List<Answer> answers = prepareAnswers(type, correctCountry, allCountries);
        taskService.addTask(question, answers);
        return new QuestionDTO(question);
    }

    private Question prepareQuestion(GeographyTaskType type, GeographyCountry country) {
        Question question = new Question();
        question.setCategory(Category.GEOGRAPHY);
        if (type == GeographyTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME) {
            question.setContentPolish("Stolicą państwa " + country.getNamePolish() + " jest:");
            question.setContentEnglish("The capital of " + country.getNamePolish() + " is:");
        }
        return question;
    }

    private List<Answer> prepareAnswers(GeographyTaskType type, GeographyCountry correctCountry, List<GeographyCountry> allCountries) {
        Answer correctAnswer = new Answer(true);
        fillAnswerContent(type, correctAnswer, correctCountry);

        List<Answer> wrongAnswers = new ArrayList<>();
        List<String> answerContents = new ArrayList<>();
        answerContents.add(correctAnswer.getContentEnglish());
        while (wrongAnswers.size() < 3) {
            GeographyCountry wrongCountry = randomElement(allCountries);
            Answer wrongAnswer = new Answer(false);
            fillAnswerContent(type, wrongAnswer, wrongCountry);
            if (!answerContents.contains(wrongAnswer.getContentEnglish())) {
                wrongAnswers.add(wrongAnswer);
                answerContents.add(wrongAnswer.getContentEnglish());
            }
        }
        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.addAll(wrongAnswers);
        return answers;
    }

    private void fillAnswerContent(GeographyTaskType type, Answer answer, GeographyCountry country) {
        if (type == GeographyTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME) {
            answer.setContentPolish(country.getCapitalPolish());
            answer.setContentEnglish(country.getCapitalEnglish());
        }
    }


}
