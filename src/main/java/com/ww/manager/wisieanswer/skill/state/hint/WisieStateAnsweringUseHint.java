package com.ww.manager.wisieanswer.skill.state.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WisieStateAnsweringUseHint extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateAnsweringUseHint.class);

    private boolean hintCorrect;
    private Long hintAnswerId;

    public WisieStateAnsweringUseHint(WisieAnswerManager manager, boolean hintCorrect, Long hintAnswerId) {
        super(manager, STATE_TYPE_VOID);
        this.hintCorrect = hintCorrect;
        this.hintAnswerId = hintAnswerId;
    }

    protected void processVoid() {
        manager.addAndSendAction(WisieAnswerAction.ANSWERED);
        logger.trace(manager.toString() + ", isHintCorrect: " + hintCorrect);
        manager.getManager().getFlow().wisieAnswered(manager.getWisie().getProfile().getId(), hintAnswerId);
    }
}
