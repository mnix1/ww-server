package com.ww.service.rival.task.geography;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.GeographyTaskType;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeographyTaskService {

    @Autowired
    GeographyTaskCountryCapitalTypeService geographyTaskCountryCapitalTypeService;
    @Autowired
    GeographyTaskMinMaxTypeService geographyTaskMinMaxTypeService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        GeographyTaskType typeValue = GeographyTaskType.valueOf(type.getValue());
        if (typeValue == GeographyTaskType.MAX_AREA
                || typeValue == GeographyTaskType.MIN_AREA
                || typeValue == GeographyTaskType.MAX_POPULATION
                || typeValue == GeographyTaskType.MIN_POPULATION) {
            return geographyTaskMinMaxTypeService.generate(type, typeValue);
        }

        return geographyTaskCountryCapitalTypeService.generate(type, typeValue);
    }

}
