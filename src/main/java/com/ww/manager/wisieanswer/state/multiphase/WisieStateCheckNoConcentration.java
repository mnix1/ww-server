package com.ww.manager.wisieanswer.state.multiphase;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class WisieStateCheckNoConcentration extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateCheckNoConcentration.class);

    public WisieStateCheckNoConcentration(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_DECISION);
    }

    protected WisieAnswerAction processWisieAnswerAction() {
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        double chance = manager.getConcentrationF1() + hobbyPart;
        boolean lostConcentration = chance < randomDouble();
        logger.trace(describe() + ", chance: " + chance + ", lostConcentration: " + lostConcentration);
        if (lostConcentration) {
            return randomElement(WisieAnswerAction.getNoConcentrationActions());
        }
        return WisieAnswerAction.NONE;
    }
}
