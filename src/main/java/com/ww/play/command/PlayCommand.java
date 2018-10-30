package com.ww.play.command;

import com.ww.play.container.PlayContainer;

public abstract class PlayCommand {
    protected PlayContainer container;

    protected PlayCommand(PlayContainer container) {
        this.container = container;
    }

    public void execute(){}

    public void revoke(){}
}
