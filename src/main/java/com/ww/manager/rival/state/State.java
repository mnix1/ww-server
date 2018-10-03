package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public abstract class State extends AbstractState {
    protected static final Logger logger = LoggerFactory.getLogger(State.class);

    protected RivalManager manager;

    protected State(RivalManager manager, String type) {
        this.manager = manager;
        setType(type);
    }

    public AbstractState startFlowable() {
        super.startFlowable();
        logger.trace(manager.toString() + ", startFlowable");
        return this;
    }
}
