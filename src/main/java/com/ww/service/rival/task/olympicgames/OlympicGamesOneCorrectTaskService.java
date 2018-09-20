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
        Set<Integer> years = new HashSet<>();
        Set<String> cities = new HashSet<>();
        List<OlympicMedal> allOlympicMedals = olympicMedalRepository.findAll();
        List<OlympicMedal> pickedOlympicMedals = new ArrayList<>(answersCount);
        while (pickedOlympicMedals.size() < answersCount) {
            OlympicMedal olympicMedal = randomElement(allOlympicMedals);
            if (typeValue == OlympicGamesTaskType.WHERE_FROM_YEAR
                    || typeValue == OlympicGamesTaskType.YEAR_FROM_WHERE) {
                whereFromYearMaybeAdd(pickedOlympicMedals, olympicMedal, years, cities);
            }
        }
        return pickedOlympicMedals;
    }

    private boolean whereFromYearMaybeAdd(List<OlympicMedal> pickedOlympicMedals, OlympicMedal olympicMedal, Set<Integer> years, Set<String> cities) {
        if (years.contains(olympicMedal.getYear()) || cities.contains(olympicMedal.getCity())) {
            return false;
        }
        pickedOlympicMedals.add(olympicMedal);
        years.add(olympicMedal.getYear());
        cities.add(olympicMedal.getCity());
        return true;
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, OlympicGamesTaskType typeValue, OlympicMedal correctOlympicMedal) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == OlympicGamesTaskType.WHERE_FROM_YEAR) {
            question.setTextContentPolish("Gdzie odbyły się " + correctOlympicMedal.getTypeLang(Language.POLISH) + " w " + correctOlympicMedal.getYear() + " roku?");
            question.setTextContentEnglish("Where the " + correctOlympicMedal.getTypeLang(Language.ENGLISH) + " took place in " + correctOlympicMedal.getYear() + "?");
        } else if (typeValue == OlympicGamesTaskType.YEAR_FROM_WHERE) {
            question.setTextContentPolish("W którym roku " + correctOlympicMedal.getTypeLang(Language.POLISH) + " zostały przeprowadzone w " + correctOlympicMedal.getCityPolish() + "?");
            question.setTextContentEnglish("In which year the " + correctOlympicMedal.getTypeLang(Language.ENGLISH) + " were held in " + correctOlympicMedal.getCity() + "?");
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
        if (typeValue == OlympicGamesTaskType.WHERE_FROM_YEAR) {
            answer.setTextContentPolish(object.getCityPolish());
            answer.setTextContentEnglish(object.getCity());
        } else if (typeValue == OlympicGamesTaskType.YEAR_FROM_WHERE) {
            answer.setTextContent(object.getYear() + "");
        }
    }

}
