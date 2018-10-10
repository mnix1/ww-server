package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.entity.outside.social.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StateClose extends State {

    public StateClose(RivalManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    protected void processVoid() {
        manager.getModel().setStatus(RivalStatus.CLOSED);
        Optional<Profile> optionalWinner = manager.getModel().findWinner();
        if (optionalWinner.isPresent()) {
            manager.getModel().setWinnerLooser(optionalWinner.get());
        } else {
            manager.getModel().setDraw(true);
        }
        manager.getModel().setResigned(false);
        manager.getRivalService().updateProfilesElo(manager);
        manager.getModel().getTeams().forEachTeam(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelEloChanged(model, profileContainer);
            manager.getModelFactory().fillModelClosed(model, profileContainer);
            manager.send(model, manager.getMessageContent(), profileContainer.getProfileId());
        });
        manager.getRivalService().disposeManager(manager);
    }
}
