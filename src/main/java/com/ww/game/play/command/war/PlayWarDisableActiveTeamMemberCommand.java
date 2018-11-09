package com.ww.game.play.command.war;

import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.container.rival.war.WarTeam;

public class PlayWarDisableActiveTeamMemberCommand extends PlayCommand {
    private Long profileId;
    private WarTeam warTeam;

    public PlayWarDisableActiveTeamMemberCommand(PlayContainer container, Long profileId) {
        super(container);
        this.profileId = profileId;
    }

    public PlayWarDisableActiveTeamMemberCommand(WarTeam warTeam) {
        super(null);
        this.warTeam = warTeam;
    }

    @Override
    public void execute() {
        if (warTeam == null) {
            warTeam = (WarTeam) container.getTeams().team(profileId);
        }
        warTeam.disableActiveTeamMember();
    }
}
