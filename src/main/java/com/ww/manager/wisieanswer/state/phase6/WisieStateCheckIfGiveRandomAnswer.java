package com.ww.manager.wisieanswer.state.phase6;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCheckIfGiveRandomAnswer extends WisieState {
    private Boolean willGiveRandomAnswer;
    private Double chance;

    public WisieStateCheckIfGiveRandomAnswer(WisieAnswerManager manager) {
        super(manager,STATE_TYPE_DECISION);
    }

    @Override
    public String describe() {
        return super.describe() + ", chance=" + chance + ", willGiveRandomAnswer=" + willGiveRandomAnswer;
    }

    @Override
    protected WisieAnswerAction processWisieAnswerAction() {
        double diffPart = (4 - manager.getDifficulty()) * 0.15;
        double attrPart = ((2 * manager.getWarWisie().getConfidenceF1() + manager.getWarWisie().getIntuitionF1()) / 2 - 0.5) * 4 / 5;
        double hobbyPart = manager.getWarWisie().isHobby() ? 0.1 : 0;
        chance = 0.5 + diffPart + attrPart + hobbyPart;
        willGiveRandomAnswer = chance > randomDouble();
        if (willGiveRandomAnswer) {
            return WisieAnswerAction.WILL_GIVE_RANDOM_ANSWER;
        }
        return WisieAnswerAction.WONT_GIVE_RANDOM_ANSWER;
    }
}
