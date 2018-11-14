package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.state.wisie.MemberWisieState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public abstract class MemberWisieIntervalState extends MemberWisieState {
    @Getter
    protected long interval;
    protected double minInterval;
    protected double maxInterval;

    public MemberWisieIntervalState(MemberWisieManager manager, MemberWisieStatus status) {
        super(manager, status);
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
        return randomDouble(minInterval, maxInterval);
    }

    @Override
    public void execute() {
        minInterval = minInterval();
        maxInterval = maxInterval();
        interval = (long) (prepareInterval() * intervalMultiply());
        super.execute();
    }

    @Override
    public String toString() {
        return super.toString() + ", interval=" + interval + ", minInterval=" + minInterval + ", maxInterval=" + maxInterval;
    }

    @Override
    public long afterInterval() {
        return interval;
    }
}
