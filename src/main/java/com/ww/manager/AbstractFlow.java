package com.ww.manager;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ww.manager.AbstractState.STATE_TYPE_FLOWABLE;

@Getter
public abstract class AbstractFlow {
    protected List<AbstractState> states = new CopyOnWriteArrayList<>();
    protected List<AbstractState> flowableStates = new CopyOnWriteArrayList<>();

    public AbstractState lastFlowableState() {
        return flowableStates.get(flowableStates.size() - 1);
    }

    public synchronized void dispose() {
        lastFlowableState().dispose();
    }

    protected synchronized AbstractState addState(AbstractState state) {
        states.add(state);
        if (state.getType().equals(STATE_TYPE_FLOWABLE)) {
            flowableStates.add(state);
        }
        return state;
    }
}
