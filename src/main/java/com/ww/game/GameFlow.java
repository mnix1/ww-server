package com.ww.game;

import com.ww.helper.TagHelper;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.ww.helper.TagHelper.randomUniqueUUID;

public abstract class GameFlow {
    public static Logger logger = LoggerFactory.getLogger(GameFlow.class);
    @Getter
    @Setter
    protected List<GameState> states = new CopyOnWriteArrayList<>();
    protected Map<String, Supplier<GameState>> stateMap = new ConcurrentHashMap<>();
    protected Map<String, Disposable> disposableMap = new ConcurrentHashMap<>();
    private boolean active = false;
    @Getter
    private String id;

    protected GameFlow() {
        this.id = TagHelper.randomUUID();
        initStateMap();
    }

    protected abstract void initStateMap();

    public void addState(GameState state) {
        states.add(state);
    }

    public GameState currentState() {
        return states.get(states.size() - 1);
    }

    public void start() {
        active = true;
    }

    public synchronized void run(String stateName) {
        run(stateName, null);
    }

    public synchronized void run(String stateName, Map<String, Object> params) {
        GameState state = stateMap.get(stateName).get();
        run(state, params);
    }

    public synchronized void run(GameState state) {
        run(state, null);
    }

    public synchronized void run(GameState state, Map<String, Object> params) {
        if (!active) {
            return;
        }
//        logger.trace("run " + toString() + ", " + state.toString());
        state.setParams(params);
        state.initProps();
        addState(state);
        state.execute();
        if (state.afterReady()) {
            if (state.stopAfter()) {
                stopAfter();
            }
            state.updateNotify();
            startAfter(state);
        }
    }

    protected synchronized void after(long interval, Consumer<Long> onNext) {
        String uuid = randomUniqueUUID(disposableMap);
        disposableMap.put(uuid, prepareFlowable(interval).subscribe(aLong -> {
            synchronized (this) {
                if (disposableMap.containsKey(uuid)) {
                    onNext.accept(interval);
                }
            }
        }));
    }

    public static Flowable<Long> prepareFlowable(long interval) {
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }

    public synchronized void startAfter(GameState state) {
        long afterInterval = state.afterInterval();
        if (afterInterval == 0) {
            //logger.trace("startAfter " + toString() + ", " + state.toString());
            state.after();
        } else {
            after(afterInterval, aLong -> {
                //   logger.trace("startAfter " + toString() + ", " + state.toString());
                state.after();
            });
        }
    }

    public synchronized boolean hasNext() {
        // logger.trace("hasNext " + toString() + " result=" + !disposableMap.isEmpty());
        return !disposableMap.isEmpty();
    }

    public synchronized void stopAfter() {
        //   logger.trace("stopAfter " + toString());
        for (Disposable disposable : disposableMap.values()) {
            disposable.dispose();
        }
        disposableMap.clear();
    }

    public synchronized void stop() {
        if (!active) {
            return;
        }
        active = false;
        //  logger.trace("stop " + toString());
        stopAfter();
    }

    public synchronized void resume() {
        if (active) {
            return;
        }
        active = true;
        //  logger.trace("stop " + toString());
        currentState().after();
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "states=" + StringUtils.join(states, ",^_^") +
                '}';
    }
}
