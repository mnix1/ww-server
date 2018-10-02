package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.container.rival.war.WarProfileContainer;

import java.util.Map;

public class WarStateWaterPistolUsed extends WarState {

    private Long profileId;

    public WarStateWaterPistolUsed(WarManager manager, Long profileId) {
        super(manager);
        this.profileId = profileId;
    }

    @Override
    protected void processVoid() {
        WarProfileContainer container = manager.getContainer().getTeamsContainer().profileContainer(profileId);
        WarProfileContainer opponentContainer = manager.getContainer().getTeamsContainer().opponentProfileContainer(profileId);
        WisieAnswerManager wisieAnswerManager = manager.getContainer().getWisieAnswerManager(opponentContainer.getProfileId());
        if (container.getWaterPistols() <= 0 || !container.getActiveTeamMember().isWisie() || wisieAnswerManager == null) {
            return;
        }
        container.decreaseWaterPistols();
        wisieAnswerManager.getFlow().waterPistol();
    }
}
