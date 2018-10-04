package com.ww.manager.wisieanswer.skill.state.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCheckIfUseHint extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateCheckIfUseHint.class);

    private boolean hintCorrect;
    public WisieStateCheckIfUseHint(WisieAnswerManager manager, boolean hintCorrect) {
        super(manager, STATE_TYPE_DECISION);
        this.hintCorrect = hintCorrect;
    }

    protected WisieAnswerAction processWisieAnswerAction() {
        if (hintCorrect) {
            logger.trace(manager.toString() + ", manager.isHintCorrect(): " + hintCorrect);
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
