package com.ww.service.rival.task.element;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.ElementTaskType;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class ElementTaskService {

    @Autowired
    ElementOneCorrectTaskService elementOneCorrectTaskService;

    @Autowired
    ElementMatchAnswerTaskService elementMatchAnswerTaskService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        ElementTaskType typeValue = ElementTaskType.valueOf(type.getValue());
        if (typeValue == ElementTaskType.MIN_ATOMIC_MASS
                || typeValue == ElementTaskType.MAX_ATOMIC_MASS) {
            return elementMatchAnswerTaskService.generate(type, difficultyLevel, typeValue);
        }
        return elementOneCorrectTaskService.generate(type, difficultyLevel, typeValue);
    }
}
