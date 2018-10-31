package com.ww.play.command;

import com.ww.play.PlayManager;
import com.ww.play.container.PlayContainer;
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
