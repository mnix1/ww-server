package com.ww.game.play.command;

import com.ww.game.play.container.PlayContainer;

public class PlaySetDefaultTaskPropsCommand extends PlayCommand {

    public PlaySetDefaultTaskPropsCommand(PlayContainer container) {
        super(container);
    }

    @Override
    public void execute() {
        container.getDecisions().defaultTaskProps();
    }
}
