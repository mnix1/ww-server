package com.ww.database;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.constant.rival.task.type.*;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.model.entity.rival.task.TaskWisdomAttribute;
import com.ww.repository.rival.task.TaskTypeRepository;
import com.ww.repository.rival.task.TaskWisdomAttributeRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@NoArgsConstructor
@Service
public class InitTaskTypesService {

    @Autowired
    private TaskTypeRepository taskTypeRepository;
    @Autowired
    private TaskWisdomAttributeRepository taskWisdomAttributeRepository;

    public void initTaskTypes() {
        List<TaskType> taskTypes = new ArrayList<>();
        initOlympicGames(taskTypes);
        initLyrics(taskTypes);
        initMemory(taskTypes);
        initNumber(taskTypes);
        initEquation(taskTypes);
        initCountry(taskTypes);
        initElement(taskTypes);
        initRiddle(taskTypes);
        initColor(taskTypes);
        initTime(taskTypes);
        List<TaskWisdomAttribute> wisdomAttributes = new ArrayList<>();
        for (TaskType taskType : taskTypes) {
            taskType.getWisdomAttributes().forEach(wisdomAttribute -> {
                wisdomAttribute.setType(taskType);
                wisdomAttributes.add(wisdomAttribute);
            });
        }
        taskTypeRepository.saveAll(taskTypes);
        taskWisdomAttributeRepository.saveAll(wisdomAttributes);
    }

    private void initOlympicGames(List<TaskType> taskTypes) {
        taskTypes.add(new TaskType(Category.OLYMPIC_GAMES, OlympicGamesTaskType.WHERE_FROM_YEAR.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.7),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.OLYMPIC_GAMES, OlympicGamesTaskType.YEAR_FROM_WHERE.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.67),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.13)
        ))));
    }

    private void initLyrics(List<TaskType> taskTypes) {
        taskTypes.add(new TaskType(Category.LYRICS, LyricsTaskTypeValue.NEXT_LINE.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.LYRICS, LyricsTaskTypeValue.PREVIOUS_LINE.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.2)
        ))));
    }

    private void initMemory(List<TaskType> taskTypes) {
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.BACKGROUND_COLOR_FROM_FIGURE_KEY.name(), TaskRenderer.TEXT_ANIMATION, TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.25)
        ))));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.SHAPE_FROM_FIGURE_KEY.name(), TaskRenderer.TEXT_ANIMATION, TaskRenderer.TEXT, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.7),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.SHAPE_FROM_BACKGROUND_COLOR.name(), TaskRenderer.TEXT_ANIMATION, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.65),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.15)
        ))));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.FIGURE_KEY_FROM_BACKGROUND_COLOR.name(), TaskRenderer.TEXT_ANIMATION, TaskRenderer.TEXT, 7, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.8),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.MEMORY, MemoryTaskType.FIGURE_KEY_FROM_SHAPE.name(), TaskRenderer.TEXT_ANIMATION, TaskRenderer.TEXT, 7, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.65),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1)
        ))));
    }

    private void initNumber(List<TaskType> taskTypes) {
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.MAX.name(), TaskRenderer.TEXT, TaskRenderer.EQUATION, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.55),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.45)
        ))));
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.MIN.name(), TaskRenderer.TEXT, TaskRenderer.EQUATION, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.5),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.5)
        ))));
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.GCD.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.7),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.LCM.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.74),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.26)
        ))));
        taskTypes.add(new TaskType(Category.NUMBER, NumberTaskType.PRIME.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.94),
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.06)
        ))));
    }

    private void initEquation(List<TaskType> taskTypes) {
        taskTypes.add(new TaskType(Category.EQUATION, EquationTaskType.ADDITION.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.9),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.EQUATION, EquationTaskType.MULTIPLICATION.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.8),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.EQUATION, EquationTaskType.MODULO.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.8),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.15)
        ))));
        taskTypes.add(new TaskType(Category.EQUATION, EquationTaskType.FIND_X.name(), TaskRenderer.TEXT_EQUATION, TaskRenderer.EQUATION, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.95),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.05)
        ))));
    }

    private void initCountry(List<TaskType> taskTypes) {
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.COUNTRY_NAME_FROM_ALPHA_2.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.05)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.COUNTRY_NAME_FROM_CAPITAL_NAME.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.COUNTRY_NAME_FROM_MAP.name(), TaskRenderer.TEXT_IMAGE_SVG, TaskRenderer.TEXT, 3, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.COUNTRY_NAME_FROM_FLAG.name(), TaskRenderer.TEXT_IMAGE_SVG, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.CAPITAL_NAME_FROM_ALPHA_3.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.5),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.CAPITAL_NAME_FROM_COUNTRY_NAME.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.55),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.CAPITAL_NAME_FROM_MAP.name(), TaskRenderer.TEXT_IMAGE_SVG, TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.CAPITAL_NAME_FROM_FLAG.name(), TaskRenderer.TEXT_IMAGE_SVG, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.5),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.MAX_POPULATION.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 3, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.4)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.MIN_POPULATION.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.25),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.4)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.MAX_AREA.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 3, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.45)
        ))));
        taskTypes.add(new TaskType(Category.COUNTRY, CountryTaskType.MIN_AREA.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.6)
        ))));
    }

    private void initElement(List<TaskType> taskTypes) {
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.MAX_ATOMIC_MASS.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.MIN_ATOMIC_MASS.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 5, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.NAME_FROM_SYMBOL.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.4)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.SYMBOL_FROM_NAME.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 0, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.8)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.NAME_FROM_SHELL_COUNT.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.SYMBOL_FROM_SHELL_COUNT.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.NUMBER_FROM_SHELL_COUNT.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.7),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.NAME_FROM_NUMBER.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.5),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.2),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.3)
        ))));
        taskTypes.add(new TaskType(Category.ELEMENT, ElementTaskType.SYMBOL_FROM_NUMBER.name(), TaskRenderer.TEXT, TaskRenderer.TEXT, 10, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.MEMORY, 0.6),
                new TaskWisdomAttribute(WisdomAttribute.LOGIC, 0.15),
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.25)
        ))));
    }

    private void initRiddle(List<TaskType> taskTypes) {
        taskTypes.add(new TaskType(Category.RIDDLE, RiddleTaskType.MISSING_CLIPART.name(), TaskRenderer.TEXT_IMAGE_PNG, TaskRenderer.TEXT, 2, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.COMBINING_FACTS, 0.09),
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.41),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.1)
        ))));
        taskTypes.add(new TaskType(Category.RIDDLE, RiddleTaskType.FIND_CLIPART.name(), TaskRenderer.TEXT_IMAGE_PNG, TaskRenderer.TEXT, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.21),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.79)
        ))));
        taskTypes.add(new TaskType(Category.RIDDLE, RiddleTaskType.FIND_DIFFERENCE_LEFT_MISSING.name(), TaskRenderer.IMAGE_PNG_TEXT_IMAGE_PNG, TaskRenderer.TEXT, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.21),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.59),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.2)
        ))));
        taskTypes.add(new TaskType(Category.RIDDLE, RiddleTaskType.FIND_DIFFERENCE_RIGHT_MISSING.name(), TaskRenderer.IMAGE_PNG_TEXT_IMAGE_PNG, TaskRenderer.TEXT, 6, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.21),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.59),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.2)
        ))));
    }

    private void initColor(List<TaskType> taskTypes) {
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.COLOR_MIXING.name(), TaskRenderer.TEXT_HTML, TaskRenderer.HTML, 4, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.1),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.8)
        ))));
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.BIGGEST_R.name(), TaskRenderer.TEXT, TaskRenderer.HTML, 8, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.6)
        ))));
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.BIGGEST_G.name(), TaskRenderer.TEXT, TaskRenderer.HTML, 8, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.6)
        ))));
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.BIGGEST_B.name(), TaskRenderer.TEXT, TaskRenderer.HTML, 8, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.6)
        ))));
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.LOWEST_R.name(), TaskRenderer.TEXT, TaskRenderer.HTML, 8, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.6)
        ))));
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.LOWEST_G.name(), TaskRenderer.TEXT, TaskRenderer.HTML, 8, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.6)
        ))));
        taskTypes.add(new TaskType(Category.COLOR, ColorTaskType.LOWEST_B.name(), TaskRenderer.TEXT, TaskRenderer.HTML, 8, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PATTERN_RECOGNITION, 0.05),
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.35),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.6)
        ))));
    }

    private void initTime(List<TaskType> taskTypes) {
        taskTypes.add(new TaskType(Category.TIME, TimeTaskType.CLOCK_ADD.name(), TaskRenderer.TEXT_DATE, TaskRenderer.DATE, 1, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.45),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.3),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.25)
        ))));
        taskTypes.add(new TaskType(Category.TIME, TimeTaskType.CLOCK_SUBTRACT.name(), TaskRenderer.TEXT_DATE, TaskRenderer.DATE, 3, new HashSet<>(Arrays.asList(
                new TaskWisdomAttribute(WisdomAttribute.PERCEPTIVITY, 0.25),
                new TaskWisdomAttribute(WisdomAttribute.COUNTING, 0.4),
                new TaskWisdomAttribute(WisdomAttribute.IMAGINATION, 0.35)
        ))));
    }


}
