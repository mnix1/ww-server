package com.ww.service.rival.task.country;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.CountryTaskType;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CountryTaskService {

    private final CountryOneCorrectTaskService countryOneCorrectTaskService;
    private final CountryMatchAnswerTaskService countryMatchAnswerTaskService;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel) {
        CountryTaskType typeValue = CountryTaskType.valueOf(type.getValue());
        if (typeValue == CountryTaskType.MAX_AREA
                || typeValue == CountryTaskType.MIN_AREA
                || typeValue == CountryTaskType.MAX_POPULATION
                || typeValue == CountryTaskType.MIN_POPULATION) {
            return countryMatchAnswerTaskService.generate(type, difficultyLevel, typeValue);
        }

        return countryOneCorrectTaskService.generate(type, difficultyLevel, typeValue);
    }

}
