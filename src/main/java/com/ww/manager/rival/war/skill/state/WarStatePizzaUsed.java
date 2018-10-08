package com.ww.manager.rival.war.skill.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.WarState;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class WarStatePizzaUsed extends WarState {
    private Long profileId;

    public WarStatePizzaUsed(WarManager manager, Long profileId) {
        super(manager, STATE_TYPE_VOID);
        this.profileId = profileId;
    }

    @Override
    protected void processVoid() {
        WarTeam team =(WarTeam)  manager.getTeam(profileId);
        WarTeam opponentTeam = manager.getModel().getTeams().opponentTeam(profileId);
        if (!team.getTeamSkills().canUse(Skill.PIZZA)
                || !opponentTeam.getActiveTeamMember().isWisie() || !opponentTeam.istActiveTeamMemberPresent()
                || !team.getActiveTeamMember().isWisie() || !team.istActiveTeamMemberPresent()) {
            return;
        }
        team.getTeamSkills().use(Skill.PIZZA);
        WisieAnswerManager wisieAnswerManager = manager.getModel().getWisieAnswerManager(team.getProfileId());
        WisieAnswerManager opponentWisieAnswerManager = manager.getModel().getWisieAnswerManager(opponentTeam.getProfileId());
        wisieAnswerManager.getFlow().getPizzaSkillFlow().pizza(opponentWisieAnswerManager);
        manager.sendNewSkillsModel();
    }
}
