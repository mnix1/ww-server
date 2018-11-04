package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieThinkKnowAnswerState extends MemberWisieIntervalState {
    public MemberWisieThinkKnowAnswerState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.THINK_KNOW_ANSWER);
    }

    @Override
    protected double minInterval() {
        return 1 - getWisie().getReflexF1();
    }

    @Override
    protected double maxInterval() {
        return 2 - getWisie().getReflexF1() - getWisie().getConcentrationF1();
    }
}
