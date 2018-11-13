package com.ww.game.play.command.war;

import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

public class PlayWarStartMemberManagerCommand extends PlayCommand {

    public PlayWarStartMemberManagerCommand(PlayContainer container) {
        super(container);
    }

    @Override
    public void execute() {
        for (RivalTeam team : container.getTeams().getTeams()) {
            WarTeam warTeam = (WarTeam) team;
            TeamMember member = warTeam.getActiveTeamMember();
            if (member.isWisie()) {
                WisieTeamMember wisieMember = (WisieTeamMember) member;
                wisieMember.currentManager().get().start();
            }
        }
    }
}
