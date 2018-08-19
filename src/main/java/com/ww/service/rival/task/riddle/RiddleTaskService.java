package com.ww.service.rival.task.riddle;

import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.constant.rival.task.type.CountryTaskType;
import com.ww.model.constant.rival.task.type.RiddleTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Clipart;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.rival.task.TaskType;
import com.ww.repository.rival.task.category.ClipartRepository;
import com.ww.service.rival.task.country.CountryMatchAnswerTaskService;
import com.ww.service.rival.task.country.CountryOneCorrectTaskService;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiddleTaskService {
    @Autowired
    RiddleClipartTaskService riddleClipartTaskService;
    @Autowired
    RiddleColorTaskService riddleColorTaskService;

    public Question generate(TaskType type, TaskDifficultyLevel difficultyLevel) {
        RiddleTaskType typeValue = RiddleTaskType.valueOf(type.getValue());
        if (typeValue == RiddleTaskType.MISSING_CLIPART) {
            return riddleClipartTaskService.generate(type, difficultyLevel, typeValue);
        }

        return riddleColorTaskService.generate(type, difficultyLevel, typeValue);
    }
}
