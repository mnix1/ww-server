package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WisieStateRunAway extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateRunAway.class);

    public WisieStateRunAway(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    protected void processVoid() {
        //action in WisieStateScareSucceeded
//        manager.addAndSendAction(WisieAnswerAction.RUN_AWAY);
        logger.trace(describe());
    }
}
