package com.ww.service.rival.task.color;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.ColorTaskType;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColorTaskService {
    @Autowired
    ColorMixingTaskService colorMixingTaskService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        ColorTaskType typeValue = ColorTaskType.valueOf(type.getValue());
        return colorMixingTaskService.generate(type, difficultyLevel, typeValue);
    }
}
