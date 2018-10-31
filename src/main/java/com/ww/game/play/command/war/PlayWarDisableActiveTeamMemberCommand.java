package com.ww.game.play.command.war;

import com.ww.model.container.rival.war.WarTeam;
import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;

public class PlayWarDisableActiveTeamMemberCommand extends PlayCommand {
    private Long profileId;

    public PlayWarDisableActiveTeamMemberCommand(PlayContainer container, Long profileId) {
        super(container);
        this.profileId = profileId;
    }

    @Override
    public void execute() {
        WarTeam warTeam = (WarTeam) container.getTeams().team(profileId);
        warTeam.disableActiveTeamMember();
    }
}
