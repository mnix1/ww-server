package com.ww.manager.rival.war.state.skill;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.WarState;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

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
        if (!team.getTeamSkills().canUse(Skill.WATER_PISTOL) || !team.getActiveTeamMember().isWisie() || wisieAnswerManager == null) {
            return;
        }
        team.getTeamSkills().use(Skill.WATER_PISTOL);
        wisieAnswerManager.getFlow().getSkillFlow().waterPistol();
        manager.sendNewSkillsModel();
    }
}
