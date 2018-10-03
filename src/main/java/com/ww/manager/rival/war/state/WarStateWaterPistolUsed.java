package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;

public class WarStateWaterPistolUsed extends WarState {

    private Long profileId;

    public WarStateWaterPistolUsed(WarManager manager, Long profileId) {
        super(manager, STATE_TYPE_VOID);
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
        manager.getModel().getTeams().forEachTeam(rivalTeam -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelSkills(model, rivalTeam);
            this.manager.send(model, this.manager.getMessageContent(), rivalTeam.getProfileId());
        });
    }
}
