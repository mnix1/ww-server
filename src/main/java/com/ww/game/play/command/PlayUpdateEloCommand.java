package com.ww.game.play.command;

import com.ww.game.GameCommand;
import com.ww.game.play.PlayManager;

public class PlayUpdateEloCommand extends GameCommand {
    private PlayManager manager;

    public PlayUpdateEloCommand(PlayManager manager) {
        super(manager.getContainer());
        this.manager = manager;
    }

    @Override
    public void execute() {
        manager.updateProfilesElo();
    }
}
