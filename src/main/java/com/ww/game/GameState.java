package com.ww.game;

import com.ww.model.container.rival.RivalTeam;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {
    protected List<GameCommand> commands = new CopyOnWriteArrayList<>();
    protected List<GameState> childStates = new CopyOnWriteArrayList<>();
    protected Instant date = Instant.now();

    public void initCommands() {
    }

    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        return new HashMap<>();
    }

    public Map<String, Object> prepareChildModel(RivalTeam team, RivalTeam opponentTeam) {
        return new HashMap<>();
    }

    public void addChildState(GameState state) {
        childStates.add(state);
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
