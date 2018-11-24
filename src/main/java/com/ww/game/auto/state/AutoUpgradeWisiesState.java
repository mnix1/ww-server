package com.ww.game.auto.state;

import com.ww.game.auto.AutoManager;

public class AutoUpgradeWisiesState extends AutoState {

    public AutoUpgradeWisiesState(AutoManager manager) {
        super(manager);
    }

    @Override
    public void execute() {
        super.execute();
        manager.getAutoService().upgradeWisies(manager.getProfile());
    }

    @Override
    public void after(){
        manager.getFlow().run("MANAGE_MAILS");
    }
}
