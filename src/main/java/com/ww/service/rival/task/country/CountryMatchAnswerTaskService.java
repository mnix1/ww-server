package com.ww.service.rival.task.country;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.CountryTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Country;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.category.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.AnswerHelper.isValueDistanceEnough;
import static com.ww.helper.RandomHelper.randomElement;

@Service
public class CountryMatchAnswerTaskService {

    @Autowired
    CountryRepository countryRepository;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel, CountryTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = TaskDifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        List<Country> countries = prepareCountries(typeValue, answersCount);
        Question question = prepareQuestion(type, difficultyLevel, typeValue);
        List<Answer> answers = prepareAnswers(typeValue, countries);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(TaskType type, TaskDifficultyLevel difficultyLevel, CountryTaskType typeValue) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == CountryTaskType.MAX_POPULATION) {
            question.setTextContentPolish("Które z państw posiada największą populację?");
            question.setTextContentEnglish("Which country has the largest population?");
        }
        if (typeValue == CountryTaskType.MIN_POPULATION) {
            question.setTextContentPolish("Które z państw posiada najmniejszą populację?");
            question.setTextContentEnglish("Which country has the smallest population?");
        }
        if (typeValue == CountryTaskType.MAX_AREA) {
            question.setTextContentPolish("Które z państw posiada największą powierzchnię?");
            question.setTextContentEnglish("Which country has the largest area?");
        }
        if (typeValue == CountryTaskType.MIN_AREA) {
            question.setTextContentPolish("Które z państw posiada najmniejszą powierzchnię?");
            question.setTextContentEnglish("Which country has the smallest area?");
        }
        return question;
    }

    private List<Answer> prepareAnswers(CountryTaskType typeValue, List<Country> countries) {
        Country correct = countries.get(0);
        for (int i = 1; i < countries.size(); i++) {
            Country country = countries.get(i);
            if ((typeValue == CountryTaskType.MAX_POPULATION && correct.getPopulation() < country.getPopulation())
                    || (typeValue == CountryTaskType.MIN_POPULATION && correct.getPopulation() > country.getPopulation())
                    || (typeValue == CountryTaskType.MAX_AREA && correct.getArea() < country.getArea())
                    || (typeValue == CountryTaskType.MIN_AREA && correct.getArea() > country.getArea())) {
                correct = country;
            }
        }
        Country finalCorrect = correct;
        return countries.stream().map(country -> {
            Answer answer = new Answer(country == finalCorrect);
            answer.setTextContentPolish(country.getNamePolish());
            answer.setTextContentEnglish(country.getNameEnglish());
            return answer;
        }).collect(Collectors.toList());
    }

    private List<Country> prepareCountries(CountryTaskType typeValue, int count) {
        List<Country> allCountries = countryRepository.findAll();
        List<Country> countries = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        while (countries.size() < count) {
            Country randomCountry = randomElement(allCountries);
            Double value = CountryTaskType.aboutPopulation(typeValue)
                    ? randomCountry.getPopulation()
                    : CountryTaskType.aboutArea(typeValue)
                    ? randomCountry.getArea() :
                    0;
            if (isValueDistanceEnough(value, values)) {
                countries.add(randomCountry);
                values.add(value);
            }
        }
        return countries;
    }

}
