package com.ww.game.play.command.war;

import com.ww.game.GameFlow;
import com.ww.game.member.flow.MemberWisieFlow;
import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

import java.util.List;

public class PlayWarStartMemberManagerCommand extends PlayCommand {
    private List<MemberWisieFlow> flows;

    public PlayWarStartMemberManagerCommand(PlayContainer container, List<MemberWisieFlow> flows) {
        super(container);
        this.flows = flows;
    }

    @Override
    public void execute() {
        for (GameFlow flow : flows) {
            flow.start();
        }
    }
}
