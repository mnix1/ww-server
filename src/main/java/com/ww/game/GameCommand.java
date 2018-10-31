package com.ww.game;

public abstract class GameCommand {
    protected GameContainer container;

    protected GameCommand(GameContainer container) {
        this.container = container;
    }

    public void execute(){}

    public void revoke(){}
}
