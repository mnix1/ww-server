package com.ww.game.auto.state;

import com.ww.game.auto.AutoManager;

public class AutoManageMailsState extends AutoState {

    public AutoManageMailsState(AutoManager manager) {
        super(manager);
    }

    @Override
    public void execute() {
        super.execute();
        manager.getAutoService().manageMails(manager.getProfile());
    }

    @Override
    public void after(){
        manager.getFlow().run("START_RIVAL");
    }
}
