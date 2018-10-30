package com.ww.play.flow;

import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.war.WarInterval;
import com.ww.play.PlayManager;
import com.ww.play.state.PlayAnsweredState;
import com.ww.play.state.PlayAnsweringTimeoutState;
import com.ww.play.state.PlayIntroState;
import com.ww.play.state.PlayPreparingNextTaskState;
import com.ww.play.state.war.*;

public class PlayWarFlow extends PlayFlow {

    public PlayWarFlow(PlayManager manager, RivalInterval interval) {
        super(manager, interval);
    }

    @Override
    protected synchronized PlayIntroState createIntroState() {
        return new PlayWarIntroState(manager.getContainer());
    }

    @Override
    protected synchronized PlayPreparingNextTaskState createPreparingNextTaskState() {
        return new PlayWarPreparingNextTaskState(manager.getContainer(), interval.getPreparingNextTaskInterval());
    }

    @Override
    protected synchronized PlayAnsweredState createAnsweredState(Long profileId, Long answerId) {
        return new PlayWarAnsweredState(getContainer(), profileId, answerId);
    }

    @Override
    protected synchronized PlayAnsweringTimeoutState createAnsweringTimeoutState() {
        return new PlayWarAnsweringTimeoutState(getContainer());
    }

    @Override
    protected synchronized void afterChoosingTaskPropsTimeoutPhase() {
        choosingWhoAnswerPhase();
    }

    @Override
    protected synchronized void afterChosenTaskDifficultyAction() {
        choosingWhoAnswerPhase();
    }

    @Override
    protected synchronized void afterRandomTaskPropsPhase() {
        after(interval.getRandomTaskPropsInterval(), aLong -> choosingWhoAnswerPhase());
    }

    protected synchronized void choosingWhoAnswerPhase() {
        WarInterval warInterval = (WarInterval) interval;
        addStateAndSend(new PlayWarChoosingWhoAnswerState(getContainer(), warInterval.getChoosingWhoAnswerInterval()));
        afterChoosingWhoAnswerPhase();
    }

    protected synchronized void afterChoosingWhoAnswerPhase() {
        WarInterval warInterval = (WarInterval) interval;
        after(warInterval.getChoosingWhoAnswerInterval(), aLong -> preparingNextTaskPhase());
    }

    public synchronized void chosenWhoAnswerAction(Long profileId, int activeIndex) {
        PlayWarChosenWhoAnswerState state = new PlayWarChosenWhoAnswerState(getContainer(), profileId, activeIndex);
        addState(state);
        if (state.isDone()) {
            stopAfter();
            afterChosenWhoAnswerAction();
        }
    }

    protected synchronized void afterChosenWhoAnswerAction() {
        preparingNextTaskPhase();
    }
}
