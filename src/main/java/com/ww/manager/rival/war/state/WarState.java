package com.ww.manager.rival.war.state;

import com.ww.manager.rival.state.State;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.war.WarContainer;

public class WarState extends State {
    protected WarManager manager;

    public WarState(WarManager manager) {
        super(manager);
        this.manager = manager;
    }
}
