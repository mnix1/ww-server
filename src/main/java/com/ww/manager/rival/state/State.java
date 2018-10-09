package com.ww.manager.rival.state;

import com.ww.manager.AbstractState;
import com.ww.manager.rival.RivalManager;
import lombok.Getter;

public abstract class State extends AbstractState {
    protected RivalManager manager;

    protected State(RivalManager manager, String type) {
        this.manager = manager;
        setType(type);
    }

    @Override
    public String describe() {
        return "State " + super.describe() + ", warManager=" + manager.describe();
    }

    @Override
    public boolean isRunning() {
        return !manager.isClosed();
    }
}
