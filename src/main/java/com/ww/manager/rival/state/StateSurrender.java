package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalStatus;

import java.util.HashMap;
import java.util.Map;

public class StateSurrender extends State {

    private Long profileId;

    public StateSurrender(RivalManager manager, Long profileId) {
        super(manager);
        this.profileId = profileId;
    }

    @Override
    protected void processVoid() {
        rivalContainer.setStatus(RivalStatus.CLOSED);
        Long winnerId = rivalContainer.getProfileIdRivalProfileContainerMap().get(profileId).getOpponentId();
        rivalContainer.setWinner(winnerId);
        rivalContainer.setResigned(true);
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelClosed(model, rivalProfileContainer);
            rivalManager.send(model, rivalManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        rivalManager.rivalService.disposeManager(rivalManager);
    }
}
