package com.ww.game.member.state.wisie.interval.simpleinterval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.state.wisie.interval.MemberWisieIntervalState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieNotSureOfAnswerState extends MemberWisieSimpleIntervalState {
    public MemberWisieNotSureOfAnswerState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.NOT_SURE_OF_ANSWER);
    }

    @Override
    protected double prepareInterval() {
        return 2 - getWisie().getReflexF1() - getWisie().getConcentrationF1();
    }

    @Override
    public void after() {
        manager.getFlow().run("RECOGNIZING_ANSWERS");
    }
}
