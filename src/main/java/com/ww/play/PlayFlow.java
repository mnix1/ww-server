package com.ww.play;

import com.ww.model.container.rival.RivalInterval;
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
    protected PlayModel model;
    protected RivalInterval interval;

    protected Map<String, Disposable> disposableMap = new ConcurrentHashMap<>();

    public PlayFlow(PlayManager manager, PlayModel model, RivalInterval interval) {
        this.manager = manager;
        this.model = model;
        this.interval = interval;
    }

    protected synchronized void addState(PlayState state) {
        model.add(state);
        state.process();

    }

    public synchronized void stopAfter() {
        for (Disposable disposable : disposableMap.values()) {
            disposable.dispose();
        }
        disposableMap.clear();
    }

    public synchronized void introPhase() {
        addState(new PlayIntroState());
        afterIntroPhase();
    }

    public synchronized void afterIntroPhase() {
        after(interval.getIntroInterval(), aLong -> endPhaseOrTaskPropsPhase());
    }

    public synchronized void endPhaseOrTaskPropsPhase() {
        if (model.isEnd()) {
            endPhase();
        } else if (model.isRandomTaskProps()) {
            randomTaskPropsPhase();
        } else {
            choosingTaskPropsPhase();
        }
    }

    public synchronized void surrenderAction(Long profileId) {
        addState(new PlaySurrenderState(profileId));
        manager.dispose();
    }

    public synchronized void endPhase() {
        addState(new PlayEndState());
        manager.dispose();
    }

    public synchronized void randomTaskPropsPhase() {
        addState(new PlayRandomTaskPropsState());
        afterRandomTaskPropsPhase();
    }

    public synchronized void afterRandomTaskPropsPhase() {
        after(interval.getRandomTaskPropsInterval(), aLong -> preparingNextTaskPhase());
    }

    public synchronized void choosingTaskPropsPhase() {
        addState(new PlayChoosingTaskPropsState());
        afterChoosingTaskPropsPhase();
    }

    public synchronized void afterChoosingTaskPropsPhase() {
        after(interval.getChoosingTaskPropsInterval(), aLong -> choosingTaskPropsTimeoutPhase());
    }

    public synchronized void choosingTaskPropsTimeoutPhase() {
        addState(new PlayChoosingTaskPropsTimeoutState());
        afterChoosingTaskPropsTimeoutPhase();
    }

    public synchronized void afterChoosingTaskPropsTimeoutPhase() {
        preparingNextTaskPhase();
    }

    public synchronized void preparingNextTaskPhase() {
        addState(new PlayPreparingNextTaskState());
        afterPreparingNextTaskPhase();
    }

    public synchronized void afterPreparingNextTaskPhase() {
        after(interval.getPreparingNextTaskInterval(), aLong -> answeringPhase());
    }

    public synchronized void answeringPhase() {
        addState(new PlayAnsweringState());
        afterAnsweringPhase();
    }

    public synchronized void afterAnsweringPhase() {
        after(interval.getAnsweringInterval(), aLong -> answeringTimeoutPhase());
    }

    public synchronized void answeringTimeoutPhase() {
        addState(new PlayAnsweringTimeoutState());
        afterAnsweringTimeoutPhase();
    }

    public synchronized void afterAnsweringTimeoutPhase() {
        after(interval.getAnsweringTimeoutInterval(), aLong -> endPhaseOrTaskPropsPhase());
    }

    public synchronized void answeredAction(Long profileId, Map<String, Object> content) {
        stopAfter();
        addState(new PlayAnsweredState(profileId, content));
        afterAnsweredAction();
    }

    public synchronized void afterAnsweredAction() {
        after(interval.getAnsweredInterval(), aLong -> endPhaseOrTaskPropsPhase());
    }

    public synchronized void chosenTaskPropsAction(Long profileId, Map<String, Object> content) {
        PlayChosenTaskPropsState state = new PlayChosenTaskPropsState(profileId, content);
        addState(state);
        if (state.isDone()) {
            stopAfter();
            afterChosenTaskPropsAction();
        }
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
