package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarProfileContainer;

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
        WarProfileContainer container = manager.getContainer().getTeamsContainer().profileContainer(profileId);
        if (container.getLifebuoys() <= 0 || !content.containsKey("index")) {
            return;
        }
        container.decreaseLifebuoys();
        Integer teamMemberIndex = ((Integer) content.get("index"));
        Optional<TeamMember> optionalTeamMember = container.getTeamMembers().stream().filter(teamMember -> teamMember.getIndex() == teamMemberIndex).findFirst();
        if (!optionalTeamMember.isPresent()) {
            return;
        }
        TeamMember teamMember = optionalTeamMember.get();
        if (!teamMember.isWisie() || teamMember.isPresent()) {
            return;
        }
        teamMember.setPresent(true);
        container.getPresentIndexes().add(teamMemberIndex);
        manager.getContainer().getTeamsContainer().forEachProfile(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelChoosingWhoAnswer(model, profileContainer);
            this.manager.send(model, this.manager.getMessageContent(), profileContainer.getProfileId());
        });
    }
}
