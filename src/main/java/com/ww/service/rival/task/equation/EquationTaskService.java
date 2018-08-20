package com.ww.service.rival.task.equation;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.EquationTaskType;
import com.ww.model.constant.rival.task.type.NumberTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.rival.task.number.NumberMatchAnswerTaskService;
import com.ww.service.rival.task.number.NumberOneCorrectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.ww.helper.AnswerHelper.difficultyCalibration;
import static com.ww.helper.AnswerHelper.numbersToString;
import static com.ww.helper.RandomHelper.randomInteger;
import static com.ww.helper.RandomHelper.randomIntegers;

@Service
public class EquationTaskService {

    @Autowired
    EquationOperationTaskService equationOperationTaskService;

    @Autowired
    EquationSolveTaskService equationSolveTaskService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        EquationTaskType typeValue = EquationTaskType.valueOf(type.getValue());
        if (typeValue == EquationTaskType.ADDITION
                || typeValue == EquationTaskType.MULTIPLICATION
                || typeValue == EquationTaskType.MODULO) {
            return equationOperationTaskService.generate(type, difficultyLevel, typeValue);
        }
        return equationSolveTaskService.generate(type, difficultyLevel, typeValue);
    }

}
