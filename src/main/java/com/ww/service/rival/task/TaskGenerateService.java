package com.ww.service.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import com.ww.repository.outside.rival.task.TaskTypeRepository;
import com.ww.service.rival.task.color.ColorTaskService;
import com.ww.service.rival.task.element.ElementTaskService;
import com.ww.service.rival.task.number.NumberTaskService;
import com.ww.service.rival.task.country.CountryTaskService;
import com.ww.service.rival.task.equation.EquationTaskService;
import com.ww.service.rival.task.memory.MemoryTaskService;
import com.ww.service.rival.task.lyrics.LyricsTaskService;
import com.ww.service.rival.task.olympicgames.OlympicGamesTaskService;
import com.ww.service.rival.task.riddle.RiddleTaskService;
import com.ww.service.rival.task.time.TimeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class TaskGenerateService {
    @Autowired
    LyricsTaskService lyricsTaskService;

    @Autowired
    CountryTaskService countryTaskService;

    @Autowired
    EquationTaskService equationTaskService;

    @Autowired
    NumberTaskService numberTaskService;

    @Autowired
    MemoryTaskService memoryTaskService;

    @Autowired
    ElementTaskService elementTaskService;

    @Autowired
    ColorTaskService colorTaskService;

    @Autowired
    RiddleTaskService riddleTaskService;

    @Autowired
    TimeTaskService timeTaskService;

    @Autowired
    OlympicGamesTaskService olympicGamesTaskService;

    @Autowired
    TaskTypeRepository taskTypeRepository;

    public Question generate(TaskType taskType, DifficultyLevel difficultyLevel, Language language) {
        if (taskType.getCategory() == Category.LYRICS) {
            return lyricsTaskService.generate(taskType, difficultyLevel, language);
        }
        if (taskType.getCategory() == Category.COUNTRY) {
            return countryTaskService.generate(taskType, difficultyLevel);
        }
        if (taskType.getCategory() == Category.EQUATION) {
            return equationTaskService.generate(taskType, difficultyLevel);
        }
        if (taskType.getCategory() == Category.NUMBER) {
            return numberTaskService.generate(taskType, difficultyLevel);
        }
        if (taskType.getCategory() == Category.MEMORY) {
            return memoryTaskService.generate(taskType, difficultyLevel);
        }
        if (taskType.getCategory() == Category.ELEMENT) {
            return elementTaskService.generate(taskType, difficultyLevel);
        }
        if (taskType.getCategory() == Category.RIDDLE) {
            return riddleTaskService.generate(taskType, difficultyLevel);
        }
        if (taskType.getCategory() == Category.COLOR) {
            return colorTaskService.generate(taskType, difficultyLevel);
        }
        if (taskType.getCategory() == Category.TIME) {
            return timeTaskService.generate(taskType, difficultyLevel);
        }
        if (taskType.getCategory() == Category.OLYMPIC_GAMES) {
            return olympicGamesTaskService.generate(taskType, difficultyLevel);
        }
        return null;
    }

    public Question generate(Category category, DifficultyLevel difficultyLevel, Language language) {
        if (category == Category.RANDOM) {
            category = Category.random();
        }
        TaskType taskType = findProperTaskType(category, difficultyLevel);
        return generate(taskType, difficultyLevel, language);
    }

    public TaskType findProperTaskType(Category category, DifficultyLevel difficultyLevel) {
        List<TaskType> possibleTaskTypes = taskTypeRepository.findAllByCategory(category)
                .stream()
                .filter(taskType -> {
                    int remainedDifficulty = difficultyLevel.getLevel() - taskType.getDifficulty();
                    return remainedDifficulty >= -30 && remainedDifficulty < 130;
                })
                .collect(Collectors.toList());
        if (possibleTaskTypes.isEmpty()) {
            throw new IllegalStateException("Not existing task type!!!");
        }
        return randomElement(possibleTaskTypes);
    }

}
