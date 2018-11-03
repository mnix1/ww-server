package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieWaitingForQuestionState extends MemberWisieIntervalState {
    public MemberWisieWaitingForQuestionState(MemberWisieContainer container) {
        super(container, MemberWisieStatus.WAITING_FOR_QUESTION);
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
