package com.ww.manager.wisieanswer.state.phase5;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCheckKnowAnswerAfterThinkingWhichMatch extends WisieState {
    private Boolean thinkKnowAnswer;
    private Double chance;

    public WisieStateCheckKnowAnswerAfterThinkingWhichMatch(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_DECISION);
    }

    @Override
    public String describe() {
        return super.describe() + ", chance=" + chance + ", thinkKnowAnswer=" + thinkKnowAnswer;
    }

    @Override
    protected WisieAnswerAction processWisieAnswerAction() {
        double diffPart = (4 - manager.getDifficulty()) * 0.05;
        double attrPart = ((manager.getWisdomSum() + manager.getIntuitionF1() + manager.getConfidenceF1()) / 3 - 0.5) * 4 / 5;
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        chance = 0.5 + diffPart + attrPart + hobbyPart;
        thinkKnowAnswer = chance > randomDouble();
        if (thinkKnowAnswer) {
            return WisieAnswerAction.NOW_KNOW_ANSWER;
        }
        return WisieAnswerAction.DOESNT_KNOW_ANSWER;
    }
}
