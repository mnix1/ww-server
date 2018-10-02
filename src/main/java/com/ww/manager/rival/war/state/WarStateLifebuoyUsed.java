package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WarStateLifebuoyUsed extends WarState {

    private Long profileId;
    private Map<String, Object> content;

    public WarStateLifebuoyUsed(WarManager manager, Long profileId, Map<String, Object> content) {
        super(manager);
        this.profileId = profileId;
        this.content = content;
    }

    @Override
    protected void processVoid() {
        WarTeam team = manager.getModel().getTeams().team(profileId);
        if (!team.getTeamSkills().canUseLifebuoy() || !content.containsKey("index")) {
            return;
        }
        team.getTeamSkills().useLifebuoy();
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
        manager.getModel().getTeams().forEachTeam(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelPresentIndexes(model, profileContainer);
            manager.getModelFactory().fillModelSkills(model, profileContainer);
            this.manager.send(model, this.manager.getMessageContent(), profileContainer.getProfileId());
        });
    }
}
