package com.ww.game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {
    protected GameContainer container;
    protected List<GameCommand> commands = new CopyOnWriteArrayList<>();

    public void initCommands() {
    }

    public void execute() {
        for (GameCommand command : commands) {
            command.execute();
        }
    }

    public void revoke() {
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).revoke();
        }
    }
}
