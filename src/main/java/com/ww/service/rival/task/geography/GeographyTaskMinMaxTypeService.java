package com.ww.service.rival.task.geography;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.GeographyTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.GeographyCountry;
import com.ww.model.entity.rival.task.Question;
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

    public Question generate(GeographyTaskType type) {
        List<GeographyCountry> countries = prepareCountries(type, 4);
        Question question = prepareQuestion(type);
        List<Answer> answers = prepareAnswers(type, countries);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(GeographyTaskType type) {
        Question question = new Question();
        question.setCategory(Category.GEOGRAPHY);
        if (type == GeographyTaskType.MAX_POPULATION) {
            question.setTextContentPolish("Które z państw posiada największą populację?");
            question.setTextContentEnglish("Which country has the largest population?");
        }
        if (type == GeographyTaskType.MIN_POPULATION) {
            question.setTextContentPolish("Które z państw posiada najmniejszą populację?");
            question.setTextContentEnglish("Which country has the smallest population?");
        }
        if (type == GeographyTaskType.MAX_AREA) {
            question.setTextContentPolish("Które z państw posiada największą powierzchnię?");
            question.setTextContentEnglish("Which country has the largest area?");
        }
        if (type == GeographyTaskType.MIN_AREA) {
            question.setTextContentPolish("Które z państw posiada najmniejszą powierzchnię?");
            question.setTextContentEnglish("Which country has the smallest area?");
        }
        return question;
    }

    private List<Answer> prepareAnswers(GeographyTaskType type, List<GeographyCountry> countries) {
        GeographyCountry correct = countries.get(0);
        for (int i = 1; i < countries.size(); i++) {
            GeographyCountry country = countries.get(i);
            if ((type == GeographyTaskType.MAX_POPULATION && correct.getPopulation() < country.getPopulation())
                    || (type == GeographyTaskType.MIN_POPULATION && correct.getPopulation() > country.getPopulation())
                    || (type == GeographyTaskType.MAX_AREA && correct.getArea() < country.getArea())
                    || (type == GeographyTaskType.MIN_AREA && correct.getArea() > country.getArea())) {
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

    private List<GeographyCountry> prepareCountries(GeographyTaskType type, int count) {
        List<GeographyCountry> allCountries = geographyCountryRepository.findAll();
        List<GeographyCountry> countries = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        while (countries.size() < count) {
            GeographyCountry randomCountry = randomElement(allCountries);
            Double value = GeographyTaskType.aboutPopulation(type)
                    ? randomCountry.getPopulation()
                    : GeographyTaskType.aboutArea(type)
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
