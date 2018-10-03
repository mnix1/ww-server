package com.ww.manager.rival.war.state;

import com.ww.manager.rival.state.AbstractState;
import com.ww.manager.rival.war.WarManager;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public abstract class WarState extends AbstractState {
    protected static final Logger logger = LoggerFactory.getLogger(WarState.class);

    protected WarManager manager;

    protected WarState(WarManager manager, String type) {
        this.manager = manager;
        setType(type);
    }

    public AbstractState startFlowable() {
        logger.trace(manager.toString() + ", startFlowable");
        super.startFlowable();
        return this;
    }
}
