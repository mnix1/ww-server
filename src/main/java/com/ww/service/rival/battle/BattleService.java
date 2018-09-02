package com.ww.service.rival.battle;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.social.Profile;
import com.ww.service.rival.RankingService;
import com.ww.service.rival.RivalService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.RewardService;
import com.ww.websocket.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BattleService extends RivalService {

    @Autowired
    protected ProfileConnectionService profileConnectionService;

    @Autowired
    protected TaskGenerateService taskGenerateService;

    @Autowired
    protected TaskRendererService taskRendererService;
    @Autowired
    protected RewardService rewardService;
    @Autowired
    protected RankingService rankingService;

    @Override
    protected void addRewardFromWin(Profile winner) {
        rewardService.addRewardFromBattleWin(winner);
    }

    @Override
    protected void rankingGameResult( Boolean isDraw, Profile winner, Profile looser) {
        rankingService.rankingGameResult(RivalType.BATTLE, isDraw, winner, looser);
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
        return Message.BATTLE_CONTENT;
    }

}
