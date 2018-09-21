package com.ww.service.rival.task.olympicgames;

import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.OlympicGamesTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.OlympicMedal;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.category.OlympicMedalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class OlympicGamesOneCorrectTaskService {

    @Autowired
    private OlympicMedalRepository olympicMedalRepository;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel, OlympicGamesTaskType typeValue) {
        int remainedDifficulty = difficultyLevel.getLevel() - type.getDifficulty();
        int answersCount = DifficultyLevel.answersCount(difficultyLevel, remainedDifficulty);
        List<OlympicMedal> olympicMedals = prepareObjects(typeValue, remainedDifficulty, answersCount);
        OlympicMedal correctOlympicMedal = olympicMedals.get(0);
        List<OlympicMedal> wrongOlympicMedals = olympicMedals.subList(1, olympicMedals.size());
        Question question = prepareQuestion(type, difficultyLevel, typeValue, correctOlympicMedal);
        List<Answer> answers = prepareAnswers(typeValue, correctOlympicMedal, wrongOlympicMedals);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private List<OlympicMedal> prepareObjects(OlympicGamesTaskType typeValue, int difficulty, int answersCount) {
        if (typeValue == OlympicGamesTaskType.CITY_FROM_YEAR
                || typeValue == OlympicGamesTaskType.YEAR_FROM_CITY) {
            return cityYearPrepareObjects(answersCount);
        } else if (typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_YEAR
                || typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_CITY
                || typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_YEAR_SPORT
                || typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_CITY_SPORT) {
            return whichAthletePrepareObjects(answersCount);
        } else if (typeValue == OlympicGamesTaskType.YEAR_FROM_ATHLETE
                || typeValue == OlympicGamesTaskType.CITY_FROM_ATHLETE) {
            return yearCityFromAthletePrepareObjects(answersCount);
        }
        throw new IllegalArgumentException();
    }

    private List<OlympicMedal> cityYearPrepareObjects(int answersCount) {
        List<OlympicMedal> allOlympicMedals = olympicMedalRepository.findAllByTeam(false);
        Set<Integer> years = new HashSet<>();
        Set<String> cities = new HashSet<>();
        List<OlympicMedal> pickedOlympicMedals = new ArrayList<>(answersCount);
        while (pickedOlympicMedals.size() < answersCount) {
            OlympicMedal olympicMedal = randomElement(allOlympicMedals);
            if (years.contains(olympicMedal.getYear()) || cities.contains(olympicMedal.getCity())) {
                continue;
            }
            pickedOlympicMedals.add(olympicMedal);
            years.add(olympicMedal.getYear());
            cities.add(olympicMedal.getCity());
        }
        return pickedOlympicMedals;
    }

    private List<OlympicMedal> whichAthletePrepareObjects(int answersCount) {
        List<OlympicMedal> allOlympicMedals = olympicMedalRepository.findAllByTeam(false);
        OlympicMedal correctOlympicMedal = randomElement(allOlympicMedals);
        List<OlympicMedal> allCorrectOlympicMedals = allOlympicMedals.stream()
                .filter(olympicMedal -> olympicMedal.getMedal() == correctOlympicMedal.getMedal()
                        && (olympicMedal.getYear().equals(correctOlympicMedal.getYear())
                        || olympicMedal.getCity().equals(correctOlympicMedal.getCity())))
                .collect(Collectors.toList());
        Set<String> correctAthletes = allCorrectOlympicMedals.stream().map(OlympicMedal::getAthlete).collect(Collectors.toSet());
        Set<String> usedAthletes = new HashSet<>();
        List<OlympicMedal> pickedOlympicMedals = new ArrayList<>(answersCount);
        pickedOlympicMedals.add(correctOlympicMedal);
        while (pickedOlympicMedals.size() < answersCount) {
            OlympicMedal olympicMedal = randomElement(allOlympicMedals);
            if (usedAthletes.contains(olympicMedal.getAthlete()) || correctAthletes.contains(olympicMedal.getAthlete())) {
                continue;
            }
            pickedOlympicMedals.add(olympicMedal);
            usedAthletes.add(olympicMedal.getAthlete());
        }
        return pickedOlympicMedals;
    }

    private List<OlympicMedal> yearCityFromAthletePrepareObjects(int answersCount) {
        List<OlympicMedal> allOlympicMedals = olympicMedalRepository.findAllByTeam(false);
        OlympicMedal correctOlympicMedal = randomElement(allOlympicMedals);
        List<OlympicMedal> allCorrectOlympicMedals = olympicMedalRepository.findAllByAthlete(correctOlympicMedal.getAthlete());
        Set<Integer> correctYears = allCorrectOlympicMedals.stream().map(OlympicMedal::getYear).collect(Collectors.toSet());
        Set<String> correctCities = allCorrectOlympicMedals.stream().map(OlympicMedal::getCity).collect(Collectors.toSet());
        Set<Integer> usedYears = new HashSet<>();
        Set<String> usedCities = new HashSet<>();
        List<OlympicMedal> pickedOlympicMedals = new ArrayList<>(answersCount);
        pickedOlympicMedals.add(correctOlympicMedal);
        while (pickedOlympicMedals.size() < answersCount) {
            OlympicMedal olympicMedal = randomElement(allOlympicMedals);
            if (usedYears.contains(olympicMedal.getYear()) || correctYears.contains(olympicMedal.getYear())
                    || usedCities.contains(olympicMedal.getCity()) || correctCities.contains(olympicMedal.getCity())) {
                continue;
            }
            pickedOlympicMedals.add(olympicMedal);
            usedYears.add(olympicMedal.getYear());
            usedCities.add(olympicMedal.getCity());
        }
        return pickedOlympicMedals;
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, OlympicGamesTaskType typeValue, OlympicMedal correctOlympicMedal) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == OlympicGamesTaskType.CITY_FROM_YEAR) {
            question.setTextContentPolish("Gdzie odbyły się " + correctOlympicMedal.getTypeLang(Language.POLISH) + " w " + correctOlympicMedal.getYear() + " roku?");
            question.setTextContentEnglish("Where the " + correctOlympicMedal.getTypeLang(Language.ENGLISH) + " took place in " + correctOlympicMedal.getYear() + "?");
        } else if (typeValue == OlympicGamesTaskType.YEAR_FROM_CITY) {
            question.setTextContentPolish("W którym roku " + correctOlympicMedal.getTypeLang(Language.POLISH) + " zostały przeprowadzone w mieście " + correctOlympicMedal.getCityPolish() + "?");
            question.setTextContentEnglish("In which year the " + correctOlympicMedal.getTypeLang(Language.ENGLISH) + " were held in " + correctOlympicMedal.getCity() + "?");
        } else if (typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_YEAR) {
            question.setTextContentPolish("Kto otrzymał " + correctOlympicMedal.getMedalLang(Language.POLISH) + " medal na IO w " + correctOlympicMedal.getYear() + " roku?");
            question.setTextContentEnglish("Who received the " + correctOlympicMedal.getMedalLang(Language.ENGLISH) + " medal at Olympic Games in " + correctOlympicMedal.getYear() + "?");
        } else if (typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_CITY) {
            question.setTextContentPolish("Kto wygrał " + correctOlympicMedal.getMedalLang(Language.POLISH) + " medal na IO w mieście " + correctOlympicMedal.getCityPolish() + "?");
            question.setTextContentEnglish("Who received the " + correctOlympicMedal.getMedalLang(Language.ENGLISH) + " medal at Olympic Games in " + correctOlympicMedal.getCity() + "?");
        } else if (typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_YEAR_SPORT) {
            question.setTextContentPolish("Kto otrzymał " + correctOlympicMedal.getMedalLang(Language.POLISH) + " medal na IO w " + correctOlympicMedal.getYear() + " roku w sporcie \"" + correctOlympicMedal.getSportPolish().toLowerCase() + "\"?");
            question.setTextContentEnglish("Who received the " + correctOlympicMedal.getMedalLang(Language.ENGLISH) + " medal at Olympic Games in " + correctOlympicMedal.getCity() + " in " + correctOlympicMedal.getSport().toLowerCase() + "?");
        } else if (typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_CITY_SPORT) {
            question.setTextContentPolish("Kto zdobył " + correctOlympicMedal.getMedalLang(Language.POLISH) + " medal na IO w mieście " + correctOlympicMedal.getCityPolish() + " w sporcie \"" + correctOlympicMedal.getSportPolish().toLowerCase() + "\"?");
            question.setTextContentEnglish("Who won the " + correctOlympicMedal.getMedalLang(Language.ENGLISH) + " medal at Olympic Games in " + correctOlympicMedal.getCity() + " in " + correctOlympicMedal.getSport().toLowerCase() + "?");
        } else if (typeValue == OlympicGamesTaskType.YEAR_FROM_ATHLETE) {
            question.setTextContentPolish("W którym roku odbyły się " + correctOlympicMedal.getTypeLang(Language.POLISH) + " w których medal " + correctOlympicMedal.getWonPolish() + " " + correctOlympicMedal.getAthlete() + "?");
        } else if (typeValue == OlympicGamesTaskType.CITY_FROM_ATHLETE) {
            question.setTextContentPolish("Gdzie odbyły się " + correctOlympicMedal.getTypeLang(Language.POLISH) + " w których medal " + correctOlympicMedal.getWonPolish() + " " + correctOlympicMedal.getAthlete() + "?");
        }

        return question;
    }

    private List<Answer> prepareAnswers(OlympicGamesTaskType typeValue, OlympicMedal correctOlympicMedal, List<OlympicMedal> wrongOlympicMedals) {
        List<Answer> answers = wrongOlympicMedals.stream().map(wrongOlympicMedal -> {
            Answer wrongAnswer = new Answer(false);
            fillAnswerContent(typeValue, wrongAnswer, wrongOlympicMedal);
            return wrongAnswer;
        }).collect(Collectors.toList());
        Answer correctAnswer = new Answer(true);
        fillAnswerContent(typeValue, correctAnswer, correctOlympicMedal);
        answers.add(correctAnswer);
        return answers;
    }

    private void fillAnswerContent(OlympicGamesTaskType typeValue, Answer answer, OlympicMedal object) {
        if (typeValue == OlympicGamesTaskType.CITY_FROM_YEAR
                || typeValue == OlympicGamesTaskType.CITY_FROM_ATHLETE) {
            answer.setTextContentPolish(object.getCityPolish());
            answer.setTextContentEnglish(object.getCity());
        } else if (typeValue == OlympicGamesTaskType.YEAR_FROM_CITY
                || typeValue == OlympicGamesTaskType.YEAR_FROM_ATHLETE) {
            answer.setTextContent(object.getYear() + "");
        } else if (typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_YEAR
                || typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_CITY
                || typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_YEAR_SPORT
                || typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_CITY_SPORT) {
            answer.setTextContent(object.getAthlete());
        }
    }

}
