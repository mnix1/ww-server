package com.ww.manager.rival.war.state;

import com.ww.manager.rival.state.State;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.war.WarContainer;

public class WarState extends State {
    protected WarManager warManager;
    protected WarContainer warContainer;

    public WarState(WarManager manager) {
        super(manager);
        this.warManager = manager;
        this.warContainer = manager.warContainer;
    }
}
