package com.ww.game.auto.state.wisor;

import com.ww.game.GameState;
import com.ww.game.auto.AutoManager;
import com.ww.game.auto.flow.AutoWisorFlow;

public class AutoWisorState extends GameState {
    protected AutoWisorFlow flow;
    protected AutoManager manager;

    public AutoWisorState(AutoWisorFlow flow, AutoManager manager) {
        this.flow = flow;
        this.manager = manager;
    }
}
