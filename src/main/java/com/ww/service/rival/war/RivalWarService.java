package com.ww.service.rival.war;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.RivalService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.wisie.ProfileWisieService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class RivalWarService extends RivalService {

    @Autowired
    private ProfileWisieService profileWisieService;

    @Autowired
    private TaskService taskService;

    @Override
    public void addRewardFromWin(Profile profile) {
        getRewardService().addRewardFromWarWin(profile);
    }

    public List<ProfileWisie> getProfileWisies(Profile profile) {
        return profileWisieService.findAllInTeam(profile.getId());
    }

    @Override
    public Question prepareQuestion(Category category, DifficultyLevel difficultyLevel) {
        Question question = super.prepareQuestion(category, difficultyLevel);
        initTaskWisdomAttributes(question);
        return question;
    }

    public void initTaskWisdomAttributes(Question question) {
        taskService.initTaskWisdomAttributes(question);
    }
}
