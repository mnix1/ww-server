package com.ww.manager.wisieanswer.state;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;

public class State {
    protected WisieAnswerManager manager;

    public State(WisieAnswerManager manager) {
        this.manager = manager;
    }

    public Flowable<Long> startFlowable() {
        if (!this.manager.isInProgress()) {
            return Flowable.empty();
        }
        return processFlowable();
    }

    protected Flowable<Long> processFlowable() {
        return Flowable.empty();
    }

    public WisieAnswerAction startWisieAnswerAction() {
        if (!this.manager.isInProgress()) {
            return null;
        }
        return processWisieAnswerAction();
    }

    protected WisieAnswerAction processWisieAnswerAction() {
        return WisieAnswerAction.NONE;
    }

    public void startVoid() {
        if (!this.manager.isInProgress()) {
            return;
        }
        processVoid();
    }

    protected void processVoid() {
    }
}
