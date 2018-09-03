package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.entity.social.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StateClose extends State {

    public StateClose(RivalManager manager) {
        super(manager);
    }

    @Override
    protected void processVoid() {
        rivalContainer.setStatus(RivalStatus.CLOSED);
        Optional<Profile> optionalWinner = rivalContainer.findWinner();
        if (optionalWinner.isPresent()) {
            rivalContainer.setWinnerLooser(optionalWinner.get());
        } else {
            rivalContainer.setDraw(true);
        }
        rivalContainer.setResigned(false);
        rivalManager.updateProfilesElo();
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelEloChanged(model, rivalProfileContainer);
            rivalContainer.fillModelClosed(model, rivalProfileContainer);
            rivalManager.send(model, rivalManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        rivalManager.getRivalService().disposeManager(rivalManager);
    }
}
