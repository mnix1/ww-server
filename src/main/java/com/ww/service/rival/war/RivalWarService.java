package com.ww.service.rival.war;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.rival.AbstractRivalService;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import com.ww.service.wisie.ProfileWisieService;
import com.ww.websocket.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RivalWarService extends AbstractRivalService {

    @Autowired
    protected ProfileWisieService profileWisieService;

    @Autowired
    protected ProfileConnectionService profileConnectionService;

    @Autowired
    protected TaskGenerateService taskGenerateService;

    @Autowired
    protected TaskRendererService taskRendererService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected RewardService rewardService;

    @Autowired
    protected ProfileService profileService;

    @Autowired
    protected RivalGlobalService rivalGlobalService;

    @Override
    public RivalGlobalService getRivalGlobalService() {
        return rivalGlobalService;
    }

    @Override
    protected void addRewardFromWin(Profile profile) {
        rewardService.addRewardFromWarWin(profile);
    }

    @Override
    protected ProfileConnectionService getProfileConnectionService() {
        return profileConnectionService;
    }

    @Override
    protected TaskGenerateService getTaskGenerateService() {
        return taskGenerateService;
    }

    @Override
    protected TaskRendererService getTaskRendererService() {
        return taskRendererService;
    }

    @Override
    public ProfileService getProfileService() {
        return profileService;
    }

    @Override
    public Message getMessageContent() {
        return Message.WAR_CONTENT;
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
