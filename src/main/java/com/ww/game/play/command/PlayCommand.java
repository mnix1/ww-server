package com.ww.game.play.command;

import com.ww.game.GameCommand;
import com.ww.game.play.container.PlayContainer;

public abstract class PlayCommand extends GameCommand {
    protected PlayContainer container;

    protected PlayCommand(PlayContainer container) {
        this.container = container;
    }

}
