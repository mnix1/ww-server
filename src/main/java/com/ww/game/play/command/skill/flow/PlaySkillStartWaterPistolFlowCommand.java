package com.ww.game.play.command.skill.flow;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.PlayManager;
import com.ww.game.play.flow.skill.PlaySkillNinjaFlow;
import com.ww.game.play.flow.skill.PlaySkillWaterPistolFlow;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

public class PlaySkillStartWaterPistolFlowCommand extends PlaySkillFlowCommand {
    private WarTeam warOpponentTeam;

    public PlaySkillStartWaterPistolFlowCommand(PlayManager manager, WarTeam warOpponentTeam) {
        super(manager);
        this.warOpponentTeam = warOpponentTeam;
    }

    @Override
    public void execute() {
        WisieTeamMember opponentMember = (WisieTeamMember) warOpponentTeam.getActiveTeamMember();
        MemberWisieManager opponentMemberManager = opponentMember.currentManager();
        PlaySkillWaterPistolFlow flow = new PlaySkillWaterPistolFlow(opponentMemberManager);
        opponentMemberManager.getFlow().innerFlow(flow);
        flow.start();
    }
}
