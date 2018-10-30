package com.ww.play.flow;

import com.ww.model.container.rival.RivalInterval;
import com.ww.play.PlayManager;
import com.ww.play.state.PlayChoosingWhoAnswerState;
import com.ww.play.state.PlayChosenWhoAnswerState;
import com.ww.play.state.PlayIntroState;
import com.ww.play.state.war.PlayWarIntroState;

public class PlayWarFlow extends PlayFlow {

    public PlayWarFlow(PlayManager manager,RivalInterval interval) {
        super(manager, interval);
    }

    @Override
    public synchronized void afterChoosingTaskPropsTimeoutPhase() {
        choosingWhoAnswerPhase();
    }

    @Override
    public synchronized PlayIntroState createIntroState() {
        return new PlayWarIntroState(manager.getContainer());
    }

    public synchronized void choosingWhoAnswerPhase() {
        getContainer().addState(new PlayChoosingWhoAnswerState(getContainer()));
        afterChoosingWhoAnswerPhase();
    }

    public synchronized void afterChoosingWhoAnswerPhase() {
        preparingNextTaskPhase();
    }

    public synchronized void chosenWhoAnswerAction() {
        stopAfter();
        getContainer().addState(new PlayChosenWhoAnswerState(getContainer()));
        afterChosenWhoAnswerAction();
    }

    public synchronized void afterChosenWhoAnswerAction() {
        preparingNextTaskPhase();
    }
}
