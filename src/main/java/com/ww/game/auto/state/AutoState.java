package com.ww.game.auto.state;

import com.ww.game.GameState;
import com.ww.game.auto.AutoManager;

public class AutoState extends GameState {
    protected AutoManager manager;

    public AutoState(AutoManager manager) {
        this.manager = manager;
    }
}
