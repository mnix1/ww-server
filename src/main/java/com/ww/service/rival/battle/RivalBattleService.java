package com.ww.service.rival.battle;

import com.ww.model.entity.social.Profile;
import com.ww.service.rival.AbstractRivalService;
import com.ww.service.rival.GlobalRivalService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import com.ww.websocket.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RivalBattleService extends AbstractRivalService {

    @Autowired
    protected ProfileConnectionService profileConnectionService;

    @Autowired
    protected TaskGenerateService taskGenerateService;

    @Autowired
    protected TaskRendererService taskRendererService;

    @Autowired
    protected RewardService rewardService;

    @Autowired
    protected ProfileService profileService;

    @Autowired
    protected GlobalRivalService globalRivalService;

    @Override
    public GlobalRivalService getGlobalRivalService() {
        return globalRivalService;
    }

    @Override
    protected void addRewardFromWin(Profile winner) {
        rewardService.addRewardFromBattleWin(winner);
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
        return Message.BATTLE_CONTENT;
    }

}
