package com.ww.service.rival.task.number;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.NumberTaskType;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NumberTaskService {

    @Autowired
    NumberMatchAnswerTaskService numberMatchAnswerTaskService;

    @Autowired
    NumberOneCorrectTaskService numberOneCorrectTaskService;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel) {
        NumberTaskType typeValue = NumberTaskType.valueOf(type.getValue());
        if (typeValue == NumberTaskType.MAX
                || typeValue == NumberTaskType.MIN) {
            return numberMatchAnswerTaskService.generate(type, difficultyLevel, typeValue);
        }

        return numberOneCorrectTaskService.generate(type, difficultyLevel, typeValue);
    }

}
