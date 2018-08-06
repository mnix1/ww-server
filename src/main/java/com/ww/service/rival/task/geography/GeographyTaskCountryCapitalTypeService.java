package com.ww.service.rival.task.geography;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.GeographyTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.GeographyCountry;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.category.GeographyCountryRepository;
import com.ww.service.rival.task.TaskRendererService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class GeographyTaskCountryCapitalTypeService {

    @Autowired
    TaskRendererService taskRendererService;
    @Autowired
    GeographyCountryRepository geographyCountryRepository;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel, GeographyTaskType taskValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        List<GeographyCountry> allCountries = geographyCountryRepository.findAll();
        GeographyCountry correctCountry = randomElement(allCountries);

        Question question = prepareQuestion(type, difficultyLevel, taskValue, correctCountry);
//        question.setDifficultyLevel();
        List<Answer> answers = prepareAnswers(taskValue, correctCountry, allCountries, answersCount);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(TaskType type, TaskDifficultyLevel difficultyLevel, GeographyTaskType taskValue, GeographyCountry country) {
        Question question = new Question(type, difficultyLevel);
        if (taskValue == GeographyTaskType.COUNTRY_NAME_FROM_ALPHA_2) {
            question.setTextContentPolish("Wskaż państwo, którego kod to " + country.getAlpha2Code());
            question.setTextContentEnglish("Indicate the country whose code is " + country.getAlpha2Code());
        }
        if (taskValue == GeographyTaskType.COUNTRY_NAME_FROM_CAPITAL_NAME) {
            question.setTextContentPolish("Wskaż państwo, którego stolica to " + country.getCapitalPolish());
            question.setTextContentEnglish("Indicate the country whose capital is " + country.getCapitalEnglish());
        }
        if (taskValue == GeographyTaskType.COUNTRY_NAME_FROM_MAP) {
            question.setImageContent(country.getMapResourcePath());
            question.setTextContentPolish("Które państwo widoczne jest na mapie?");
            question.setTextContentEnglish("Which country is visible on the map?");
        }
        if (taskValue == GeographyTaskType.COUNTRY_NAME_FROM_FLAG) {
            question.setImageContent(country.getFlagResourcePath());
            question.setTextContentPolish("Jest to flaga państwa");
            question.setTextContentEnglish("Which country's flag is it?");
        }
        if (taskValue == GeographyTaskType.CAPITAL_NAME_FROM_ALPHA_3) {
            question.setTextContentPolish("Stolicą państwa, którego kod to " + country.getAlpha3Code() + " jest");
            question.setTextContentEnglish("Indicate the country whose code is " + country.getAlpha3Code());
        }
        if (taskValue == GeographyTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME) {
            question.setTextContentPolish("Stolicą państwa " + country.getNamePolish() + " jest");
            question.setTextContentEnglish("The capital of " + country.getNameEnglish() + " is");
        }
        if (taskValue == GeographyTaskType.CAPITAL_NAME_FROM_MAP) {
            question.setImageContent(country.getMapResourcePath());
            question.setTextContentPolish("Stolica państwa widocznego na mapie to");
            question.setTextContentEnglish("The state capital that can be seen on the map is");
        }
        if (taskValue == GeographyTaskType.CAPITAL_NAME_FROM_FLAG) {
            question.setImageContent(country.getFlagResourcePath());
            question.setTextContentPolish("Stolica państwa, którego flaga widoczna jest obok to");
            question.setTextContentEnglish("The capital of the country whose flag is visible next to it is");
        }
        return question;
    }

    private List<Answer> prepareAnswers(GeographyTaskType typeValue, GeographyCountry correctCountry, List<GeographyCountry> allCountries, int answersCount) {
        Answer correctAnswer = new Answer(true);
        fillAnswerContent(typeValue, correctAnswer, correctCountry);

        List<Answer> wrongAnswers = new ArrayList<>();
        List<String> answerContents = new ArrayList<>();
        answerContents.add(correctAnswer.getTextContentEnglish());
        while (wrongAnswers.size() < answersCount - 1) {
            GeographyCountry wrongCountry = randomElement(allCountries);
            Answer wrongAnswer = new Answer(false);
            fillAnswerContent(typeValue, wrongAnswer, wrongCountry);
            if (!answerContents.contains(wrongAnswer.getTextContentEnglish())) {
                wrongAnswers.add(wrongAnswer);
                answerContents.add(wrongAnswer.getTextContentEnglish());
            }
        }
        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.addAll(wrongAnswers);
        return answers;
    }

    private void fillAnswerContent(GeographyTaskType typeValue, Answer answer, GeographyCountry country) {
        if (typeValue == GeographyTaskType.COUNTRY_NAME_FROM_ALPHA_2
                || typeValue == GeographyTaskType.COUNTRY_NAME_FROM_CAPITAL_NAME
                || typeValue == GeographyTaskType.COUNTRY_NAME_FROM_MAP
                || typeValue == GeographyTaskType.COUNTRY_NAME_FROM_FLAG) {
            answer.setTextContentPolish(country.getNamePolish());
            answer.setTextContentEnglish(country.getNameEnglish());
        }
        if (typeValue == GeographyTaskType.CAPITAL_NAME_FROM_ALPHA_3
                || typeValue == GeographyTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME
                || typeValue == GeographyTaskType.CAPITAL_NAME_FROM_MAP
                || typeValue == GeographyTaskType.CAPITAL_NAME_FROM_FLAG) {
            answer.setTextContentPolish(country.getCapitalPolish());
            answer.setTextContentEnglish(country.getCapitalEnglish());
        }
    }

}
