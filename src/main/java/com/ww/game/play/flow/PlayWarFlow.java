package com.ww.game.play.flow;

import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import com.ww.game.play.state.*;
import com.ww.game.play.state.skill.PlaySkillHintState;
import com.ww.game.play.state.war.*;
import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.war.WarInterval;
import com.ww.model.container.rival.war.WarTeam;

public class PlayWarFlow extends PlayFlow {

    public PlayWarFlow(PlayManager manager, RivalInterval interval) {
        super(manager, interval);
    }

    @Override
    protected synchronized PlayIntroState createIntroState() {
        return new PlayWarIntroState(getContainer());
    }

    @Override
    protected synchronized PlayPreparingNextTaskState createPreparingNextTaskState() {
        return new PlayWarPreparingNextTaskState(getContainer(), interval.getPreparingNextTaskInterval());
    }

    @Override
    protected synchronized PlayAnsweringState createAnsweringState() {
        return new PlayWarAnsweringState(manager, interval.getAnsweringInterval());
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
        addStateAndSend(createChoosingWhoAnswerState());
        afterChoosingWhoAnswerPhase();
    }

    protected synchronized PlayState createChoosingWhoAnswerState() {
        return new PlayWarChoosingWhoAnswerState(getContainer(), ((WarInterval) interval).getChoosingWhoAnswerInterval());
    }

    protected synchronized void afterChoosingWhoAnswerPhase() {
        WarInterval warInterval = (WarInterval) interval;
        after(warInterval.getChoosingWhoAnswerInterval(), aLong -> preparingNextTaskPhase());
    }

    public synchronized void chosenWhoAnswerAction(Long profileId, int activeIndex) {
        PlayWarChosenWhoAnswerState state = new PlayWarChosenWhoAnswerState(getContainer(), profileId, activeIndex);
        addChildState(state);
        if (state.isDone()) {
            stopAfter();
            afterChosenWhoAnswerAction();
        }
    }

    protected synchronized void afterChosenWhoAnswerAction() {
        preparingNextTaskPhase();
    }

    public synchronized void hintSkillAction(WarTeam warTeam, Long answerId) {
        GameState state = new PlaySkillHintState(getContainer(), warTeam, answerId);
        addChildState(state);
    }
}
