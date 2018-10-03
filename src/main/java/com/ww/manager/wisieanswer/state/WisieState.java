package com.ww.manager.wisieanswer.state;

import com.ww.manager.rival.state.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.wisie.WisieAnswerAction;
import lombok.Getter;

@Getter
public abstract class WisieState extends AbstractState {
    protected WisieAnswerManager manager;

    protected WisieState(WisieAnswerManager manager, String type) {
        this.manager = manager;
        setType(type);
    }

    public WisieAnswerAction startWisieAnswerAction() {
        return processWisieAnswerAction();
    }

    protected WisieAnswerAction processWisieAnswerAction() {
        return WisieAnswerAction.NONE;
    }
}
