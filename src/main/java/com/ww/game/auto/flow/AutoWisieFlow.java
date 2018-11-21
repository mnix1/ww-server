package com.ww.game.auto.flow;

import com.ww.game.GameFlow;
import com.ww.game.auto.AutoManager;

public class AutoWisieFlow extends GameFlow {
    private AutoManager manager;

    public AutoWisieFlow(AutoManager manager){
        this.manager = manager;
        initStateMap();
    }

    @Override
    protected void initStateMap() {
    }

    @Override
    public void start() {
    }
}
