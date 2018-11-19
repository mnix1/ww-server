package com.ww.service.rival.battle;

import com.ww.model.container.Resources;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.RivalService;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.season.RivalSeasonService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import org.springframework.stereotype.Service;

@Service
public class RivalBattleService extends RivalService {

    public RivalBattleService(ConnectionService connectionService, TaskGenerateService taskGenerateService, TaskRendererService taskRendererService, RewardService rewardService, RivalSeasonService rivalSeasonService, ProfileService profileService, RivalGlobalService rivalGlobalService, RivalProfileSeasonService rivalProfileSeasonService) {
        super(connectionService, taskGenerateService, taskRendererService, rewardService, rivalSeasonService, profileService, rivalGlobalService, rivalProfileSeasonService);
    }

    @Override
    public void addRewardFromWin(Profile profile) {
        getRewardService().addSendRewardFromRivalWin(profile, new Resources(1L));
    }
}
