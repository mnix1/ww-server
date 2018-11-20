package com.ww.game.auto.state;

import com.ww.game.auto.AutoManager;

public class AutoStartRivalState extends AutoState {

    public AutoStartRivalState(AutoManager manager) {
        super(manager);
    }

    @Override
    public void execute() {
        super.execute();
        manager.getAutoService().startRival(manager);
    }
}
