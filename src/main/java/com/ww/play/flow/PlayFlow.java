package com.ww.play.flow;

import com.ww.model.container.rival.RivalInterval;
import com.ww.play.PlayManager;
import com.ww.play.communication.PlayCommunication;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.*;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.ww.helper.TagHelper.randomUniqueUUID;

public class PlayFlow {
    protected PlayManager manager;
    protected RivalInterval interval;

    protected Map<String, Disposable> disposableMap = new ConcurrentHashMap<>();

    public PlayFlow(PlayManager manager, RivalInterval interval) {
        this.manager = manager;
        this.interval = interval;
    }

    protected PlayCommunication getCommunication() {
        return manager.getCommunication();
    }

    protected PlayContainer getContainer() {
        return manager.getContainer();
    }

    protected synchronized void addState(PlayState state) {
        getContainer().addState(state);
        state.process();
    }

    protected synchronized void send() {
        getCommunication().send();
    }

    protected synchronized void addStateAndSend(PlayState state) {
        addState(state);
        send();
    }

    public synchronized void stopAfter() {
        for (Disposable disposable : disposableMap.values()) {
            disposable.dispose();
        }
        disposableMap.clear();
    }

    public synchronized void introPhase() {
        addStateAndSend(createIntroState());
        afterIntroPhase();
    }

    public synchronized PlayIntroState createIntroState() {
        return new PlayIntroState(manager.getContainer());
    }

    public synchronized void afterIntroPhase() {
        after(interval.getIntroInterval(), aLong -> endPhaseOrTaskPropsPhase());
    }

    public synchronized void endPhaseOrTaskPropsPhase() {
        if (getContainer().isEnd()) {
            endPhase();
        } else if (getContainer().isRandomTaskProps()) {
            randomTaskPropsPhase();
        } else {
            choosingTaskPropsPhase();
        }
    }

    public synchronized void surrenderAction(Long profileId) {
        addStateAndSend(createSurrenderState(profileId));
        manager.dispose();
    }

    protected synchronized PlaySurrenderState createSurrenderState(Long profileId) {
        return new PlaySurrenderState(getContainer(), profileId);
    }

    public synchronized void endPhase() {
        addStateAndSend(createEndState());
        manager.dispose();
    }

    protected synchronized PlayEndState createEndState() {
        return new PlayEndState(getContainer());
    }

    public synchronized void randomTaskPropsPhase() {
        addStateAndSend(createRandomTaskPropsState());
        afterRandomTaskPropsPhase();
    }

    protected synchronized PlayRandomTaskPropsState createRandomTaskPropsState() {
        return new PlayRandomTaskPropsState(getContainer());
    }

    public synchronized void afterRandomTaskPropsPhase() {
        after(interval.getRandomTaskPropsInterval(), aLong -> preparingNextTaskPhase());
    }

    public synchronized void choosingTaskPropsPhase() {
        addStateAndSend(createChoosingTaskPropsState());
        afterChoosingTaskPropsPhase();
    }

    protected synchronized PlayChoosingTaskPropsState createChoosingTaskPropsState() {
        return new PlayChoosingTaskPropsState(getContainer());
    }

    public synchronized void afterChoosingTaskPropsPhase() {
        after(interval.getChoosingTaskPropsInterval(), aLong -> choosingTaskPropsTimeoutPhase());
    }

    public synchronized void choosingTaskPropsTimeoutPhase() {
        addStateAndSend(createChoosingTaskPropsTimeoutState());
        afterChoosingTaskPropsTimeoutPhase();
    }

    protected synchronized PlayChoosingTaskPropsTimeoutState createChoosingTaskPropsTimeoutState() {
        return new PlayChoosingTaskPropsTimeoutState(getContainer());
    }

    public synchronized void afterChoosingTaskPropsTimeoutPhase() {
        preparingNextTaskPhase();
    }

    public synchronized void preparingNextTaskPhase() {
        addStateAndSend(createPreparingNextTaskState());
        afterPreparingNextTaskPhase();
    }

    protected synchronized PlayPreparingNextTaskState createPreparingNextTaskState() {
        return new PlayPreparingNextTaskState(getContainer());
    }

    public synchronized void afterPreparingNextTaskPhase() {
        after(interval.getPreparingNextTaskInterval(), aLong -> answeringPhase());
    }

    public synchronized void answeringPhase() {
        addStateAndSend(createAnsweringState());
        afterAnsweringPhase();
    }

    protected synchronized PlayAnsweringState createAnsweringState() {
        return new PlayAnsweringState(getContainer());
    }

    public synchronized void afterAnsweringPhase() {
        after(interval.getAnsweringInterval(), aLong -> answeringTimeoutPhase());
    }

    public synchronized void answeringTimeoutPhase() {
        addStateAndSend(createAnsweringTimeoutState());
        afterAnsweringTimeoutPhase();
    }

    protected synchronized PlayAnsweringTimeoutState createAnsweringTimeoutState() {
        return new PlayAnsweringTimeoutState(getContainer());
    }

    public synchronized void afterAnsweringTimeoutPhase() {
        after(interval.getAnsweringTimeoutInterval(), aLong -> endPhaseOrTaskPropsPhase());
    }

    public synchronized void answeredAction(Long profileId, Map<String, Object> content) {
        stopAfter();
        addStateAndSend(createAnsweredState(profileId, content));
        afterAnsweredAction();
    }

    protected synchronized PlayAnsweredState createAnsweredState(Long profileId, Map<String, Object> content) {
        return new PlayAnsweredState(getContainer(), profileId, content);
    }

    public synchronized void afterAnsweredAction() {
        after(interval.getAnsweredInterval(), aLong -> endPhaseOrTaskPropsPhase());
    }

    public synchronized void chosenTaskPropsAction(Long profileId, Map<String, Object> content) {
        PlayChosenTaskPropsState state = createChosenTaskPropsState(profileId, content);
        addState(state);
        if (state.isDone()) {
            stopAfter();
            send();
            afterChosenTaskPropsAction();
        }
    }

    protected synchronized PlayChosenTaskPropsState createChosenTaskPropsState(Long profileId, Map<String, Object> content) {
        return new PlayChosenTaskPropsState(getContainer(), profileId, content);
    }

    public synchronized void afterChosenTaskPropsAction() {
        preparingNextTaskPhase();
    }

    private synchronized void after(long interval, Consumer<Long> onNext) {
        String uuid = randomUniqueUUID(disposableMap);
        disposableMap.put(uuid, prepareFlowable(interval).subscribe(aLong -> {
            synchronized (this) {
                if (disposableMap.containsKey(uuid)) {
                    onNext.accept(aLong);
                }
            }
        }));
    }

    private Flowable<Long> prepareFlowable(long interval) {
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
