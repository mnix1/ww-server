package com.ww.service.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.TaskTypeRepository;
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

    public Question generate(Category category, DifficultyLevel difficultyLevel) {
        if (category == Category.RANDOM) {
            category = Category.random();
        }
        TaskType taskType = findProperTaskType(category, difficultyLevel);
        if (category == Category.LYRICS) {
            return lyricsTaskService.generate(taskType, difficultyLevel, Language.ALL);
        }
        if (category == Category.COUNTRY) {
            return countryTaskService.generate(taskType, difficultyLevel);
        }
        if (category == Category.EQUATION) {
            return equationTaskService.generate(taskType, difficultyLevel);
        }
        if (category == Category.NUMBER) {
            return numberTaskService.generate(taskType, difficultyLevel);
        }
        if (category == Category.MEMORY) {
            return memoryTaskService.generate(taskType, difficultyLevel);
        }
        if (category == Category.ELEMENT) {
            return elementTaskService.generate(taskType, difficultyLevel);
        }
        if (category == Category.RIDDLE) {
            return riddleTaskService.generate(taskType, difficultyLevel);
        }
        if (category == Category.COLOR) {
            return colorTaskService.generate(taskType, difficultyLevel);
        }
        if (category == Category.TIME) {
            return timeTaskService.generate(taskType, difficultyLevel);
        }
        if (category == Category.OLYMPIC_GAMES) {
            return olympicGamesTaskService.generate(taskType, difficultyLevel);
        }
        return null;
    }

    private TaskType findProperTaskType(Category category, DifficultyLevel difficultyLevel) {
        List<TaskType> possibleTaskTypes = taskTypeRepository.findAllByCategory(category)
                .stream()
                .filter(taskType -> difficultyLevel.getLevel() - taskType.getDifficulty() > -25)
                .collect(Collectors.toList());
        if (possibleTaskTypes.isEmpty()) {
            throw new IllegalStateException("Not existing task type!!!");
        }
        return randomElement(possibleTaskTypes);
    }

}
