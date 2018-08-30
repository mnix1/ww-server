package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalStatus;

import java.util.HashMap;
import java.util.Map;

public class StateClose extends State {

    public StateClose(RivalManager manager) {
        super(manager);
    }

    @Override
    protected void processVoid() {
        rivalContainer.setStatus(RivalStatus.CLOSED);
        String winnerTag = rivalContainer.findWinnerTag();
        rivalContainer.setWinnerTag(winnerTag);
        rivalContainer.setResigned(false);
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelClosed(model, rivalProfileContainer);
            rivalManager.send(model, rivalManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        rivalManager.rivalService.disposeManager(rivalManager);
    }
}
