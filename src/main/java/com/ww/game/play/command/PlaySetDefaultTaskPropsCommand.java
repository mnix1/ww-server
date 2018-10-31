package com.ww.game.play.command;

import com.ww.game.GameCommand;
import com.ww.game.play.container.PlayContainer;

public class PlaySetDefaultTaskPropsCommand extends GameCommand {

    public PlaySetDefaultTaskPropsCommand(PlayContainer container) {
        super(container);
    }

    @Override
    public void execute() {
        container.getDecisions().defaultTaskProps();
    }
}
