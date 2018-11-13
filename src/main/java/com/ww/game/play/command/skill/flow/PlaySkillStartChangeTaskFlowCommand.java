package com.ww.game.play.command.skill.flow;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.PlayManager;
import com.ww.game.play.flow.skill.PlaySkillChangeTaskFlow;
import com.ww.game.play.flow.skill.PlaySkillHintFlow;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

public class PlaySkillStartChangeTaskFlowCommand extends PlaySkillFlowCommand {
    private WarTeam warTeam;

    public PlaySkillStartChangeTaskFlowCommand(PlayManager manager, WarTeam warTeam) {
        super(manager);
        this.warTeam = warTeam;
    }

    @Override
    public void execute() {
        WisieTeamMember member = (WisieTeamMember) warTeam.getActiveTeamMember();
        MemberWisieManager memberManager = member.currentManager().get();
        PlaySkillChangeTaskFlow flow = new PlaySkillChangeTaskFlow(memberManager);
        memberManager.getFlow().innerFlow(flow);
        flow.start();
    }
}
