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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
        List<OlympicMedal> allOlympicMedals = new ArrayList<>(olympicMedalRepository.findAll());
        Collections.shuffle(allOlympicMedals);
        if (typeValue == OlympicGamesTaskType.CITY_FROM_YEAR
                || typeValue == OlympicGamesTaskType.YEAR_FROM_CITY) {
            return cityYearPrepareObjects(allOlympicMedals, answersCount);
        } else if (typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_YEAR
                || typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_CITY
                || typeValue == OlympicGamesTaskType.YEAR_FROM_ATHLETE
                || typeValue == OlympicGamesTaskType.CITY_FROM_ATHLETE
                || typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_YEAR_SPORT
                || typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_CITY_SPORT) {
            return athletePrepareObjects(allOlympicMedals, answersCount);
        }
        throw new IllegalArgumentException();
    }

    private List<OlympicMedal> cityYearPrepareObjects(List<OlympicMedal> allOlympicMedals, int answersCount) {
        OlympicMedal correctOlympicMedal = randomElement(allOlympicMedals);
        List<OlympicMedal> pickedOlympicMedals = allOlympicMedals.stream()
                .filter(olympicMedal -> !olympicMedal.getTeam()
                        && !olympicMedal.getYear().equals(correctOlympicMedal.getYear())
                        && !olympicMedal.getCity().equals(correctOlympicMedal.getCity()))
                .limit(answersCount - 1)
                .collect(Collectors.toList());
        pickedOlympicMedals.add(0, correctOlympicMedal);
        return pickedOlympicMedals;
    }

    private List<OlympicMedal> athletePrepareObjects(List<OlympicMedal> allOlympicMedals, int answersCount) {
        OlympicMedal correctOlympicMedal = randomElement(allOlympicMedals);
        List<OlympicMedal> pickedOlympicMedals = allOlympicMedals.stream()
                .filter(olympicMedal -> !olympicMedal.getTeam()
                        && !olympicMedal.getYear().equals(correctOlympicMedal.getYear())
                        && !olympicMedal.getCity().equals(correctOlympicMedal.getCity())
                        && !olympicMedal.getAthlete().equals(correctOlympicMedal.getAthlete())
                        && !olympicMedal.getSport().equals(correctOlympicMedal.getSport()))
                .limit(answersCount - 1)
                .collect(Collectors.toList());
        pickedOlympicMedals.add(0, correctOlympicMedal);
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
            question.setTextContentPolish("Kto otrzymał " + correctOlympicMedal.getMedalLang(Language.POLISH) + " medal na IO w " + correctOlympicMedal.getYear() + " roku w sporcie " + correctOlympicMedal.getSportPolish().toLowerCase() + "?");
            question.setTextContentEnglish("Who received the " + correctOlympicMedal.getMedalLang(Language.ENGLISH) + " medal at Olympic Games in " + correctOlympicMedal.getCity() + " in " + correctOlympicMedal.getSport().toLowerCase() + "?");
        } else if (typeValue == OlympicGamesTaskType.WHICH_ATHLETE_FROM_MEDAL_CITY_SPORT) {
            question.setTextContentPolish("Kto zdobył " + correctOlympicMedal.getMedalLang(Language.POLISH) + " medal na IO w mieście " + correctOlympicMedal.getCityPolish() + " w sporcie " + correctOlympicMedal.getSportPolish().toLowerCase() + "?");
            question.setTextContentEnglish("Who won the " + correctOlympicMedal.getMedalLang(Language.ENGLISH) + " medal at Olympic Games in " + correctOlympicMedal.getCity() + " in " + correctOlympicMedal.getSport().toLowerCase() + "?");
        } else if (typeValue == OlympicGamesTaskType.YEAR_FROM_ATHLETE) {
            question.setTextContentPolish("W którym roku odbyły się " + correctOlympicMedal.getMedalLang(Language.POLISH) + " w których medal zdobył(a) " + correctOlympicMedal.getAthlete() + "?");
        }else if (typeValue == OlympicGamesTaskType.CITY_FROM_ATHLETE) {
            question.setTextContentPolish("Gdzie odbyły się " + correctOlympicMedal.getTypeLang(Language.POLISH) + " w których medal zdobył(a) " + correctOlympicMedal.getAthlete() + "?");
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
