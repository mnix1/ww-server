package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieLookingForAnswerState extends MemberWisieIntervalState {
    public MemberWisieLookingForAnswerState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.LOOKING_FOR_ANSWER);
    }

    @Override
    protected double prepareInterval() {
        double sumInterval = manager.getContainer().getAnswerCount() * (2 - getWisie().getSpeedF1() - getWisie().getWisdomSum());
        return hobbyImpact(sumInterval * (5 - getWisie().getSpeedF1() - getWisie().getIntuitionF1() - getWisie().getCunningF1() - getWisie().getWisdomSum()));
    }
}
