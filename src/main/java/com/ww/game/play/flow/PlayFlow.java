package com.ww.game.play.flow;

import com.ww.game.GameFlow;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.RivalInterval;
import com.ww.game.play.PlayManager;
import com.ww.game.play.communication.PlayCommunication;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.*;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.ww.helper.TagHelper.randomUniqueUUID;

public class PlayFlow extends GameFlow {
    protected PlayManager manager;
    protected RivalInterval interval;

    public PlayFlow(PlayManager manager, RivalInterval interval) {
        this.manager = manager;
        this.interval = interval;
    }

    protected PlayCommunication getCommunication() {
        return manager.getCommunication();
    }

    @Override
    protected PlayContainer getContainer() {
        return manager.getContainer();
    }

    protected synchronized void send() {
        getCommunication().send();
    }

    protected synchronized void addStateAndSend(PlayState state) {
        addState(state);
        send();
    }

    public synchronized void introPhase() {
        addStateAndSend(createIntroState());
        afterIntroPhase();
    }

    protected synchronized PlayIntroState createIntroState() {
        return new PlayIntroState(manager.getContainer());
    }

    protected synchronized void afterIntroPhase() {
        after(interval.getIntroInterval(), aLong -> endPhaseOrTaskPropsPhase());
    }

    protected synchronized void endPhaseOrTaskPropsPhase() {
        if (getContainer().isEnd()) {
            endPhase();
        } else if (getContainer().isRandomTaskProps()) {
            randomTaskPropsPhase();
        } else {
            choosingTaskCategoryPhase();
        }
    }

    protected synchronized void endPhase() {
        addStateAndSend(createEndState());
        manager.dispose();
    }

    protected synchronized PlayEndState createEndState() {
        return new PlayEndState(getContainer(), manager);
    }

    protected synchronized void randomTaskPropsPhase() {
        addStateAndSend(createRandomTaskPropsState());
        afterRandomTaskPropsPhase();
    }

    protected synchronized PlayRandomTaskPropsState createRandomTaskPropsState() {
        return new PlayRandomTaskPropsState(getContainer());
    }

    protected synchronized void afterRandomTaskPropsPhase() {
        after(interval.getRandomTaskPropsInterval(), aLong -> preparingNextTaskPhase());
    }

    protected synchronized void choosingTaskCategoryPhase() {
        addStateAndSend(createChoosingTaskCategoryState());
        afterChoosingTaskPropsPhase(interval.getChoosingTaskCategoryInterval());
    }

    protected synchronized PlayChoosingTaskCategoryState createChoosingTaskCategoryState() {
        return new PlayChoosingTaskCategoryState(getContainer(), interval.getChoosingTaskCategoryInterval());
    }

    protected synchronized void choosingTaskDifficultyPhase() {
        addStateAndSend(createChoosingTaskDifficultyState());
        afterChoosingTaskPropsPhase(interval.getChoosingTaskDifficultyInterval());
    }

    protected synchronized PlayChoosingTaskDifficultyState createChoosingTaskDifficultyState() {
        return new PlayChoosingTaskDifficultyState(getContainer(), interval.getChoosingTaskDifficultyInterval());
    }

    protected synchronized void afterChoosingTaskPropsPhase(long interval) {
        after(interval, aLong -> choosingTaskPropsTimeoutPhase());
    }

    protected synchronized void choosingTaskPropsTimeoutPhase() {
        addState(createChoosingTaskPropsTimeoutState());
        afterChoosingTaskPropsTimeoutPhase();
    }

    protected synchronized PlayChoosingTaskPropsTimeoutState createChoosingTaskPropsTimeoutState() {
        return new PlayChoosingTaskPropsTimeoutState(getContainer());
    }

    protected synchronized void afterChoosingTaskPropsTimeoutPhase() {
        preparingNextTaskPhase();
    }

    protected synchronized void preparingNextTaskPhase() {
        addStateAndSend(createPreparingNextTaskState());
        afterPreparingNextTaskPhase();
    }

    protected synchronized PlayPreparingNextTaskState createPreparingNextTaskState() {
        return new PlayPreparingNextTaskState(getContainer(), interval.getPreparingNextTaskInterval());
    }

    protected synchronized void afterPreparingNextTaskPhase() {
        after(interval.getPreparingNextTaskInterval(), aLong -> answeringPhase());
    }

    protected synchronized void answeringPhase() {
        addStateAndSend(createAnsweringState());
        afterAnsweringPhase();
    }

    protected synchronized PlayAnsweringState createAnsweringState() {
        return new PlayAnsweringState(getContainer(), interval.getAnsweringInterval());
    }

    protected synchronized void afterAnsweringPhase() {
        after(interval.getAnsweringInterval(), aLong -> answeringTimeoutPhase());
    }

    protected synchronized void answeringTimeoutPhase() {
        addStateAndSend(createAnsweringTimeoutState());
        afterAnsweringTimeoutPhase();
    }

    protected synchronized PlayAnsweringTimeoutState createAnsweringTimeoutState() {
        return new PlayAnsweringTimeoutState(getContainer());
    }

    protected synchronized void afterAnsweringTimeoutPhase() {
        after(interval.getAnsweringTimeoutInterval(), aLong -> endPhaseOrTaskPropsPhase());
    }

    public synchronized void surrenderAction(Long profileId) {
        stopAfter();
        addStateAndSend(createSurrenderState(profileId));
        manager.dispose();
    }

    protected synchronized PlaySurrenderState createSurrenderState(Long profileId) {
        return new PlaySurrenderState(getContainer(), profileId, manager);
    }

    public synchronized void answeredAction(Long profileId, Long answerId) {
        stopAfter();
        addStateAndSend(createAnsweredState(profileId, answerId));
        afterAnsweredAction();
    }

    protected synchronized PlayState createAnsweredState(Long profileId, Long answerId) {
        return new PlayAnsweredState(getContainer(), profileId, answerId);
    }

    public synchronized void afterAnsweredAction() {
        after(interval.getAnsweredInterval(), aLong -> endPhaseOrTaskPropsPhase());
    }

    public synchronized void chosenTaskCategoryAction(Category category) {
        stopAfter();
        addState(createChosenTaskCategoryState(category));
        afterChosenTaskCategoryAction();
    }

    protected synchronized PlayChosenTaskCategoryState createChosenTaskCategoryState(Category category) {
        return new PlayChosenTaskCategoryState(getContainer(), category);
    }

    protected synchronized void afterChosenTaskCategoryAction() {
        choosingTaskDifficultyPhase();
    }

    public synchronized void chosenTaskDifficultyAction(DifficultyLevel difficultyLevel) {
        stopAfter();
        addState(createChosenTaskDifficultyState(difficultyLevel));
        afterChosenTaskDifficultyAction();
    }

    protected synchronized PlayChosenTaskDifficultyState createChosenTaskDifficultyState(DifficultyLevel difficultyLevel) {
        return new PlayChosenTaskDifficultyState(getContainer(), difficultyLevel);
    }

    protected synchronized void afterChosenTaskDifficultyAction() {
        preparingNextTaskPhase();
    }
}
