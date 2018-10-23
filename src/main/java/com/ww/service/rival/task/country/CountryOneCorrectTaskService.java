package com.ww.service.rival.task.country;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.CountryTaskType;
import com.ww.model.entity.outside.rival.task.Answer;
import com.ww.model.entity.inside.task.Country;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import com.ww.repository.inside.category.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

@Service
@AllArgsConstructor
public class CountryOneCorrectTaskService {

    private final CountryRepository countryRepository;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, CountryTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = DifficultyLevel.answersCount(remainedDifficulty);
        List<Country> allCountries = countryRepository.findAll();
        Country correctCountry = randomElement(allCountries);
        Question question = prepareQuestion(type, difficultyLevel, typeValue, correctCountry);
        List<Answer> answers = prepareAnswers(typeValue, correctCountry, allCountries, answersCount);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, CountryTaskType typeValue, Country country) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == CountryTaskType.COUNTRY_NAME_FROM_ALPHA_2) {
            question.setTextContentPolish("Wskaż państwo, którego kod to " + country.getAlpha2Code());
            question.setTextContentEnglish("Indicate the country whose code is " + country.getAlpha2Code());
        }
        if (typeValue == CountryTaskType.COUNTRY_NAME_FROM_CAPITAL_NAME) {
            question.setTextContentPolish("Wskaż państwo, którego stolica to " + country.getCapitalPolish());
            question.setTextContentEnglish("Indicate the country whose capital is " + country.getCapitalEnglish());
        }
        if (typeValue == CountryTaskType.COUNTRY_NAME_FROM_MAP) {
            question.setImageContent(country.getMapResourcePath());
            question.setTextContentPolish("Które państwo widoczne jest na mapie?");
            question.setTextContentEnglish("Which country is visible on the map?");
        }
        if (typeValue == CountryTaskType.COUNTRY_NAME_FROM_FLAG) {
            question.setImageContent(country.getFlagResourcePath());
            question.setTextContentPolish("Jest to flaga państwa");
            question.setTextContentEnglish("Which country's flag is it?");
        }
        if (typeValue == CountryTaskType.CAPITAL_NAME_FROM_ALPHA_3) {
            question.setTextContentPolish("Stolicą państwa, którego kod to " + country.getAlpha3Code() + " jest");
            question.setTextContentEnglish("Indicate the country whose code is " + country.getAlpha3Code());
        }
        if (typeValue == CountryTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME) {
            question.setTextContentPolish("Stolicą państwa " + country.getNamePolish() + " jest");
            question.setTextContentEnglish("The capital of " + country.getNameEnglish() + " is");
        }
        if (typeValue == CountryTaskType.CAPITAL_NAME_FROM_MAP) {
            question.setImageContent(country.getMapResourcePath());
            question.setTextContentPolish("Stolica państwa widocznego na mapie to");
            question.setTextContentEnglish("The state capital that can be seen on the map is");
        }
        if (typeValue == CountryTaskType.CAPITAL_NAME_FROM_FLAG) {
            question.setImageContent(country.getFlagResourcePath());
            question.setTextContentPolish("Stolica państwa, którego flaga widoczna jest obok to");
            question.setTextContentEnglish("The capital of the country whose flag is visible next to it is");
        }
        return question;
    }

    private List<Answer> prepareAnswers(CountryTaskType typeValue, Country correctCountry, List<Country> allCountries, int answersCount) {
        Answer correctAnswer = new Answer(true);
        fillAnswerContent(typeValue, correctAnswer, correctCountry);

        List<Answer> wrongAnswers = new ArrayList<>();
        List<String> answerContents = new ArrayList<>();
        answerContents.add(correctAnswer.getTextContentEnglish());
        while (wrongAnswers.size() < answersCount - 1) {
            Country wrongCountry = randomElement(allCountries);
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

    private void fillAnswerContent(CountryTaskType typeValue, Answer answer, Country country) {
        if (typeValue == CountryTaskType.COUNTRY_NAME_FROM_ALPHA_2
                || typeValue == CountryTaskType.COUNTRY_NAME_FROM_CAPITAL_NAME
                || typeValue == CountryTaskType.COUNTRY_NAME_FROM_MAP
                || typeValue == CountryTaskType.COUNTRY_NAME_FROM_FLAG) {
            answer.setTextContentPolish(country.getNamePolish());
            answer.setTextContentEnglish(country.getNameEnglish());
        }
        if (typeValue == CountryTaskType.CAPITAL_NAME_FROM_ALPHA_3
                || typeValue == CountryTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME
                || typeValue == CountryTaskType.CAPITAL_NAME_FROM_MAP
                || typeValue == CountryTaskType.CAPITAL_NAME_FROM_FLAG) {
            answer.setTextContentPolish(country.getCapitalPolish());
            answer.setTextContentEnglish(country.getCapitalEnglish());
        }
    }

}
