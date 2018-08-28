package com.ww.service.rival.war;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.rival.task.Question;
import com.ww.service.hero.ProfileHeroService;
import com.ww.service.rival.RivalService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.RewardService;
import com.ww.websocket.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarService extends RivalService {

    @Autowired
    protected ProfileHeroService profileHeroService;

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

    @Override
    protected void addRewardFromWin(String winnerTag) {
        rewardService.addRewardFromWarWin(winnerTag);
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
    protected Message getMessageContent() {
        return Message.WAR_CONTENT;
    }

    public List<ProfileHero> getProfileHeroes(Long profileId) {
        return profileHeroService.listTeam(profileId);
    }

    public Question prepareQuestion(Category category, TaskDifficultyLevel difficultyLevel) {
        Question question = super.prepareQuestion(category, difficultyLevel);
        taskService.initTaskWisdomAtributes(question);
        return question;
    }
}
