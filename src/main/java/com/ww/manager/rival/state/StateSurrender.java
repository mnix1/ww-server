package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalStatus;

import java.util.HashMap;
import java.util.Map;

public class StateSurrender extends State {

    private Long profileId;

    public StateSurrender(RivalManager manager, Long profileId) {
        super(manager, STATE_TYPE_VOID);
        this.profileId = profileId;
    }

    @Override
    protected void processVoid() {
        manager.getModel().setStatus(RivalStatus.CLOSED);
        if(manager.getModel().isOpponent()) {
            manager.getModel().setWinnerLooser(manager.getModel().getTeams().opponentTeam(profileId).getProfile());
            manager.getModel().setResigned(true);
        } else {
            manager.getModel().setDraw(true);
        }
        manager.updateProfilesElo();
        manager.getModel().getTeams().forEachTeam(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelClosed(model, profileContainer);
            manager.getModelFactory().fillModelEloChanged(model, profileContainer);
            manager.send(model, manager.getMessageContent(), profileContainer.getProfileId());
        });
        manager.getAbstractRivalService().disposeManager(manager);
    }
}
