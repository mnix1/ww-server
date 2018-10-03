package com.ww.manager.wisieanswer.state.phase6;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomElement;

public class WisieStateSurrender extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateSurrender.class);

    public WisieStateSurrender(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    protected void processVoid() {
        manager.addAndSendAction(WisieAnswerAction.SURRENDER);
    }
}
