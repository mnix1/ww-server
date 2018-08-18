package com.ww.service.rival.task.chemistry;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.ChemistryTaskType;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class ChemistryTaskService {

    @Autowired
    ChemistryTaskOneCorrectService chemistryTaskOneCorrectService;

    @Autowired
    ChemistryTaskMinMaxService chemistryTaskMinMaxService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        ChemistryTaskType typeValue = ChemistryTaskType.valueOf(type.getValue());
        if (typeValue == ChemistryTaskType.MIN_ATOMIC_MASS
                || typeValue == ChemistryTaskType.MAX_ATOMIC_MASS) {
            return chemistryTaskMinMaxService.generate(type, difficultyLevel, typeValue);
        }
        return chemistryTaskOneCorrectService.generate(type, difficultyLevel, typeValue);
    }
}
