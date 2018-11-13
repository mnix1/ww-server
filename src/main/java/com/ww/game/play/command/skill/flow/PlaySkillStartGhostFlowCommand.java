package com.ww.game.play.command.skill.flow;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.PlayManager;
import com.ww.game.play.flow.skill.PlaySkillGhostFlow;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

public class PlaySkillStartGhostFlowCommand extends PlaySkillFlowCommand {
    private WarTeam warTeam;
    private WarTeam warOpponentTeam;

    public PlaySkillStartGhostFlowCommand(PlayManager manager, WarTeam warTeam, WarTeam warOpponentTeam) {
        super(manager);
        this.warTeam = warTeam;
        this.warOpponentTeam = warOpponentTeam;
    }

    @Override
    public void execute() {
        WisieTeamMember member = (WisieTeamMember) warTeam.getActiveTeamMember();
        MemberWisieManager memberManager = member.currentManager().get();
        WisieTeamMember opponentMember = (WisieTeamMember) warOpponentTeam.getActiveTeamMember();
        MemberWisieManager opponentMemberManager = opponentMember.currentManager().get();
        PlaySkillGhostFlow flow = new PlaySkillGhostFlow(memberManager, opponentMemberManager);
        memberManager.getFlow().innerFlow(flow);
        flow.start();
    }
}
