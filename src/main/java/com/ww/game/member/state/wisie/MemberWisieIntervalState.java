package com.ww.game.member.state.wisie;

import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;
import lombok.Getter;

import static com.ww.helper.RandomHelper.randomDouble;

public abstract class MemberWisieIntervalState extends MemberWisieState {
    @Getter
    protected long interval;

    public MemberWisieIntervalState(MemberWisieContainer container, MemberWisieStatus status) {
        super(container, status);
        this.interval = (long) (prepareInterval() * intervalMultiply());
    }

    protected long intervalMultiply() {
        return 1000;
    }

    protected double minInterval() {
        return 0;
    }

    protected double maxInterval() {
        return 0;
    }

    protected double prepareInterval() {
        return randomDouble(minInterval(), maxInterval());
    }
}
