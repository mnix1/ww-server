package com.ww.game.member.state.wisie.interval.simpleinterval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.state.wisie.MemberWisieState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import lombok.Getter;

public abstract class MemberWisieSimpleIntervalState extends MemberWisieState {
    @Getter
    protected long interval;
    protected double betweenInterval;

    public MemberWisieSimpleIntervalState(MemberWisieManager manager, MemberWisieStatus status) {
        super(manager, status);
    }

    @Override
    public void initProps() {
        betweenInterval = prepareInterval();
        interval = (long) (betweenInterval * intervalMultiply());
    }

    protected long intervalMultiply() {
        return manager.getPlayManager().getInterval().wisieIntervalMultiply();
    }

    protected double prepareInterval() {
        return 2 - getWisie().getReflexF1() - getWisie().getConcentrationF1();
    }

    @Override
    public String toString() {
        return super.toString() + ", interval=" + interval + ", betweenInterval=" + betweenInterval;
    }

    @Override
    public long afterInterval() {
        return interval;
    }
}
