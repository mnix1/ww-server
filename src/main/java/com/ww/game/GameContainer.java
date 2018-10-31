package com.ww.game;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class GameContainer {
    protected List<GameState> states = new CopyOnWriteArrayList<>();

    public GameState currentState() {
        return states.get(states.size() - 1);
    }

    public void addState(GameState state) {
        states.add(state);
    }
}
