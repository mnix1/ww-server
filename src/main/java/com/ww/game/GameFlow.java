package com.ww.game;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.ww.helper.TagHelper.randomUniqueUUID;

public abstract class GameFlow {
    protected Map<String, Disposable> disposableMap = new ConcurrentHashMap<>();

    public abstract void start();

    protected synchronized void after(long interval, Consumer<Long> onNext) {
        if (interval == 0) {
            callNext(interval, onNext);
            return;
        }
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

    protected synchronized void stopAfter() {
        for (Disposable disposable : disposableMap.values()) {
            disposable.dispose();
        }
        disposableMap.clear();
    }

    protected abstract GameContainer getContainer();

    protected synchronized void addState(GameState state) {
        getContainer().addState(state);
        state.initCommands();
        state.execute();
    }

    protected synchronized void addChildState(GameState state) {
        getContainer().currentState().addChildState(state);
        state.initCommands();
        state.execute();
    }
}
