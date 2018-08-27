package com.ww.manager;

import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.battle.BattleContainer;
import com.ww.model.container.rival.battle.BattleProfileContainer;
import com.ww.service.rival.battle.BattleService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;

public class BattleManager extends RivalManager {

    public BattleManager(RivalInitContainer bic, BattleService battleService, ProfileConnectionService profileConnectionService) {
        this.rivalService = battleService;
        this.profileConnectionService = profileConnectionService;
        Long creatorId = bic.getCreatorProfile().getId();
        Long opponentId = bic.getOpponentProfile().getId();
        this.rivalContainer = new BattleContainer();
        this.rivalContainer.addProfile(creatorId, new BattleProfileContainer(bic.getCreatorProfile(), opponentId));
        this.rivalContainer.addProfile(opponentId, new BattleProfileContainer(bic.getOpponentProfile(), creatorId));
    }

    protected Message getMessageReadyFast() {
        return Message.BATTLE_READY_FAST;
    }

    protected Message getMessageContent() {
        return Message.BATTLE_CONTENT;
    }
}
