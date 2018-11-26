package com.ww.game.play.command.war;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

public class PlayWarInitMemberManagerCommand extends PlayCommand {
    private PlayWarAnsweringFlowContainer flowContainer;
    private PlayManager manager;

    public PlayWarInitMemberManagerCommand(PlayWarAnsweringFlowContainer flowContainer, PlayManager manager) {
        super(manager.getContainer());
        this.flowContainer = flowContainer;
        this.manager = manager;
    }

    @Override
    public void execute() {
        for (RivalTeam team : container.getTeams().getTeams()) {
            WarTeam warTeam = (WarTeam) team;
            TeamMember member = warTeam.getActiveTeamMember();
            if (member.isWisie()) {
                WisieTeamMember wisieMember = (WisieTeamMember) member;
                wisieMember.refreshCache(container.getTasks().question());
                MemberWisieManager memberManager = new MemberWisieManager(warTeam, wisieMember, manager);
                wisieMember.addManager(memberManager);
                flowContainer.addWisieFlow(memberManager.getFlow());
            }
        }
    }
}
