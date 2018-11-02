package com.ww.game.member.state.wisie;

import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieThinkKnowAnswerState extends MemberWisieIntervalState {
    public MemberWisieThinkKnowAnswerState(MemberWisieContainer container) {
        super(container, MemberWisieStatus.THINK_KNOW_ANSWER);
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
