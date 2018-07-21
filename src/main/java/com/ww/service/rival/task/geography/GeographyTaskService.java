package com.ww.service.rival.task.geography;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.GeographyTaskType;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.GeographyCountry;
import com.ww.model.entity.rival.task.Question;
import com.ww.repository.rival.task.category.GeographyCountryRepository;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class GeographyTaskService {

    @Autowired
    TaskService taskService;
    @Autowired
    TaskRendererService taskRendererService;

    @Autowired
    GeographyCountryRepository geographyCountryRepository;

    public Question generate(GeographyTaskType type) {
        List<GeographyCountry> allCountries = geographyCountryRepository.findAll();
        GeographyCountry correctCountry = randomElement(allCountries);

        Question question = prepareQuestion(type, correctCountry);
        List<Answer> answers = prepareAnswers(type, correctCountry, allCountries);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(GeographyTaskType type, GeographyCountry country) {
        Question question = new Question();
        question.setCategory(Category.GEOGRAPHY);
        if (type == GeographyTaskType.COUNTRY_NAME_FROM_CAPITAL_NAME) {
            question.setContentPolish("Wskaż państwo, którego stolica to " + country.getCapitalPolish());
            question.setContentEnglish("Indicate the state whose capital is " + country.getCapitalEnglish());
        }
        if (type == GeographyTaskType.COUNTRY_NAME_FROM_MAP) {
            question.setTaskRenderer(TaskRenderer.TEXT_IMAGE);
            question.setContentPolish(taskRendererService.prepareQuestionContentTextImage("Które państwo widoczne jest na mapie?", country.getMapResourcePath()));
            question.setContentEnglish(taskRendererService.prepareQuestionContentTextImage("Which country is visible on the map?", country.getMapResourcePath()));
        }
        if (type == GeographyTaskType.COUNTRY_NAME_FROM_FLAG) {
            question.setTaskRenderer(TaskRenderer.TEXT_IMAGE);
            question.setContentPolish(taskRendererService.prepareQuestionContentTextImage("Jest to flaga państwa", country.getFlagResourcePath()));
            question.setContentEnglish(taskRendererService.prepareQuestionContentTextImage("Which country's flag is it?", country.getFlagResourcePath()));
        }
        if (type == GeographyTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME) {
            question.setContentPolish("Stolicą państwa " + country.getNamePolish() + " jest");
            question.setContentEnglish("The capital of " + country.getNameEnglish() + " is");
        }
        if (type == GeographyTaskType.CAPITAL_NAME_FROM_MAP) {
            question.setTaskRenderer(TaskRenderer.TEXT_IMAGE);
            question.setContentPolish(taskRendererService.prepareQuestionContentTextImage("Stolica państwa widocznego na mapie to", country.getMapResourcePath()));
            question.setContentEnglish(taskRendererService.prepareQuestionContentTextImage("The state capital that can be seen on the map is", country.getMapResourcePath()));
        }
        if (type == GeographyTaskType.CAPITAL_NAME_FROM_FLAG) {
            question.setTaskRenderer(TaskRenderer.TEXT_IMAGE);
            question.setContentPolish(taskRendererService.prepareQuestionContentTextImage("Stolica państwa, którego flaga widoczna jest obok to", country.getFlagResourcePath()));
            question.setContentEnglish(taskRendererService.prepareQuestionContentTextImage("The capital of the country whose flag is visible next to it is", country.getFlagResourcePath()));
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
        if (type == GeographyTaskType.COUNTRY_NAME_FROM_CAPITAL_NAME
                || type == GeographyTaskType.COUNTRY_NAME_FROM_MAP
                || type == GeographyTaskType.COUNTRY_NAME_FROM_FLAG) {
            answer.setContentPolish(country.getNamePolish());
            answer.setContentEnglish(country.getNameEnglish());
        }
        if (type == GeographyTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME
                || type == GeographyTaskType.CAPITAL_NAME_FROM_MAP
                || type == GeographyTaskType.CAPITAL_NAME_FROM_FLAG) {
            answer.setContentPolish(country.getCapitalPolish());
            answer.setContentEnglish(country.getCapitalEnglish());
        }
    }

}
