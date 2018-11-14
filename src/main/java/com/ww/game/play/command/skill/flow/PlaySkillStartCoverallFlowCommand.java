package com.ww.game.play.command.skill.flow;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.PlayManager;
import com.ww.game.play.flow.skill.PlaySkillCoverallFlow;
import com.ww.game.play.flow.skill.PlaySkillPizzaFlow;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

public class PlaySkillStartCoverallFlowCommand extends PlaySkillFlowCommand {
    private WarTeam warTeam;
    private WarTeam warOpponentTeam;

    public PlaySkillStartCoverallFlowCommand(PlayManager manager, WarTeam warTeam, WarTeam warOpponentTeam) {
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
        PlaySkillCoverallFlow flow = new PlaySkillCoverallFlow(memberManager, opponentMemberManager);
        memberManager.getFlow().innerFlow(flow);
        flow.start();
    }
}
