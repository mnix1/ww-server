package com.ww.manager.wisieanswer.state.phase6;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import com.ww.model.entity.outside.rival.task.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static com.ww.helper.RandomHelper.randomElement;

public class WisieStateAnsweringPhase6 extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateAnsweringPhase6.class);

    public WisieStateAnsweringPhase6(WisieAnswerManager manager) {
        super(manager,STATE_TYPE_DECISION);
    }

    protected void processVoid() {
        manager.addAndSendAction(WisieAnswerAction.ANSWERED);
        Answer answer = randomElement(new ArrayList<>(manager.getQuestion().getAnswers()));
        logger.trace(describe() + ", correctAnswer: " + answer.getCorrect());
        manager.getManager().getFlow().wisieAnswered(manager.getWisie().getProfile().getId(), answer.getId());
    }
}
