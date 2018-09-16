package com.ww.manager.wisieanswer.state.phase3;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateCheckFoundAnswerLookingFor extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateCheckFoundAnswerLookingFor.class);

    public StateCheckFoundAnswerLookingFor(WisieAnswerManager manager) {
        super(manager);
    }

    protected WisieAnswerAction processWisieAnswerAction() {
        double diffPart = (4 - manager.getDifficulty()) * 0.05;
        double attrPart = (manager.getWisdomSum() - 0.5) * 4 / 5;
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        double chance = 0.5 + diffPart + attrPart + hobbyPart;
        boolean foundAnswerLookingFor = chance > randomDouble();
        logger.trace(manager.getWisie().getWisie().getNamePolish() + ", " + manager.lastAction().name() + ", chance: " + chance+ ", foundAnswerLookingFor: " + foundAnswerLookingFor);
        if (foundAnswerLookingFor) {
            return WisieAnswerAction.FOUND_ANSWER_LOOKING_FOR;
        }
        return WisieAnswerAction.NO_FOUND_ANSWER_LOOKING_FOR;
    }
}
