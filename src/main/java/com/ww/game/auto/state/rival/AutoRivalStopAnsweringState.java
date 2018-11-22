package com.ww.game.auto.state.rival;

import com.ww.game.auto.AutoManager;

public class AutoRivalStopAnsweringState extends AutoRivalState {

    public AutoRivalStopAnsweringState(AutoManager manager) {
        super(manager);
    }

    @Override
    public void execute() {
        super.execute();
        container.dispose();
    }
}
