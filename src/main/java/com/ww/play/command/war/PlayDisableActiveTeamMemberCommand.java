package com.ww.play.command.war;

import com.ww.model.container.rival.war.WarTeam;
import com.ww.play.command.PlayCommand;
import com.ww.play.container.PlayContainer;

public class PlayDisableActiveTeamMemberCommand extends PlayCommand {
    private Long profileId;

    public PlayDisableActiveTeamMemberCommand(PlayContainer container, Long profileId) {
        super(container);
        this.profileId = profileId;
    }

    @Override
    public void execute() {
        WarTeam warTeam = (WarTeam) container.getTeams().team(profileId);
        warTeam.disableActiveTeamMember();
    }
}
