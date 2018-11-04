package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlayPrepareNextTaskCommand;
import com.ww.game.play.state.PlayChoosingTaskPropsTimeoutState;
import com.ww.game.play.state.PlayState;
import com.ww.model.constant.rival.RivalStatus;

public class PlayWarChoosingTaskPropsTimeoutState extends PlayChoosingTaskPropsTimeoutState {
    public PlayWarChoosingTaskPropsTimeoutState(PlayManager manager) {
        super(manager);
    }

    @Override
    public void after() {
        manager.getFlow().run("CHOOSING_WHO_ANSWER");
    }
}
