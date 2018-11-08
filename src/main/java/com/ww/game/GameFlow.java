package com.ww.game;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static com.ww.helper.TagHelper.randomUniqueUUID;

public abstract class GameFlow {
    private static Logger logger = LoggerFactory.getLogger(GameFlow.class);
    protected List<GameState> states = new CopyOnWriteArrayList<>();
    protected Map<String, GameState> stateMap = new ConcurrentHashMap<>();
    protected Map<String, Disposable> disposableMap = new ConcurrentHashMap<>();

    protected abstract void initStateMap();

    public void addState(GameState state) {
        states.add(state);
    }

    public GameState currentState() {
        return states.get(states.size() - 1);
    }

    public synchronized void run(String stateName) {
        GameState state = stateMap.get(stateName);
        run(state);
    }

    public synchronized void run(GameState state) {
        logger.trace("run " + state.toString());
        addState(state);
        state.initCommands();
        state.execute();
        if (state.afterReady()) {
            stopAfter();
            state.updateNotify();
            startAfter(state);
        }
    }

    protected synchronized void after(long interval, Consumer<Long> onNext) {
        String uuid = randomUniqueUUID(disposableMap);
        disposableMap.put(uuid, prepareFlowable(interval).subscribe(aLong -> {
            synchronized (this) {
                if (disposableMap.containsKey(uuid)) {
                    callNext(interval, onNext);
                }
            }
        }));
    }

    private synchronized void callNext(long interval, Consumer<Long> onNext) {
        try {
            onNext.accept(interval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Flowable<Long> prepareFlowable(long interval) {
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }

    public synchronized void startAfter() {
        startAfter(currentState());
    }

    public synchronized void startAfter(GameState state) {
        long afterInterval = state.afterInterval();
        if (afterInterval == 0) {
            state.after();
        } else {
            after(afterInterval, aLong -> state.after());
        }
    }

    public synchronized void stopAfter() {
        for (Disposable disposable : disposableMap.values()) {
            disposable.dispose();
        }
        disposableMap.clear();
    }
}
