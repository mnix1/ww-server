package com.ww.service.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.task.GeographyTaskType;
import com.ww.model.constant.rival.task.MathTaskType;
import com.ww.model.constant.rival.task.MemoryTaskType;
import com.ww.model.constant.rival.task.MusicTaskType;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import com.ww.repository.rival.task.AnswerRepository;
import com.ww.repository.rival.task.ProfileQuestionRepository;
import com.ww.repository.rival.task.QuestionRepository;
import com.ww.service.rival.task.geography.GeographyTaskService;
import com.ww.service.rival.task.math.MathTaskService;
import com.ww.service.rival.task.memory.MemoryTaskService;
import com.ww.service.rival.task.music.MusicTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class TaskGenerateService {
    @Autowired
    MusicTaskService musicTaskService;

    @Autowired
    GeographyTaskService geographyTaskService;

    @Autowired
    MathTaskService mathTaskService;

    @Autowired
    MemoryTaskService memoryTaskService;

    public Question generate(Category category) {
        if (category == Category.MUSIC) {
            return musicTaskService.generate(Language.ALL, MusicTaskType.random());
        }
        if (category == Category.GEOGRAPHY) {
            return geographyTaskService.generate(GeographyTaskType.random());
        }
        if (category == Category.MATH) {
            return mathTaskService.generate(MathTaskType.random());
        }
        if (category == Category.MEMORY) {
            return memoryTaskService.generate(MemoryTaskType.random());
        }
        return null;
    }

}
