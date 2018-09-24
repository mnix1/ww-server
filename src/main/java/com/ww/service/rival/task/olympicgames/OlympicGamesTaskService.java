package com.ww.service.rival.task.olympicgames;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.type.OlympicGamesTaskType;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OlympicGamesTaskService {

    @Autowired
    private OlympicGamesOneCorrectTaskService olympicGamesOneCorrectTaskService;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel) {
        OlympicGamesTaskType typeValue = OlympicGamesTaskType.valueOf(type.getValue());
        return olympicGamesOneCorrectTaskService.generate(type, difficultyLevel, typeValue);
    }
}
