package com.ww.manager.wisieanswer.state.phase2;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateCheckKnowAnswerAfterThinkingAboutQuestion extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateCheckKnowAnswerAfterThinkingAboutQuestion.class);

    public StateCheckKnowAnswerAfterThinkingAboutQuestion(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_DECISION);
    }

    protected WisieAnswerAction processWisieAnswerAction() {
        double diffPart = (4 - manager.getDifficulty()) * 0.05;
        double attrPart = ((manager.getWisdomSum() + manager.getConfidenceF1()) / 2 - 0.5) * 4 / 5;
        double hobbyPart = manager.isHobby() ? 0.1 : 0;
        double chance = 0.5 + diffPart + attrPart + hobbyPart;
        boolean thinkKnowAnswer = chance > randomDouble();
        logger.trace(manager.toString() + ", chance: " + chance+ ", thinkKnowAnswer: " + thinkKnowAnswer);
        if (thinkKnowAnswer) {
            return WisieAnswerAction.THINK_KNOW_ANSWER;
        }
        return WisieAnswerAction.NOT_SURE_OF_ANSWER;
    }
}
