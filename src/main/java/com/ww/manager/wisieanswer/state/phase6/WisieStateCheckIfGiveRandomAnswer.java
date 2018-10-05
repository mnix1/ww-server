package com.ww.manager.wisieanswer.state.phase6;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCheckIfGiveRandomAnswer extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateCheckIfGiveRandomAnswer.class);

    public WisieStateCheckIfGiveRandomAnswer(WisieAnswerManager manager) {
        super(manager,STATE_TYPE_DECISION);
    }

    protected WisieAnswerAction processWisieAnswerAction() {
        double diffPart = (4 - manager.getDifficulty()) * 0.15;
        double attrPart = ((2 * manager.getConfidenceF1() + manager.getIntuitionF1()) / 2 - 0.5) * 4 / 5;
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        double chance = 0.5 + diffPart + attrPart + hobbyPart;
        boolean willGiveRandomAnswer = chance > randomDouble();
        logger.trace(describe() + ", chance: " + chance + ", willGiveRandomAnswer: " + willGiveRandomAnswer);
        if (willGiveRandomAnswer) {
            return WisieAnswerAction.WILL_GIVE_RANDOM_ANSWER;
        }
        return WisieAnswerAction.WONT_GIVE_RANDOM_ANSWER;
    }
}
