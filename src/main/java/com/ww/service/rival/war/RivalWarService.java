package com.ww.service.rival.war;

import com.ww.game.play.container.PlayContainer;
import com.ww.model.constant.social.ExperienceSource;
import com.ww.model.container.Resources;
import com.ww.model.container.rival.RivalResult;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.RivalWisieService;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ConnectionService;
import com.ww.service.social.ExperienceService;
import com.ww.service.social.RewardService;
import com.ww.service.wisie.ProfileWisieService;
import org.springframework.stereotype.Service;

@Service
public class RivalWarService extends RivalWisieService {

    private final RewardService rewardService;
    private final ExperienceService experienceService;

    public RivalWarService(ConnectionService connectionService, TaskGenerateService taskGenerateService, TaskRendererService taskRendererService, RivalGlobalService rivalGlobalService, RivalProfileSeasonService rivalProfileSeasonService, ProfileWisieService profileWisieService, TaskService taskService, RewardService rewardService, ExperienceService experienceService) {
        super(connectionService, taskGenerateService, taskRendererService, rivalGlobalService, rivalProfileSeasonService, profileWisieService, taskService);
        this.rewardService = rewardService;
        this.experienceService = experienceService;
    }

    @Override
    public void addReward(Profile winner, ProfileSeason winnerSeason) {
        rewardService.addSendRewardFromRivalWin(winner, winnerSeason, new Resources(2L));
    }

    @Override
    public void addExperience(PlayContainer container) {
        RivalResult result = container.getResult();
        if (result.getDraw()) {
            experienceService.add(container.getInit().getCreatorProfile().getId(), ExperienceSource.WAR_LOST.getGain());
            experienceService.add(container.getInit().getCreatorProfile().getId(), ExperienceSource.WAR_LOST.getGain());
        } else {
            Long winnerId = result.getWinner().getId();
            experienceService.add(winnerId, ExperienceSource.WAR_WIN.getGain());
            experienceService.add(container.getTeams().opponent(winnerId).getProfileId(), ExperienceSource.WAR_LOST.getGain());
        }
    }
}
