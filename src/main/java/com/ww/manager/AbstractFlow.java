package com.ww.manager;

import com.ww.helper.Describe;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ww.manager.AbstractState.STATE_TYPE_FLOWABLE;

@Getter
public abstract class AbstractFlow implements Describe {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractFlow.class);

    protected List<AbstractState> states = new CopyOnWriteArrayList<>();
    protected List<AbstractState> flowableStates = new CopyOnWriteArrayList<>();

    public AbstractState lastFlowableState() {
        return flowableStates.get(flowableStates.size() - 1);
    }

    public synchronized void dispose() {
        lastFlowableState().dispose();
    }

    @Override
    public String describe() {
        return ", class=" + this.getClass().getName() + ", statesCount=" + states.size() + ", flowableStatesCount=" + flowableStates.size();
    }

    public String statesDescribe() {
        StringBuilder describe = new StringBuilder();
        for (AbstractState state : states) {
            describe.append("\n  ");
            describe.append(state.getClass().getName());
        }
        return describe.toString();
    }

    protected synchronized AbstractState addState(AbstractState state) {
        states.add(state);
        if (state.getType().equals(STATE_TYPE_FLOWABLE)) {
            flowableStates.add(state);
        }
        return state;
    }
}
