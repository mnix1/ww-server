package com.ww.manager.rival.war.state;

import com.ww.manager.AbstractState;
import com.ww.manager.rival.war.WarManager;
import lombok.Getter;

public abstract class WarState extends AbstractState {
    protected WarManager manager;

    protected WarState(WarManager manager, String type) {
        this.manager = manager;
        setType(type);
    }

    @Override
    public String describe() {
        return "WarState " + super.describe() + ", warManager=" + manager.describe();
    }

    @Override
    public boolean isRunning() {
        return !manager.isClosed();
    }

}

