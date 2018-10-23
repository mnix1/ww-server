package com.ww.service.rival.task.riddle;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.RiddleTaskType;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RiddleTaskService {
    private final RiddleClipartTaskService riddleClipartTaskService;
    private final RiddleDifferenceTaskService riddleDifferenceTaskService;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel) {
        RiddleTaskType typeValue = RiddleTaskType.valueOf(type.getValue());
        if (typeValue == RiddleTaskType.MISSING_CLIPART
                || typeValue == RiddleTaskType.FIND_CLIPART) {
            return riddleClipartTaskService.generate(type, difficultyLevel, typeValue);
        }
        return riddleDifferenceTaskService.generate(type, difficultyLevel, typeValue);
    }
}
