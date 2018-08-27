package com.ww.manager;

import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.container.rival.war.WarProfileContainer;
import com.ww.service.rival.war.WarService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;

public class WarManager extends RivalManager {

    protected Integer getIntroInterval(){
        return 20500;
    }

    public WarManager(RivalInitContainer bic, WarService warService, ProfileConnectionService profileConnectionService) {
        this.rivalService = warService;
        this.profileConnectionService = profileConnectionService;
        Long creatorId = bic.getCreatorProfile().getId();
        Long opponentId = bic.getOpponentProfile().getId();
        this.rivalContainer = new WarContainer();
        this.rivalContainer.addProfile(creatorId, new WarProfileContainer(bic.getCreatorProfile(), warService.getProfileHeroes(creatorId), opponentId));
        this.rivalContainer.addProfile(opponentId, new WarProfileContainer(bic.getOpponentProfile(), warService.getProfileHeroes(opponentId), creatorId));
    }

    protected Message getMessageReadyFast() {
        return Message.WAR_READY_FAST;
    }

    protected Message getMessageContent() {
        return Message.WAR_CONTENT;
    }
}
