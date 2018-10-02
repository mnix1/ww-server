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
        manager.getContainer().setStatus(RivalStatus.CLOSED);
        if(manager.getContainer().isOpponent()) {
            manager.getContainer().setWinnerLooser(manager.getContainer().getTeamsContainer().opponentTeamContainer(profileId).getProfile());
            manager.getContainer().setResigned(true);
        } else {
            manager.getContainer().setDraw(true);
        }
        manager.updateProfilesElo();
        manager.getContainer().getTeamsContainer().forEachProfile(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelClosed(model, profileContainer);
            manager.getModelFactory().fillModelEloChanged(model, profileContainer);
            manager.send(model, manager.getMessageContent(), profileContainer.getProfileId());
        });
        manager.getAbstractRivalService().disposeManager(manager);
    }
}
