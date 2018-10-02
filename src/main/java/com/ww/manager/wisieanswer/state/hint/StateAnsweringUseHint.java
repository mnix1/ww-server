package com.ww.manager.wisieanswer.state.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateAnsweringUseHint extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateAnsweringUseHint.class);

    public StateAnsweringUseHint(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    protected void processVoid() {
        manager.addAndSendAction(WisieAnswerAction.ANSWERED);
        logger.trace(manager.toString() + ", hintAnswerId: " + manager.isHintCorrect());
        manager.getManager().getFlow().wisieAnswered(manager.getWisie().getProfile().getId(), manager.getHintAnswerId());
    }
}
