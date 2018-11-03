package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieThinkingState extends MemberWisieIntervalState {
    public MemberWisieThinkingState(MemberWisieContainer container) {
        super(container, MemberWisieStatus.THINKING);
    }

    @Override
    protected double minInterval() {
        return 1.75 + (container.getDifficulty() - 4) * 0.25 - getWisie().getWisdomSum();
    }

    @Override
    protected double maxInterval() {
        return 5.75 + (container.getDifficulty() - 4) * 0.25 - getWisie().getWisdomSum() * 5;
    }

    @Override
    protected double prepareInterval() {
        return hobbyImpact(super.prepareInterval());
    }
}
