package com.ww.game.member.state.wisie.interval.simpleinterval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.game.member.state.wisie.interval.MemberWisieIntervalState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieThinkKnowAnswerState extends MemberWisieSimpleIntervalState {
    public MemberWisieThinkKnowAnswerState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.THINK_KNOW_ANSWER);
    }

    @Override
    protected double prepareInterval() {
        return 2 - getWisie().getReflexF1() - getWisie().getConcentrationF1();
    }

    @Override
    public void after() {
        manager.getFlow().run("LOOKING_FOR_ANSWER");
    }
}
