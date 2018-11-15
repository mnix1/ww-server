package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieDoesntKnowAnswerState extends MemberWisieIntervalState {
    public MemberWisieDoesntKnowAnswerState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.DOESNT_KNOW_ANSWER);
    }

    @Override
    protected double prepareInterval() {
        return 2 - getWisie().getReflexF1() - getWisie().getConcentrationF1();
    }

    @Override
    public void after() {
        manager.getFlow().run("THINKING_GIVE_RANDOM_ANSWER");
    }
}
