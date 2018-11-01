package com.ww.manager.wisieanswer.state.phase6;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.randomElement;

public class WisieStateSurrender extends WisieState {
    public WisieStateSurrender(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    protected void processVoid() {
        manager.addAndSendAction(MemberWisieStatus.SURRENDER);
    }
}
