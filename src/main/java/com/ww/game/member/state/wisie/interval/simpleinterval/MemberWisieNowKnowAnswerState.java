package com.ww.game.member.state.wisie.interval.simpleinterval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.state.wisie.interval.MemberWisieIntervalState;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.HashMap;
import java.util.Map;

public class MemberWisieNowKnowAnswerState extends MemberWisieSimpleIntervalState {
    public MemberWisieNowKnowAnswerState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.NOW_KNOW_ANSWER);
    }

    @Override
    protected double prepareInterval() {
        return 2 - getWisie().getReflexF1() - getWisie().getConcentrationF1();
    }

    @Override
    public void after() {
        Map<String, Object> params = new HashMap<>();
        params.put("paramsPart", 2 * getWisie().getWisdomSum() + 0.2 * getWisie().getIntuitionF1());
        manager.getFlow().run("ANSWERED", params);
    }
}
