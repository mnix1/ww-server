package com.ww.game.play.command.skill;

import com.ww.game.GameState;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.flow.skill.PlaySkillHintFlow;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

public class PlaySkillStartHintFlowCommand extends PlaySkillFlowCommand {
    private WarTeam warTeam;
    private Long answerId;

    public PlaySkillStartHintFlowCommand(PlayManager manager, WarTeam warTeam, Long answerId) {
        super(manager);
        this.warTeam = warTeam;
        this.answerId = answerId;
    }

    @Override
    public void execute() {
        WisieTeamMember member = (WisieTeamMember) warTeam.getActiveTeamMember();
        MemberWisieManager memberManager = member.currentManager();
        memberManager.getFlow().stopAfter();
        GameState onSkillStartState = memberManager.getFlow().currentState();
        PlaySkillHintFlow flow = new PlaySkillHintFlow(memberManager, answerId);
        flow.start();
    }
}
