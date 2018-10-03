package com.ww.manager.wisieanswer.state.skill.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WisieStateAnsweringUseHint extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateAnsweringUseHint.class);

    public WisieStateAnsweringUseHint(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    protected void processVoid() {
        manager.addAndSendAction(WisieAnswerAction.ANSWERED);
        logger.trace(manager.toString() + ", isHintCorrect: " + manager.isHintCorrect());
        manager.getManager().getFlow().wisieAnswered(manager.getWisie().getProfile().getId(), manager.getHintAnswerId());
    }
}
