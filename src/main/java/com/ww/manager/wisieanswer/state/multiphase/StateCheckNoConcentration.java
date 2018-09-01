package com.ww.manager.wisieanswer.state.multiphase;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class StateCheckNoConcentration extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateCheckNoConcentration.class);

    public StateCheckNoConcentration(WisieAnswerManager manager) {
        super(manager);
    }

    protected WisieAnswerAction processWisieAnswerAction() {
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        double chance = manager.getConcentrationF1() + hobbyPart;
        boolean lostConcentration = chance < randomDouble();
        logger.debug(manager.getWisie().getWisie().getNamePolish() + ", " + manager.lastAction().name() + ", chance: " + chance + ", lostConcentration: " + lostConcentration);
        if (lostConcentration) {
            return randomElement(WisieAnswerAction.getNoConcentrationActions());
        }
        return WisieAnswerAction.NONE;
    }
}
