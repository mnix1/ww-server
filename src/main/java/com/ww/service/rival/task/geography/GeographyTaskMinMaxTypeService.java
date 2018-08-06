package com.ww.service.rival.task.geography;

import com.ww.model.constant.rival.task.type.GeographyTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.GeographyCountry;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.category.GeographyCountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class GeographyTaskMinMaxTypeService {

    @Autowired
    GeographyCountryRepository geographyCountryRepository;

    public Question generate(TaskType type, GeographyTaskType typeValue) {
        List<GeographyCountry> countries = prepareCountries(typeValue, 4);
        Question question = prepareQuestion(type, typeValue);
        List<Answer> answers = prepareAnswers(typeValue, countries);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(TaskType type, GeographyTaskType typeValue) {
        Question question = new Question(type);
        if (typeValue == GeographyTaskType.MAX_POPULATION) {
            question.setTextContentPolish("Które z państw posiada największą populację?");
            question.setTextContentEnglish("Which country has the largest population?");
        }
        if (typeValue == GeographyTaskType.MIN_POPULATION) {
            question.setTextContentPolish("Które z państw posiada najmniejszą populację?");
            question.setTextContentEnglish("Which country has the smallest population?");
        }
        if (typeValue == GeographyTaskType.MAX_AREA) {
            question.setTextContentPolish("Które z państw posiada największą powierzchnię?");
            question.setTextContentEnglish("Which country has the largest area?");
        }
        if (typeValue == GeographyTaskType.MIN_AREA) {
            question.setTextContentPolish("Które z państw posiada najmniejszą powierzchnię?");
            question.setTextContentEnglish("Which country has the smallest area?");
        }
        return question;
    }

    private List<Answer> prepareAnswers(GeographyTaskType typeValue, List<GeographyCountry> countries) {
        GeographyCountry correct = countries.get(0);
        for (int i = 1; i < countries.size(); i++) {
            GeographyCountry country = countries.get(i);
            if ((typeValue == GeographyTaskType.MAX_POPULATION && correct.getPopulation() < country.getPopulation())
                    || (typeValue == GeographyTaskType.MIN_POPULATION && correct.getPopulation() > country.getPopulation())
                    || (typeValue == GeographyTaskType.MAX_AREA && correct.getArea() < country.getArea())
                    || (typeValue == GeographyTaskType.MIN_AREA && correct.getArea() > country.getArea())) {
                correct = country;
            }
        }
        GeographyCountry finalCorrect = correct;
        return countries.stream().map(country -> {
            Answer answer = new Answer(country == finalCorrect);
            answer.setTextContentPolish(country.getNamePolish());
            answer.setTextContentEnglish(country.getNameEnglish());
            return answer;
        }).collect(Collectors.toList());
    }

    private List<GeographyCountry> prepareCountries(GeographyTaskType typeValue, int count) {
        List<GeographyCountry> allCountries = geographyCountryRepository.findAll();
        List<GeographyCountry> countries = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        while (countries.size() < count) {
            GeographyCountry randomCountry = randomElement(allCountries);
            Double value = GeographyTaskType.aboutPopulation(typeValue)
                    ? randomCountry.getPopulation()
                    : GeographyTaskType.aboutArea(typeValue)
                    ? randomCountry.getArea() :
                    0;
            if (isValueDistanceEnough(value, values)) {
                countries.add(randomCountry);
                values.add(value);
            }
        }
        return countries;
    }

    private boolean isValueDistanceEnough(Double value, List<Double> values) {
        return values.stream().noneMatch(e -> Math.abs(value - e) / value < 0.05);
    }

}
