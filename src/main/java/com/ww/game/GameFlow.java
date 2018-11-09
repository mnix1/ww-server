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
    private List<Consumer<GameFlow>> outerFlowConsumers = new CopyOnWriteArrayList<>();
    private List<GameFlow> outerFlows = new CopyOnWriteArrayList<>();
    private GameFlow innerFlow;

    protected abstract void initStateMap();

    public void addState(GameState state) {
        states.add(state);
    }

    public GameState currentState() {
        return states.get(states.size() - 1);
    }

    public synchronized void run(String stateName) {
        run(stateName, null);
    }

    public synchronized void run(String stateName, Map<String, Object> params) {
        GameState state = stateMap.get(stateName);
        run(state, params);
    }

    public synchronized void run(GameState state) {
        run(state, null);
    }

    public synchronized void run(GameState state, Map<String, Object> params) {
        logger.trace("run " + state.toString());
        state.setParams(params);
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

    public synchronized void notifyOuter(GameFlow flow) {
        try {
            for (int i = 0; i < outerFlows.size(); i++) {
                if (outerFlows.get(i) == flow) {
                    outerFlowConsumers.get(i).accept(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void innerFlow(GameFlow flow) {
        if (innerFlow != null) {
            innerFlow.innerFlow(flow);
            return;
        }
        innerFlow = flow;
        stopAfter();
        GameState state = currentState();
        flow.addOuterFlow(this, f -> {
            innerFlow = null;
            state.after();
        });
    }

    public void addOuterFlow(GameFlow flow, Consumer<GameFlow> consumer) {
        outerFlows.add(flow);
        outerFlowConsumers.add(consumer);
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

    private Flowable<Long> prepareFlowable(long interval) {
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
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

    public synchronized void stop() {
        stopAfter();
        if (innerFlow != null) {
            innerFlow.stopAfter();
        }
    }
}
