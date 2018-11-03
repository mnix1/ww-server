package com.ww.game.play.command.war;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.PlayManager;
import com.ww.game.play.PlayWarManager;
import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

public class PlayWarInitMemberManagerCommand extends PlayCommand {
    private PlayManager manager;

    public PlayWarInitMemberManagerCommand(PlayManager manager) {
        super(manager.getContainer());
        this.manager = manager;
    }

    @Override
    public void execute() {
        for (RivalTeam team : container.getTeams().getTeams()) {
            WarTeam warTeam = (WarTeam) team;
            TeamMember teamMember = warTeam.getActiveTeamMember();
            if (teamMember.isWisie()) {
                WisieTeamMember wisieTeamMember = (WisieTeamMember) teamMember;
                wisieTeamMember.refreshCache(container.getTasks().question());
                MemberWisieManager memberManager = new MemberWisieManager(warTeam, wisieTeamMember, manager);
                wisieTeamMember.addManager(memberManager);
            }
        }
    }
}
