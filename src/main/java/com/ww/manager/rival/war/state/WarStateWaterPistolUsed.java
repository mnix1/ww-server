package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.container.rival.war.WarTeam;

public class WarStateWaterPistolUsed extends WarState {

    private Long profileId;

    public WarStateWaterPistolUsed(WarManager manager, Long profileId) {
        super(manager);
        this.profileId = profileId;
    }

    @Override
    protected void processVoid() {
        WarTeam container = manager.getModel().getTeamsContainer().team(profileId);
        WarTeam opponentContainer = manager.getModel().getTeamsContainer().opponentTeam(profileId);
        WisieAnswerManager wisieAnswerManager = manager.getModel().getWisieAnswerManager(opponentContainer.getProfileId());
        if (container.getTeamSkills().getWaterPistols() <= 0 || !container.getActiveTeamMember().isWisie() || wisieAnswerManager == null) {
            return;
        }
        container.getTeamSkills().decreaseWaterPistols();
        wisieAnswerManager.getFlow().waterPistol();
    }
}
