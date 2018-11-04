package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.game.member.state.wisie.MemberWisieState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import lombok.Getter;

import static com.ww.helper.RandomHelper.randomDouble;

public abstract class MemberWisieIntervalState extends MemberWisieState {
    @Getter
    protected long interval;

    public MemberWisieIntervalState(MemberWisieManager manager, MemberWisieStatus status) {
        super(manager, status);
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
