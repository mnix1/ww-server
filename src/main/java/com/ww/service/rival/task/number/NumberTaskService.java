package com.ww.service.rival.task.number;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.NumberTaskType;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NumberTaskService {

    private final NumberMatchAnswerTaskService numberMatchAnswerTaskService;
    private final NumberOneCorrectTaskService numberOneCorrectTaskService;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel) {
        NumberTaskType typeValue = NumberTaskType.valueOf(type.getValue());
        if (typeValue == NumberTaskType.MAX
                || typeValue == NumberTaskType.MIN) {
            return numberMatchAnswerTaskService.generate(type, difficultyLevel, typeValue);
        }

        return numberOneCorrectTaskService.generate(type, difficultyLevel, typeValue);
    }

}
