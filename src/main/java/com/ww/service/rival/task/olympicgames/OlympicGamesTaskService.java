package com.ww.service.rival.task.olympicgames;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.task.OlympicGamesType;
import com.ww.model.constant.rival.task.type.ElementTaskType;
import com.ww.model.constant.rival.task.type.OlympicGamesTaskType;
import com.ww.model.entity.rival.task.OlympicMedal;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.category.OlympicMedalRepository;
import com.ww.service.rival.task.element.ElementMatchAnswerTaskService;
import com.ww.service.rival.task.element.ElementOneCorrectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OlympicGamesTaskService {

//    @Autowired
//    private OlympicGamesMatchAnswerTaskService olympicGamesMatchAnswerTaskService;

    @Autowired
    private OlympicGamesOneCorrectTaskService olympicGamesOneCorrectTaskService;

    public Question generate(TaskType type, DifficultyLevel difficultyLevel) {
        OlympicGamesTaskType typeValue = OlympicGamesTaskType.valueOf(type.getValue());
//        if (typeValue == OlympicGamesTaskType.MAX_COUNTRY
//                ) {
//            return olympicGamesMatchAnswerTaskService.generate(type, difficultyLevel, typeValue);
//        }
        return olympicGamesOneCorrectTaskService.generate(type, difficultyLevel, typeValue);
    }
}
