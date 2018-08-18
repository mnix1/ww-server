package com.ww.service.rival.task.country;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.CountryTaskType;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryTaskService {

    @Autowired
    CountryTaskOneCorrectTypeService countryTaskOneCorrectTypeService;
    @Autowired
    CountryTaskMinMaxTypeService countryTaskMinMaxTypeService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        CountryTaskType typeValue = CountryTaskType.valueOf(type.getValue());
        if (typeValue == CountryTaskType.MAX_AREA
                || typeValue == CountryTaskType.MIN_AREA
                || typeValue == CountryTaskType.MAX_POPULATION
                || typeValue == CountryTaskType.MIN_POPULATION) {
            return countryTaskMinMaxTypeService.generate(type, difficultyLevel, typeValue);
        }

        return countryTaskOneCorrectTypeService.generate(type, difficultyLevel, typeValue);
    }

}
