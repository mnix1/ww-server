package com.ww.manager.rival.war.skill.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.WarState;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;
import java.util.Optional;

public class WarStateLifebuoyUsed extends WarState {

    private Long profileId;
    private Map<String, Object> content;

    public WarStateLifebuoyUsed(WarManager manager, Long profileId, Map<String, Object> content) {
        super(manager, STATE_TYPE_VOID);
        this.profileId = profileId;
        this.content = content;
    }

    @Override
    protected void processVoid() {
        WarTeam team = manager.getModel().getTeams().team(profileId);
        if (!team.getTeamSkills().canUse(Skill.LIFEBUOY) || !content.containsKey("index")) {
            return;
        }
        team.getTeamSkills().use(Skill.LIFEBUOY);
        Integer teamMemberIndex = ((Integer) content.get("index"));
        Optional<TeamMember> optionalTeamMember = team.getTeamMembers().stream().filter(teamMember -> teamMember.getIndex() == teamMemberIndex).findFirst();
        if (!optionalTeamMember.isPresent()) {
            return;
        }
        TeamMember teamMember = optionalTeamMember.get();
        if (!teamMember.isWisie() || teamMember.isPresent()) {
            return;
        }
        teamMember.setPresent(true);
        team.getPresentIndexes().add(teamMemberIndex);
        manager.sendNewSkillsModel((m, wT) -> manager.getModelFactory().fillModelPresentIndexes(m, wT));
    }
}
