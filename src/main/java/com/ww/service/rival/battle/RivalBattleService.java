package com.ww.service.rival.battle;

import com.ww.game.play.container.PlayContainer;
import com.ww.model.constant.social.ExperienceSource;
import com.ww.model.container.Resources;
import com.ww.model.container.rival.RivalResult;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.RivalService;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ConnectionService;
import com.ww.service.social.ExperienceService;
import com.ww.service.social.RewardService;
import org.springframework.stereotype.Service;

@Service
public class RivalBattleService extends RivalService {

    private final RewardService rewardService;
    private final ExperienceService experienceService;

    public RivalBattleService(ConnectionService connectionService, TaskGenerateService taskGenerateService, TaskRendererService taskRendererService, RivalGlobalService rivalGlobalService, RivalProfileSeasonService rivalProfileSeasonService, RewardService rewardService, ExperienceService experienceService) {
        super(connectionService, taskGenerateService, taskRendererService, rivalGlobalService, rivalProfileSeasonService);
        this.rewardService = rewardService;
        this.experienceService = experienceService;
    }

    @Override
    public void addReward(Profile winner, ProfileSeason winnerSeason) {
        rewardService.addSendRewardFromRivalWin(winner, winnerSeason, new Resources(1L));
    }

    @Override
    public void addExperience(PlayContainer container) {
        RivalResult result = container.getResult();
        if (result.getDraw()) {
            experienceService.add(container.getInit().getCreatorProfile().getId(), ExperienceSource.BATTLE_LOST.getGain());
            experienceService.add(container.getInit().getOpponentProfile().getId(), ExperienceSource.BATTLE_LOST.getGain());
        } else {
            Long winnerId = result.getWinner().getId();
            experienceService.add(winnerId, ExperienceSource.BATTLE_WIN.getGain());
            experienceService.add(container.getTeams().opponent(winnerId).getProfileId(), ExperienceSource.BATTLE_LOST.getGain());
        }
    }
}
