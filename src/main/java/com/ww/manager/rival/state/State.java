package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.container.rival.RivalContainer;
import io.reactivex.Flowable;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
public class State {
    protected static final Logger logger = LoggerFactory.getLogger(StateChosenTaskProps.class);

    protected RivalManager rivalManager;
    protected RivalContainer rivalContainer;

    public State(RivalManager rivalManager) {
        this.rivalManager = rivalManager;
        this.rivalContainer = rivalManager.getRivalContainer();
    }

    public Flowable<Long> startFlowable() {
        if (this.rivalManager.isClosed()) {
            return Flowable.empty();
        }
        Flowable f =  processFlowable();
        logger.trace("Status: " + rivalContainer.getStatus().name());
        return f;
    }

    protected Flowable<Long> processFlowable() {
        return Flowable.empty();
    }

    public void startVoid() {
        if (this.rivalManager.isClosed()) {
            return;
        }
        processVoid();
    }

    protected void processVoid() {
    }

    public Boolean startBoolean() {
        if (this.rivalManager.isClosed()) {
            return null;
        }
        return processBoolean();
    }

    protected Boolean processBoolean() {
        return null;
    }
}
