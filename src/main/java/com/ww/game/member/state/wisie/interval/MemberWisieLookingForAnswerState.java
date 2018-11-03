package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieLookingForAnswerState extends MemberWisieIntervalState {
    public MemberWisieLookingForAnswerState(MemberWisieContainer container) {
        super(container, MemberWisieStatus.LOOKING_FOR_ANSWER);
    }

    @Override
    protected double prepareInterval() {
        double sumInterval = container.getAnswerCount() * (2 - getWisie().getSpeedF1() - getWisie().getWisdomSum());
        return hobbyImpact(sumInterval * (5 - getWisie().getSpeedF1() - getWisie().getIntuitionF1() - getWisie().getCunningF1() - getWisie().getWisdomSum()));
    }
}
