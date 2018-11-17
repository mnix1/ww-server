package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.state.wisie.interval.simpleinterval.MemberWisieSimpleIntervalState;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.RandomHelper.randomDouble;

public class MemberWisieWaitingForQuestionState extends MemberWisieSimpleIntervalState {
    private boolean lostConcentration;
    private double chanceNotLostConcentration;
    private double random;

    public MemberWisieWaitingForQuestionState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.WAITING_FOR_QUESTION);
    }

    @Override
    public void updateNotify() {
    }

    @Override
    public void initProps() {
        super.initProps();
        random = randomDouble();
        if (getWisie().isHobby()) {
            chanceNotLostConcentration = 1;
            lostConcentration = false;
        } else {
            chanceNotLostConcentration = getWisie().getConcentrationF1();
            lostConcentration = chanceNotLostConcentration <= random;
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", lostConcentration=" + lostConcentration + ", chanceNotLostConcentration=" + chanceNotLostConcentration + ", random=" + random;
    }

    @Override
    public void after() {
        if (lostConcentration) {
            Map<String, Object> params = new HashMap<>();
            params.put("afterStateName", "RECOGNIZING_QUESTION");
            manager.getFlow().run("LOST_CONCENTRATION", params);
        } else {
            manager.getFlow().run("RECOGNIZING_QUESTION");
        }
    }
}
