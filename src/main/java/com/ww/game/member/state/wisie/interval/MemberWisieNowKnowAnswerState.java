package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.HashMap;
import java.util.Map;

public class MemberWisieNowKnowAnswerState extends MemberWisieIntervalState {
    public MemberWisieNowKnowAnswerState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.NOW_KNOW_ANSWER);
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
    public void after() {
        Map<String, Object> params = new HashMap<>();
        params.put("paramsPart", getWisie().getWisdomSum() + 2 * getWisie().getIntuitionF1());
        manager.getFlow().run("ANSWERED", params);
    }
}
