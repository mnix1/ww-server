package com.ww.manager.heroanswer.state;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;

public class State {
    protected HeroAnswerManager manager;

    public State(HeroAnswerManager manager) {
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

    public HeroAnswerAction startHeroAnswerAction() {
        if (!this.manager.isInProgress()) {
            return null;
        }
        return processHeroAnswerAction();
    }

    protected HeroAnswerAction processHeroAnswerAction() {
        return HeroAnswerAction.ANSWERED;
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
