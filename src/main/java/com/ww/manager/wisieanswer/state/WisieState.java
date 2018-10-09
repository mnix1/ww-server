package com.ww.manager.wisieanswer.state;

import com.ww.manager.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.wisie.WisieAnswerAction;
import lombok.Getter;

public abstract class WisieState extends AbstractState {
    protected WisieAnswerManager manager;

    protected WisieState(WisieAnswerManager manager, String type) {
        this.manager = manager;
        setType(type);
    }

    @Override
    public String describe() {
        return "WisieState " + super.describe() + ", warManager=" + manager.describe();
    }

    @Override
    public boolean isRunning() {
        return manager.isRunning();
    }

    public WisieAnswerAction startWisieAnswerAction() {
        if (!startIfRunning()) {
            logger.trace(describe() + " NO startWisieAnswerAction");
            return WisieAnswerAction.NONE;
        }
        WisieAnswerAction result = processWisieAnswerAction();
        logger.trace(describe() + " startWisieAnswerAction, result=" + result);
        return result;
    }

    protected WisieAnswerAction processWisieAnswerAction() {
        return WisieAnswerAction.NONE;
    }
}
