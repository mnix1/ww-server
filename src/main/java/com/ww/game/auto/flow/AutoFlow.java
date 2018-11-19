package com.ww.game.auto.flow;

import com.ww.game.GameFlow;
import com.ww.game.auto.AutoManager;
import com.ww.game.auto.state.AutoManageBooksState;
import com.ww.game.auto.state.AutoUpgradeWisiesState;

public class AutoFlow extends GameFlow {
    private AutoManager manager;

    public AutoFlow(AutoManager manager){
        this.manager = manager;
        initStateMap();
    }

    @Override
    protected void initStateMap() {
        stateMap.put("MANAGE_BOOKS", () -> new AutoManageBooksState(manager));
        stateMap.put("UPGRADE_WISIES", () -> new AutoUpgradeWisiesState(manager));
    }
}
