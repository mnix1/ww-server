package com.ww.manager.rival.war.state.skill;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.WarState;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class WarStateKidnappingUsed extends WarState {

    private Long profileId;

    public WarStateKidnappingUsed(WarManager manager, Long profileId) {
        super(manager, STATE_TYPE_VOID);
        this.profileId = profileId;
    }

    @Override
    protected void processVoid() {
        WarTeam team = manager.getModel().getTeams().team(profileId);
        WarTeam opponentTeam = manager.getModel().getTeams().opponentTeam(profileId);
        if (!team.getTeamSkills().canUse(Skill.KIDNAPPING) || !team.getActiveTeamMember().isWisie() || !opponentTeam.getActiveTeamMember().isWisie()) {
            return;
        }
        team.getTeamSkills().use(Skill.KIDNAPPING);
        WisieAnswerManager wisieAnswerManager = manager.getModel().getWisieAnswerManager(team.getProfileId());
        WisieAnswerManager opponentWisieAnswerManager = manager.getModel().getWisieAnswerManager(opponentTeam.getProfileId());
        wisieAnswerManager.getFlow().getSkillFlow().kidnapping(opponentWisieAnswerManager);
        manager.sendNewSkillsModel();
    }
}
