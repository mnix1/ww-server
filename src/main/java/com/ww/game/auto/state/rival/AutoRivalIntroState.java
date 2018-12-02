package com.ww.game.auto.state.rival;

import com.ww.game.auto.AutoManager;
import com.ww.game.auto.container.AutoPlayContainer;
import com.ww.game.auto.state.AutoState;

public class AutoRivalIntroState extends AutoState {

    public AutoRivalIntroState(AutoManager manager) {
        super(manager);
    }

    @Override
    public void execute() {
        super.execute();
        manager.initAutoPlayContainer();
    }
}
