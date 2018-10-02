package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import io.reactivex.Flowable;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
public class State {
    protected static final Logger logger = LoggerFactory.getLogger(StateChosenTaskProps.class);

    protected RivalManager manager;

    public State(RivalManager manager) {
        this.manager = manager;
    }

    public Flowable<Long> startFlowable() {
        if (manager.isClosed()) {
            return Flowable.empty();
        }
        Flowable f =  processFlowable();
        logger.trace("Status: " + manager.getModel().getStatus().name());
        return f;
    }

    protected Flowable<Long> processFlowable() {
        return Flowable.empty();
    }

    public void startVoid() {
        if (manager.isClosed()) {
            return;
        }
        processVoid();
    }

    protected void processVoid() {
    }

    public Boolean startBoolean() {
        if (manager.isClosed()) {
            return null;
        }
        return processBoolean();
    }

    protected Boolean processBoolean() {
        return null;
    }
}
