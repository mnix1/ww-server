package com.ww.game.play.state.skill.ghost;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieNotifyOuterFlowCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;

public class PlaySkillWasNotScaredState extends PlaySkillState {

    public PlaySkillWasNotScaredState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieNotifyOuterFlowCommand(flow, manager));
    }

    @Override
    protected double minInterval() {
        return 2 - getWisie().getReflexF1() - getWisie().getConcentrationF1();
    }

    @Override
    protected double maxInterval() {
        return 3 - getWisie().getReflexF1() - 2 * getWisie().getConcentrationF1();
    }

    @Override
    public void updateNotify() {
    }

    @Override
    public void after() {
        if (!manager.getFlow().hasNext()) {
            manager.getFlow().get().currentState().after();
        }
    }
}
