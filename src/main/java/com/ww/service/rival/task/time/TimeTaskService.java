package com.ww.service.rival.task.time;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.TimeTaskType;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTaskService {
    @Autowired
    TimeClockTaskService timeClockTaskService;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel) {
        TimeTaskType typeValue = TimeTaskType.valueOf(type.getValue());
        if (typeValue == TimeTaskType.ANALOG_CLOCK_ADD
                || typeValue == TimeTaskType.ANALOG_CLOCK_SUBTRACT
                || typeValue == TimeTaskType.DIGITAL_CLOCK_ADD
                || typeValue == TimeTaskType.DIGITAL_CLOCK_SUBTRACT
                ) {
            return timeClockTaskService.generate(type, difficultyLevel, typeValue);
        }

        return null;
    }
}
