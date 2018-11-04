package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.Optional;

public class MemberWisieWaitingForQuestionState extends MemberWisieIntervalState {
    public MemberWisieWaitingForQuestionState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.WAITING_FOR_QUESTION);
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
    public Optional<String> afterStateName() {
        return Optional.of("RECOGNIZING_QUESTION");
    }

}
