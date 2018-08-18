package com.ww.service.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.TaskTypeRepository;
import com.ww.service.rival.task.chemistry.ChemistryTaskService;
import com.ww.service.rival.task.geography.GeographyTaskService;
import com.ww.service.rival.task.math.MathTaskService;
import com.ww.service.rival.task.memory.MemoryTaskService;
import com.ww.service.rival.task.music.MusicTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class TaskGenerateService {
    @Autowired
    MusicTaskService musicTaskService;

    @Autowired
    GeographyTaskService geographyTaskService;

    @Autowired
    MathTaskService mathTaskService;

    @Autowired
    MemoryTaskService memoryTaskService;

    @Autowired
    ChemistryTaskService chemistryTaskService;

    @Autowired
    TaskTypeRepository taskTypeRepository;

    public Question generate(Category category, TaskDifficultyLevel difficultyLevel) {
        if (category == Category.RANDOM) {
            category = Category.random();
        }
        TaskType taskType = findProperTaskType(category, difficultyLevel);
        if (category == Category.MUSIC) {
            return musicTaskService.generate(taskType, difficultyLevel, Language.ALL);
        }
        if (category == Category.GEOGRAPHY) {
            return geographyTaskService.generate(taskType, difficultyLevel);
        }
        if (category == Category.MATH) {
            return mathTaskService.generate(taskType, difficultyLevel);
        }
        if (category == Category.MEMORY) {
            return memoryTaskService.generate(taskType, difficultyLevel);
        }
        if (category == Category.CHEMISTRY) {
            return chemistryTaskService.generate(taskType, difficultyLevel);
        }
        return null;
    }

    private TaskType findProperTaskType(Category category, TaskDifficultyLevel difficultyLevel) {
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
