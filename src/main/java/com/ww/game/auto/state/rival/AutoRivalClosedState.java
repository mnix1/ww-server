package com.ww.game.auto.state.rival;

import com.ww.game.auto.AutoManager;

public class AutoRivalClosedState extends AutoRivalState {

    public AutoRivalClosedState(AutoManager manager) {
        super(manager);
    }

    @Override
    public void execute() {
        super.execute();
        manager.dispose();
    }
}
