package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieNoFoundAnswerLookingForState extends MemberWisieIntervalState {
    public MemberWisieNoFoundAnswerLookingForState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.NO_FOUND_ANSWER_LOOKING_FOR);
    }

    @Override
    protected double prepareInterval() {
        return 3 - getWisie().getSpeedF1() - getWisie().getReflexF1() - getWisie().getConcentrationF1();
    }

    @Override
    public void after() {
        manager.getFlow().run("THINKING_WHICH_ANSWER_MATCH");
    }
}
