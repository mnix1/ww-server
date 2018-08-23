package com.ww.service.rival.task.riddle;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.RiddleTaskType;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiddleTaskService {
    @Autowired
    RiddleClipartTaskService riddleClipartTaskService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        RiddleTaskType typeValue = RiddleTaskType.valueOf(type.getValue());
        if (typeValue == RiddleTaskType.MISSING_CLIPART) {
            return riddleClipartTaskService.generate(type, difficultyLevel, typeValue);
        }
        return null;
    }
}
