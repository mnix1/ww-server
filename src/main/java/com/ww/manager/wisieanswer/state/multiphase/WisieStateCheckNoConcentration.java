package com.ww.manager.wisieanswer.state.multiphase;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class WisieStateCheckNoConcentration extends WisieState {
    private Boolean lostConcentration;
    private Double chance;

    public WisieStateCheckNoConcentration(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_DECISION);
    }

    @Override
    public String describe() {
        return super.describe() + ", chance=" + chance + ", lostConcentration=" + lostConcentration;
    }

    @Override
    protected MemberWisieStatus processWisieAnswerAction() {
        double hobbyPart = manager.getWarWisie().isHobby() ? 0.1 : 0;
        chance = manager.getWarWisie().getConcentrationF1() + hobbyPart;
        lostConcentration = chance < randomDouble();
        if (lostConcentration) {
            return randomElement(MemberWisieStatus.getNoConcentrationActions());
        }
        return MemberWisieStatus.NONE;
    }
}
