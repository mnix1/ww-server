package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieFoundAnswerLookingForState extends MemberWisieIntervalState {
    public MemberWisieFoundAnswerLookingForState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.FOUND_ANSWER_LOOKING_FOR);
    }

    @Override
    protected double prepareInterval() {
        return 3 - getWisie().getSpeedF1() - getWisie().getReflexF1() - getWisie().getConcentrationF1();
    }
}
