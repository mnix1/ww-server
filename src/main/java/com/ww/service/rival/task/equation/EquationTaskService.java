package com.ww.service.rival.task.equation;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.EquationTaskType;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ww.helper.AnswerHelper.difficultyCalibration;

@Service
@AllArgsConstructor
public class EquationTaskService {

    private final EquationOperationTaskService equationOperationTaskService;
    private final EquationSolveTaskService equationSolveTaskService;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel) {
        EquationTaskType typeValue = EquationTaskType.valueOf(type.getValue());
        if (typeValue == EquationTaskType.ADDITION
                || typeValue == EquationTaskType.MULTIPLICATION
                || typeValue == EquationTaskType.MODULO) {
            return equationOperationTaskService.generate(type, difficultyLevel, typeValue);
        }
        return equationSolveTaskService.generate(type, difficultyLevel, typeValue);
    }

}
