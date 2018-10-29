package com.ww.play.war;

import com.ww.model.container.rival.RivalInterval;
import com.ww.play.PlayFlow;
import com.ww.play.PlayManager;
import com.ww.play.PlayModel;
import com.ww.play.war.state.PlayChoosingWhoAnswerState;
import com.ww.play.war.state.PlayChosenWhoAnswerState;

public class PlayWarFlow extends PlayFlow {

    public PlayWarFlow(PlayManager manager, PlayModel model, RivalInterval interval) {
        super(manager, model, interval);
    }

    @Override
    public synchronized void afterChoosingTaskPropsTimeoutPhase() {
        choosingWhoAnswerPhase();
    }

    public synchronized void choosingWhoAnswerPhase() {
        model.add(new PlayChoosingWhoAnswerState());
        afterChoosingWhoAnswerPhase();
    }

    public synchronized void afterChoosingWhoAnswerPhase() {
        preparingNextTaskPhase();
    }

    public synchronized void chosenWhoAnswerAction() {
        stopAfter();
        model.add(new PlayChosenWhoAnswerState());
        afterChosenWhoAnswerAction();
    }

    public synchronized void afterChosenWhoAnswerAction() {
        preparingNextTaskPhase();
    }
}
