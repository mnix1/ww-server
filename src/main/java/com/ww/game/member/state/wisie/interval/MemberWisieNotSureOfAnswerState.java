package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieNotSureOfAnswerState extends MemberWisieIntervalState {
    public MemberWisieNotSureOfAnswerState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.NOT_SURE_OF_ANSWER);
    }

    @Override
    protected double minInterval() {
        return 1 - getWisie().getReflexF1();
    }

    @Override
    protected double maxInterval() {
        return 2 - getWisie().getReflexF1() - getWisie().getConcentrationF1();
    }

    @Override
    public void after() {
        manager.getFlow().run("RECOGNIZING_ANSWERS");
    }
}
