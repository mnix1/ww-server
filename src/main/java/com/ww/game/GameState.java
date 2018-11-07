package com.ww.game;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {
    protected List<GameCommand> commands = new CopyOnWriteArrayList<>();
    protected Instant date = Instant.now();

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

    public void updateNotify() {
    }

    public boolean afterReady() {
        return true;
    }

    public long afterInterval() {
        return 0;
    }

    public void after() {
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }
}
