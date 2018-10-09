package com.ww.manager.rival.war.skill.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.WarState;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class WarStateChangeTaskUsed extends WarState {
    private Long profileId;

    public WarStateChangeTaskUsed(WarManager manager, Long profileId) {
        super(manager, STATE_TYPE_VOID);
        this.profileId = profileId;
    }

    @Override
    protected void processVoid() {
        WarTeam team =(WarTeam)  manager.getTeam(profileId);
        if (!team.getTeamSkills().canUse(Skill.CHANGE_TASK)
                || !team.getActiveTeamMember().isWisie() || !team.istActiveTeamMemberPresent()) {
            return;
        }
        team.getTeamSkills().use(Skill.CHANGE_TASK);
        WisieAnswerManager wisieAnswerManager = manager.getModel().getWisieAnswerManager(team.getProfileId());
        wisieAnswerManager.getFlow().getChangeTaskSkillFlow().changeTask();
        manager.sendNewSkillsModel();
    }
}
