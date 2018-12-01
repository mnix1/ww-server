package com.ww.service.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.RivalService;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ConnectionService;
import com.ww.service.wisie.ProfileWisieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RivalWisieService extends RivalService {

    protected final ProfileWisieService profileWisieService;
    private final TaskService taskService;

    public RivalWisieService(ConnectionService connectionService, TaskGenerateService taskGenerateService, TaskRendererService taskRendererService, RivalGlobalService rivalGlobalService, RivalProfileSeasonService rivalProfileSeasonService, ProfileWisieService profileWisieService, TaskService taskService) {
        super(connectionService, taskGenerateService, taskRendererService, rivalGlobalService, rivalProfileSeasonService);
        this.profileWisieService = profileWisieService;
        this.taskService = taskService;
    }

    public List<ProfileWisie> getProfileWisies(Profile profile) {
        return profileWisieService.findAllInTeam(profile.getId());
    }

    @Override
    public Question prepareQuestion(Category category, DifficultyLevel difficultyLevel, Language language) {
        Question question = super.prepareQuestion(category, difficultyLevel, language);
        initTaskWisdomAttributes(question);
        return question;
    }

    public void initTaskWisdomAttributes(Question question) {
        taskService.initTaskWisdomAttributes(question);
    }
}
