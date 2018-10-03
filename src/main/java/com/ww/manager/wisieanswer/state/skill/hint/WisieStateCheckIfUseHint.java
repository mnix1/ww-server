package com.ww.manager.wisieanswer.state.skill.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCheckIfUseHint extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateCheckIfUseHint.class);

    public WisieStateCheckIfUseHint(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_DECISION);
    }

    protected WisieAnswerAction processWisieAnswerAction() {
        if (manager.isHintCorrect()) {
            logger.trace(manager.toString() + ", manager.isHintCorrect(): " + manager.isHintCorrect());
            return WisieAnswerAction.WILL_USE_HINT;
        }
        double diffPart = (4 - manager.getDifficulty()) * 0.05;
        double attrPart = ((manager.getWisdomSum() + manager.getIntuitionF1() + manager.getConfidenceF1()) / 3 - 0.5) * 4 / 5;
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        double chance = 0.5 + diffPart + attrPart + hobbyPart;
        boolean useHint = chance <= randomDouble();
        logger.trace(manager.toString() + ", chance: " + chance + ", useHint: " + useHint + ", hintNotCorrect");
        if (useHint) {
            return WisieAnswerAction.WILL_USE_HINT;
        }
        return WisieAnswerAction.WONT_USE_HINT;
    }
}
