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
        WarTeam team = manager.getModel().getTeams().team(profileId);
        WarTeam opponentContainer = manager.getModel().getTeams().opponentTeam(profileId);
        WisieAnswerManager wisieAnswerManager = manager.getModel().getWisieAnswerManager(opponentContainer.getProfileId());
        if (!team.getTeamSkills().canUseWaterPistol() || !team.getActiveTeamMember().isWisie() || wisieAnswerManager == null) {
            return;
        }
        team.getTeamSkills().useWaterPistol();
        wisieAnswerManager.getFlow().waterPistol();
    }
}
