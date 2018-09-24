package com.ww.service.rival.task.olympicgames;

import com.ww.model.constant.Gender;
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
        int answersCount = DifficultyLevel.answersCount(remainedDifficulty);
        List<OlympicMedal> olympicMedals = prepareObjects(typeValue, answersCount);
        OlympicMedal correctOlympicMedal = olympicMedals.get(0);
        List<OlympicMedal> wrongOlympicMedals = olympicMedals.subList(1, olympicMedals.size());
        Question question = prepareQuestion(type, difficultyLevel, typeValue, correctOlympicMedal);
        List<Answer> answers = prepareAnswers(typeValue, correctOlympicMedal, wrongOlympicMedals);
        question.setAnswers(new HashSet<>(answers));
        return question;
    }

    private Question prepareQuestion(TaskType type, DifficultyLevel difficultyLevel, OlympicGamesTaskType typeValue, OlympicMedal cOM) {
        Question question = new Question(type, difficultyLevel);
        if (typeValue == OlympicGamesTaskType.CITY_FROM_YEAR) {
            question.setTextContentPolish("Gdzie odbyły się " + cOM.getTypeLang(Language.POLISH) + " w " + cOM.getYear() + " roku?");
            question.setTextContentEnglish("Where the " + cOM.getTypeLang(Language.ENGLISH) + " took place in " + cOM.getYear() + "?");
        } else if (typeValue == OlympicGamesTaskType.YEAR_FROM_CITY) {
            question.setTextContentPolish("W którym roku " + cOM.getTypeLang(Language.POLISH) + " zostały przeprowadzone w mieście " + cOM.getCityPolish() + "?");
            question.setTextContentEnglish("In which year the " + cOM.getTypeLang(Language.ENGLISH) + " were held in " + cOM.getCity() + "?");
        } else if (typeValue == OlympicGamesTaskType.ATHLETE_FROM_MEDAL_YEAR) {
            question.setTextContentPolish("Kto otrzymał " + cOM.getMedalLang(Language.POLISH) + " medal na IO w " + cOM.getYear() + " roku?");
            question.setTextContentEnglish("Who received the " + cOM.getMedalLang(Language.ENGLISH) + " medal at Olympic Games in " + cOM.getYear() + "?");
        } else if (typeValue == OlympicGamesTaskType.ATHLETE_FROM_MEDAL_CITY) {
            question.setTextContentPolish("Kto wygrał " + cOM.getMedalLang(Language.POLISH) + " medal na IO w mieście " + cOM.getCityPolish() + "?");
            question.setTextContentEnglish("Who received the " + cOM.getMedalLang(Language.ENGLISH) + " medal at Olympic Games in " + cOM.getCity() + "?");
        } else if (typeValue == OlympicGamesTaskType.ATHLETE_FROM_MEDAL_YEAR_SPORT) {
            question.setTextContentPolish("Kto otrzymał " + cOM.getMedalLang(Language.POLISH) + " medal na IO w " + cOM.getYear() + " roku w sporcie \"" + cOM.getSportPolish().toLowerCase() + "\"?");
            question.setTextContentEnglish("Who received the " + cOM.getMedalLang(Language.ENGLISH) + " medal at Olympic Games in " + cOM.getCity() + " in " + cOM.getSport().toLowerCase() + "?");
        } else if (typeValue == OlympicGamesTaskType.ATHLETE_FROM_MEDAL_CITY_SPORT) {
            question.setTextContentPolish("Kto zdobył " + cOM.getMedalLang(Language.POLISH) + " medal na IO w mieście " + cOM.getCityPolish() + " w sporcie \"" + cOM.getSportPolish().toLowerCase() + "\"?");
            question.setTextContentEnglish("Who won the " + cOM.getMedalLang(Language.ENGLISH) + " medal at Olympic Games in " + cOM.getCity() + " in " + cOM.getSport().toLowerCase() + "?");
        } else if (typeValue == OlympicGamesTaskType.YEAR_FROM_ATHLETE) {
            question.setTextContentPolish("W którym roku odbyły się " + cOM.getTypeLang(Language.POLISH) + " w których medal " + cOM.getWonPolish() + " " + cOM.getAthlete() + "?");
            question.setTextContentEnglish("In which year the " + cOM.getTypeLang(Language.ENGLISH) + " were held in which " + cOM.getAthlete() + " won the medal?");
        } else if (typeValue == OlympicGamesTaskType.CITY_FROM_ATHLETE) {
            question.setTextContentPolish("Gdzie odbyły się " + cOM.getTypeLang(Language.POLISH) + " w których medal " + cOM.getWonPolish() + " " + cOM.getAthlete() + "?");
            question.setTextContentEnglish("Where the " + cOM.getTypeLang(Language.ENGLISH) + " took place in which " + cOM.getAthlete() + " won the medal?");
        } else if (typeValue == OlympicGamesTaskType.COUNTRY_FROM_MEDAL_YEAR_FOR_POPULAR_ONLY_TEAM_SPORT) {
            String beginPolish = cOM.getGender() == Gender.MEN ? "Reprezentanci którego kraju zdobyli " : "Reprezentantki którego kraju zdobyły ";
            question.setTextContentPolish(beginPolish + cOM.getMedal().getNamePolish() + " medal na IO w " + cOM.getYear() + " roku w sporcie \"" + cOM.getSportPolish().toLowerCase() + "\"?");
            question.setTextContentEnglish("The representatives of which country won the " + cOM.getMedal().getNameEnglish() + " medal at Olympic Games in " + cOM.getYear() + " in " + cOM.getSport() + "?");
        } else if (typeValue == OlympicGamesTaskType.COUNTRY_FROM_ATHLETE) {
            question.setTextContentPolish("Dla jakiego państwa medal na IO " + cOM.getWonPolish() + " " + cOM.getAthlete() + "?");
            question.setTextContentEnglish("For which country the medal on the Olympic Games won " + cOM.getAthlete() + "?");
        } else if (typeValue == OlympicGamesTaskType.ATHLETE_FROM_COUNTRY) {
            question.setTextContentPolish("Kto reprezentował na IO kraj " + cOM.getCountryPolish() + "?");
            question.setTextContentEnglish("At the Olympic Games, the " + cOM.getIocCountryCode() + " country was represented by:");
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
        } else if (typeValue == OlympicGamesTaskType.ATHLETE_FROM_MEDAL_YEAR
                || typeValue == OlympicGamesTaskType.ATHLETE_FROM_MEDAL_CITY
                || typeValue == OlympicGamesTaskType.ATHLETE_FROM_COUNTRY
                || typeValue == OlympicGamesTaskType.ATHLETE_FROM_MEDAL_YEAR_SPORT
                || typeValue == OlympicGamesTaskType.ATHLETE_FROM_MEDAL_CITY_SPORT) {
            answer.setTextContent(object.getAthlete());
        } else if (typeValue == OlympicGamesTaskType.COUNTRY_FROM_MEDAL_YEAR_FOR_POPULAR_ONLY_TEAM_SPORT
                || typeValue == OlympicGamesTaskType.COUNTRY_FROM_ATHLETE) {
            answer.setTextContentPolish(object.getCountryPolish());
            answer.setTextContentEnglish(object.getCountryEnglish());
        }
    }

    private List<OlympicMedal> prepareObjects(OlympicGamesTaskType typeValue, int answersCount) {
        if (typeValue == OlympicGamesTaskType.CITY_FROM_YEAR
                || typeValue == OlympicGamesTaskType.YEAR_FROM_CITY) {
            return cityYearPrepareObjects(answersCount);
        } else if (typeValue == OlympicGamesTaskType.ATHLETE_FROM_MEDAL_YEAR
                || typeValue == OlympicGamesTaskType.ATHLETE_FROM_MEDAL_CITY
                || typeValue == OlympicGamesTaskType.ATHLETE_FROM_MEDAL_YEAR_SPORT
                || typeValue == OlympicGamesTaskType.ATHLETE_FROM_MEDAL_CITY_SPORT) {
            return athletePrepareObjects(answersCount);
        } else if (typeValue == OlympicGamesTaskType.YEAR_FROM_ATHLETE
                || typeValue == OlympicGamesTaskType.CITY_FROM_ATHLETE) {
            return yearCityFromAthletePrepareObjects(answersCount);
        } else if (typeValue == OlympicGamesTaskType.COUNTRY_FROM_MEDAL_YEAR_FOR_POPULAR_ONLY_TEAM_SPORT) {
            return countryPopularTeamSportPrepareObjects(answersCount);
        } else if (typeValue == OlympicGamesTaskType.COUNTRY_FROM_ATHLETE) {
            return countryAthletePrepareObjects(answersCount);
        } else if (typeValue == OlympicGamesTaskType.ATHLETE_FROM_COUNTRY) {
            return athleteCountryPrepareObjects(answersCount);
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

    private List<OlympicMedal> athletePrepareObjects(int answersCount) {
        List<OlympicMedal> allOlympicMedals = olympicMedalRepository.findAllByTeam(false);
        OlympicMedal correctOlympicMedal = randomElement(allOlympicMedals);
        List<OlympicMedal> allCorrectOlympicMedals = allOlympicMedals.stream()
                .filter(olympicMedal -> olympicMedal.getMedal() == correctOlympicMedal.getMedal()
                        && (olympicMedal.getYear().equals(correctOlympicMedal.getYear())
                        || olympicMedal.getCity().equals(correctOlympicMedal.getCity())))
                .collect(Collectors.toList());
        return athletePrepareObjectsHelper(allOlympicMedals, allCorrectOlympicMedals, answersCount, correctOlympicMedal);
    }

    private List<OlympicMedal> athleteCountryPrepareObjects(int answersCount) {
        List<OlympicMedal> allOlympicMedals = olympicMedalRepository.findAllByTeamAndCountryMapped(false, true);
        OlympicMedal correctOlympicMedal = randomElement(allOlympicMedals);
        List<OlympicMedal> allCorrectOlympicMedals = olympicMedalRepository.findAllByIocCountryCode(correctOlympicMedal.getIocCountryCode());
        return athletePrepareObjectsHelper(allOlympicMedals, allCorrectOlympicMedals, answersCount, correctOlympicMedal);
    }


    private List<OlympicMedal> athletePrepareObjectsHelper(List<OlympicMedal> allOlympicMedals, List<OlympicMedal> allCorrectOlympicMedals, int answersCount, OlympicMedal correctOlympicMedal) {
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

    private List<OlympicMedal> countryPopularTeamSportPrepareObjects(int answersCount) {
        List<OlympicMedal> allOlympicMedals = olympicMedalRepository.findAllByPopularTeamSportAndCountryMapped(true, true);
        OlympicMedal correctOlympicMedal = randomElement(allOlympicMedals);
        List<OlympicMedal> allCorrectOlympicMedals = allOlympicMedals.stream()
                .filter(olympicMedal -> olympicMedal.getMedal() == correctOlympicMedal.getMedal()
                        && (olympicMedal.getYear().equals(correctOlympicMedal.getYear())
                        || olympicMedal.getCity().equals(correctOlympicMedal.getCity())))
                .collect(Collectors.toList());
        return countryPrepareObjectsHelper(allOlympicMedals, allCorrectOlympicMedals, answersCount, correctOlympicMedal);
    }

    private List<OlympicMedal> countryAthletePrepareObjects(int answersCount) {
        List<OlympicMedal> allOlympicMedals = olympicMedalRepository.findAllByTeamAndCountryMapped(false, true);
        OlympicMedal correctOlympicMedal = randomElement(allOlympicMedals);
        List<OlympicMedal> allCorrectOlympicMedals = olympicMedalRepository.findAllByAthlete(correctOlympicMedal.getAthlete());
        return countryPrepareObjectsHelper(allOlympicMedals, allCorrectOlympicMedals, answersCount, correctOlympicMedal);
    }

    private List<OlympicMedal> countryPrepareObjectsHelper(List<OlympicMedal> allOlympicMedals, List<OlympicMedal> allCorrectOlympicMedals, int answersCount, OlympicMedal correctOlympicMedal) {
        Set<String> correctCountries = allCorrectOlympicMedals.stream().map(OlympicMedal::getIocCountryCode).collect(Collectors.toSet());
        Set<String> usedCountries = new HashSet<>();
        List<OlympicMedal> pickedOlympicMedals = new ArrayList<>(answersCount);
        pickedOlympicMedals.add(correctOlympicMedal);
        while (pickedOlympicMedals.size() < answersCount) {
            OlympicMedal olympicMedal = randomElement(allOlympicMedals);
            if (usedCountries.contains(olympicMedal.getIocCountryCode()) || correctCountries.contains(olympicMedal.getIocCountryCode())) {
                continue;
            }
            pickedOlympicMedals.add(olympicMedal);
            usedCountries.add(olympicMedal.getIocCountryCode());
        }
        return pickedOlympicMedals;
    }

}
