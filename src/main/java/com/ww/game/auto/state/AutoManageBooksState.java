package com.ww.game.auto.state;

import com.ww.game.auto.AutoManager;

public class AutoManageBooksState extends AutoState {

    public AutoManageBooksState(AutoManager manager) {
        super(manager);
    }

    @Override
    public void execute() {
        super.execute();
        manager.manageBooks();
    }

    @Override
    public void after(){
        manager.getFlow().run("UPGRADE_WISIES");
    }
}
