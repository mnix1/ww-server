package com.ww.manager.wisieanswer.state.phase6;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.entity.outside.rival.task.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;
import static com.ww.helper.RandomHelper.randomElementIndex;

public class StateAnsweringPhase6 extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateAnsweringPhase6.class);

    public StateAnsweringPhase6(WisieAnswerManager manager) {
        super(manager,STATE_TYPE_DECISION);
    }

    protected void processVoid() {
        manager.addAndSendAction(WisieAnswerAction.ANSWERED);
        Answer answer = randomElement(new ArrayList<>(manager.getQuestion().getAnswers()));
        logger.trace(manager.toString() + ", correctAnswer: " + answer.getCorrect());
        manager.getManager().getFlow().wisieAnswered(manager.getWisie().getProfile().getId(), answer.getId());
    }
}
