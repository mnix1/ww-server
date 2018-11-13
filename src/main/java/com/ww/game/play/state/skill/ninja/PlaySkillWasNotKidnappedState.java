package com.ww.game.play.state.skill.ninja;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;

public class PlaySkillWasNotKidnappedState extends PlaySkillState {

    public PlaySkillWasNotKidnappedState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
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
        flow.notifyOuter(manager.getFlow());
    }
}
