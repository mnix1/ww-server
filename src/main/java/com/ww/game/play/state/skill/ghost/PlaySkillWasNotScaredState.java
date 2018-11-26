package com.ww.game.play.state.skill.ghost;

import com.ww.game.member.command.MemberWisieRemoveMostOuterFlowCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.flow.skill.PlaySkillFlowOpponent;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.game.play.state.skill.PlaySkillState;

public class PlaySkillWasNotScaredState extends PlaySkillOpponentState {

    public PlaySkillWasNotScaredState(PlaySkillFlowOpponent flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieRemoveMostOuterFlowCommand(flow, opponentWarTeam.getProfileId()));
    }

    @Override
    protected double minInterval() {
        return 2 - getOpponentWisie().getReflexF1() - getOpponentWisie().getConcentrationF1();
    }

    @Override
    protected double maxInterval() {
        return 3 - getOpponentWisie().getReflexF1() - 2 * getOpponentWisie().getConcentrationF1();
    }

    @Override
    public void updateNotify() {
    }

    @Override
    public void after() {
        flow.getFlowContainer().runMostOuter(opponentWarTeam.getProfileId());
    }
}
