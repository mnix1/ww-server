package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.state.wisie.MemberWisieState;
import com.ww.game.member.state.wisie.interval.simpleinterval.MemberWisieSimpleIntervalState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public abstract class MemberWisieIntervalState extends MemberWisieSimpleIntervalState {
    protected double minInterval;
    protected double maxInterval;

    public MemberWisieIntervalState(MemberWisieManager manager, MemberWisieStatus status) {
        super(manager, status);
    }

    @Override
    public void initProps() {
        minInterval = minInterval();
        maxInterval = maxInterval();
        super.initProps();
    }

    protected double minInterval() {
        return 0;
    }

    protected double maxInterval() {
        return 0;
    }

    protected double prepareInterval() {
        return randomDouble(minInterval, maxInterval);
    }

    @Override
    public String toString() {
        return super.toString() + ", minInterval=" + minInterval + ", maxInterval=" + maxInterval;
    }
}
