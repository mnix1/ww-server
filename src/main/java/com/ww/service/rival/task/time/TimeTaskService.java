package com.ww.service.rival.task.time;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.RiddleTaskType;
import com.ww.model.constant.rival.task.type.TimeTaskType;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.service.rival.task.riddle.RiddleClipartTaskService;
import com.ww.service.rival.task.riddle.RiddleColorTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTaskService {
    @Autowired
    TimeClockTaskService timeClockTaskService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        TimeTaskType typeValue = TimeTaskType.valueOf(type.getValue());
        if (typeValue == TimeTaskType.CLOCK_ADD
                || typeValue == TimeTaskType.CLOCK_SUBTRACT) {
            return timeClockTaskService.generate(type, difficultyLevel, typeValue);
        }

        return null;
    }
}
