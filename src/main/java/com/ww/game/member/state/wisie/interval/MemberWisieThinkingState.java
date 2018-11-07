package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieThinkingState extends MemberWisieIntervalState {
    public MemberWisieThinkingState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.THINKING);
    }

    @Override
    protected double minInterval() {
        return 1.75 + (manager.getContainer().getDifficulty() - 4) * 0.25 - getWisie().getWisdomSum();
    }

    @Override
    protected double maxInterval() {
        return 5.75 + (manager.getContainer().getDifficulty() - 4) * 0.25 - getWisie().getWisdomSum() * 5;
    }

    @Override
    protected double prepareInterval() {
        return hobbyImpact(super.prepareInterval());
    }

    @Override
    public void after() {
        manager.getFlow().run("THINK_KNOW_ANSWER");
    }
}
