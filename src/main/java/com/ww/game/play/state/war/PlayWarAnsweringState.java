package com.ww.game.play.state.war;

import com.ww.game.play.PlayWarManager;
import com.ww.game.play.command.war.PlayWarInitMemberManagerCommand;
import com.ww.game.play.state.PlayAnsweringState;

public class PlayWarAnsweringState extends PlayAnsweringState {
    private PlayWarManager manager;

    public PlayWarAnsweringState(PlayWarManager manager, long interval) {
        super(manager.getContainer(), interval);
        this.manager = manager;
    }

    @Override
    public void initCommands() {
        super.initCommands();
        commands.add(new PlayWarInitMemberManagerCommand(manager));
    }
}
