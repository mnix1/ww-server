package com.ww.game.play.command;

import com.ww.game.play.container.PlayContainer;

public abstract class PlayCommand {
    protected PlayContainer container;

    protected PlayCommand(PlayContainer container) {
        this.container = container;
    }

    public void execute(){}

    public void revoke(){}
}
