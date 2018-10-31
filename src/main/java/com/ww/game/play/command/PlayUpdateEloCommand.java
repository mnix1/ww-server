package com.ww.game.play.command;

import com.ww.game.play.PlayManager;
import com.ww.game.play.container.PlayContainer;
import com.ww.service.RivalService;

public class PlayUpdateEloCommand extends PlayCommand {
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
